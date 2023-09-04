# springboot-file-preview-aspose

- 官网下载：https://releases.aspose.com/words/java/

**Aspose 文档转换、文档预览**


## Word转PDF操作

```java
    //未授权认证情况：会有水印和数量(页数)限制
    @Test
    public void doc2pdf_test() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        String sourceFile = "D:\\temp\\word\\341000黄山市2020年年度体检报告.docx";//输入的文件
        String targetFile = "D:\\temp\\word\\to\\341000黄山市2020年年度体检报告.pdf";//输出的文件
        doc2pdf(sourceFile, targetFile);
    }

    /**
     * Word转PDF操作
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void doc2pdf(String sourceFile, String targetFile) {
        try {
            long old = System.currentTimeMillis();
            FileOutputStream os = new FileOutputStream(targetFile);
            com.aspose.words.Document doc = new com.aspose.words.Document(sourceFile);
            doc.save(os, com.aspose.words.SaveFormat.PDF);
            os.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

## PDF转Word操作

```java
    //未授权认证情况：会有水印和数量(页数)限制
    @Test
    public void pdf2doc_test() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        String sourceFile = "D:\\temp\\pdf\\800M.pdf";//输入的文件
        String targetFile = "D:\\temp\\pdf\\to\\800M.docx";//输出的文件
        pdf2doc(sourceFile, targetFile);
    }

    /**
     * PDF转Word操作
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void pdf2doc(String sourceFile, String targetFile) {
        try {
            long old = System.currentTimeMillis();
            FileOutputStream os = new FileOutputStream(targetFile);
            com.aspose.pdf.Document doc = new com.aspose.pdf.Document(sourceFile);//加载源文件数据
            doc.save(os, com.aspose.pdf.SaveFormat.DocX);//设置转换文件类型并转换
            os.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```


## 破解方案

> 仅供学习，不推荐商用，支持正版。

```java
   /**
     * 修改words jar包里面的校验
     */
    @Test
    public void modifyWordsJar() {
        try {
            //这一步是完整的jar包路径,选择自己解压的jar目录
            ClassPool.getDefault().insertClassPath("D:\\maven\\aspose-words-23.5-jdk17.jar");
            //获取指定的class文件对象
            CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.words.zzKH");
            //从class对象中解析获取指定的方法
            CtMethod[] methodA = zzZJJClass.getDeclaredMethods("zzYEV");
            //遍历重载的方法
            for (CtMethod ctMethod : methodA) {
                CtClass[] ps = ctMethod.getParameterTypes();
                if (ctMethod.getName().equals("zzYEV")) {
                    System.out.println("ps[0].getName==" + ps[0].getName());
                    //替换指定方法的方法体
//                    ctMethod.setBody("{this.zzZ3l = new java.util.Date(Long.MAX_VALUE);this.zzWSL = com.aspose.words.zzYeQ.zzXgr;zzWiV = this;}");
                    ctMethod.setBody("{this.zzY2p = new java.util.Date(Long.MAX_VALUE);this.zzXDf = com.aspose.words.zzYiF.zzZik;zzVQN = this;}");
                }
            }
            //这一步就是将破译完的代码放在桌面上
            zzZJJClass.writeFile("D:\\maven\\aspose-words\\");

            //获取指定的class文件对象
            CtClass zzZJJClassB = ClassPool.getDefault().getCtClass("com.aspose.words.zzM9");
            //从class对象中解析获取指定的方法
            CtMethod methodB = zzZJJClassB.getDeclaredMethod("zzXLz");
            //替换指定方法的方法体
            methodB.setBody("{return 256;}");
            //这一步就是将破译完的代码放在桌面上
            zzZJJClassB.writeFile("D:\\maven\\aspose-words\\");
        } catch (Exception e) {
            System.out.println("错误==" + e);
        }

    }
```