package com.zja.parts.download.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-27 22:55
 */
@CrossOrigin
@Slf4j
@Api(tags = {"BigFileSplitController"}, description = "采用Range断点续传进行下载大文件")
@RestController
@RequestMapping(value = "large/file/parts")
public class RangeDownloadController {

    @GetMapping("/download")
    public ResponseEntity<Object> downloadFile(@RequestParam String fileName,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws IOException {
        // String fileName = "建筑方案.zip";
        Path filePath = Paths.get("D:\\temp\\zip", fileName);
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        if (StringUtils.isEmpty(fileName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        // 获取文件大小
        long fileSize = Files.size(filePath);

        // 获取 Range 请求头
        String rangeHeader = request.getHeader(HttpHeaders.RANGE);

        // 如果没有 Range 请求头，直接返回整个文件
        if (rangeHeader == null) {
            try (FileInputStream fis = new FileInputStream(filePath.toString())) {
                response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
                // 设置响应头以支持文件下载
                response.setContentType("application/octet-stream");
                IOUtils.copy(fis, response.getOutputStream());
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                throw new RuntimeException("文件下载失败", e);
            }
        }

        // 验证 Range 请求头格式是否正确，格式：Range: bytes=0-10000
        if (!rangeHeader.matches("bytes=\\d+-\\d*")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 解析 Range 请求头，若是bytes=0-0，则返回文件大小Content-Range: bytes */169705
        if (rangeHeader.equals("bytes=0-0")) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                    .build();
        }

        // 解析 Range 请求头
        String[] parts = rangeHeader.split("-");
        long start = Long.parseLong(parts[0].replace("bytes=", "").trim());
        long end = parts.length > 1 ? Long.parseLong(parts[1].trim()) : fileSize - 1;

        // 验证范围是否有效
        if (start > end || end >= fileSize) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                    .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                    .build();
        }

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(end - start + 1);
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
        headers.setContentDispositionFormData("attachment", encodedFileName);

        // 读取文件片段
        try (FileInputStream fis = new FileInputStream(filePath.toString());
             FileChannel fc = fis.getChannel()) {
            byte[] bytes = new byte[(int) (end - start + 1)];
            fc.read(ByteBuffer.wrap(bytes), start);
            return new ResponseEntity<>(bytes, headers, HttpStatus.PARTIAL_CONTENT);
        }
    }

    @GetMapping("/download/v2")
    public ResponseEntity<byte[]> downloadFileV1(@RequestParam("file") String fileName, HttpServletRequest request) throws IOException {

        Path filePath = Paths.get("D:\\temp\\zip", fileName);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        long contentLength = Files.size(filePath);

        // Parse the Range header
        String rangeHeader = request.getHeader(HttpHeaders.RANGE); // Range: bytes=0-1023 or bytes=1024-2047
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            Pattern pattern = Pattern.compile("(\\d+)-(\\d*)");
            Matcher matcher = pattern.matcher(rangeHeader.substring(6));

            if (matcher.find()) {
                long start = Long.parseLong(matcher.group(1));
                long end = matcher.group(2).isEmpty() ? contentLength - 1 : Long.parseLong(matcher.group(2));

                if (start < 0 || end >= contentLength || start > end) {
                    return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
                }

                // Use SeekableByteChannel for efficient reading
                try (SeekableByteChannel channel = FileChannel.open(filePath, StandardOpenOption.READ)) {
                    byte[] bytes = new byte[(int) (end - start + 1)];
                    channel.position(start);
                    channel.read(ByteBuffer.wrap(bytes));

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentLength(contentLength);
                    headers.set(HttpHeaders.CONTENT_RANGE, start + "-" + end + "/" + contentLength);

                    return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                            .headers(headers)
                            .body(bytes);
                }
            }
        }

        // If no range is specified, send the whole file
        byte[] bytes = Files.readAllBytes(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(contentLength);

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }


    // @GetMapping("range/download/{filename}")
    // public ResponseEntity<Resource> downloadFileV2(@PathVariable("filename") String filename,
    //                                              HttpServletRequest request) {
    //     // 获取文件
    //     File file = new File("path/to/file/" + filename);
    //
    //     // 获取 Range 请求头
    //     // String rangeHeader = request.getHeader("Range");
    //     String rangeHeader = request.getHeader("Range");
    //
    //     // 构建 ResponseEntity
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
    //
    //     if (rangeHeader != null) {
    //         // 解析 Range 头
    //         // createByteRange(long firstBytePos)
    //         // HttpRange httpRange = HttpRange.createByteRange(rangeHeader);
    //         // createByteRange(long firstBytePos, long lastBytePos)
    //         HttpRange httpRange = HttpRange.createByteRange(rangeHeader);
    //         long start = httpRange.getRangeStart(file.length());
    //         long end = httpRange.getRangeEnd(file.length());
    //         long contentLength = end - start + 1;
    //
    //         headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
    //         headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + file.length());
    //         headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
    //
    //         // 返回部分内容
    //         return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
    //                 .headers(headers)
    //                 .body(new FileSystemResource(file).region(start, contentLength));
    //     } else {
    //         // 返回完整文件
    //         headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
    //         return ResponseEntity.ok()
    //                 .headers(headers)
    //                 .body(new FileSystemResource(file));
    //     }
    // }
}
