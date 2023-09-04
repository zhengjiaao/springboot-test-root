/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-04 12:53
 * @Since:
 */
package com.zja.aspose;

import com.aspose.cells.License;
import com.aspose.cells.Workbook;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/09/04 12:53
 */
public class CellsTest {

    //未授权认证情况：会有水印和数量(页数)限制
    @Test
    public void excelToPdf_test() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        String sourceFile = "D:\\temp\\excel\\sample.xlsx";//输入的文件
        String targetFile = "D:\\temp\\excel\\to\\sample.pdf";//输出的文件
        excelToPdf(sourceFile, targetFile);
    }

    /**
     * Excel转PDF操作
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void excelToPdf(String sourceFile, String targetFile) {
        try {
            long old = System.currentTimeMillis();
            FileOutputStream os = new FileOutputStream(targetFile);
            Workbook excel = new Workbook(sourceFile);//加载源文件数据
            excel.save(os, com.aspose.cells.SaveFormat.PDF);//设置转换文件类型并转换
            os.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改Cells jar包里面的校验
     */
    @Test
    public void modifyExcelJar() {
        try {
            //这一步是完整的jar包路径,选择自己解压的jar目录
            ClassPool.getDefault().insertClassPath("D:\\maven\\aspose-cells-23.2.jar");
            //获取指定的class文件对象
//            CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.cells.License");
            CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.cells.p0h");
            //从class对象中解析获取所有方法
            CtMethod[] methodA = zzZJJClass.getDeclaredMethods();
            for (CtMethod ctMethod : methodA) {
                //获取方法获取参数类型
                CtClass[] ps = ctMethod.getParameterTypes();
                //筛选同名方法，入参是Document
                if (ps.length == 1 && ctMethod.getName().equals("a") && ps[0].getName().equals("org.w3c.dom.Document")) {
                    System.out.println("ps[0].getName==" + ps[0].getName());
                    //替换指定方法的方法体
//                    ctMethod.setBody("{a = this;com.aspose.cells.zblc.a();}");
                    ctMethod.setBody("{a = this;com.aspose.cells.r84.a();}");
                }
            }
            //这一步就是将破译完的代码放在桌面上
            zzZJJClass.writeFile("D:\\maven\\aspose-cells\\");

        } catch (Exception e) {
            System.out.println("错误==" + e);
        }
    }
}
