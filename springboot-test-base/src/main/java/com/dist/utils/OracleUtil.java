package com.dist.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Blob;

/**
 * @author ChiV3n
 * @date 2018/7/13.
 */
public class OracleUtil {
    private static Logger logger= LoggerFactory.getLogger(OracleUtil.class);
    private OracleUtil(){
        super();
    }
    public static String blob2String(Blob blob){
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = blob.getBinaryStream();
            byte[] buffer = new byte[(int)blob.length()];
            int count =0;
           while ((count=inputStream.read(buffer))>0){
               logger.info("大小： {}",count);
           }
            sb.append(new String(buffer));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return sb.toString();
    }
}
