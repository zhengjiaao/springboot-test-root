/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-04 15:15
 * @Since:
 */
package com.zja.aspose;

import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/09/04 15:15
 */
public class SlidesTest {


    //未授权认证情况：会有水印和数量(页数)限制
    @Test
    public void PptToPdf_test() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        String sourceFile = "D:\\temp\\ppt\\年终总结.pptx";//输入的文件
        String targetFile = "D:\\temp\\ppt\\to\\年终总结.pdf";//输出的文件
        PptToPdf(sourceFile, targetFile);
    }

    public static void PptToPdf(String sourceFile, String targetFile) {
        try {
            long old = System.currentTimeMillis();
            FileOutputStream os = new FileOutputStream(targetFile);
            Presentation ppt = new Presentation(sourceFile);//加载源文件数据
            ppt.save(os, com.aspose.slides.SaveFormat.Pdf);//设置转换文件类型并转换
            os.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改Slides jar包里面的校验
     */
    @Test
    public void modifyPptJar() {
        try {
            //这一步是完整的jar包路径,选择自己解压的jar目录
            ClassPool.getDefault().insertClassPath("D:\\maven\\aspose-slides-23.1-jdk16.jar");
            CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.slides.internal.oh.public");
            CtMethod[] methodA = zzZJJClass.getDeclaredMethods();
            for (CtMethod ctMethod : methodA) {
                CtClass[] ps = ctMethod.getParameterTypes();
                if (ps.length == 3 && ctMethod.getName().equals("do")) {
                    System.out.println("ps[0].getName==" + ps[0].getName());
                    ctMethod.setBody("{}");
                }
            }
            //这一步就是将破译完的代码放在桌面上
            zzZJJClass.writeFile("D:\\maven\\aspose-slides\\");
        } catch (Exception e) {
            System.out.println("错误==" + e);
        }
    }

}
