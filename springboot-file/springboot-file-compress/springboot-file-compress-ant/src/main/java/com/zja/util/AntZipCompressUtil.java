/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-11 10:29
 * @Since:
 */
package com.zja.util;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;

/**
 * Ant 把文件/目录进行zip解压缩
 */
public class AntZipCompressUtil {

    public final static String encoding = "GBK";

    /**
     * 压缩文件/文件夹到zip , 若是文件夹，则把文件夹下的所有子文件及子文件夹(含空文件夹)都压缩
     *
     * @param srcPathname 需要被压缩的文件或文件夹路径    例：C:\temp\test or 例：C:\temp\a.txt
     * @param zipFilepath 将要生成的zip文件路径 例：C:\temp\test.zip
     */
    public static void zip(String srcPathname, String zipFilepath) throws RuntimeException {
        File file = new File(srcPathname);
        if (!file.exists()) {
            throw new RuntimeException("source file or directory " + srcPathname + " does not exist.");
        }

        Project proj = new Project();
        FileSet fileSet = new FileSet();
        fileSet.setProject(proj);
        // 判断是目录还是文件
        if (file.isDirectory()) {
            fileSet.setDir(file);
            // ant中include/exclude规则在此都可以使用
            // 比如:
            // fileSet.setExcludes("**/*.txt");
            // fileSet.setIncludes("**/*.xls");
        } else {
            fileSet.setFile(file);
        }

        Zip zip = new Zip();
        zip.setProject(proj);
        zip.setDestFile(new File(zipFilepath));
        zip.addFileset(fileSet);
        zip.setEncoding(encoding);
        zip.execute();

        System.out.println("compress successed.");
    }

    /**
     * 解压缩文件和文件夹
     *
     * @param zipFilepath 需要被解压的zip文件路径 例：C:\temp\test.zip
     * @param destDir 将要被解压到哪个文件夹 例：C:\temp\test1
     */
    public static void unzip(String zipFilepath, String destDir) throws BuildException, RuntimeException {
        if (!new File(zipFilepath).exists()) {
            throw new RuntimeException("zip file " + zipFilepath + " does not exist.");
        }

        Project proj = new Project();
        Expand expand = new Expand();
        expand.setProject(proj);
        expand.setTaskType("unzip");
        expand.setTaskName("unzip");
        expand.setEncoding(encoding);

        expand.setSrc(new File(zipFilepath));
        expand.setDest(new File(destDir));
        expand.execute();

        System.out.println("uncompress successed.");
    }

    public static void main(String[] args) {

        //压缩
        AntZipCompressUtil.zip("D:\\temp\\zip\\测试目录", "D:\\temp\\zip\\存储目录\\a.zip");
        //解压
        AntZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\a.zip", "D:\\temp\\zip\\存储目录\\a");
    }
}
