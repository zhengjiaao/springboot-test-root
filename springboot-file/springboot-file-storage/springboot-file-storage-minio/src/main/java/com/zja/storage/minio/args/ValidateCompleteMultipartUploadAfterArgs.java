package com.zja.storage.minio.args;

import io.minio.ObjectArgs;

/**
 * @Author: zhengja
 * @Date: 2024-12-26 9:23
 */
public class ValidateCompleteMultipartUploadAfterArgs extends ObjectArgs {

    protected String matchETag; // The ETag value to match, Please pay attention to ETag != md5.
    protected long matchLength; // The length of the object.

    public String matchETag() {
        return matchETag;
    }

    public long matchLength() {
        return matchLength;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<Builder, ValidateCompleteMultipartUploadAfterArgs> {
        @Override
        protected void validate(ValidateCompleteMultipartUploadAfterArgs args) {
            super.validate(args);
        }

        public Builder matchETag(String matchETag) {
            operations.add(args -> args.matchETag = matchETag);
            return this;
        }

        public Builder matchLength(long matchLength) {
            operations.add(args -> args.matchLength = matchLength);
            return this;
        }

    }
}
