package com.zja.bigFile;

import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/6 11:33
 */
@Data
public class BigFileSplitTest implements Serializable{

    // 文件路径
    private String filePath;

    // 文件大小
    private long fileLength;

    // 文件名称
    private String fileName;

    // 以多大来切割文件
    private long blockSize;

    // 文件块数
    private int size;

    // 切割后的文件的存放路径
    private String destPath;

    // 切割后的每块的文件名
    private List<String> destFileName;

    public BigFileSplitTest() {
        super();
        this.destFileName = new ArrayList<String>();
    }

    public BigFileSplitTest(String filePath, String destPath) {
        this(filePath, 1024*1024, destPath);
    }

    public BigFileSplitTest(String filePath, long blockSize, String destPath) {
        this();
        this.filePath = filePath;
        this.blockSize = blockSize;
        this.destPath = destPath;
        init();

    }

    /**
     *
     * @Title: init
     * @Description: 初始化必须参数，比如说文件切割后的名字，分成几块，之类的
     * @param:
     * @return: void
     * @throws
     */
    public void init() {
        File src = null;
        // 如果 文件地址为空，或者文件不存在则终止程序
        if(this.filePath == null || !(src = new File(this.filePath)).exists()) {
            return ;
        }
        // 该地址为文件夹也终止
        if(src.isDirectory()) {
            return ;
        }

        File dest = null;
        // 如果目标文件不存在则终止程序
        if( this.destPath == null  ) {
            return ;
        }
        dest = new File(this.destPath);
        // 如果目标文件不存在则创建
        if(!dest.exists()) {
            dest.mkdirs();
        }

        // 文件大小
        this.fileLength = src.length();

        // 文件名称
        this.fileName = src.getName();

        if(this.blockSize > this.fileLength) {
            this.blockSize = this.fileLength;
        }

        // 分块
        this.size = (int)Math.ceil( (src.length()*1.0 / this.blockSize) );

        initDestFileName();
    }

    /**
     *
     * @Title: initDestFileName
     * @Description: 初始化切割后的文件的名称
     * @param:
     * @return: void
     * @throws
     */
    public void initDestFileName() {
        for (int i = 0; i < this.size; i++) {
            this.destFileName.add( this.destPath + File.separator + this.fileName + ".temp"+i );
        }
    }

    /**
     *
     * @Title: split
     * @Description: 切割文件   ，确定起始点
     * @param:
     * @return: void
     * @throws
     */
    public void split() {
        long beginPos = 0;
        long actualBlockSize = this.getBlockSize();

        for (int i = 0; i < this.size; i++) {
            // 当最后一块的大小比分块的大小小的时候，实际大小为 总长度-起点
            if(this.fileLength - beginPos <= this.blockSize ) {
                actualBlockSize = this.fileLength - beginPos;
            }

            BigFileSpli(i, beginPos, actualBlockSize);
            // 起点 = 上一次的结尾 + 实际读取的长度
            beginPos += actualBlockSize;
        }
    }


    /**
     *
     * @Title: splitDetil
     * @Description: 切割文件的具体实现方法
     * @param:
     * @return: void
     * @throws
     */
    public void BigFileSpli(int index,long beginPos,long actualBlockSize) {

        File desc = new File(this.destFileName.get(index));

        if(desc.exists()){
            return;
        }
        /*if (desc.exists()){
            desc.delete();
        }*/

        // 目标文件不存在则创建
        if(!new File(desc.getParent()).exists()) {
            new File(desc.getParent()).mkdirs();
        }

        // 创建源文件
        File src = new File(this.filePath);

        RandomAccessFile raf = null;
        BufferedOutputStream bos = null;
        try {
            // 设置只读读取
            raf = new RandomAccessFile(src, "r");
            bos = new BufferedOutputStream( new FileOutputStream(desc) );

            // 读取文件
            raf.seek(beginPos);

            // 缓冲
            byte [] flush = new byte[1024];
            // 记录每次读取的长度
            int len = 0;
            while (-1 != (len = raf.read(flush))) {
                if( (actualBlockSize - len )>0 ) {
                    bos.write(flush, 0, len);
                    actualBlockSize -= len;
                }else {
                    // 写出最后一块后关闭循环
                    bos.write(flush,0,(int)actualBlockSize);
                    break;
                }
            }

            bos.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                raf.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**文件合并
     *
     * @param destPath 合并后的文件位置
     */
    public void merge(String destPath) {
        File dest = new File(destPath);
        if(destPath==null) {
            return ;
        }
        // 目标文件不存在则创建
        if(!new File(dest.getParent()).exists()) {
            new File(dest.getParent()).mkdirs();
        }

        if (dest.exists()){
            dest.delete();
        }

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            // 此处是追加，因为是文件合并
            bos = new BufferedOutputStream( new FileOutputStream( dest ,true) );
            for (int i = 0; i < this.size; i++) {

                bis = new BufferedInputStream( new FileInputStream( new File(this.destFileName.get(i)) ) );

                byte [] flush = new byte[1024];
                int len = 0;
                while( -1!=(len = bis.read(flush)) ) {
                    bos.write(flush,0,len);
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


    //开启测试-成功
    public static void main(String[] args) {
        BigFileSplitTest bigFileSpli = new BigFileSplitTest("D:\\FileTest\\2.wmv", 1024*1024, "D:\\FileTest\\文件分片");

        bigFileSpli.split();
        bigFileSpli.merge("D:\\FileTest\\文件分片\\2-wmv合并结果.wmv");
    }


}
