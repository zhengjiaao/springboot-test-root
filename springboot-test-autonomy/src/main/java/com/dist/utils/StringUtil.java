package com.dist.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String的工具类。
 *
 * @author 何顺
 * @create 2014年12月11日
 */
public class StringUtil extends StringUtils {

	private static String NumRegex = "^(\\d+)(.*)";
	/**
	 *  中文unicode编码范围
	 */
	private static String ZhUnicode = "[\u4e00-\u9fa5]";
    /**
     * 分离以数字开头的字符串
     * @author 何顺
     * @param str
     * @return
     */
    public static String splitNumberStrat(String str) {
        Pattern pattern = Pattern.compile(NumRegex);
        Matcher matcher = pattern.matcher(str);
      //数字开头
        if (matcher.matches()) {
            return matcher.group(2);
        }
        return str;
    }

    /**
     * utf-8格式解码。
     * @param str 字符串
     * @return 解码后的子ufc
     * @throws UnsupportedEncodingException 转码异常
     */
    public static String utf8Dncode(String str) throws UnsupportedEncodingException {
        if (isBlank(str)) {
            return "";
        }
        return String.valueOf(Charset.forName("UTF-8").encode(str));
        //        return java.net.URLDecoder.decode(str, "UTF-8");
    }

    /**
     * urf-8格式转码。
     * @param str 字符串
     * @return 转码后的字符串
     * @throws UnsupportedEncodingException 转码异常
     */
    public static String utf8Encode(String str) throws UnsupportedEncodingException {
        if (isBlank(str)) {
            return "";
        }
        return java.net.URLEncoder.encode(str, "UTF-8");
    }

    public static boolean isUTF8(String str) throws UnsupportedEncodingException {
        if (Charset.forName("GB2312").newEncoder().canEncode(str)) {
            return true;
        }
        return false;
    }

    /**
     * 将一个字符串的首字母改为小写
     * @param str 源字符串
     * @return
     */
    public static String toLowerFirstChar(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 将字符串的首字母大写
     * @param str 源字符串
     */
    public static String toUpperFirstChar(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    /**
    * 返回中文首字母
    * @param strChinese
    * @param bUpCase
    * @return
    */
       public static String getPYIndexStr(String strChinese, boolean bUpCase){

           try{

               StringBuffer buffer = new StringBuffer();

               byte b[] = strChinese.getBytes("GBK");//把中文转化成byte数组

               for(int i = 0; i < b.length; i++){

                   if((b[i] & 255) > 128){

                       int char1 = b[i++] & 255;

                       char1 <<= 8;//左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方

                       int chart = char1 + (b[i] & 255);

                       buffer.append(getPYIndexChar((char)chart, bUpCase));

                       continue;

                   }

                   char c = (char)b[i];

                   if(!Character.isJavaIdentifierPart(c)) {
                	 //确定指定字符是否可以是 Java 标识符中首字符以外的部分。
                       c = 'A';
                   }
                   buffer.append(c);

               }

               return buffer.toString();

           }catch(Exception e){

               e.printStackTrace();
           }

           return null;

       }

       /**
        * 得到首字母
        * @param strChinese
        * @param bUpCase
        * @return
        */
       private static char getPYIndexChar(char strChinese, boolean bUpCase){

           int charGBK = strChinese;

           char result;

           if(charGBK >= 45217 && charGBK <= 45252) {
               result = 'A';
           }
           else if(charGBK >= 45253 && charGBK <= 45760) {
               result = 'B';
           }
           else if(charGBK >= 45761 && charGBK <= 46317) {
               result = 'C';
           }
           else if(charGBK >= 46318 && charGBK <= 46825) {
               result = 'D';
           }
           else if(charGBK >= 46826 && charGBK <= 47009) {
               result = 'E';
           }
           else if(charGBK >= 47010 && charGBK <= 47296) {

               result = 'F';
           }
           else if(charGBK >= 47297 && charGBK <= 47613) {

               result = 'G';
           }
           else if(charGBK >= 47614 && charGBK <= 48118) {
               result = 'H';
           }
           else if(charGBK >= 48119 && charGBK <= 49061) {
               result = 'J';
           }
           else if(charGBK >= 49062 && charGBK <= 49323) {
               result = 'K';
           }
           else if(charGBK >= 49324 && charGBK <= 49895) {
               result = 'L';
           }
           else if(charGBK >= 49896 && charGBK <= 50370) {
               result = 'M';
           }
           else if(charGBK >= 50371 && charGBK <= 50613) {
               result = 'N';
           }
           else if(charGBK >= 50614 && charGBK <= 50621) {
               result = 'O';
           }
           else if(charGBK >= 50622 && charGBK <= 50905) {
               result = 'P';
           }
           else if(charGBK >= 50906 && charGBK <= 51386) {
               result = 'Q';
           }
           else if(charGBK >= 51387 && charGBK <= 51445) {
               result = 'R';
           }
           else if(charGBK >= 51446 && charGBK <= 52217) {
               result = 'S';
           }
           else if(charGBK >= 52218 && charGBK <= 52697) {
               result = 'T';
           }
           else if(charGBK >= 52698 && charGBK <= 52979) {
               result = 'W';
           }
           else if(charGBK >= 52980 && charGBK <= 53688) {
               result = 'X';
           }
           else if(charGBK >= 53689 && charGBK <= 54480) {
               result = 'Y';
           }
           else if(charGBK >= 54481 && charGBK <= 55289) {
               result = 'Z';
           }
           else {
               result = (char)(65 + (new Random()).nextInt(25));
           }
           if(!bUpCase) {
               result = Character.toLowerCase(result);
           }
           return result;

       }
       
       /**
        * 判断字符串是否为空
        * @param str
        * @return
        */
       public static boolean isNullOrEmpty(String str){
    	   
    	   if(null == str || "".equalsIgnoreCase(str)){
    		   return true;
    	   }
    	   return false;
       }
       /**
        * 字符串转unicode
        * @param text
        * @return
        */
       public static String string2Unicode(String text) {
    	   
    	   if(isNullOrEmpty(text)) {
    		   return "";
    	   }
    	   
    	   StringBuffer unicode = new StringBuffer();
    	   
    	    for (int i = 0; i < text.length(); i++) {
    	 
    	        // 取出每一个字符
    	        char c = text.charAt(i);
    	        // 转换为unicode
    	        unicode.append("\\u" + Integer.toHexString(c));
    	    }
    	 
    	    return unicode.toString();
       }
       
       /**
        * 替换一个字符串中的某些指定字符
        * @param strData String 原始字符串
        * @param regex String 要替换的字符串
        * @param replacement String 替代字符串
        * @return String 替换后的字符串
        */
       private static String replaceString(String strData, String regex,
               String replacement)
       {
           if (strData == null)
           {
               return null;
           }
           int index;
           index = strData.indexOf(regex);
           String strNew = "";
           if (index >= 0)
           {
               while (index >= 0)
               {
                   strNew += strData.substring(0, index) + replacement;
                   strData = strData.substring(index + regex.length());
                   index = strData.indexOf(regex);
               }
               strNew += strData;
               return strNew;
           }
           return strData;
       }
    
       /**
        * 替换字符串中特殊字符，如XML中的大于号>，小于号<等等转义
        */
     public static String encodeSpecialString(String strData)
       {
           if (strData == null)
           {
               return "";
           }
           strData = replaceString(strData, "&", "&amp;");
           strData = replaceString(strData, "<", "&lt;");
           strData = replaceString(strData, ">", "&gt;");
           strData = replaceString(strData, "&apos;", "&apos;");
           strData = replaceString(strData, "\"", "&quot;");
           return strData;
       }
    
       /**
        * 还原字符串中特殊字符，如XML中的大于号>，小于号<等等转义
        */
      public static String decodeSpecialString(String strData)
       {
           strData = replaceString(strData, "&lt;", "<");
           strData = replaceString(strData, "&gt;", ">");
           strData = replaceString(strData, "&apos;", "&apos;");
           strData = replaceString(strData, "&quot;", "\"");
           strData = replaceString(strData, "&amp;", "&");
           return strData;
       }
      /**
       * 判断是否中文字符
       * @param str
       * @return
       */
      public static boolean isChineseChar(String str){
          boolean temp = false;
          Pattern p=Pattern.compile(ZhUnicode);
          Matcher m=p.matcher(str); 
          if(m.find()){ 
              temp =  true;
          }
          return temp;
      }

	/**
	 * 字符串匹配度，匹配不上返回数字0；否则返回匹配次数
	 * @param text 文本
	 * @param keyword  要匹配的关键字
	 * @return
	 */
	public static int hit(String text, String keyword) {

		//e表示需要匹配的数据，使用Pattern建立匹配模式  
		Pattern p = Pattern.compile(keyword);
		//使用Matcher进行各种查找替换操作  
		Matcher m = p.matcher(text);
		int count = 0;
		while (m.find()) {
			count++;
		}

		return count;

	}
	
	 /** 
     * 将字节数组转换成16进制字符串 
     * @param b 
     * @return 
     */  
    public static String byte2hex(byte[] b) {  
        StringBuilder sbDes = new StringBuilder();  
        String tmp = null;  
        for (int i = 0; i < b.length; i++) {  
            tmp = (Integer.toHexString(b[i] & 0xFF));  
            if (1 == tmp.length()) {  
                sbDes.append("0");  
            }  
            sbDes.append(tmp);  
        }  
        return sbDes.toString();  
    }  
	
	public static void main(String[]args) throws Exception{
		
		System.out.println(StringUtil.hit("abcaaabbbbbabab", "ef"));
		
		
	}
}
