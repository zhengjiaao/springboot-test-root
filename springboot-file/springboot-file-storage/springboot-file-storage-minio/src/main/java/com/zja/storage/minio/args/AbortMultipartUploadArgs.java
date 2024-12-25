package com.zja.storage.minio.args;

import io.minio.ObjectArgs;

/**
 * @Author: zhengja
 * @Date: 2024-12-24 16:30
 */
public class AbortMultipartUploadArgs extends ObjectArgs {
    protected String uploadId;    // 上传id

    public String uploadId() {
        return uploadId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<Builder, AbortMultipartUploadArgs> {
        @Override
        protected void validate(AbortMultipartUploadArgs args) {
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
    }
}
