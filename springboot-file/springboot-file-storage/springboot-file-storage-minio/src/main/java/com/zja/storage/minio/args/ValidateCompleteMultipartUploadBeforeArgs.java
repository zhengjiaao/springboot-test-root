package com.zja.storage.minio.args;

import io.minio.ObjectArgs;

/**
 * @Author: zhengja
 * @Date: 2024-12-26 9:54
 */
public class ValidateCompleteMultipartUploadBeforeArgs extends ObjectArgs {

    protected String uploadId;  // The upload ID of the multipart upload.
    protected long objectPartSize; // The number of parts in the object.

    public String uploadId() {
        return uploadId;
    }

    public long objectPartSize() {
        return objectPartSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<Builder, ValidateCompleteMultipartUploadBeforeArgs> {
        @Override
        protected void validate(ValidateCompleteMultipartUploadBeforeArgs args) {
            super.validate(args);
            validateUploadId(args.uploadId);
            validateObjectPartSize(args.objectPartSize);
        }

        private void validateObjectPartSize(long objectPartSize) {
            if (objectPartSize < 1) {
                throw new IllegalArgumentException("objectPartSize must be greater than 0");
            }
        }

        private void validateUploadId(String uploadId) {
            validateNotEmptyString(uploadId, "uploadId");
        }

        public Builder uploadId(String uploadId) {
            validateUploadId(uploadId);
            operations.add(args -> args.uploadId = uploadId);
            return this;
        }

        public Builder objectPartSize(long objectPartSize) {
            validateObjectPartSize(objectPartSize);
            operations.add(args -> args.objectPartSize = objectPartSize);
            return this;
        }

    }

}
