package com.zja.util;

import net.sf.json.JSONNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/** 对象属性值为 null的值转为 空字符串""
 * @author zhengja@dist.com.cn
 * @data 2019/8/20 13:59
 */
public class PropertyNullToStrUtil {

    /**
     * 将json对象中包含的null和JSONNull属性修改成""
     *
     * @param jsonObj
     */
    public static JSONObject filterNull(JSONObject jsonObj) {
        Iterator<String> it = jsonObj.keys();
        Object obj = null;
        String key = null;
        while (it.hasNext()) {
            key = it.next();
            obj = jsonObj.get(key);
            if (obj instanceof JSONObject) {
                filterNull((JSONObject) obj);
            }
            if (obj instanceof JSONArray) {
                JSONArray objArr = (JSONArray) obj;
                for (int i = 0; i < objArr.length(); i++) {
                    filterNull(objArr.getJSONObject(i));
                }
            }
            if (obj == null || obj instanceof JSONNull) {
                jsonObj.put(key, "");
            }
            if (obj.equals(null)) {
                jsonObj.put(key, "");
            }
        }
        return jsonObj;
    }
}
