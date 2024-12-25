package com.zja.storage.minio.args;

import io.minio.BucketArgs;

/**
 * @Author: zhengja
 * @Date: 2024-12-24 15:02
 */
public class ListMultipartUploadsArgs extends BucketArgs {
    protected String delimiter;   // 分隔符
    protected String encodingType;    // 编码类型
    protected String keyMarker;   // 键标记
    protected Integer maxUploads; // 最大上传数量
    protected String prefix;  // 前缀
    protected String uploadIdMarker;  // 上传标记

    public String delimiter() {
        return delimiter;
    }

    public String encodingType() {
        return encodingType;
    }

    public String keyMarker() {
        return keyMarker;
    }

    public Integer maxUploads() {
        return maxUploads;
    }

    public String prefix() {
        return prefix;
    }

    public String uploadIdMarker() {
        return uploadIdMarker;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends BucketArgs.Builder<Builder, ListMultipartUploadsArgs> {
        @Override
        protected void validate(ListMultipartUploadsArgs args) {
            super.validate(args);
        }

        public Builder delimiter(String delimiter) {
            operations.add(args -> args.delimiter = delimiter);
            return this;
        }

        public Builder encodingType(String encodingType) {
            operations.add(args -> args.encodingType = encodingType);
            return this;
        }

        public Builder keyMarker(String keyMarker) {
            operations.add(args -> args.keyMarker = keyMarker);
            return this;
        }

        public Builder maxUploads(Integer maxUploads) {
            operations.add(args -> args.maxUploads = maxUploads);
            return this;
        }

        public Builder prefix(String prefix) {
            operations.add(args -> args.prefix = prefix);
            return this;
        }

        public Builder uploadIdMarker(String uploadIdMarker) {
            operations.add(args -> args.uploadIdMarker = uploadIdMarker);
            return this;
        }
    }
}
