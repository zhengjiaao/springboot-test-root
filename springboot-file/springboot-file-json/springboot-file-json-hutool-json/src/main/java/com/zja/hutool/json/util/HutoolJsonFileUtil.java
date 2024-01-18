package com.zja.hutool.json.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author: zhengja
 * @since: 2024/01/18 15:49
 */
public class HutoolJsonFileUtil {

    public static File writeJSON(JSON jsonObject, File file, Charset charset) {
        return FileUtil.writeString(jsonObject.toString(), file, charset);
    }

    public static JSON readJSON(File file, Charset charset) {
        return JSONUtil.readJSON(file, charset);
    }

    public static JSONObject readJSONObject(File file, Charset charset) {
        return JSONUtil.readJSONObject(file, charset);
    }

    public static JSONArray readJSONArray(File file, Charset charset) {
        return JSONUtil.readJSONArray(file, charset);
    }

}
