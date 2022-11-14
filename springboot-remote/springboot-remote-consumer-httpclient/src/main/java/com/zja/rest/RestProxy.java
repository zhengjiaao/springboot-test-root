package com.zja.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zja.response.ResponseData;
import com.zja.rest.annotation.*;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;
import org.springframework.core.env.Environment;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dingchw
 * @date 2019/4/2.
 */
public class RestProxy implements InvocationHandler{
    private Environment env;

    private Class<?> interfaceClass;

    public Object bind(Class<?> cls, Environment env){
        this.interfaceClass = cls;
        this.env = env;
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},this);
    }
    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        String interfaceName = method.getDeclaringClass().getName();
        Class clzz = Class.forName(interfaceName);
        Annotation clzzAnnotation = clzz.getAnnotation(DistRest.class);
        if(null == clzzAnnotation){
            //throw new Exception("DistRest注解缺失>>>");
            return null;
        }
        //URL根路径
        StringBuilder urlBuilder = new StringBuilder();
        String distRestUrl = ((DistRest)clzzAnnotation).url();
        String expPattern = "\\$\\{[a-zA-Z0-9.]+\\}";
        if(distRestUrl.matches(expPattern)){
            distRestUrl = distRestUrl.replace("${","").replace("}","");
            distRestUrl = env.getProperty(distRestUrl.trim());
        }
        urlBuilder.append(distRestUrl);
        urlBuilder.append("/");
        //根uri
        Annotation requestMappingAnnotation = clzz.getAnnotation(DistRequest.class);
        if(null != requestMappingAnnotation){
            urlBuilder.append(((DistRequest)requestMappingAnnotation).value());
            urlBuilder.append("/");
        }
        //解析方法上的注解路径和返回类类型
        Annotation[] methodAnnotations = method.getDeclaredAnnotations();
        String focusMethod = "";
        Class clazz = null;
        if(methodAnnotations.length == 0){
            throw new Exception("方法【" + method.getName() + "】没有配置注解，请检查配置");
        }
        for(Annotation methodAnnotation : methodAnnotations){
            if(methodAnnotation instanceof DistGet){
                urlBuilder.append(((DistGet)methodAnnotation).value());
                clazz = ((DistGet)methodAnnotation).resultClass();
                focusMethod = "GET";
            }else if(methodAnnotation instanceof DistPost){
                urlBuilder.append(((DistPost)methodAnnotation).value());
                clazz = ((DistPost)methodAnnotation).resultClass();
                focusMethod = "POST";
            }else if(methodAnnotation instanceof DistPut){
                urlBuilder.append(((DistPut)methodAnnotation).value());
                clazz = ((DistPut)methodAnnotation).resultClass();
                focusMethod = "PUT";
            }else if(methodAnnotation instanceof DistDelete){
                urlBuilder.append(((DistDelete)methodAnnotation).value());
                clazz = ((DistDelete)methodAnnotation).resultClass();
                focusMethod = "DELETE";
            }else{
                throw new Exception("方法【" + method.getName() + "】存在无效注解，请检查配置");
            }
            urlBuilder.append("/");
            break;
        }
        //解析参数上的注解
        //@RequestParam
        Map<String,Object> parameterValueMap = new HashMap<>();
        //@PathVariable
        Map<String,Object> pathVariableValueMap = new HashMap<>();
        //@ReqeustBody
        Object bodyObj = null;
        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        Parameter[] parameters = method.getParameters();
        for(int i = 0; i < parametersAnnotations.length; i++){
            Annotation[] parameterAnnotations = parametersAnnotations[i];
            for(Annotation parameterAnnotation : parameterAnnotations){
                if(parameterAnnotation instanceof DistParam){
                    String paramName = ((DistParam)parameterAnnotation).value();
                    if(paramName.isEmpty()){
                        parameterValueMap.put(parameters[i].getName(),args[i]);
                    }else{
                        parameterValueMap.put(paramName,args[i]);
                    }
                }else if(parameterAnnotation instanceof DistRequestBody){
                    bodyObj = args[i];
                }else if(parameterAnnotation instanceof DistPathVariable){
                    String paramName = ((DistPathVariable)parameterAnnotation).value();
                    if(paramName.isEmpty()){
                        pathVariableValueMap.put(parameters[i].getName(),args[i]);
                    }else{
                        pathVariableValueMap.put(paramName,args[i]);
                    }
                }
            }
        }
        String url = urlBuilder.toString();
        //@PathVariable配置
        for(Map.Entry<String,Object> pathEntry : pathVariableValueMap.entrySet()){
            url = url.replace("{" + pathEntry.getKey() + "}",pathEntry.getValue().toString());
        }
        //请求数据
        RestTemplate restTemplate = new RestTemplate();
        ResponseData responseData = null;
        switch (focusMethod){
            case "GET":
                StringBuilder parameterUriSb = new StringBuilder();
                if(null != parameterValueMap && parameterValueMap.entrySet().size() > 0){
                    parameterUriSb.append("?");
                    for(Map.Entry<String,Object> parameterValueEntry : parameterValueMap.entrySet()){
                        Object paramObj = parameterValueEntry.getValue();
                        if(paramObj instanceof List){
                            for(String paramElem : (List<String>)paramObj){
                                parameterUriSb.append(parameterValueEntry.getKey());
                                parameterUriSb.append("=");
                                parameterUriSb.append(paramElem);
                                parameterUriSb.append("&");
                            }
                            continue;
                        }
                        parameterUriSb.append(parameterValueEntry.getKey());
                        parameterUriSb.append("=");
                        parameterUriSb.append(parameterValueEntry.getValue());
                        parameterUriSb.append("&");
                    }
                    String parameterUri = parameterUriSb.toString();
                    parameterUri = parameterUri.substring(0,parameterUri.lastIndexOf("&"));
                    url += parameterUri;
                }
                try {
                    responseData = restTemplate.getForObject(url, ResponseData.class);
                }catch(Exception e){
                    throw new Exception("服务调用异常>>>");
                }
                break;
            case "POST":
                try {
                    responseData = restTemplate.postForObject(url, bodyObj, ResponseData.class);
                }catch(Exception e){
                    throw new Exception("服务调用异常>>>");
                }
                break;
            case "PUT":
                try {
                    responseData = this.exchange(url, HttpMethod.PUT, bodyObj, ResponseData.class);
                }catch(Exception e){
                    throw new Exception("服务调用异常>>>");
                }
                break;
            case "DELETE":
                try {
                    responseData = this.exchange(url, HttpMethod.DELETE, bodyObj, ResponseData.class);
                }catch(Exception e){
                    throw new Exception("服务调用异常>>>");
                }
                break;
        }
        return parseResult(responseData,clazz);
    }

    /**
     * 解析结果
     * @param responseData
     * @param cls
     * @return
     */
    private Object parseResult(ResponseData responseData, Class cls){
        if(null != responseData){
            Object data = responseData.getData();
            if(null == data){
                return null;
            }
            if(data instanceof List){
                List listObj = (List)data;
                if(null == listObj || listObj.size() == 0){
                    return null;
                }
                return JSONArray.parseArray(JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue),cls==Void.class?Object.class:cls);
            }else{
                return JSONObject.parseObject(JSONObject.toJSONString(data,SerializerFeature.WriteMapNullValue),cls==Void.class?Object.class:cls);
            }
        }
        return null;
    }

    private <T> T exchange(String url, HttpMethod method, Object bodyObj, Class<T> bodyType) {
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), Charset.forName("UTF-8"));
        // 请求体
        headers.setContentType(mediaType);
        String str = null;
        if(null != bodyObj){
            str = JSONObject.toJSONString(bodyObj);
        }
        // 发送请求
        HttpEntity<String> entity = new HttpEntity<>(str, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> resultEntity = restTemplate.exchange(url, method, entity, bodyType);
        return resultEntity.getBody();
    }
}
