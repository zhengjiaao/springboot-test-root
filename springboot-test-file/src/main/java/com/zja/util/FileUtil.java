package com.zja.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**文件操作工具
 *
 */
public class FileUtil {

    private FileUtil() {
        super();
    }

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final List<String> NO_NEED_TRANSFORM_FILTER = new ArrayList<>();
    private static final List<String> NEED_TRANSFORM_CHARSET_FILTER = new ArrayList<>();
    private static final List<String> NEED_DOWNLOAD_FILTER = new ArrayList<>();

    static {
        NO_NEED_TRANSFORM_FILTER.add("pdf");
        NO_NEED_TRANSFORM_FILTER.add("jpg");
        NO_NEED_TRANSFORM_FILTER.add("png");
        NO_NEED_TRANSFORM_FILTER.add("jpeg");
        NO_NEED_TRANSFORM_FILTER.add("gif");
        NO_NEED_TRANSFORM_FILTER.add("tif");


        NEED_TRANSFORM_CHARSET_FILTER.add("txt");
        NEED_TRANSFORM_CHARSET_FILTER.add("xml");
        NEED_TRANSFORM_CHARSET_FILTER.add("dwg");
        NEED_TRANSFORM_CHARSET_FILTER.add("doc");
        NEED_TRANSFORM_CHARSET_FILTER.add("docx");
        NEED_TRANSFORM_CHARSET_FILTER.add("ppt");
        NEED_TRANSFORM_CHARSET_FILTER.add("pptx");
        NEED_TRANSFORM_CHARSET_FILTER.add("xlsx");
        NEED_TRANSFORM_CHARSET_FILTER.add("xls");

        NEED_DOWNLOAD_FILTER.add("zip");
        NEED_DOWNLOAD_FILTER.add("rar");

    }


    public static void main(String[] args) {
        //目标位置  将该位置下的文件生成树的形状
        String path = "D:\\FileTest\\文件合成位置";
        //过滤掉path目录下的文件/文件夹等
        List<String> ignore = new ArrayList<>(Arrays.asList("9839783-200201.prj", "107M视频.wmv"));
        System.out.println("ignore-过滤掉: " + ignore);
        Node tree = createTree(path, ignore);
        String jsonString = JSON.toJSONString(tree);
        //打印出json数据 可以使用-json在线格式化工具查看
        System.out.println(jsonString);

    }


    /**
     * 获取文件或目录大小
     *
     * @param file
     * @return String 转换后的文件大小
     * @Title: getFileSizes
     * @author projectNo
     */
    public static long getFileSizes(final File file) {
        if (file.isFile()) {
            return file.length();
        }
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null) {
            for (final File child : children) {
                total += getFileSizes(child);
            }
        }
        return total;
    }


    /**
     * 递归获取目录大小
     * @param file
     * @return long 目录大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        File flist[] = file.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 获取文件修改时间
     *
     * @param file 文件
     * @return String 文件修改时间
     * @Title: getFileTime
     * @author projectNo
     */
    public static String getFileTime(File file) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        long time = file.lastModified();
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try (InputStream inStream = new FileInputStream(oldPath);
             FileOutputStream fs = new FileOutputStream(newPath);) {

            int byteread = 0;
            File oldfile = new File(oldPath);
            File newDir = new File(newPath.replace('\\', '/').substring(0,
                    newPath.replace('\\', '/').lastIndexOf('/')));
            if (!newDir.exists()) {
                newDir.mkdirs();
            }
            if (oldfile.exists()) { // 文件存在时
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
            }
            return true;
        } catch (Exception e) {
            logger.error("复制单个文件操作出错");
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 创建目录
     *
     * @param dirPath
     */
    public static void createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dirPath 将要删除的文件目录
     */
    public static void deleteDir(String dirPath) {
        try {
            FileUtils.deleteDirectory(new File(dirPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过文件url 获取文件后缀
     *
     * @param fileUrl
     * @return
     */
    public static String getFileSufix(String fileUrl) {
        int splitIndex = fileUrl.lastIndexOf('.');
        return fileUrl.substring(splitIndex + 1);
    }

    /**
     * 获取文件后缀
     *
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取文件名的后缀名，注意图片后缀的获取
     *
     * @param fileName 文件名
     * @return 后缀名，没有带点号“.”
     */
    public static String getSuffix(String fileName) {

        Assert.hasLength(fileName);

        String result = "";
        try {

            String hex = "";
            File file = new File(fileName);
            if (!file.exists()) {
                return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            }
            InputStream is = new FileInputStream(file);
            byte[] bt = new byte[2];
            is.read(bt);

            hex = bytesToHexString(bt);
            is.close();
            if (hex.equals("ffd8")) {
                result = "jpg";
            } else if (hex.equals("4749")) {
                result = "gif";
            } else if (hex.equals("8950")) {
                result = "png";
            } else {
                result = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());

        }

        return result;
    }

    /**
     * 修改文件的后缀
     *
     * @param fileName      文件名
     * @param replaceSuffix 要修改的后缀名，不带.
     * @return
     */
    public static String replaceFileSuffix(String fileName, String replaceSuffix) {
        if (fileName.contains(".")) {
            int i = fileName.lastIndexOf(".");
            String substring = fileName.substring(0, i);
            fileName = substring + "." + replaceSuffix;
        }
        return fileName;
    }

    /**
     * 删除文件后缀
     *
     * @param fileName
     * @return
     */
    public static String delFileNameSuffix(String fileName) {
        if (fileName == null) {
            return null;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }


    /**
     * 获取文件名称（包括文件的后缀）
     *
     * @param fileUrl 文件路径
     * @return
     */
    public static String getFileName(String fileUrl) {
        //fileUrl = fileUrl.replace("\\", "/").replace("//", "/");
        int splitIndex = fileUrl.lastIndexOf('/');
        return fileUrl.substring(splitIndex + 1);
    }

    /**
     * 仅保留文件名 不保留后缀
     */
    public static String getFileNameExcludeSuffix(String pathandname) {
        //pathandname = pathandname.replace("\\", "/").replace("//", "/");
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 保留文件名及后缀
     */
    public static String getFileNameWithSuffix(String pathandname) {
        //pathandname = pathandname.replace("\\", "/").replace("//", "/");
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }

    /**
     * 检查文件类型是否合法
     *
     * @param file
     * @return
     */
    public static final boolean isLegalFileType(File file) {
        boolean isLegal = false;
        if (null == file) {
            return isLegal;
        }
        String fileSuffix = getFileSuffix(file);
        for (String s : NO_NEED_TRANSFORM_FILTER) {
            if (s.equalsIgnoreCase(fileSuffix)) {
                isLegal = true;
                break;
            }
        }
        return isLegal;
    }

    /**
     * 根据文件路径检测文件的合法性
     *
     * @param filePath
     * @return
     */
    public static boolean legalFile(String filePath) {

        File file = new File(filePath);
        FileInputStream fis = null;
        try {
            if (file.exists() && file.isFile()) {
                fis = new FileInputStream(file);
                if (fis.available() > 0) {
                    return true;
                } else {
                    file.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 将二进制 转出 java.io.file
     *
     * @param srcFile
     * @param path
     */
    public static final boolean byte2File(byte[] srcFile, String path) {
        FileOutputStream out = null;
        String targetFolder = path.substring(0, path.lastIndexOf(File.separator));
        try {
            File folder = new File(targetFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            out = new FileOutputStream(path);
            out.write(srcFile);
            out.flush();
        } catch (Exception e) {
            return false;
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * file 转 byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[5 * 1024 * 1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }

    /**
     * 一次性取读取所有 byte[]
     *
     * @param filePath 文件路径
     * @return
     */
    public static byte[] FileTobyte(String filePath) {
        byte[] buffer = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            buffer = new byte[(int) new File(filePath).length()];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                //一次向数组中写入数组长度的字节
                logger.info("文件下载中。。。。");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }


    /**
     * InputStream 转 byte[]
     *
     * @param inStream
     * @return
     */
    public static byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * 将 byte[] 写入 response
     * 返回给前端直接下载
     *
     * @param fileName            文件名称
     * @param buffer              获取的文件字节
     * @param httpServletResponse httpServletResponse
     * @throws IOException
     */
    public void fileWriteResponse(String fileName, byte[] buffer, HttpServletResponse httpServletResponse) throws IOException {

        httpServletResponse.setContentType("application/force-download");
        httpServletResponse.addHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        httpServletResponse.setContentLength(buffer.length);

        InputStream in = new ByteArrayInputStream(buffer);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        //方式一 : 适合文件在20M 以下
        /*System.out.println("length=== "+buffer.length);
        outputStream.write(buffer, 0, buffer.length);
*/
        //方式二 ：适合大文件
        int byteCount = StreamUtils.copy(in, outputStream);
        System.out.println(fileName + "-byteCount: " + byteCount);
    }


    /**
     * 创建文件目录的树结构
     * @param basePath
     * @param ignores
     * @return
     */
    public static Node createTree(String basePath, List<String> ignores) {
        File file = new File(basePath);

        if (!file.exists()) {
            throw new IllegalArgumentException("路径错误 == " + basePath);
        }

        String rootNodeID = getNodeID();
        String rootNodeName = file.getName();

        Node rootNode = new Node(rootNodeID, rootNodeName);
        doProcess(rootNode, file, ignores);
        return rootNode;
    }

    /**生成文件夹的目录树结构 具体实现
     * @param rootNode 根节点
     * @param file   文件
     * @param ignores  可选过滤掉：文件、目录等
     */
    private static void doProcess(final Node rootNode, File file, List<String> ignores) {

        String rootNodeName = rootNode.getFileName();
        // 派出的文件
        if (ignores.contains(rootNodeName)) {
            return;
        }
        // 获取文件或目录大小 排除过滤掉的文件大小
        rootNode.setFileLength(getFileSizes(file, ignores));
        // 获取文件最后修改的时间
        rootNode.setLastModifiedTime(getFileTime(file));

        // 单文件
        if (file.isFile()) {
            rootNode.setFileType("file");
            return;
        }
        //如果不是文文件,也不是目录
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("路径错误 == " + file.getAbsolutePath());
        } else {
            rootNode.setFileType("directory");
        }

        File[] files = file.listFiles((f, n) -> !ignores.contains(n));

        //遍历当前节点的子节点，判断当前根节点是否有子节点对象，y：获取它，将子目录节点添加 n：创建子节点结合对象childrens，放入当前节点并设置childrens给根节点
        Arrays.stream(files).forEach(f -> {
            Node currentNode = new Node(getNodeID(), f.getName());
            Optional.ofNullable(rootNode.getChildrens()).map(list -> list.add(currentNode)).orElseGet(() -> {
                List<Node> childrens = new ArrayList<>();
                childrens.add(currentNode);
                rootNode.setChildrens(childrens);
                return null;
            });
            doProcess(currentNode, f, ignores);
        });
    }

    /**
     * 获取节点的名字
     *
     * @return
     */
    private static String getNodeID() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 获取文件或目录大小 排除过滤文件大小
     *
     * @param file
     * @return String 转换后的文件大小
     * @Title: getFileSizes
     * @author projectNo
     */
    public static long getFileSizes(final File file, List<String> ignores) {

        // 派出的文件
        if (ignores.contains(file.getName())) {
            return 0;
        }
        if (file.isFile()) {
            return file.length();
        }
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null) {
            for (final File child : children) {
                total += getFileSizes(child, ignores);
            }
        }
        return total;
    }

    /**
     * 树结构 实体类
     */
    static class Node {
        private String id;
        //文件名称
        private String fileName;
        //文件类型: 目录directory/文件file
        private String fileType;
        //文件大小
        private long fileLength;
        //最后修改时间
        private String lastModifiedTime;
        //文件夹下的子目录
        private List<Node> childrens;

        public Node() {
        }

        public Node(String id) {
            this.id = id;
        }

        public Node(String id, String fileName) {
            this.id = id;
            this.fileName = fileName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public long getFileLength() {
            return fileLength;
        }

        public void setFileLength(long fileLength) {
            this.fileLength = fileLength;
        }

        public String getLastModifiedTime() {
            return lastModifiedTime;
        }

        public void setLastModifiedTime(String lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
        }

        public List<Node> getChildrens() {
            return childrens;
        }

        public void setChildrens(List<Node> childrens) {
            this.childrens = childrens;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "id='" + id + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", fileType='" + fileType + '\'' +
                    ", fileLength=" + fileLength +
                    ", lastModifiedTime='" + lastModifiedTime + '\'' +
                    ", childrens=" + childrens +
                    '}';
        }
    }

}
