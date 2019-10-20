package com.dist.utils.string;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 替换资源文件预览url工具
 * 针对现场内网无法访问外网，需要将外网url替换成内网url
 * @author yinxp@dist.com.cn
 * @date 2018/9/6
 */
@Component
public  class ReplaceUtil {

    // 内外网预览地址是否相互独立，不相同
    @Value("${file.separate}")
    private Boolean separate;

    // 外网地址
    @Value("${file.public.path}")
    private  String publicUrl;

    // 内网地址
    @Value("${file.private.path}")
    private  String privateUrl;

    public ReplaceUtil() {
    }

    public ReplaceUtil(String publicUrl, String privateUrl, Boolean separate) {
        this.publicUrl = publicUrl;
        this.privateUrl = privateUrl;
        this.separate = separate;
    }

    /**
     * 外网替换成内网
     * @param url
     * @return
     */
    public String publicReplaceToPrivate(String url) {
        if (separate) {
            return replace(url,publicUrl,privateUrl);
        }
        return url;
    }

    /**
     *内网替换成外网
     * @param url
     * @return
     */
    public String privateReplaceToPublic(String url) {
        if (separate) {
            return replace(url,privateUrl,publicUrl);
        }
        return url;
    }

    /**
     * 将url中的oldUrl替换成replaceUrl
     * @param url
     * @param oldUrl
     * @param replaceUrl
     * @return
     */
    private String replace(String url,String oldUrl,String replaceUrl) {
        if (url == null) {
            return null;
        }
        return url.replaceAll(oldUrl, replaceUrl);
    }

}
