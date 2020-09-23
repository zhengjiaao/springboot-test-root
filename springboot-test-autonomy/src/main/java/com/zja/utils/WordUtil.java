//package com.dist.utils;
//
//import com.dist.dto.WordReportTemplateDTO;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import sun.misc.BASE64Encoder;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
///**
// * freemarker 模板生成html，word，xml等页面或静态页面
// *
// * 参考：https://www.cnblogs.com/x54256/p/8669120.html
// *
// * @author yangmin
// * @date 2019/1/23 11:24
// * @desc
// */
//public class WordUtil {
//
//    /**
//     * 生成word文件
//     * @param dataMap word中需要展示的动态数据，用map集合来保存
//     * @param templateName word模板名称，例如：test.ftl
//     * @param filePath 文件生成的目标路径，例如：D:/wordFile/
//     * @param fileName 生成的文件名称，例如：test.doc
//     */
//    @SuppressWarnings("unchecked")
//    public static void createWord(Map dataMap, String templateName, String filePath, String fileName){
//        try {
//            //创建配置实例
//            Configuration configuration = new Configuration();//Configuration();
//            //设置编码
//            configuration.setDefaultEncoding("UTF-8");
//            //ftl模板文件
//            configuration.setClassForTemplateLoading(WordUtil.class,"/templates");
//            //获取模板
//            Template template = configuration.getTemplate(templateName);
//            //输出文件
//            File outFile = new File(filePath + File.separator + fileName);
//            //如果输出目标文件夹不存在，则创建
//            if (!outFile.getParentFile().exists()){
//                outFile.getParentFile().mkdirs();
//            }
//            //将模板和数据模型合并生成文件
//            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
//            //生成文件
//            template.process(dataMap, out);
//            //关闭流
//            out.flush();
//            out.close();
//            System.out.println("==== 生成word成功！ ====");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args){
//        Map<String, Object> dataMap = new HashMap<String, Object>();
//        dataMap.put("title", "2017年全国土地供应分析评价报告");
//        dataMap.put("unit", "自然资源部");
//        dataMap.put("datetime", "2018 年 12 月");
//        List<WordReportTemplateDTO> sections = new LinkedList<>();
//
//        WordReportTemplateDTO section1 = new WordReportTemplateDTO();
//        section1.setName("建设用地");
//        section1.setParagraph("长江经济带建设用地累计供应3.3万平方公里，占全国供应总量的45%，上中下游分别占21%、11%和13%.");
//        section1.setHasImage(false);
//        section1.setImagename("");
//        section1.setImagedata("");
//        List<WordReportTemplateDTO> subsections = new LinkedList<>();
//        WordReportTemplateDTO section11 = new WordReportTemplateDTO();
//        section11.setName("全国供应总量");
//        section11.setParagraph("");
//        section11.setHasImage(true);
//        section11.setImagename("国有建设用地各类型供应情况");
//        section11.setImagedata(WordUtil.getImageBase64("D:/doc/国有建设用地各类型供应情况.png"));
//        WordReportTemplateDTO section12 = new WordReportTemplateDTO();
//        section12.setName("各省供应总量");
//        section12.setParagraph("");
//        section12.setHasImage(true);
//        section12.setImagename("国有建设用地供应与经济增长");
//        section12.setImagedata(WordUtil.getImageBase64("D:/doc/国有建设用地供应与经济增长.png"));
//        subsections.add(section11);
//        subsections.add(section12);
//        section1.setSections(subsections);
//        sections.add(section1);
//
//        WordReportTemplateDTO section2 = new WordReportTemplateDTO();
//        section2.setName("建设用地");
//        section2.setParagraph("2001年以来，长江经济带建设用地累计供应3.3万平方公里，占全国供应总量的45%，上中下游分别占21%、11%和13%.");
//        section2.setHasImage(false);
//        section2.setImagename("");
//        section2.setImagedata("");
//        List<WordReportTemplateDTO> subsections2 = new LinkedList<>();
//        WordReportTemplateDTO section21 = new WordReportTemplateDTO();
//        section21.setName("全国供应总量");
//        section21.setParagraph("");
//        section21.setHasImage(true);
//        section21.setImagename("国有建设用地各类型供应情况");
//        section21.setImagedata(WordUtil.getImageBase64("D:/doc/国有建设用地各类型供应情况.png"));
//        WordReportTemplateDTO section22 = new WordReportTemplateDTO();
//        section22.setName("各省供应总量");
//        section22.setParagraph("");
//        section22.setHasImage(true);
//        section22.setImagename("国有建设用地供应与经济增长");
//        section22.setImagedata(WordUtil.getImageBase64("D:/doc/国有建设用地供应与经济增长.png"));
//
//        List<WordReportTemplateDTO> subsections3 = new LinkedList<>();
//        WordReportTemplateDTO section221 = new WordReportTemplateDTO();
//        section221.setName("省供应总量");
//        section221.setParagraph("");
//        section221.setHasImage(true);
//        section221.setImagename("省有建设用地各类型供应情况");
//        section221.setImagedata(WordUtil.getImageBase64("D:/doc/国有建设用地各类型供应情况.png"));
//        subsections3.add(section221);
//        section22.setSections(subsections3);
//
//        List<WordReportTemplateDTO> subsections4 = new LinkedList<>();
//        WordReportTemplateDTO section2211 = new WordReportTemplateDTO();
//        section2211.setName("市供应总量");
//        section2211.setParagraph("");
//        section2211.setHasImage(true);
//        section2211.setImagename("市有建设用地各类型供应情况");
//        section2211.setImagedata(WordUtil.getImageBase64("D:/doc/国有建设用地各类型供应情况.png"));
//        WordReportTemplateDTO section2212 = new WordReportTemplateDTO();
//        section2212.setName("市供应总量1");
//        section2212.setParagraph("");
//        section2212.setHasImage(false);
//        section2212.setImagename(null);
//        section2212.setImagedata(null);
//
//        subsections4.add(section2211);
//        subsections4.add(section2212);
//        section221.setSections(subsections4);
//
//        subsections2.add(section21);
//        subsections2.add(section22);
//        section2.setSections(subsections2);
//        sections.add(section2);
//        dataMap.put("sections", sections);
//
//        WordUtil.createWord(dataMap, "wordReportTemplate.ftl", "D:/doc/", "用freemarker生成Word文档.doc");
//    }
//
//    /**
//     * 获取图片的base64字符流
//     * @param imgFile
//     * @return
//     */
//    public static String getImageBase64(String imgFile)
//    {
//        InputStream in = null;
//        byte[] data = null;
//        //读取图片字节数组
//        try{
//            in = new FileInputStream(imgFile);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//        //对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);
//    }
//
//}
