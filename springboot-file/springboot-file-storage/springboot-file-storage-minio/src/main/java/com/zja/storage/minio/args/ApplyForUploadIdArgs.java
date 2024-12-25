package com.zja.storage.minio.args;

import io.minio.ObjectArgs;

/**
 * @Author: zhengja
 * @Date: 2024-12-24 15:02
 */
public class ApplyForUploadIdArgs extends ObjectArgs {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ObjectArgs.Builder<Builder, ApplyForUploadIdArgs> {
        @Override
        protected void validate(ApplyForUploadIdArgs args) {
            super.validate(args);
        }
    }
}
