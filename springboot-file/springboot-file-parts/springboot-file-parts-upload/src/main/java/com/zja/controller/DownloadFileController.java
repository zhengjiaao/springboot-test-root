package com.zja.controller;

import com.zja.util.FileUtil;
import com.zja.util.ZipUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * 文件以流的方式下载
 */
@Slf4j
@Api(tags = {"FileDownloadController"}, description = "普通文件下载")
@RestController
@RequestMapping(value = "rest/file")
public class DownloadFileController {

    /****************************** 文件下载 *****************************/

    @ApiOperation(value = "文件下载/切片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadFile", method = RequestMethod.GET)
    public void getFile(@ApiParam(value = "下载文件路径名称") @RequestParam String pathName,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        long begin = System.currentTimeMillis();
        try {
            System.out.println("下载的文件地址：" + pathName);

            File file = new File(pathName);

            // 文件写出
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            // 获取文件读取
            RandomAccessFile read = new RandomAccessFile(file, "rw");

            // 断点分部下载起始点
            long startPos = 0;
            long endPos = -1;
            Enumeration<String> names = request.getHeaderNames();
            while (names.hasMoreElements()) {
                String n = names.nextElement();
                String rangeData = request.getHeader(n);
                System.out.println(n + "---" + request.getHeader(n));
                if ("range".equalsIgnoreCase(n)) {
                    if (rangeData != null && rangeData.indexOf("=") != -1) {
                        String[] arr = rangeData.substring(rangeData.indexOf("=") + 1).split("-");
                        if (arr.length == 2) {
                            startPos = Integer.valueOf(arr[0]);
                            endPos = Integer.valueOf(arr[1]);
                            System.out.println(startPos + "---" + endPos);
                        }
                    }
                }
            }

            if (endPos != -1) {
                read.seek(startPos);
            } else {
                endPos = file.length();
            }

            // 设置返回类型
            response.setContentType("multipart/form-data");
            // 文件名转码一下，不然会出现中文乱码
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("--chenparty下载站--" + file.getName(), "UTF-8"));
            // 设置返回的文件的大小
            response.setContentLength((int) file.length());

            byte[] b = new byte[10 * 1024 * 1024];
            int len = 0;

            // 获取真实的读取大小，因为 rang读取是包头包尾的，如果 1-2 读取的就是 2 个，直接 2-1 的话就是一个，所以这里得+1
            long actualLen = endPos - startPos + 1;

            while (-1 != (len = read.read(b))) {
                if (actualLen - len >= 0) {
                    out.write(b, 0, len);
                    // 记录实时的 真实读取大小
                    actualLen -= len;
                } else {
                    // 真实大小读取完毕
                    out.write(b, 0, (int) actualLen);
                    break;
                }
            }

            read.close();
            out.close();

            long end = System.currentTimeMillis();
            System.out.println("用时= " + (end - begin));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "下载本地文件或下载切片文件", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadLocalFile", method = RequestMethod.GET)
    public void downloadLocalFile(@ApiParam(value = "文件绝对路径", required = true) @RequestParam("filePathName") String filePathName,
                                  HttpServletResponse response) throws Exception {

        //将文件路径中的 \\或// 替换成 /
        filePathName = filePathName.replace("\\", "/").replace("//", "/");
        //获取文件名称-包括文件后缀
        String fileName = filePathName.substring(filePathName.lastIndexOf("/") + 1);
        String browser = "";

        // 清空response
        response.reset();
        //告知客户端响应正文类型-以流的方式下载
        response.setContentType("application/octet-stream");
        //自动判断下载文件类型
        //response.setContentType("multipart/form-data");
        // 设置类型强制下载不打开 使用图片下载
        //response.setContentType("application/force-download");

        //获得请求头中的User-Agent，根据不同浏览器解决文件名中文乱码
        browser = response.getHeader("User-Agent");
        if (-1 < browser.indexOf("MSIE 6.0") || -1 < browser.indexOf("MSIE 7.0")) {
            // IE6, IE7 浏览器
            response.addHeader("content-disposition", "attachment;filename="
                    + new String(fileName.getBytes(), "ISO8859-1"));
        } else if (-1 < browser.indexOf("MSIE 8.0")) {
            // IE8
            response.addHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
        } else if (-1 < browser.indexOf("MSIE 9.0")) {
            // IE9
            response.addHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
        } else if (-1 < browser.indexOf("Chrome")) {
            // 谷歌
            response.addHeader("content-disposition",
                    "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
        } else if (-1 < browser.indexOf("Safari")) {
            // 苹果
            response.addHeader("content-disposition", "attachment;filename="
                    + new String(fileName.getBytes(), "ISO8859-1"));
        } else {
            // 火狐或者其他的浏览器
            response.addHeader("content-disposition",
                    "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
        }

        //要下载的文件路径=绝对路径+相对路径  根据场景选择路径
        String filePath = "c:/文件位置" + "/" + filePathName;

        File file = new File(filePathName);
        //判断文件是否存在
        if (!file.exists()) {
            return;
        }

        //下载方式一 不建议文件大于100M
        InputStream inStream = new FileInputStream(file);
        // 循环取出流中的数据
        byte[] b = new byte[10 * 1024 * 1024];
        int len;
        try {
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            //前端可能获取不到长度
            response.setContentLength((int) file.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }

    }

    @ApiOperation(value = "下载文件", notes = "下载文件")
    @RequestMapping(value = "download/byFileName", method = {RequestMethod.GET})
    public void downloadFileByFileName(@PathVariable @ApiParam(value = "文件名称") String fileName,
                                       HttpServletResponse response) {

        String filePathName ="D://FileTest//文件分片位置"+File.separator+fileName;
        if (!fileName.equals("") && fileName !=null) {
            try {
                InputStream in = new ByteArrayInputStream(FileUtil.File2byte(filePathName));
                OutputStream out = response.getOutputStream();
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                StreamUtils.copy(in, out);
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("沒有找到指定文件");
        }
    }


    @ApiOperation(value = "下载本地文件2", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadLocalFile2", method = RequestMethod.GET)
    public void downloadLocalFile2(@ApiParam(value = "文件绝对路径", required = true) @RequestParam("filePathName") String filePathName,HttpServletResponse response
    ) throws Exception {

        //将文件路径中的 \\或// 替换成 /
        filePathName = filePathName.replace("\\", "/").replace("//", "/");
        //获取文件名称-包括文件后缀
        String fileName = filePathName.substring(filePathName.lastIndexOf("/") + 1);
        String browser = "";

        //要下载的文件路径=绝对路径+相对路径  根据场景选择路径 , "c:/文件位置" 可以配置在yml文件里
        String filePath = "c:/文件位置" + "/" + filePathName;

        //下载方式二 一次性读取文件所有字节下载  不建议文件大于100M
        byte[] buffer = null;
        OutputStream os = null;
        try {
            // 清空response
            response.reset();
            //自动判断下载文件类型
            //httpServletResponse.setContentType("multipart/form-data");
            // 设置类型强制下载不打开 一般图片下载
            //response.setContentType("application/force-download");
            //告知客户端响应正文类型 -流方式下载
            response.setContentType("application/octet-stream");
            //获得请求头中的User-Agent，根据不同浏览器解决文件名中文乱码
            browser = response.getHeader("User-Agent");

            if (-1 < browser.indexOf("MSIE 6.0") || -1 < browser.indexOf("MSIE 7.0")) {
                // IE6, IE7 浏览器 content-disposition通知客户端已下载的方式接受数据
                response.addHeader("content-disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
            } else if (-1 < browser.indexOf("Chrome")) {
                // 谷歌
                response.addHeader("content-disposition",
                        "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("MSIE 8.0")) {
                // IE8
                response.addHeader("content-disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("MSIE 9.0")) {
                // IE9
                response.addHeader("content-disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("Safari")) {
                // 苹果
                response.addHeader("content-disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
            } else {
                // 火狐或者其他的浏览器
                response.addHeader("content-disposition",
                        "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            }
            //获取文件的所有字节
            buffer = downloadFile(filePath);
            //前端能够成功获取长度
            response.setContentLength(buffer.length);
            os = response.getOutputStream();
            os.write(buffer, 0, buffer.length);
            os.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //一次性读取所有文件字节，不建议文件大于100M
    public byte[] downloadFile(String filePath) throws Exception {
        byte[] buffer = null;
        try (FileInputStream inputStream = new FileInputStream(filePath);) {
            buffer = new byte[(int) new File(filePath).length()];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                //一次向数组中写入数组长度的字节
                log.info("文件下载中。。。。");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return buffer;
    }


    @ApiOperation(value = "http下载本地文件以流的方式", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadLocalFileByFlow", method = RequestMethod.GET)
    public void downloadLocalFileByFlow(@ApiParam(value = "文件绝对路径", required = true) @RequestParam("filePathName") String filePathName,
                                        HttpServletResponse response) throws Exception {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(filePathName);
            // 取得文件名-包括后缀
            String filename = file.getName();
            // 取得文件的后缀名。
            //String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件-适合下载小型文件 大概100M 以内
            InputStream fis = new BufferedInputStream(new FileInputStream(filePathName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @ApiOperation(value = "本地下载网络文件", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadNetworkFile", method = RequestMethod.GET)
    public void downloadNetworkFile(@ApiParam(value = "文件绝对路径", required = true) @RequestParam("filePathName") String filePathName) throws Exception {

        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL("windine.blogdriver.com/logo.gif");

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream("c:/abc.gif");

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "通过URL下载网络文件", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadNetworkFileByURL", method = RequestMethod.GET)
    public Object downloadNetworkFileByURL(@ApiParam(value = "文件网络路径 如http://", required = true) @RequestParam("urlStr") String urlStr,
                                           @ApiParam(value = "文件保存路 径如c:/test/", required = true) @RequestParam("savePath") String savePath,
                                           @ApiParam(value = "文件名称 如a.txt", required = true) @RequestParam("fileName") String fileName) throws Exception {

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = FileUtil.readStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }

        String result = "info:" + url + " download success";

        System.out.println(result);
        return result;

        /*//测试
        downloadNetworkFileByURL("http://172.18.33.247:8081/files/5ad6a162b1727a35cc7dff9b",
                "test.png","/Users/zhuominjie/Desktop/1工作/4归一化平台/");
         */

    }

    @ApiOperation(value = "下载文件或在线打开文件", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadOrOnlineOpen", method = RequestMethod.GET)
    public void downloadOrOnlineOpen(@ApiParam(value = "文件绝对路径", required = true) @RequestParam("filePathName") String filePathName,
                                     @ApiParam(value = "是否在线打开true/false", required = true) @RequestParam("isOnLine") boolean isOnLine,
                                     HttpServletResponse response) throws Exception {
        File f = new File(filePathName);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePathName);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        br.close();
        out.close();
    }

    @ApiOperation(value = "下载文件,有返回结果", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/return/downloadfile", method = RequestMethod.GET)
    public Object downloadFileByReturn(@ApiParam(value = "文件绝对路径", required = true) @RequestParam("filePathName") String filePathName,
                                       HttpServletResponse response) throws Exception {

        //被下载的文件在服务器中的路径
        File file = new File(filePathName);

        if (file.exists()) {
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            //载之后的文件显示的名字
            response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }

                return "success";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "file doesn't found!";
    }

    @ApiOperation(value = "文件压缩后下载", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadCompressedFile", method = RequestMethod.GET)
    public Object downloadCompressedFile(@ApiParam(value = "下载的文件绝对路径", required = true) @RequestParam("filePathName") String filePathName,
                                         @ApiParam(value = "压缩文件绝对路径", required = true) @RequestParam("zipPath") String zipPath,
                                         HttpServletResponse response) throws Exception {
        //提供下载文件前进行压缩，即服务端生成压缩文件
        File file = new File(zipPath);
        FileOutputStream fos = new FileOutputStream(file);
        ZipUtils.toZip(filePathName, fos, true);
        //1.获取要下载的文件的绝对路径
        String realPath = zipPath;
        //2.获取要下载的文件名
        String fileName = realPath.substring(realPath.lastIndexOf(File.separator) + 1);
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        //3.设置content-disposition响应头控制浏览器以下载的形式打开文件
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "utf-8"));
        //获取文件输入流
        InputStream in = new FileInputStream(realPath);
        int len = 0;
        byte[] buffer = new byte[1024];
        OutputStream out = response.getOutputStream();
        while ((len = in.read(buffer)) > 0) {
            //将缓冲区的数据输出到客户端浏览器
            out.write(buffer, 0, len);
        }
        in.close();
        return file;
    }

}
