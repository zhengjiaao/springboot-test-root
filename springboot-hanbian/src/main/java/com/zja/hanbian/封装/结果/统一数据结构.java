package com.zja.hanbian.封装.结果;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 14:40
 */
public class 统一数据结构<T> extends HttpEntity<T> {
    private final Object status;

    public 统一数据结构(HttpStatus status) {
        this((T) null, (MultiValueMap)null, (HttpStatus)status);
    }

    public 统一数据结构(@Nullable T body, HttpStatus status) {
        this(body, (MultiValueMap)null, (HttpStatus)status);
    }

    public 统一数据结构(MultiValueMap<String, String> headers, HttpStatus status) {
        this((T) null, headers, (HttpStatus)status);
    }

    public 统一数据结构(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers);
        Assert.notNull(status, "HttpStatus must not be null");
        this.status = status;
    }

    private 统一数据结构(@Nullable T body, @Nullable MultiValueMap<String, String> headers, Object status) {
        super(body, headers);
        Assert.notNull(status, "HttpStatus must not be null");
        this.status = status;
    }

    public HttpStatus getStatusCode() {
        return this.status instanceof HttpStatus ? (HttpStatus)this.status : HttpStatus.valueOf((Integer)this.status);
    }

    public int getStatusCodeValue() {
        return this.status instanceof HttpStatus ? ((HttpStatus)this.status).value() : (Integer)this.status;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!super.equals(other)) {
            return false;
        } else {
            统一数据结构<?> otherEntity = (统一数据结构)other;
            return ObjectUtils.nullSafeEquals(this.status, otherEntity.status);
        }
    }

    public int hashCode() {
        return 29 * super.hashCode() + ObjectUtils.nullSafeHashCode(this.status);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.status.toString());
        if (this.status instanceof HttpStatus) {
            builder.append(' ');
            builder.append(((HttpStatus)this.status).getReasonPhrase());
        }

        builder.append(',');
        T body = this.getBody();
        HttpHeaders headers = this.getHeaders();
        if (body != null) {
            builder.append(body);
            builder.append(',');
        }

        builder.append(headers);
        builder.append('>');
        return builder.toString();
    }

    public static BodyBuilder status(HttpStatus status) {
        Assert.notNull(status, "HttpStatus must not be null");
        return new DefaultBuilder(status);
    }

    public static BodyBuilder status(int status) {
        return new DefaultBuilder(status);
    }

    public static BodyBuilder ok() {
        return status(HttpStatus.OK);
    }

    public static <T> 统一数据结构<T> 返回数据(@Nullable T body) {
        return ok().body(body);
    }

    public static <T> 统一数据结构<T> 数据(@Nullable T body) {
        return ok().body(body);
    }

    public static <T> 统一数据结构<T> ok(@Nullable T body) {
        return ok().body(body);
    }

    public static <T> 统一数据结构<T> of(Optional<T> body) {
        Assert.notNull(body, "Body must not be null");
        return (统一数据结构)body.map(统一数据结构::ok).orElseGet(() -> {
            return notFound().build();
        });
    }

    public static BodyBuilder created(URI location) {
        return (BodyBuilder)status(HttpStatus.CREATED).location(location);
    }

    public static BodyBuilder accepted() {
        return status(HttpStatus.ACCEPTED);
    }

    public static HeadersBuilder<?> noContent() {
        return status(HttpStatus.NO_CONTENT);
    }

    public static BodyBuilder badRequest() {
        return status(HttpStatus.BAD_REQUEST);
    }

    public static HeadersBuilder<?> notFound() {
        return status(HttpStatus.NOT_FOUND);
    }

    public static BodyBuilder unprocessableEntity() {
        return status(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private static class DefaultBuilder implements BodyBuilder {
        private final Object statusCode;
        private final HttpHeaders headers = new HttpHeaders();

        public DefaultBuilder(Object statusCode) {
            this.statusCode = statusCode;
        }

        public BodyBuilder header(String headerName, String... headerValues) {
            String[] var3 = headerValues;
            int var4 = headerValues.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String headerValue = var3[var5];
                this.headers.add(headerName, headerValue);
            }

            return this;
        }

        public BodyBuilder headers(@Nullable HttpHeaders headers) {
            if (headers != null) {
                this.headers.putAll(headers);
            }

            return this;
        }

        public BodyBuilder headers(Consumer<HttpHeaders> headersConsumer) {
            headersConsumer.accept(this.headers);
            return this;
        }

        public BodyBuilder allow(HttpMethod... allowedMethods) {
            this.headers.setAllow(new LinkedHashSet(Arrays.asList(allowedMethods)));
            return this;
        }

        public BodyBuilder contentLength(long contentLength) {
            this.headers.setContentLength(contentLength);
            return this;
        }

        public BodyBuilder contentType(MediaType contentType) {
            this.headers.setContentType(contentType);
            return this;
        }

        public BodyBuilder eTag(String etag) {
            if (!etag.startsWith("\"") && !etag.startsWith("W/\"")) {
                etag = "\"" + etag;
            }

            if (!etag.endsWith("\"")) {
                etag = etag + "\"";
            }

            this.headers.setETag(etag);
            return this;
        }

        public BodyBuilder lastModified(ZonedDateTime date) {
            this.headers.setLastModified(date);
            return this;
        }

        public BodyBuilder lastModified(Instant date) {
            this.headers.setLastModified(date);
            return this;
        }

        public BodyBuilder lastModified(long date) {
            this.headers.setLastModified(date);
            return this;
        }

        public BodyBuilder location(URI location) {
            this.headers.setLocation(location);
            return this;
        }

        public BodyBuilder cacheControl(CacheControl cacheControl) {
            this.headers.setCacheControl(cacheControl);
            return this;
        }

        public BodyBuilder varyBy(String... requestHeaders) {
            this.headers.setVary(Arrays.asList(requestHeaders));
            return this;
        }

        public <T> 统一数据结构<T> build() {
            return this.body((T) null);
        }

        public <T> 统一数据结构<T> body(@Nullable T body) {
            return new 统一数据结构(body, this.headers, this.statusCode);
        }
    }

    public interface BodyBuilder extends HeadersBuilder<BodyBuilder> {
        BodyBuilder contentLength(long var1);

        BodyBuilder contentType(MediaType var1);

        <T> 统一数据结构<T> body(@Nullable T var1);
    }

    public interface HeadersBuilder<B extends HeadersBuilder<B>> {
        B header(String var1, String... var2);

        B headers(@Nullable HttpHeaders var1);

        B headers(Consumer<HttpHeaders> var1);

        B allow(HttpMethod... var1);

        B eTag(String var1);

        B lastModified(ZonedDateTime var1);

        B lastModified(Instant var1);

        B lastModified(long var1);

        B location(URI var1);

        B cacheControl(CacheControl var1);

        B varyBy(String... var1);

        <T> 统一数据结构<T> build();
    }
}
