package com.zja.storage.minio.args;

import io.minio.ObjectArgs;

/**
 * @Author: zhengja
 * @Date: 2024-12-24 15:02
 */
public class ListPartsArgs extends ObjectArgs {
    protected String uploadId;    // 上传id
    protected Integer maxParts;   // 分片数量
    protected Integer partNumberMarker;   // 分片标记

    public String uploadId() {
        return this.uploadId;
    }

    public Integer maxParts() {
        return this.maxParts;
    }

    public Integer partNumberMarker() {
        return this.partNumberMarker;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<Builder, ListPartsArgs> {
        @Override
        protected void validate(ListPartsArgs args) {
            super.validate(args);
            validateUploadId(args.uploadId);
        }

        private void validateUploadId(String uploadId) {
            validateNotEmptyString(uploadId, "uploadId");
        }

        public Builder uploadId(String uploadId) {
            validateUploadId(uploadId);
            operations.add(args -> args.uploadId = uploadId);
            return this;
        }

        public Builder maxParts(Integer maxParts) {
            operations.add(args -> args.maxParts = maxParts);
            return this;
        }

        public Builder partNumberMarker(Integer partNumberMarker) {
            operations.add(args -> args.partNumberMarker = partNumberMarker);
            return this;
        }
    }
}
