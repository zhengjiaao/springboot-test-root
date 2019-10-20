package com.dist.utils;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 自定义druid密码回调
 * @author weifj
 *
 */
@Component("dbPasswordCallback")
//@Lazy(value = true)
public class DBPasswordCallbackUtil extends DruidPasswordCallback {

	private static final long serialVersionUID = 1L;

	@Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        // 连接数据源中密码密文
        String pwdEncrypt = properties.getProperty("password");
        System.out.println("pwdEncrypt="+pwdEncrypt);
        if (StringUtils.isNotBlank(pwdEncrypt)) {
            try {
                //这里的password是将配置得到的密码进行解密之后的值
                //TODO 自定义加解密-将pwd进行解密;
                //String password = DesEncryptRSA.getInstance().decrypt(pwdEncrypt);
                //setPassword(password.toCharArray());

                System.out.println("password进来了");
                //使用默认加密和解密方式测试
                String password =ConfigTools.decrypt(pwdEncrypt);
                System.out.println("password= "+password);
                System.out.println("password.toCharArray()= "+password.toCharArray());
                setPassword(password.toCharArray());
            } catch (Exception e) {
                setPassword(pwdEncrypt.toCharArray());
            }
        }
    }

    /**
     * 测试密码加解密
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

	    String password = "123456";

	    //1.默认私钥加密 MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAocbCrurZGbC5GArEHKlAfDSZi7gFBnd4yxOt0rwTqKBFzGyhtQLu5PRKjEiOXVa95aeIIBJ6OhC2f8FjqFUpawIDAQABAkAPejKaBYHrwUqUEEOe8lpnB6lBAsQIUFnQI/vXU4MV+MhIzW0BLVZCiarIQqUXeOhThVWXKFt8GxCykrrUsQ6BAiEA4vMVxEHBovz1di3aozzFvSMdsjTcYRRo82hS5Ru2/OECIQC2fAPoXixVTVY7bNMeuxCP4954ZkXp7fEPDINCjcQDywIgcc8XLkkPcs3Jxk7uYofaXaPbg39wuJpEmzPIxi3k0OECIGubmdpOnin3HuCP/bbjbJLNNoUdGiEmFL5hDI4UdwAdAiEAtcAwbm08bKN7pwwvyqaCBC//VnEWaq39DCzxr+Z2EIk=
        String encrypt = ConfigTools.encrypt(password);
        System.out.println("encryptKey:"+encrypt);
        //加密后的密码123
        String psw = "Biyu5YzU+6sxDRbmWEa3B2uUcImzDo0BuXjTlL505+/pTb+/0Oqd3ou1R6J8+9Fy3CYrM18nBDqf6wAaPgUGOg==";
        //默认公钥解密 MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHGwq7q2RmwuRgKxBypQHw0mYu4BQZ3eMsTrdK8E6igRcxsobUC7uT0SoxIjl1WveWniCASejoQtn/BY6hVKWsCAwEAAQ==
        String decrypt = ConfigTools.decrypt(psw);
        System.out.println("decrypt="+decrypt);


        //2.通过druid-1.1.10.jar 生成私钥和公钥及密文密码，且私钥加密password 明文密码 123456
        //私钥加密
        String privateKey ="MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAjpshnY/9ePWSszHMsq8xe/Rl81wNrkkA5RLAVKfTDM5U1uAST3nWPujm8dxC/QYMtd90gPYrfuuW6HU14K0r4wIDAQABAkAxpq2jkFiuXsI/Bd3BcOIHyGYevzQ8NErNdyPj1bLTyBQqEl2E8PaHmorw1q4ePJTYyxyJ45SoPnR2jIDL5GTBAiEA4ji7sROZoo9sfJfJrjjIzGQb0SfBmwE5dyg0wLvjprECIQChYLMjPophf5ciLKOXBcYoiqskj2SN2GEixNdAS/lI0wIhAKiNWNthHTGP52cVuUFEMeiIFmnKU2hoiyaLIHMSC1ahAiBL7OUbZedRa+g9aeclvM+4b1WKA/T5T56T7yye0Z/dhQIhAKhnwa8LNkB2UPGhz/4QGtPyz6E52YiVs0sIr7jqETca";
        String passwordencrypt = ConfigTools.encrypt(privateKey,password);
        System.out.println("passwordencrypt="+passwordencrypt);
        //加密后的密码123
        String pwd = "WXMhyvf/mfrP3gRxWHDN1P22aa+FnH4vwclmFEO0GK5YtcEi/lVVvc93KN/T39tC1WEij6tMZCU+e87JoQ3N0g==";
        //公钥解密
        String publicKey="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI6bIZ2P/Xj1krMxzLKvMXv0ZfNcDa5JAOUSwFSn0wzOVNbgEk951j7o5vHcQv0GDLXfdID2K37rluh1NeCtK+MCAwEAAQ==";
        String pwddecrypt = ConfigTools.decrypt(publicKey,pwd);
        System.out.println("pwddecrypt="+pwddecrypt);

        //3.使用RSA自定义生成的私钥和公钥
        //自定义公钥加密

        //自定义私钥解密

    }
	
}
