package com.zja.storage.minio.args;

import io.minio.ObjectArgs;
import io.minio.messages.Part;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-12-24 15:02
 */
public class CompleteMultipartUploadArgs extends ObjectArgs {
    protected String uploadId;    // 上传id
    protected Part[] parts;       // 分片数组

    public String uploadId() {
        return uploadId;
    }

    public Part[] parts() {
        return parts;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<Builder, CompleteMultipartUploadArgs> {
        @Override
        protected void validate(CompleteMultipartUploadArgs args) {
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

        public Builder parts(Part[] parts) {
            validateParts(parts);
            operations.add(args -> args.parts = parts);
            return this;
        }

        public Builder parts(List<Part> parts) {
            validateParts(parts);
            final Part[] copyParts = toPartArray(parts);
            operations.add(args -> args.parts = copyParts);
            return this;
        }

        private Part[] toPartArray(List<Part> partList) {
            Part[] parts = new Part[partList.size()];
            for (int i = 0; i < partList.size(); i++) {
                parts[i] = partList.get(i);
            }
            return parts;
        }

        private void validateParts(Part[] parts) {
            if (parts == null) {
                throw new IllegalArgumentException("parts is null.");
            }
            if (parts.length < 1) {
                throw new IllegalArgumentException("parts must have at least 1 part");
            }
        }

        private void validateParts(List<Part> partList) {
            if (CollectionUtils.isEmpty(partList)){
                throw new IllegalArgumentException("parts is null.");
            }
        }
    }
}
