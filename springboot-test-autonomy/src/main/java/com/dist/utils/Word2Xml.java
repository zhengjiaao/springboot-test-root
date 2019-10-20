package com.dist.utils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.File;

/**
 * freemarker 模板生成html，word，xml等页面或静态页面
 *
 * 参考：https://www.cnblogs.com/x54256/p/8669120.html
 * @author zhengja@dist.com.cn
 * @data 2019/4/29 15:10
 */
public class Word2Xml {

    /**
     * @param filePath    word目录
     * @param xmlFilePath 生成xml存放路径
     * @Description:
     * @author Administrator
     */
    public static void wordToXml(String filePath, String xmlFilePath) {
        try {
            ActiveXComponent app = new ActiveXComponent("Word.Application"); //启动word
            app.setProperty("Visible", new Variant(false)); //为false时设置word不可见，为true时是可见要不然看不到Word打打开文件的过程
            Dispatch docs = app.getProperty("Documents").toDispatch();

            //打开编辑器
            Dispatch doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[]{filePath, new Variant(false), new Variant(true)}, new int[1]).toDispatch(); //打开word文档
            Dispatch.call(doc, "SaveAs", xmlFilePath, 11);//xml文件格式宏11
            Dispatch.call(doc, "Close", false);
            app.invoke("Quit", 0);
            System.out.println("---------word转换完成--------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        ///word转成 xml 再修改后最为 ftl ，作为模板
        //wordToXml("D:/doc/报告模板 - 副本.docx","D:/doc/test.xml");

        //修改文件后缀，ftl用做 freemarker模板
        File file=new File("D:/doc/test.xml");
        file.renameTo(new File("D:/doc/wordReportTemplate.ftl"));

    }
}