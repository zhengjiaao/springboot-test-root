package com.zja.storage.minio.args;

import io.minio.ObjectArgs;

/**
 * @Author: zhengja
 * @Date: 2024-12-24 15:02
 */
public class ApplyUploadIdArgs extends ObjectArgs {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<Builder, ApplyUploadIdArgs> {
        @Override
        protected void validate(ApplyUploadIdArgs args) {
            super.validate(args);
        }
    }
}
