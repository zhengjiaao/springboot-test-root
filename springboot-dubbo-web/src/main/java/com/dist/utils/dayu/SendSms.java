package com.dist.utils.dayu;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wsw
 * @Date 2019/7/20
 */
@Slf4j
@Component
public class SendSms {

    private final Logger LOG = LoggerFactory.getLogger(SendSms.class);

    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    public static final String REP_CODE = "OK";

    @Value("${ali.app_key}")
    private String appKey;     // 注册后生成的appkey

    @Value("${ali.app_secret}")
    private String appSecret;      // 注册后生成的secret

    @Value("${ali.sign_name}")
    private String signName;     // 短信签名

    @Value("${ali.template_code}")
    private String templateCode;     // 短信模板ID



    public Map<String, String> sendSms(String phone, String code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        String smsParamJson = json.toJSONString();
        return taobaoSendSms(phone, smsParamJson);
    }

    // 淘宝发送短信
    private Map<String, String> taobaoSendSms(String phone, String code) {

        DefaultProfile profile = DefaultProfile.getProfile("default", appKey, appSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(DOMAIN);
        request.setAction("SendSms");
        request.setVersion("2017-05-25");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", code);

        Map<String, String> result = new HashMap<>();
        try {
            CommonResponse response = client.getCommonResponse(request);
            String rep = response.getData();
            JSONObject object = JSONObject.parseObject(rep);
            String repCode = object.getString("Code");
            String repMessage = object.getString("Message");
            if (REP_CODE.equalsIgnoreCase(repCode)) {
                result.put("success", REP_CODE);
            } else {
                result.put("error", repMessage);

            }
        } catch (ClientException e) {
            LOG.error(">send sms error {}", e.getErrMsg());
            result.put("error", e.getErrMsg());
        }
        return result;
    }

}
