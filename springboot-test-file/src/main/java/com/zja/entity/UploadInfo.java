package com.zja.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by IntelliJ Idea 2018.2
 *
 * @author: qyp
 * Date: 2019-05-31 14:10
 */
public class UploadInfo implements Serializable {

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 保存文件所在文件夹的路径
     */
    private String filePath;

    /**
     * 新的文件名
     */
    private String newFileName;

    /**
     * 原文件名
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String ext;

    /**
     * 分块总数
     */
    private Integer chunkCount;
    /**
     * 分块编号
     */
    private String chunkNo;

    /**
     * 上传的批次（唯一识别）
     */
    private String batchNo;

    public UploadInfo() {}

    private UploadInfo(long fileSize, String filePath, String newFileName, String fileName, String ext, String chunkNo, String batchNo, Integer chunkCount) {
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.newFileName = newFileName;
        this.fileName = fileName;
        this.ext = ext;
        this.chunkNo = chunkNo;
        this.batchNo = batchNo;
        this.chunkCount = chunkCount;
    }

    private UploadInfo(Builder builder) {
        this.fileSize = builder.fileSize;
        this.filePath = builder.filePath;
        this.newFileName = builder.newFileName;
        this.fileName = builder.fileName;
        this.ext = builder.ext;
        this.chunkNo = builder.chunkNo;
        this.batchNo = builder.batchNo;
        this.chunkCount = builder.chunkCount;
    }

    public static class Builder {
        private Integer chunkCount;
        private String chunkNo;
        private String batchNo;

        private String filePath;
        private String fileName;
        private String newFileName;
        private long fileSize;
        private String ext;

        public Builder(Integer chunkCount, String chunkNo, String batchNo) {
            this.chunkCount = chunkCount;
            this.chunkNo = chunkNo;
            this.batchNo = batchNo;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder newFileName(String newFileName) {
            this.newFileName = newFileName;
            return this;
        }

        public Builder fileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder ext(String ext) {
            this.ext = ext;
            return this;
        }
        public UploadInfo build() {
            return new UploadInfo(this);
        }
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getChunkNo() {
        return chunkNo;
    }

    public void setChunkNo(String chunkNo) {
        this.chunkNo = chunkNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getChunkCount() {
        return chunkCount;
    }

    public void setChunkCount(Integer chunkCount) {
        this.chunkCount = chunkCount;
    }

    @Override
    public int hashCode() {
        int result = 14;
        result = result + 31 * filePath.hashCode();
        result = result + 31 * fileName.hashCode();
        result = result + 31 * chunkNo.hashCode();
        result = result + 31 * batchNo.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UploadInfo)) {
            return false;
        }
        UploadInfo u = (UploadInfo) obj;
        if (!this.getFilePath().equals(u.getFilePath())) {
            return false;
        }
        if (!this.getFileName().equals(u.getFileName())) {
            return false;
        }
        if (!this.getBatchNo().equals(u.getBatchNo())) {
            return false;
        }
        if (!this.getChunkNo().equals(u.getChunkNo())) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        UploadInfo b1 = new Builder(10, "1", "1").filePath("aa").fileName("aa").build();
        UploadInfo b2 = new Builder(10, "2", "1").filePath("aa").fileName("aa").build();
        UploadInfo b3 = new Builder(10, "3", "1").filePath("aa").fileName("aa").build();
        List<UploadInfo> list = new ArrayList<>();
        list.add(b1);
        list.add(b2);
        System.out.println(list.contains(b3));
    }
}
