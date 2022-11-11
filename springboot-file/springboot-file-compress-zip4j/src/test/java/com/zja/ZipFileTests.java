/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-11 9:48
 * @Since:
 */
package com.zja;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ExcludeFileFilter;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ZipFileTests {

    //把 文件 添加到现有 zip
    @Test
    public void test() throws ZipException {

        //创建包含单个文件的 zip 文件/将单个文件添加到现有 zip
        new ZipFile("filename.zip").addFile("filename.ext");
        //or
        new ZipFile("filename.zip").addFile(new File("filename.ext"));

        //创建包含多个文件的 zip 文件/将多个文件添加到现有 zip
        new ZipFile("filename.zip").addFiles(Arrays.asList(new File("first_file"), new File("second_file")));
    }

    //把 文件夹 添加到现有 zip
    @Test
    public void test2() throws ZipException {

        //通过向其添加文件夹来创建 zip 文件/将文件夹添加到现有 zip
        new ZipFile("filename.zip").addFolder(new File("/users/some_user/folder_to_add"));


        //or 使用 ExcludeFileFilter 将文件夹添加到 zip 时排除某些文件
        /*ExcludeFileFilter excludeFileFilter = filesToExclude::contains;
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setExcludeFileFilter(excludeFileFilter);
        new ZipFile("filename.zip").addFolder(new File("/users/some_user/folder_to_add"), zipParameters);*/
    }

    //把 流 添加到现有 zip
    @Test
    public void test3() throws ZipException {

        //从流创建 zip 文件/将流添加到现有 zip
        //new ZipFile("filename.zip").addStream(inputStream, new ZipParameters());
    }

    //从 zip 中提取文件
    @Test
    public void test4() throws ZipException {
        //从 zip 中提取所有文件
        new ZipFile("filename.zip").extractAll("/destination_directory");

        //从受密码保护的 zip 中提取所有文件
        new ZipFile("filename.zip", "password".toCharArray()).extractAll("/destination_directory");

        //从 zip 中提取单个文件
        new ZipFile("filename.zip").extractFile("fileNameInZip.txt", "/destination_directory");

        //从 zip 中提取文件夹
        new ZipFile("filename.zip").extractFile("folderNameInZip/", "/destination_directory");

        //从受密码保护的 zip 中提取单个文件
        new ZipFile("filename.zip", "password".toCharArray()).extractFile("fileNameInZip.txt", "/destination_directory");

        //从 zip 中提取单个文件并为其指定新文件名 若提取的文件是一个目录， newFileName参数将用作目录名
        new ZipFile("filename.zip", "password".toCharArray()).extractFile("fileNameInZip.txt", "/destination_directory", "newfileName.txt");

    }

    //获取 zip 文件中条目的输入流
    @Test
    public void test5() throws IOException {
        ZipFile zipFile = new ZipFile("filename.zip");
        FileHeader fileHeader = zipFile.getFileHeader("entry_name_in_zip.txt");
        //FileHeader fileHeader = zipFile.getFileHeader("root_folder/entry_name_in_zip.txt");
        InputStream inputStream = zipFile.getInputStream(fileHeader);
    }

    //创建受密码保护的 zip 文件/将文件添加到具有密码保护的现有 zip
    //AES 加密
    @Test
    public void test51() throws IOException {
        //AES 加密

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        // Below line is optional. AES 256 is used by default. You can override it to use AES 128. AES 192 is supported only for extracting.
        zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

        List<File> filesToAdd = Arrays.asList(
                new File("somefile"),
                new File("someotherfile")
        );

        ZipFile zipFile = new ZipFile("filename.zip", "password".toCharArray());
        zipFile.addFiles(filesToAdd, zipParameters);

        //Zip 标准加密
        //如果那你想使用Zip标准加密而不是 AES，替换zipParameters.setEncryptionMethod(EncryptionMethod.AES);为 zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);.
        //您可以省略该行来设置 AES 密钥强度。顾名思义，这仅适用于 AES 加密。
    }

    //从 zip 文件中删除文件/目录
    @Test
    public void test6() throws IOException {

        //如果fileNameInZipToRemove代表一个文件夹，则该文件夹下的所有文件和文件夹也将被删除
        new ZipFile("filename.zip").removeFile("fileNameInZipToRemove");
        new ZipFile("filename.zip").removeFile("root-folder/folder1/fileNameInZipToRemove");


        //想确保要删除的文件存在于 zip 文件中
        ZipFile zipFile = new ZipFile("someZip.zip");
        FileHeader fileHeader = zipFile.getFileHeader("fileNameInZipToRemove");

        if (fileHeader == null) {
            // file does not exist
        }

        zipFile.removeFile(fileHeader);
    }

    //一个 zip 文件中删除多个文件和文件夹
    @Test
    public void test7() throws IOException {
        ZipFile zipFile = new ZipFile("someZip.zip");
        List<String> filesToRemove = Arrays.asList("file1.txt", "file2.txt", "some-folder/", "some-new-folder-1/somefile.pdf");

        zipFile.removeFiles(filesToRemove);
    }

    //重命名 zip 文件中的条目,使用 Zip4j 重命名 zip 文件中的条目有三种方法
    @Test
    public void test8() throws IOException {

        //一种方法是传入文件头和新文件名
        ZipFile zipFile = new ZipFile("sample.zip");
        FileHeader fileHeader = zipFile.getFileHeader("entry-to-be-changed.pdf");
        zipFile.renameFile(fileHeader, "new-file-name.pdf");

        //第二种方法是只传入要更改的文件名（而不是文件头）和新文件名
        new ZipFile("filename.zip").renameFile("entry-to-be-changed.pdf", "new-file-name.pdf");

        //也可以一次更改多个文件名
        Map<String, String> fileNamesMap = new HashMap<>();
        fileNamesMap.put("firstFile.txt", "newFileFirst.txt");
        fileNamesMap.put("secondFile.pdf", "newSecondFile.pdf");
        fileNamesMap.put("some-folder/thirdFile.bin", "some-folder/newThirdFile.bin");
        new ZipFile("filename.zip").renameFiles(fileNamesMap);

        //要修改文件夹内的条目名称，新文件名也应包含完整的父路径
        new ZipFile("filename.zip").renameFile("some-folder/some-sub-folder/some-entry.pdf", "some-folder/some-sub-folder/new-entry.pdf");
        //如果缺少父路径，则文件将放在 zip 文件的根目录下 文件重命名后，some-new-entry.pdf将存在于 zip 文件的根目录而不是 at some-folder/some-sub-folder/
        new ZipFile("filename.zip").renameFile("some-folder/some-sub-folder/some-entry.pdf", "some-new-entry.pdf");
    }

    //将条目“移动”到不同文件夹, 可以达到重命名效果
    @Test
    public void test9() throws IOException {
        //将 some-entry.pdffrom移动some-folder/some-sub-folder/到folder-to-be-moved-to/sub-folder/，文件也将重命名为new-entry.pdf. 要仅移动文件，请使用相同的文件名而不是新文件名。
        new ZipFile("filename.zip").renameFile("some-folder/some-sub-folder/some-entry.pdf", "folder-to-be-moved-to/sub-folder/new-entry.pdf");
    }

    //列出 zip 中的所有文件
    @Test
    public void test10() throws IOException {
        List<FileHeader> fileHeaders = new ZipFile("zipfile.zip").getFileHeaders();
        fileHeaders.stream().forEach(fileHeader -> System.out.println(fileHeader.getFileName()));
    }

    //检查 zip 文件是否受密码保护
    @Test
    public void test11() throws IOException {
        new ZipFile("encrypted_zip_file.zip").isEncrypted();
    }

}
