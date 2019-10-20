package com.dist.util;

import com.dist.entity.BigFileSplit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 大文件分片及合并：使用建议大于100M以上的文件
 * <p>
 * 后端合并：只是测试分片是否成功，不建议做项目使用，因过简单
 * <p>
 * 解决问题：解决前端下载大文件(300M以上)使用response.getOutputStream().write(b, 0, len); 下载太慢，或请求失败，也会导致其它接口请求延迟卡顿
 * 使用流程思想：前端下载大文件-传要下载文件路径->调用切片接口->返回切片后的数据信息给前端->前端开启多线程再调用下载切片接口(和普通下载接口一样)->所有下载完毕->前端进行合并分片
 *
 * @author zhengja@dist.com.cn
 * @data 2019/9/6 13:34
 */
@Component
public class BigFileSplitUtil {

    //文件存储位置：要分片的文件路径 D:/FileTest/文件存储位置
    @Value("${source.fileStorageLocation}")
    private String fileStorageLocation;

    //文件分片位置：分片后的文件路径  D:/FileTest/文件分片位置
    @Value("${source.fileFragmentationLocation}")
    private String fileFragmentationLocation;

    //文件合成位置：分片合成后的路径  D:/FileTest/文件合成位置
    @Value("${source.fileCompositePosition}")
    private String fileCompositePosition;


    /**
     * 文件分片
     *
     * @param split 参数：filePath、destPath、blockSize
     * @return
     */
    public BigFileSplit split(BigFileSplit split) {
        //初始化必须参数，比如说文件切割后的名字，分成几块，之类的
        init(split);

        long beginPos = 0;
        long actualBlockSize = split.getBlockSize();

        for (int i = 0; i < split.getSize(); i++) {
            // 当最后一块的大小比分块的大小小的时候，实际大小为 总长度-起点
            if (split.getFileLength() - beginPos <= split.getBlockSize()) {
                actualBlockSize = split.getFileLength() - beginPos;
            }
            //割文件的具体实现方法
            BigFileSpli(i, beginPos, actualBlockSize, split.getFilePath(), split.getDestFileNames());
            // 起点 = 上一次的结尾 + 实际读取的长度
            beginPos += actualBlockSize;
        }

        return split;
    }

    /**
     * 切割文件的具体实现方法
     *
     * @param index           文件块数
     * @param beginPos        起始点
     * @param actualBlockSize 实际块大小
     * @param filePath        要分割的文件路径
     * @param destFileName    切割后的每块的文件名
     */
    private void BigFileSpli(int index, long beginPos, long actualBlockSize, String filePath, List<String> destFileName) {

        File desc = new File(fileFragmentationLocation + "/" + destFileName.get(index));

        //分片如果存在，跳过分片
        /*if (desc.exists()) {
            return;
        }*/
        //分片如果存在--> 删除分片-->重新分片
        if (desc.exists()){
            desc.delete();
        }

        // 目标文件不存在则创建
        if (!new File(desc.getParent()).exists()) {
            new File(desc.getParent()).mkdirs();
        }

        // 创建源文件
        File src = new File(fileStorageLocation + File.separator + filePath);

        RandomAccessFile raf = null;
        BufferedOutputStream bos = null;
        try {
            // 设置只读读取
            raf = new RandomAccessFile(src, "r");
            bos = new BufferedOutputStream(new FileOutputStream(desc));

            // 读取文件
            raf.seek(beginPos);

            // 缓冲
            byte[] flush = new byte[5 * 1024 * 1024];
            // 记录每次读取的长度
            int len = 0;
            while (-1 != (len = raf.read(flush))) {
                if ((actualBlockSize - len) > 0) {
                    bos.write(flush, 0, len);
                    actualBlockSize -= len;
                } else {
                    // 写出最后一块后关闭循环
                    bos.write(flush, 0, (int) actualBlockSize);
                    break;
                }
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化必须参数，比如说文件切割后的名字，分成几块，之类的
     *
     * @param split
     */
    private void init(BigFileSplit split) {
        File src = null;
        // 如果 文件地址为空，或者文件不存在则终止程序
        if (split.getFilePath() == null || !(src = new File(fileStorageLocation + File.separator + split.getFilePath())).exists()) {
            return;
        }
        // 该地址为文件夹也终止
        if (src.isDirectory()) {
            return;
        }

        File dest = null;
        // 如果目标文件不存在则终止程序
        if (split.getDestPath() == null) {
            return;
        }
        dest = new File(fileFragmentationLocation + File.separator + split.getDestPath());
        // 如果目标文件不存在则创建
        if (!dest.exists()) {
            dest.mkdirs();
        }

        // 文件大小
        split.setFileLength(src.length());

        // 文件名称
        split.setFileName(src.getName());

        if (split.getBlockSize() > split.getFileLength()) {
            split.setBlockSize(split.getFileLength());
        }

        // 分块
        split.setSize((int) Math.ceil((src.length() * 1.0 / split.getBlockSize())));

        //生成分片列表
        List<String> destFileName = new ArrayList<>(split.getSize());

        //生成割后的文件的名称-->列表
        generateFileNames(split.getSize(), split.getDestPath(), split.getFileName(), destFileName);

        split.setDestFileNames(destFileName);
    }

    /**
     * 先生成所有分片的文件名
     */
    private static void generateFileNames(int size, String destPath, String fileName, List<String> destFileName) {
        for (int i = 0; i < size; i++) {
            //注意 如若 filename.txt.tem_1 这种写法 会导致前端获取不到文件大小
            destFileName.add(destPath + "/" + i + "_" + fileName);
        }
    }


    /**
     * 文件合并
     *
     * @param destPath     合并后的文件位置
     * @param size         文件块数
     * @param destFileName 切割后的每块的文件名
     */
    public void fileConsolidated(String destPath, int size, List<String> destFileName) {
        File dest = new File(fileCompositePosition + File.separator + destPath);
        if (destPath == null) {
            return;
        }
        // 目标文件不存在则创建
        if (!new File(dest.getParent()).exists()) {
            new File(dest.getParent()).mkdirs();
        }

        //删除已存在的，重新合成新的文件
        if (dest.exists()) {
            dest.delete();
        }

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            // 此处是追加 true，因为是文件合并
            bos = new BufferedOutputStream(new FileOutputStream(dest, true));
            for (int i = 0; i < size; i++) {

                bis = new BufferedInputStream(new FileInputStream(new File(fileFragmentationLocation + File.separator + destFileName.get(i))));

                byte[] flush = new byte[1024 * 1024];
                int len = 0;
                while (-1 != (len = bis.read(flush))) {
                    bos.write(flush, 0, len);
                }

                bos.flush();
                bis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //一次性获取文件的所有字节
    public byte[] downloadFile(String filePath) throws Exception {
        byte[] buffer =null;
        try(FileInputStream inputStream = new FileInputStream(filePath);) {
            buffer = new byte[(int)new File(filePath).length()];
            int count=0;
            while((count= inputStream.read(buffer))>0){
                //一次向数组中写入数组长度的字节
                System.out.println("文件下载中。。。。");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return buffer;
    }



}
