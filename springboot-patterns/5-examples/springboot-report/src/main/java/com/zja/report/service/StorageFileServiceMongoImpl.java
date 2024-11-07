package com.zja.report.service;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.result.DeleteResult;
import com.zja.report.model.dto.StorageDTO;
import com.zja.report.model.dto.StorageNodeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用MongoDB 存储服务实现
 *
 * @Author: zhengja
 * @Date: 2024-09-04 9:50
 */
@Slf4j
public class StorageFileServiceMongoImpl implements StorageFileService {

    final GridFsTemplate gridFsTemplate;

    final MongoTemplate mongoTemplate;

    // 存储类型
    static final String STORAGE_TYPE_MONGO = "mongo";

    // 空间或节点的前缀，在未设置时，自动生成的前缀
    static final String STORAGE_PREFIX = "auto-";

    // 延迟删除文件
    private final Map<String, Long> delayedFiles = new ConcurrentHashMap<>();

    public StorageFileServiceMongoImpl(GridFsTemplate gridFsTemplate, MongoTemplate mongoTemplate) {
        this.gridFsTemplate = gridFsTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    private GridFSFile findGridFSFile(String fileId) {
        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(new ObjectId(fileId))));
        if (fsFile == null) {
            log.error("mongodb文件不存在，_id={}", fileId);
            throw new RuntimeException("文件不存在");
        }
        return fsFile;
    }

    public String getStorageType() {
        return STORAGE_TYPE_MONGO;
    }

    @Override
    public StorageNodeDTO createNode(String spaceId, String nodeName) {
        if (StringUtils.isEmpty(spaceId) || StringUtils.isEmpty(nodeName)) {
            throw new RuntimeException("spaceId or nodeName 不能为空");
        }

        return mongoTemplate.save(StorageNodeDTO.builder()
                .spaceId(spaceId)
                .name(nodeName)
                .createTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).build());
    }

    @Override
    public StorageNodeDTO getNode(String nodeId) {
        return mongoTemplate.findById(nodeId, StorageNodeDTO.class);
    }

    @Override
    public List<StorageNodeDTO> getRootNodes(String spaceId) {
        Query query = new Query(Criteria.where("spaceId").is(spaceId).and("parentId").exists(false));
        return mongoTemplate.find(query, StorageNodeDTO.class);
    }

    @Override
    public boolean deleteNode(String nodeId) {
        try {
            Query query = new Query(Criteria.where("_id").is(nodeId));
            DeleteResult deleteResult = mongoTemplate.remove(query, StorageNodeDTO.class);

            // 删除该节点下的所有文件
            if (deleteResult.getDeletedCount() > 0) {
                deleteFilesByNodeId(nodeId);
            }

            return true;
        } catch (Exception e) {
            log.error("mongodb删除节点失败，_id={}", nodeId, e);
            return false;
        }
    }

    /**
     * 根据 nodeId 删除所有关联的文件
     *
     * @param nodeId 要删除的节点 ID
     */
    private void deleteFilesByNodeId(String nodeId) {
        Query query = new Query(Criteria.where("metadata.nodeId").is(nodeId));
        gridFsTemplate.delete(query);
    }

    @Override
    public StorageDTO uploadFile(String fileName, InputStream is, String spaceId, String nodeId) throws IOException {
        if (StringUtils.isEmpty(spaceId)) {
            throw new RuntimeException("spaceId 不能为空");
        }
        if (StringUtils.isNotEmpty(nodeId) && ObjectUtils.isEmpty(getNode(nodeId))) {
            throw new RuntimeException("节点不存在");
        }

        Document metadata = new Document();
        metadata.append("spaceId", spaceId);
        metadata.append("nodeId", nodeId);

        String fileId = gridFsTemplate.store(is, fileName, metadata).toString();

        return getFileMetaData(fileId);
    }

    @Override
    public StorageDTO uploadFile(String fileName, InputStream is, String nodeId) throws IOException {
        StorageNodeDTO node = getNode(nodeId);
        if (StringUtils.isNotEmpty(nodeId) && ObjectUtils.isEmpty(node)) {
            throw new RuntimeException("节点不存在");
        }

        return uploadFile(fileName, is, node.getSpaceId(), nodeId);
    }

    @Override
    public StorageDTO uploadFile(String fileName, InputStream is) throws IOException {
        return uploadFile(fileName, is, generateRandomId(), null);
    }

    @Override
    public StorageDTO uploadFile(MultipartFile file) throws IOException {
        return uploadFile(Objects.requireNonNull(file.getOriginalFilename()), file.getInputStream(), generateRandomId(), null);
    }

    @Override
    public StorageDTO uploadFile(String filePath) throws IOException {
        MultipartFile multipartFile = toMultipartFile(filePath);
        return uploadFile(multipartFile);
    }

    @Override
    public StorageDTO uploadFile(String filePath, String nodeId) throws IOException {
        StorageNodeDTO node = getNode(nodeId);
        if (ObjectUtils.isEmpty(node)) {
            throw new RuntimeException("节点不存在");
        }

        try (FileInputStream is = new FileInputStream(filePath)) {
            return uploadFile(FilenameUtils.getName(filePath), is, node.getSpaceId(), node.getId());
        } catch (Exception e) {
            log.error("文件上传失败，filePath={}", filePath, e);
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public StorageDTO getFileMetaData(String fileId) {
        GridFSFile gridFSFile = findGridFSFile(fileId);
        Document metadata = gridFSFile.getMetadata();

        return StorageDTO.builder()
                .spaceId(metadata != null ? metadata.getString("spaceId") : null)
                .nodeId(metadata != null ? metadata.getString("nodeId") : null)
                .fileId(fileId)
                .fileName(gridFSFile.getFilename())
                .fileSize(gridFSFile.getLength())
                .storageType(getStorageType()).build();
    }

    @Override
    public List<StorageDTO> getSpaceFiles(String spaceId) {
        return getFiles("metadata.spaceId", spaceId);
    }

    @Override
    public List<StorageDTO> getNodeFiles(String nodeId) {
        return getFiles("metadata.nodeId", nodeId);
    }

    private List<StorageDTO> getFiles(String metadataKey, String metadataValue) {
        Query query = new Query(Criteria.where(metadataKey).is(metadataValue));
        GridFSFindIterable gridFSFiles = gridFsTemplate.find(query);

        List<StorageDTO> result = new ArrayList<>();
        for (GridFSFile gridFSFile : gridFSFiles) {
            Document metadata = gridFSFile.getMetadata();
            result.add(StorageDTO.builder()
                    .spaceId(metadata != null ? metadata.getString("spaceId") : null)
                    .nodeId(metadata != null ? metadata.getString("nodeId") : null)
                    .fileId(gridFSFile.getObjectId().toString())
                    .fileName(gridFSFile.getFilename())
                    .fileSize(gridFSFile.getLength())
                    .storageType(getStorageType()).build());
        }

        return result;
    }

    @Override
    public String getFileName(String fileId) {
        return getFileMetaData(fileId).getFileName();
    }

    @Override
    public String getFileUrl(String fileId) {
        // 获取当前请求的上下文路径和协议
        HttpServletRequest servletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // 构建文件的完整 URL
        String baseUrl = servletRequest.getScheme() + "://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort() + servletRequest.getContextPath();
        String fileUrl = baseUrl + "/rest/storage/download/" + fileId;

        return fileUrl;
    }

    @Deprecated
    @Override
    public String getFilePreviewUrl(String fileId) {
        // todo MongoDB 暂时不支持预览，未接入预览服务，项目上若存在部署了其他预览服务，可以自行接入例如：doc、kkfile等
        return "";
    }

    @Override
    public InputStream downloadFile(String fileId) throws IOException {
        GridFSFile gridFSFile = findGridFSFile(fileId);
        return gridFsTemplate.getResource(gridFSFile).getInputStream();
    }

    @Override
    public void downloadFile(String fileId, HttpServletResponse response) throws IOException {
        downloadFile(fileId, null, response);
    }

    @Override
    public void downloadFile(String fileId, String fileName, HttpServletResponse response) throws IOException {
        try (InputStream inputStream = downloadFile(fileId)) {
            // 设置下载时的文件名称
            if (StringUtils.isBlank(fileName)) {
                fileName = getFileName(fileId);
                fileName = StringUtils.isNotEmpty(fileName) ? fileName : "请手动重命此文件名称";
            }
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
            // 设置响应头以支持文件下载
            response.setContentType("application/octet-stream");

            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    @Override
    public void downloadFile(String fileId, String filePath) throws IOException {
        Files.createDirectories(Paths.get(filePath).getParent());
        try (InputStream inputStream = downloadFile(fileId); FileOutputStream outputStream = new FileOutputStream(filePath)) {
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.error("下载文件失败: fileId={},filePath={}", fileId, filePath, e);
            throw new RuntimeException("下载文件失败", e);
        }
    }

    @Override
    public boolean exists(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(new ObjectId(fileId))));
        return file != null;
    }

    @Override
    public boolean deleteFile(String fileId) {
        try {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(new ObjectId(fileId))));
            return true;
        } catch (Exception e) {
            log.error("mongodb删除文件失败,_id={}", fileId, e);
            return false;
        }
    }

    @Override
    public void deleteFileByDelayed(String fileId, long delayInMillis) {
        long deleteTime = System.currentTimeMillis() + delayInMillis;
        delayedFiles.put(fileId, deleteTime);
    }

    @Async
    @Scheduled(fixedDelay = 60000) // 每分钟执行一次，可根据需求调整
    public void deleteFilesScheduled() {
        long currentTime = System.currentTimeMillis();

        for (Map.Entry<String, Long> entry : delayedFiles.entrySet()) {
            String fileId = entry.getKey();
            Long deleteTime = entry.getValue();

            if (currentTime >= deleteTime) {
                deleteFile(fileId);
                delayedFiles.remove(fileId);
            }
        }
    }

    /**
     * 生成随机Id
     *
     * @return Id，示例：auto-xxxxxxxxxxxx
     */
    private String generateRandomId() {
        return STORAGE_PREFIX + UUID.randomUUID().toString();
    }

    private static MultipartFile toMultipartFile(String filePath) {
        try {
            File file = new File(filePath);
            FileItem fileItem = new DiskFileItemFactory().createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, file.getName());
            try (FileInputStream is = new FileInputStream(file)) {
                IOUtils.copy(is, fileItem.getOutputStream());
            }
            return new CommonsMultipartFile(fileItem);
        } catch (IOException e) {
            throw new RuntimeException("转换失败,File --> MultipartFile", e);
        }
    }

}
