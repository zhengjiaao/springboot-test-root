package com.dist.utils;

/**
 * @author yinxp@dist.com.cn
 * @date 2019/4/28
 */
public abstract class NumberUtil {


    // private static final String[] cnNumbers = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    private static final String[] cnNumbers = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
    // String series[] = new String[] { "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿" };
    // String series[] = new String[] { "个", "十", "百", "千", "万", "十", "百", "千", "亿" };
    private static final String series[] = new String[] { "", "十", "百", "千", "万", "十", "百", "千", "亿" };


    /**
     * 阿拉伯数字转中文汉字
     * @param number
     * @return
     */
    public static String numberToChinaStr(Integer number) {
        if (number == null || number.longValue() < 0) {
            return null;
        }
        String s = String.valueOf(number);
        StringBuffer sb = new StringBuffer();
        // 先把数字转为中文
        for (int i = 0; i < s.length(); i++) {
            String index = String.valueOf(s.charAt(i));
            sb = sb.append(cnNumbers[Integer.parseInt(index)]);
        }

        String sss = String.valueOf(sb);
        int i = 0;
        // 再加上位符：十、百、千 ...
        for (int j = sss.length(); j > 0; j--) {
            sb = sb.insert(j, series[i++]);
        }
        return sb.toString();
    }

}
