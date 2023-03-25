package com.zja.service;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

/**
 * This class provides methods for uploading and merging file chunks.
 */
@Service
public class ChunkUploadService {
    /**
     * Uploads a chunk of a file with the given parameters.
     *
     * @param file the chunk file to upload
     * @param chunkNumber the number of the chunk being uploaded
     * @param totalChunks the total number of chunks the file is split into
     * @param identifier the identifier of the file being uploaded
     * @param filename the name of the file being uploaded
     * @throws IOException if an I/O error occurs
     */
    public void upload(MultipartFile file, Integer chunkNumber, Integer totalChunks, String identifier, String filename) throws IOException {
        Path chunkPath = Paths.get("uploads", identifier, chunkNumber.toString());
        Files.createDirectories(chunkPath.getParent());
        if (!Files.exists(chunkPath)) {
            Files.write(chunkPath, file.getBytes());
        } else {
            // handle case where chunk file already exists
        }
    }

    /**
     * This method merges all uploaded chunks into a single file for a given file identifier and filename.
     * If not all chunks have been uploaded, it returns without merging.
     * @param identifier the identifier of the file being uploaded
     * @param filename the name of the file being uploaded
     * @param totalChunks the total number of chunks the file is split into
     * @throws IOException if an I/O error occurs
     */
    public void merge(String identifier, String filename, Integer totalChunks) throws IOException {
        if (!isUploadComplete(identifier, totalChunks)) {
            // handle case where not all chunks have been uploaded
            return;
        }
        Path dirPath = Paths.get("uploads", identifier);
        Path filePath = Paths.get("uploads", filename);
        try (OutputStream out = Files.newOutputStream(filePath)) {
            Files.list(dirPath)
                    .filter(path -> !Files.isDirectory(path))
                    .sorted(Comparator.comparingInt(path -> Integer.parseInt(path.getFileName().toString())))
                    .forEachOrdered(path -> {
                        try (InputStream in = Files.newInputStream(path)) {
                            IOUtils.copy(in, out);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
    }

    /**
     * This method checks whether all chunks have been uploaded for a given file identifier and total number of chunks.
     * If all chunks have been uploaded, it returns true. Otherwise, it returns false.
     * @param identifier the identifier of the file being uploaded
     * @param totalChunks the total number of chunks the file is split into
     * @return true if all chunks have been uploaded, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public boolean isUploadComplete(String identifier, Integer totalChunks) throws IOException {
        Path dirPath = Paths.get("uploads", identifier);
        long count = Files.list(dirPath)
                .filter(path -> !Files.isDirectory(path))
                .count();
        return count == totalChunks;
    }
}
