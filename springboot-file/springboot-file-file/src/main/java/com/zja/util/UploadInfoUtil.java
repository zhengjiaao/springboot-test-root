package com.zja.util;

import com.zja.entity.UploadInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**前端 大文件分片后上传->后端接收分片->合并分片
 *
 */
public class UploadInfoUtil {

    public static final List<UploadInfo> uploadFileList = new ArrayList<>(10);

    /**
     * 分片保存完了  合并并保存
     *
     * @param uploadInfo 上传的文件信息
     * @return
     */
    public static boolean uploaded(UploadInfo uploadInfo) throws IOException {

        if (!uploadFileList.contains(uploadInfo)) {
            synchronized (uploadFileList) {
                if (!uploadFileList.contains(uploadInfo)) {
                    uploadFileList.add(uploadInfo);
                }
                boolean allUploaded = isAllUploaded(uploadInfo.getBatchNo(), uploadInfo.getChunkCount());
                if (allUploaded) {

                    mergeFile(uploadInfo.getFilePath(), uploadInfo.getBatchNo(), uploadInfo.getExt(), uploadInfo.getChunkCount());

                    uploadFileList.removeIf(item -> uploadInfo.getBatchNo().equals(item.getBatchNo()));
                }
            }
        }
        return true;
    }

    /**
     * 合并所有分块
     * @param filePath 保存的目录
     * @param batchNo   批次号
     * @param ext   文件后缀
     * @param chunkCount 总块数
     * @throws IOException
     */
    private static void mergeFile(String filePath, String batchNo, String ext, Integer chunkCount) throws IOException {

        String mergePath = filePath + File.separator + batchNo + ext;

        SequenceInputStream s;
        //合并前两块
        InputStream s1 = new FileInputStream(filePath + File.separator + batchNo + "_" + 1);
        InputStream s2 = new FileInputStream(filePath + File.separator + batchNo + "_" + 2);
        s = new SequenceInputStream(s1, s2);

        for (int i = 3; i <= chunkCount; i++) {
            FileInputStream s_next = new FileInputStream(filePath + File.separator + batchNo + "_" + i);
            s = new SequenceInputStream(s, s_next);
        }

        saveStreamToFile(s, mergePath);
    }

    /**
     * 保存最终合并的文件
     *
     * @param s
     * @param mergePath
     */
    private static void saveStreamToFile(SequenceInputStream s, String mergePath) throws IOException {
        FileOutputStream out = new FileOutputStream(mergePath);
        byte[] buf = new byte[1024];
        try {
            for (int len; (len = s.read(buf)) != -1; ) {
                out.write(buf, 0, len);
                out.flush();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (s != null) {
                s.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }

    /**
     * 根据批次获取当前批次已经上传的数量，然后与分块总数比较判断是否全部上传
     *
     * @param batheNo    批次
     * @param chunkCount 分块数量
     * @return
     */
    private static boolean isAllUploaded(String batheNo, Integer chunkCount) {
        int alreadySize = uploadFileList.stream()
                .filter(item -> batheNo.equals(item.getBatchNo()))
                .distinct()
                .collect(Collectors.toList())
                .size();
        return alreadySize == chunkCount;
    }

    /**
     * 保存的文件名路径为  指定路径 ${upload_path}/batchNo/batchNo_1
     * 每一批一个文件夹
     *
     * @param savePath
     * @param fileFullName
     * @param file
     * @return
     * @throws Exception
     */
    public static boolean saveFile(final String savePath,
                                   final String fileFullName,
                                   final MultipartFile file)
            throws Exception {
        byte[] data = readInputStream(file.getInputStream());

        //保存分片文件的路径  不完整的不需要后缀
        File uploadFile = new File(savePath + File.separator + fileFullName);

        //判断文件夹是否存在，不存在就创建一个
        File fileDirectory = new File(savePath);
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdirs()) {
                throw new Exception("文件夹创建失败！路径为：" + savePath);
            }
        }

        //创建输出流
        try (FileOutputStream outStream = new FileOutputStream(uploadFile)) {
            outStream.write(data);
            outStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return uploadFile.exists();
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * 获取文件后缀 包括 .
     * @param fileName
     * @return
     */
    public static String getExt(String fileName) {
        if (fileName.indexOf(".") != -1) {
            return fileName.substring(fileName.indexOf("."));
        }
        return "";
    }

}
