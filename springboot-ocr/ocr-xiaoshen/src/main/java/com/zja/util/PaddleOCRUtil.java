/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-14 16:22
 * @Since:
 */
package com.zja.util;

import com.alibaba.fastjson.JSONArray;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/11/14 16:22
 */
public class PaddleOCRUtil {
    private PaddleOCRUtil() {

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String ocrImages = ocrImages(Collections.singletonList("D:\\temp\\ocr\\ppocr_img\\imgs\\15.png"), false);
        System.out.println(ocrImages);

        // String ocrPdf = ocrPdf("D:\\temp\\ocr\\ppocr_img\\pdf\\input.pdf", 0, false);
        // System.out.println(ocrPdf);
    }

    public static String ocrImagesNoCoordinates(List<String> imagesPath, boolean useGpu) throws IOException, InterruptedException {
        String ocrData = ocrImages(imagesPath, useGpu);
        return removeCoordinates(ocrData);
    }

    public static String ocrImages(List<String> imagesPath, boolean useGpu) throws IOException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (String imagePath : imagesPath) {
            String result = ocrImage(imagePath, useGpu);
            stringBuilder.append(result).append(",");
        }
        if (stringBuilder.length() > 2) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    public static String ocrPdfNoCoordinates(String pdfPath, int pageNum, boolean useGpu) throws IOException, InterruptedException {
        String ocrData = ocrPdf(pdfPath, pageNum, useGpu);
        return removeCoordinates(ocrData);
    }

    public static String ocrPdf(String pdfPath, int pageNum, boolean useGpu) throws IOException, InterruptedException {
        File pdfFile = new File(pdfPath);
        if (!pdfFile.exists()) {
            throw new RuntimeException("文件不存在.");
        }
        String imageOutputDir = createImageOutputDir(pdfFile.getParentFile().getAbsolutePath());

        List<String> images = pdfToImages(pdfPath, imageOutputDir, pageNum);

        return ocrImages(images, useGpu);
    }

    private static String removeCoordinates(String data) {
        if (StringUtils.isEmpty(data)) {
            return data;
        }

        if ("[[]]".equals(data) || "[]".equals(data)) {
            return "[]";
        }

        JSONArray jsonArray = JSONArray.parseArray(data);
        int i = 0;
        for (Object item : jsonArray) {
            if (item instanceof JSONArray) {
                JSONArray array2 = jsonArray.getJSONArray(i);
                removeFirstGroup(array2);
            }
            i++;
        }

        return jsonArray.toJSONString();
    }

    private static void removeFirstGroup(JSONArray jsonArray) {
        for (Object item : jsonArray) {
            if (item instanceof JSONArray) {
                JSONArray subArray = (JSONArray) item;
                System.out.println(subArray);
                if (subArray.size() > 0) {
                    subArray.remove(0);
                }
            }
        }
    }

    // 推荐 ocrImages 保持格式一致。
    private static String ocrImage(String inputImagePath, boolean useGpu) throws IOException, InterruptedException {
        String command = "paddleocr --image_dir " + inputImagePath + " --use_angle_cls true" + " --use_gpu " + useGpu;
        return PaddleOCRCommandUtil.command(command).toString();
    }

    /**
     * todo 有个小问题，当 pageNum=0 ，提取全部内容，此时，无法进行区分提取的哪页数据内容。
     *
     * @param pageNum 控制推理前面几页，默认为 0，表示推理所有页。
     */
    private static String ocrPdfOld(String inputPdfPath, int pageNum, boolean useGpu) throws IOException, InterruptedException {
        String command = "paddleocr --image_dir " + inputPdfPath + " --use_angle_cls true" + " --use_gpu " + useGpu + " --page_num " + pageNum;
        return PaddleOCRCommandUtil.command(command).toString();
    }

    /**
     * pdf 转为 图像
     *
     * @param pdfFilePath pdf路径
     * @param page        转换第几页未图像，默认为 0，表示转换所有页未图像。
     * @return 返回图像列表
     */
    private static List<String> pdfToImages(String pdfFilePath, String imageOutputDir, int page) {
        return isAllPage(page) ? pdfAllPageToImage(pdfFilePath, imageOutputDir) : pdfOnePageToImage(pdfFilePath, imageOutputDir, page);
    }

    private static boolean isAllPage(int page) {
        return page < 1;
    }

    // pdf 指定页转为图像
    private static List<String> pdfOnePageToImage(String pdfFilePath, String imageOutputDir, int page) {
        List<String> imageList = new ArrayList<>();
        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI((page - 1), 300);

            String outputFilePath = imageOutputDir + File.separator + page + "_page.png";
            ImageIO.write(image, "png", new File(outputFilePath));
            imageList.add(outputFilePath);

            return imageList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // pdf 所有页转为图像
    private static List<String> pdfAllPageToImage(String pdfFilePath, String imageOutputDir) {
        List<String> imageList = new ArrayList<>();
        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 300); // 设置dpi（像素密度）
                String outputFilePath = imageOutputDir + File.separator + (pageIndex + 1) + "_page.jpg";
                ImageIO.write(image, "png", new File(outputFilePath));
                imageList.add(outputFilePath);
            }

            return imageList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String createImageOutputDir(String parentFilePath) {
        String imageOutputDir = parentFilePath + File.separator + "images";
        File imageOutputDirFile = new File(imageOutputDir);
        if (!imageOutputDirFile.exists()) {
            imageOutputDirFile.mkdirs();
        }
        return imageOutputDir;
    }
}
