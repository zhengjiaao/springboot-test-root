package com.zja.storage.minio.args;

import io.minio.ObjectVersionArgs;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhengja
 * @Date: 2024-12-24 15:24
 */
public class GetPresignedPartUrlArgs extends ObjectVersionArgs {
    public static final int DEFAULT_EXPIRY_TIME = (int) TimeUnit.DAYS.toSeconds(7);

    protected String uploadId;    // 上传id，需要申请
    protected int partNumber; // 分片编号,从1开始
    protected int expiry = DEFAULT_EXPIRY_TIME; // 失效时间，以秒为单位失效；默认为7天。

    public String uploadId() {
        return uploadId;
    }

    public int partNumber() {
        return partNumber;
    }

    public int expiry() {
        return expiry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectVersionArgs.Builder<Builder, GetPresignedPartUrlArgs> {

        @Override
        protected void validate(GetPresignedPartUrlArgs args) {
            super.validate(args);
            validateUploadId(args.uploadId);
            validatePartNumber(args.partNumber);
        }

        private void validateUploadId(String uploadId) {
            validateNotEmptyString(uploadId, "uploadId");
        }

        private void validatePartNumber(int partNumber) {
            if (partNumber < 1) {
                throw new IllegalArgumentException("partNumber must be greater than 0, but was: " + partNumber);
            }
        }

        public Builder uploadId(String uploadId) {
            validateUploadId(uploadId);
            operations.add(args -> args.uploadId = uploadId);
            return this;
        }

        public Builder partNumber(int partNumber) {
            validatePartNumber(partNumber);
            operations.add(args -> args.partNumber = partNumber);
            return this;
        }

        public Builder expiry(int expiry) {
            operations.add(args -> args.expiry = expiry);
            return this;
        }

        public Builder expiry(int duration, TimeUnit unit) {
            return expiry((int) unit.toSeconds(duration));
        }
    }
}
