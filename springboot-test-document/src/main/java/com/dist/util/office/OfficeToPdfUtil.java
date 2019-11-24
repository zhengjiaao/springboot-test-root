package com.dist.util.office;

import com.dist.util.file.FileUtil;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


/**
 * 需要把动态库：jacob-1.18-x64.dll 单元测试需要手动放入至测试路径下
 * office转pdf工具
 * 使用系统安装的office套件
 */
public class OfficeToPdfUtil {

    private static Logger log = LoggerFactory.getLogger(OfficeToPdfUtil.class);

    private static final int wdFormatPDF = 17;
    private static final int xlTypePDF = 0;
    private static final int ppSaveAsPDF = 32;

    private static final String PPT = "ppt";
    private static final String PPTX = "pptx";
    private static final String DOC = "doc";
    private static final String DOCX = "docx";
    private static final String PDF = "pdf";
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private static final String TXT = "txt";

    static {
        String libName = "dll/jacob-1.18-x64.dll";
        String jacobPath = OfficeToPdfUtil.class.getClassLoader().getResource("").getPath().replace("%20", " ") + libName;
//		String jacobPath = OfficeToPdfUtil.class.getClassLoader().getResource(libName).getPath().replace("%20", " ");
        log.info("jacob path>>>>>>>>>>>>>>>>>>>>>" + jacobPath);
        System.setProperty("jacob.dll.path", jacobPath);
    }

    /**
     * （DOC、DOCX、TXT、PPT、PPTX、XLS、XLSX）转换成 pdf 格式文件
     * @param inputFile 待转的文件地址
     * @param pdfFile   转之后的存储地址
     * @return 转换是否成功
     */
    public static boolean convert2PDF(String inputFile, String pdfFile) {
        String suffix = getFileSuffix(inputFile);
        String newInputFile = "";
        String newPdfFile = "";
        // 如果文件路径中含有空格，则需要先替换掉
        if (inputFile.contains(" ")) {
            newInputFile = inputFile.replace(" ", "");
            newPdfFile = pdfFile.replace(" ", "");
            File dir = new File(newInputFile.substring(0, newInputFile.lastIndexOf(File.separator)));
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            FileUtil.copyFile(inputFile, newInputFile);
            File file = new File(newInputFile);
            if (!file.exists()) {
                log.error("文件不存在！");
                return false;
            }
            if (PDF.equals(suffix)) {
                log.error("PDF not need to convert!");
                return false;
            }
            if (DOC.equals(suffix) || DOCX.equals(suffix) || TXT.equals(suffix)) {
                boolean flag = word2PDF(newInputFile, newPdfFile);
                new File(newInputFile).delete();
                if (flag) {
                    FileUtil.copyFile(newPdfFile, pdfFile);
                }
                new File(newPdfFile).delete();
                return flag;
            } else if (PPT.equals(suffix) || PPTX.equals(suffix)) {
                boolean flag = ppt2PDF(newInputFile, newPdfFile);
                new File(newInputFile).delete();
                if (flag) {
                    FileUtil.copyFile(newPdfFile, pdfFile);
                }
                new File(newPdfFile).delete();
                return flag;
            } else if (XLS.equals(suffix) || XLSX.equals(suffix)) {
                boolean flag = excel2PDF(newInputFile, newPdfFile);
                new File(newInputFile).delete();
                if (flag) {
                    FileUtil.copyFile(newPdfFile, pdfFile);
                }
                new File(newPdfFile).delete();
                return flag;
            } else {
                log.error("文件格式不支持转换!");
                return false;
            }
        } else {
            File file = new File(inputFile);
            if (!file.exists()) {
                log.error("文件不存在！");
                return false;
            }
            if (PDF.equals(suffix)) {
                log.error("PDF not need to convert!");
                return false;
            }
            if (DOC.equals(suffix) || DOCX.equals(suffix) || TXT.equals(suffix)) {
                return word2PDF(inputFile, pdfFile);
            }
            if (PPT.equals(suffix) || PPTX.equals(suffix)) {
                return ppt2PDF(inputFile, pdfFile);
            } else if (XLS.equals(suffix) || XLSX.equals(suffix)) {
                return excel2PDF(inputFile, pdfFile);
            } else {
                log.error("文件格式不支持转换!");
                return false;
            }
        }
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    private static String getFileSuffix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }

    /**
     * word2pdf
     *
     * @param inputFile doc路径
     * @param pdfFile   pdf路径
     * @return 判断是否转换成功
     */
    private static boolean word2PDF(String inputFile, String pdfFile) {
        ActiveXComponent app = null;
        try {
            long startTime = System.currentTimeMillis();
            // 打开word应用程序
            app = new ActiveXComponent("Word.Application");
            // 设置word不可见
            app.setProperty("Visible", false);
            // 获得word中所有打开的文档,返回Documents对象
            Dispatch docs = app.getProperty("Documents").toDispatch();
            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            Dispatch doc = Dispatch.call(docs, "Open", inputFile, false, true).toDispatch();
            Dispatch.call(doc, "ExportAsFixedFormat", pdfFile, wdFormatPDF);
            // 关闭文档
            Dispatch.call(doc, "Close", false);
            // 关闭word应用程序
            app.invoke("Quit", 0);
            long endTime = System.currentTimeMillis();
            log.info("word文件转换为pdf格式耗时/毫秒:" + (endTime - startTime));
            return true;
        } catch (Exception e) {
            System.out.println("word转PDF格式失败" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * excel文件转PDF
     * @param inputFile
     * @param pdfFile
     * @return
     */
    public static boolean excel2PDF(String inputFile, String pdfFile) {
        try {
            long startTime = System.currentTimeMillis();
            ActiveXComponent app = new ActiveXComponent("Excel.Application");
            app.setProperty("Visible", false);
            Dispatch excels = app.getProperty("Workbooks").toDispatch();
            Dispatch excel = Dispatch.call(excels, "Open", inputFile, false, true).toDispatch();
            Dispatch.call(excel, "ExportAsFixedFormat", xlTypePDF, pdfFile);
            Dispatch.call(excel, "Close", false);
            app.invoke("Quit");
            long endTime = System.currentTimeMillis();
            log.info("xls文件转换为pdf格式 耗时/毫秒:" + (endTime - startTime));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private static boolean ppt2PDF(String inputFile, String pdfFile) {
        try {
            long startTime = System.currentTimeMillis();
            ActiveXComponent app = new ActiveXComponent("PowerPoint.Application");
            Dispatch ppts = app.getProperty("Presentations").toDispatch();
            Dispatch ppt = Dispatch.call(ppts, "Open", inputFile, true, true, true).toDispatch();
            Dispatch.call(ppt, "SaveAs", pdfFile, ppSaveAsPDF);
            Dispatch.call(ppt, "Close");
            app.invoke("Quit");
            long endTime = System.currentTimeMillis();
            log.info("ppt文件转换为pdf格式 耗时/毫秒:" + (endTime - startTime));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
