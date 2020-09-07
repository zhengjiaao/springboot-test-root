package com.dist.utils.dayu;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 向大鱼平台提起手机验证码服务
 */
@Slf4j
@Component
public class DayuSendMessage {

    private static final String URL = "http://gw.api.taobao.com/router/rest";     //  大鱼短信服务url

    @Value("${dayu.appKey}")
    private String appKey;     // 注册后生成的appkey
    @Value("${dayu.appSecret}")
    private String appSecret;      // 注册后生成的secret
    @Value("${dayu.userId}")
    private String userId;     // 公共回传参数
    @Value("${dayu.smsType}")
    private String smsType;     // 短信类型
    @Value("${dayu.signName}")
    private String signName;     // 短信签名
    @Value("${dayu.templateCode}")
    private String templateCode;     // 短信模板ID


    /**
     * 向淘宝大鱼短信平台发送请求
     * @param tel   手机号
     * @param code  验证码
     * @return
     */
    public boolean taobaoSendMoblieMessage(String tel, String code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        String smsParamJson = json.toJSONString();
        Map<String,String> result = (Map<String, String>) taobaoSendMoblieMessage(userId,smsType,signName,tel,smsParamJson,templateCode);
        if (result.containsKey("success")) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param userId 公共回传参数
     * @param signName 短信签名（需要在控制台添加短信签名，或者使用一个有效签名即可）
     * @param tel 短信接收号码，群发短信需传入多个号码，以英文逗号分隔 如:1390000000,1380000000
     * @param smsParamJson 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
     */
    public Object taobaoSendMoblieMessage(String userId, String smsType,String signName, String tel, String smsParamJson, String templateCode) {

        TaobaoClient client = new DefaultTaobaoClient(URL, appKey, appSecret);

        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();

        req.setExtend(userId); // 公共回传参数
        req.setSmsType(smsType); // 短信类型
        req.setSmsFreeSignName(signName); // 短信签名
        req.setSmsParamString(smsParamJson); // 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
        req.setRecNum(tel); // 短信接收号码，群发短信需传入多个号码，以英文逗号分隔 如:1390000000,1380000000
        req.setSmsTemplateCode(templateCode); // 短信模板ID，传入的模板必须是在阿里大于“管理中心-短信模板管理”中的可用模板

        AlibabaAliqinFcSmsNumSendResponse rsp;
        Map<String, String> result = new HashMap<String, String>();
        try {
            rsp = client.execute(req);
            String respBody = rsp.getBody();
            if (respBody.contains("error_response")) {
                result.put("error", respBody);
                return result;
            }
            result.put("success", respBody);
            return result;
        } catch (ApiException e) {
            result.put("error", e.getErrMsg());
            return result;
        }
    }
}
