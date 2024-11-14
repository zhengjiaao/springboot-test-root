package com.zja.java.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * 文件工具类
 *
 * @Author: zhengja
 * @Date: 2024-09-02 15:20
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 获取文件名(包含扩展名)
     *
     * @param filePath 文件路径，如：/Users/zhengja/Documents/test.txt
     * @return test.txt
     */
    public static String getFileName(String filePath) {
        filePath = filePath.replace("\\", "/");
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 获取文件名(不包含扩展名)
     *
     * @param filePath 文件路径，如：/Users/zhengja/Documents/test.txt
     * @return test
     */
    public static String getFileNameWithoutExtension(String filePath) {
        return getFileName(filePath).replace(getFileExtension(filePath, true), "");
    }

    /**
     * 移除文件扩展名
     *
     * @param filePath 文件路径，如：/Users/zhengja/Documents/test.txt
     * @return test
     */
    public static String removeExtension(String filePath) {
        if (filePath == null) {
            return null;
        } else {
            String fileExtension = getFileExtension(filePath, true);
            return filePath.substring(0, filePath.length() - fileExtension.length());
        }
    }

    /**
     * 获取文件后缀，不包含.
     *
     * @param fileName 文件名，如：test.txt
     * @return txt
     */
    public static String getFileExtension(String fileName) {
        return getFileExtension(fileName, false);
    }

    /**
     * 获取文件名（不包含后缀）和文件后缀
     *
     * @param fileName   文件名，如：test.txt
     * @param includeDot 是否返回带点的扩展名, 如：true返回.txt，false返回txt
     * @return .txt/txt
     */
    public static String getFileExtension(String fileName, boolean includeDot) {
        fileName = getFileName(fileName);

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            // 如果文件名中没有后缀
            return "";
        }

        return includeDot ? ("." + fileName.substring(lastDotIndex + 1)) : fileName.substring(lastDotIndex + 1);
    }

    /**
     * 获取文件路径，不包含文件名
     *
     * @param filePath 文件路径, 如：/Users/zhengja/Documents/test.txt
     * @return /Users/zhengja/Documents/
     */
    public static String getFilePathWithoutFileName(String filePath) {
        // 查找最后一个斜杠的位置
        int lastSlashIndex = filePath.lastIndexOf("/");

        if (lastSlashIndex != -1) {
            return filePath.substring(0, lastSlashIndex);
        } else {
            int lastBackslashIndex = filePath.lastIndexOf("\\");
            if (lastBackslashIndex != -1) {
                return filePath.substring(0, lastBackslashIndex);
            } else {
                return "";
            }
        }
    }

    public static boolean isFile(String filePath) {
        return new File(filePath).isFile();
    }

    public static boolean isDirectory(String filePath) {
        return new File(filePath).isDirectory();
    }

    public static boolean isExists(String filePath) {
        return new File(filePath).exists();
    }

    public static boolean notExists(String filePath) {
        return !isExists(filePath);
    }

    public static void delete(String filePath) throws IOException {
        delete(Paths.get(filePath));
    }

    public static boolean deleteIfExists(String filePath) throws IOException {
        return deleteIfExists(Paths.get(filePath));
    }

    public static File createParentDirectories(String filePath) throws IOException {
        return createParentDirectories(new File(filePath));
    }

    public static File createParentDirectories(File file) throws IOException {
        return mkdirs(getParentFile(file));
    }

    private static File mkdirs(File directory) throws IOException {
        if (directory != null && !directory.mkdirs() && !directory.isDirectory()) {
            throw new IOException("Cannot create directory '" + directory + "'.");
        } else {
            return directory;
        }
    }

    private static File getParentFile(File file) {
        return file == null ? null : file.getParentFile();
    }

    public static boolean isExists(Path path, LinkOption... options) throws IOException {
        return Files.exists(path, options);
    }

    public static Path copy(Path source, Path target, CopyOption... options) throws IOException {
        return Files.copy(source, target, options);
    }

    public static Path move(Path source, Path target, CopyOption... options) throws IOException {
        return Files.move(source, target, options);
    }

    public static Path createParentDirectories(Path path) throws IOException {
        File parentFile = getParentFile(path.toFile());
        return Files.createDirectories(parentFile.toPath());
    }

    public static Path createDirectories(Path path) throws IOException {
        return Files.createDirectories(path);
    }

    public static Path createDirectory(Path path) throws IOException {
        return Files.createDirectory(path);
    }

    public static Path createFile(Path path) throws IOException {
        return Files.createFile(path);
    }

    public static Path createLink(Path link, Path existing) throws IOException {
        return Files.createLink(link, existing);
    }

    public static void delete(Path path) throws IOException {
        Files.delete(path);
    }

    public static boolean deleteIfExists(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    private static void deleteDirectory(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return;
        }

        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static Path write(Path path, byte[] bytes) throws IOException {
        return Files.write(path, bytes);
    }

    public static Path write(Path path, byte[] bytes, OpenOption... options) throws IOException {
        return Files.write(path, bytes, options);
    }

    public static Path write(Path path, String content, Charset cs, OpenOption... options) throws IOException {
        return Files.write(path, content.getBytes(cs), options);
    }

    public static Path write(Path path, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        return Files.write(path, lines, cs, options);
    }

    public static InputStream newInputStream(Path path) throws IOException {
        return Files.newInputStream(path);
    }

    public static InputStream newInputStream(Path path, OpenOption... options) throws IOException {
        return Files.newInputStream(path, options);
    }

    public static OutputStream newOutputStream(Path path) throws IOException {
        return Files.newOutputStream(path);
    }

    public static OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
        return Files.newOutputStream(path, options);
    }

    public static BufferedReader newBufferedReader(Path path) throws IOException {
        return Files.newBufferedReader(path);
    }

    public static Reader newBufferedReader(Path path, Charset cs) throws IOException {
        return Files.newBufferedReader(path, cs);
    }

    public static BufferedWriter newBufferedWriter(Path path) throws IOException {
        return Files.newBufferedWriter(path);
    }

    public static BufferedWriter newBufferedWriter(Path path, Charset cs) throws IOException {
        return Files.newBufferedWriter(path, cs);
    }

    public static byte[] readAllBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    public static List<String> readAllLines(Path path) throws IOException {
        return Files.readAllLines(path);
    }

    public static List<String> readAllLines(Path path, Charset cs) throws IOException {
        return Files.readAllLines(path, cs);
    }

    // -----------------------io utils-start-------------------------

    // 文件拷贝 用法参考：FileCopyUtils.copy
    public static int copy(String sourceFilePath, String targetFilePath) throws IOException {
        return copy(new File(sourceFilePath), new File(targetFilePath));
    }

    public static int copy(File in, File out) throws IOException {
        notNull(in, "No InputStream specified");
        notNull(out, "No OutputStream specified");
        return copy(Files.newInputStream(in.toPath()), Files.newOutputStream(out.toPath()));
    }

    public static void copy(byte[] in, File out) throws IOException {
        notNull(in, "No InputStream specified");
        notNull(out, "No OutputStream specified");
        copy((InputStream) (new ByteArrayInputStream(in)), (OutputStream) Files.newOutputStream(out.toPath()));
    }

    public static byte[] copyToByteArray(File in) throws IOException {
        notNull(in, "No input File specified");
        return copyToByteArray(Files.newInputStream(in.toPath()));
    }

    private static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        notNull(in, "No InputStream specified");
        notNull(out, "No OutputStream specified");
        Throwable e = null;

        int byteCount;
        try {
            long count = copyLarge(in, out);
            byteCount = count > 2147483647L ? -1 : (int) count;
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {
            close(in, e);
            close(out, e);
        }

        return byteCount;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;

        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += (long) n;
        }

        return count;
    }

    public static void copy(byte[] in, OutputStream out) throws IOException {
        notNull(in, "No input byte array specified");
        notNull(out, "No OutputStream specified");
        Throwable e = null;

        try {
            out.write(in);
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {
            close(out, e);
        }
    }

    public static byte[] copyToByteArray(InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            copy((InputStream) in, (OutputStream) out);
            return out.toByteArray();
        }
    }

    public static int copy(Reader in, Writer out) throws IOException {
        notNull(in, "No Reader specified");
        notNull(out, "No Writer specified");
        Throwable e = null;

        try {
            int charCount = 0;

            int charsRead;
            for (char[] buffer = new char[4096]; (charsRead = in.read(buffer)) != -1; charCount += charsRead) {
                out.write(buffer, 0, charsRead);
            }

            out.flush();
            return charCount;
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {
            close(in, e);
            close(out, e);
        }
    }

    public static void copy(String in, Writer out) throws IOException {
        notNull(in, "No input String specified");
        notNull(out, "No Writer specified");
        Throwable e = null;

        try {
            out.write(in);
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {
            close(out, e);
        }

    }

    public static String copyToString(Reader in) throws IOException {
        if (in == null) {
            return "";
        } else {
            StringWriter out = new StringWriter(4096);
            copy((Reader) in, (Writer) out);
            return out.toString();
        }
    }

    public static void copyToFile(InputStream inputStream, File file) throws IOException {
        OutputStream out = openOutputStream(file);
        Throwable e = null;

        try {
            copy(inputStream, out);
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {
            close(out, e);
        }

    }

    public static void copyURLToFile(URL source, File destination) throws IOException {
        InputStream stream = source.openStream();
        Throwable e = null;

        try {
            copyInputStreamToFile(stream, destination);
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {
            close(stream, e);
        }

    }

    public static void copyURLToFile(URL source, File destination, int connectionTimeoutMillis, int readTimeoutMillis) throws IOException {
        URLConnection connection = source.openConnection();
        connection.setConnectTimeout(connectionTimeoutMillis);
        connection.setReadTimeout(readTimeoutMillis);
        InputStream stream = connection.getInputStream();
        Throwable e = null;

        try {
            copyInputStreamToFile(stream, destination);
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {
            close(stream, e);
        }
    }

    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        InputStream stream = source;
        Throwable e = null;
        try {
            copyToFile(stream, destination);
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {
            close(stream, e);
        }
    }

    private static void close(Closeable closeable, Throwable e) throws IOException {
        if (closeable != null) {
            if (e != null) {
                try {
                    closeable.close();
                } catch (Throwable e2) {
                    e.addSuppressed(e2);
                }
            } else {
                closeable.close();
            }
        }
    }

    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    public static void copyFileToDirectory(File sourceFile, File destinationDir, boolean preserveFileDate) throws IOException {
        Objects.requireNonNull(sourceFile, "sourceFile");
        requireDirectoryIfExists(destinationDir, "destinationDir");
        copyFile(sourceFile, new File(destinationDir, sourceFile.getName()), preserveFileDate);
    }

    private static File requireDirectoryIfExists(File directory, String name) {
        Objects.requireNonNull(directory, name);
        if (directory.exists()) {
            requireDirectory(directory, name);
        }

        return directory;
    }

    private static File requireDirectory(File directory, String name) {
        Objects.requireNonNull(directory, name);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not a directory: '" + directory + "'");
        } else {
            return directory;
        }
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        copyFile(srcFile, destFile, preserveFileDate ? new CopyOption[]{StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate, CopyOption... copyOptions) throws IOException {
        copyFile(srcFile, destFile, preserveFileDate ? addCopyAttributes(copyOptions) : copyOptions);
    }

    private static CopyOption[] addCopyAttributes(CopyOption... copyOptions) {
        CopyOption[] actual = (CopyOption[]) Arrays.copyOf(copyOptions, copyOptions.length + 1);
        Arrays.sort(actual, 0, copyOptions.length);
        if (Arrays.binarySearch(copyOptions, 0, copyOptions.length, StandardCopyOption.COPY_ATTRIBUTES) >= 0) {
            return copyOptions;
        } else {
            actual[actual.length - 1] = StandardCopyOption.COPY_ATTRIBUTES;
            return actual;
        }
    }

    public static void copyFile(File srcFile, File destFile, CopyOption... copyOptions) throws IOException {
        requireFileCopy(srcFile, destFile);
        requireFile(srcFile, "srcFile");
        requireCanonicalPathsNotEquals(srcFile, destFile);
        createParentDirectories(destFile);
        requireFileIfExists(destFile, "destFile");
        if (destFile.exists()) {
            requireCanWrite(destFile, "destFile");
        }

        Files.copy(srcFile.toPath(), destFile.toPath(), copyOptions);
        requireEqualSizes(srcFile, destFile, srcFile.length(), destFile.length());
    }

    private static File requireFileIfExists(File file, String name) {
        Objects.requireNonNull(file, name);
        return file.exists() ? requireFile(file, name) : file;
    }

    private static void requireEqualSizes(File srcFile, File destFile, long srcLen, long dstLen) throws IOException {
        if (srcLen != dstLen) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
        }
    }

    public static long copyFile(File input, OutputStream output) throws IOException {
        InputStream fis = Files.newInputStream(input.toPath());
        Throwable e = null;

        long bufferCount;
        try {
            bufferCount = copyLarge(fis, output);
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {
            close(fis, e);
        }

        return bufferCount;
    }

    public static void copyToDirectory(File sourceFile, File destinationDir) throws IOException {
        Objects.requireNonNull(sourceFile, "sourceFile");
        if (sourceFile.isFile()) {
            copyFileToDirectory(sourceFile, destinationDir);
        } else {
            if (!sourceFile.isDirectory()) {
                throw new FileNotFoundException("The source " + sourceFile + " does not exist");
            }

            copyDirectoryToDirectory(sourceFile, destinationDir);
        }
    }

    public static void copyDirectoryToDirectory(File sourceDir, File destinationDir) throws IOException {
        requireDirectoryIfExists(sourceDir, "sourceDir");
        requireDirectoryIfExists(destinationDir, "destinationDir");
        copyDirectory(sourceDir, new File(destinationDir, sourceDir.getName()), true);
    }

    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, (FileFilter) null, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, filter, preserveFileDate, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter fileFilter, boolean preserveFileDate, CopyOption... copyOptions) throws IOException {
        requireFileCopy(srcDir, destDir);
        requireDirectory(srcDir, "srcDir");
        requireCanonicalPathsNotEquals(srcDir, destDir);
        List<String> exclusionList = null;
        String srcDirCanonicalPath = srcDir.getCanonicalPath();
        String destDirCanonicalPath = destDir.getCanonicalPath();
        if (destDirCanonicalPath.startsWith(srcDirCanonicalPath)) {
            File[] srcFiles = listFiles(srcDir, fileFilter);
            if (srcFiles.length > 0) {
                exclusionList = new ArrayList(srcFiles.length);
                File[] files = srcFiles;
                int length = srcFiles.length;

                for (int i = 0; i < length; ++i) {
                    File srcFile = files[i];
                    File copiedFile = new File(destDir, srcFile.getName());
                    exclusionList.add(copiedFile.getCanonicalPath());
                }
            }
        }

        doCopyDirectory(srcDir, destDir, fileFilter, exclusionList, preserveFileDate, preserveFileDate ? addCopyAttributes(copyOptions) : copyOptions);
    }

    private static void doCopyDirectory(File srcDir, File destDir, FileFilter fileFilter, List<String> exclusionList, boolean preserveDirDate, CopyOption... copyOptions) throws IOException {
        File[] srcFiles = listFiles(srcDir, fileFilter);
        requireDirectoryIfExists(destDir, "destDir");
        mkdirs(destDir);
        requireCanWrite(destDir, "destDir");
        File[] var7 = srcFiles;
        int var8 = srcFiles.length;

        for (int var9 = 0; var9 < var8; ++var9) {
            File srcFile = var7[var9];
            File dstFile = new File(destDir, srcFile.getName());
            if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
                if (srcFile.isDirectory()) {
                    doCopyDirectory(srcFile, dstFile, fileFilter, exclusionList, preserveDirDate, copyOptions);
                } else {
                    copyFile(srcFile, dstFile, copyOptions);
                }
            }
        }

        if (preserveDirDate) {
            setLastModified(srcDir, destDir);
        }

    }


    private static void setLastModified(File sourceFile, File targetFile) throws IOException {
        Objects.requireNonNull(sourceFile, "sourceFile");
        setLastModified(targetFile, lastModified(sourceFile));
    }

    private static void setLastModified(File file, long timeMillis) throws IOException {
        Objects.requireNonNull(file, "file");
        if (!file.setLastModified(timeMillis)) {
            throw new IOException(String.format("Failed setLastModified(%s) on '%s'", timeMillis, file));
        }
    }

    public static long lastModified(File file) throws IOException {
        return Files.getLastModifiedTime((Path) Objects.requireNonNull(file.toPath(), "file")).toMillis();
    }

    private static void requireFileCopy(File source, File destination) throws FileNotFoundException {
        requireExistsChecked(source, "source");
        Objects.requireNonNull(destination, "destination");
    }

    private static File requireExistsChecked(File file, String fileParamName) throws FileNotFoundException {
        Objects.requireNonNull(file, fileParamName);
        if (!file.exists()) {
            throw new FileNotFoundException("File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
        } else {
            return file;
        }
    }

    private static void requireCanonicalPathsNotEquals(File file1, File file2) throws IOException {
        String canonicalPath = file1.getCanonicalPath();
        if (canonicalPath.equals(file2.getCanonicalPath())) {
            throw new IllegalArgumentException(String.format("File canonical paths are equal: '%s' (file1='%s', file2='%s')", canonicalPath, file1, file2));
        }
    }

    private static File[] listFiles(File directory, FileFilter fileFilter) throws IOException {
        requireDirectoryExists(directory, "directory");
        File[] files = fileFilter == null ? directory.listFiles() : directory.listFiles(fileFilter);
        if (files == null) {
            throw new IOException("Unknown I/O error listing contents of directory: " + directory);
        } else {
            return files;
        }
    }

    private static File requireDirectoryExists(File directory, String name) {
        requireExists(directory, name);
        requireDirectory(directory, name);
        return directory;
    }

    private static File requireExists(File file, String fileParamName) {
        Objects.requireNonNull(file, fileParamName);
        if (!file.exists()) {
            throw new IllegalArgumentException("File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
        } else {
            return file;
        }
    }

    public static void copyToDirectory(Iterable<File> sourceIterable, File destinationDir) throws IOException {
        Objects.requireNonNull(sourceIterable, "sourceIterable");
        Iterator iterator = sourceIterable.iterator();

        while (iterator.hasNext()) {
            File src = (File) iterator.next();
            copyFileToDirectory(src, destinationDir);
        }

    }

    public static FileInputStream openInputStream(File file) throws IOException {
        Objects.requireNonNull(file, "file");
        return new FileInputStream(file);
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        Objects.requireNonNull(file, "file");
        if (file.exists()) {
            requireFile(file, "file");
            requireCanWrite(file, "file");
        } else {
            createParentDirectories(file);
        }

        return new FileOutputStream(file, append);
    }

    private static File requireFile(File file, String name) {
        Objects.requireNonNull(file, name);
        if (!file.isFile()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not a file: " + file);
        } else {
            return file;
        }
    }

    private static void requireCanWrite(File file, String name) {
        Objects.requireNonNull(file, "file");
        if (!file.canWrite()) {
            throw new IllegalArgumentException("File parameter '" + name + " is not writable: '" + file + "'");
        }
    }

    public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        } else {
            int remaining;
            int count;
            for (remaining = length; remaining > 0; remaining -= count) {
                int location = length - remaining;
                count = input.read(buffer, offset + location, remaining);
                if (-1 == count) {
                    break;
                }
            }

            return length - remaining;
        }
    }

    public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    public static void readFully(InputStream input, byte[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    // -----------------------io utils-end-------------------------

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\temp\\excel\\input2.xlsx";
        System.out.println(getFileName(filePath));
        System.out.println(getFileNameWithoutExtension(filePath));
        System.out.println(removeExtension(filePath));
        System.out.println(getFileExtension(filePath));
        System.out.println(getFileExtension(filePath, true));
        System.out.println(getFilePathWithoutFileName(filePath));
        System.out.println(isFile(filePath));
        System.out.println(isDirectory(filePath));
        System.out.println(isExists(filePath));

        // 删除文件
        delete(filePath);

        // 删除目录
        deleteDirectory(Paths.get("D:\\temp\\excel"));
    }
}
