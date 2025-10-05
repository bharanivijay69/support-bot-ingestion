package dev.bharani.supportbot.ingestion.controller;

import dev.bharani.supportbot.ingestion.loader.DataLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class FileLoaderController {

    private final DataLoader dataLoader;

    public FileLoaderController(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Handles POST requests to /load-files endpoint.
     * Accepts a directory path as a request parameter and triggers file processing.
     * Uses DataLoader to load files into a vector database.
     * Example: /load-files?directory_path=C:/support-bot-ingestion/src/main/resources/docs
     * Returns a status and message indicating success or error.
     *
     * @param directoryPath the path to the directory containing files to load
     * @return a map with status and message
     */
    @PostMapping("/load-files")
    public Map<String, String> loadFiles(@RequestParam("directory_path") String directoryPath) {
        try {
            dataLoader.processFilesInDirectory(directoryPath);
            return Map.of("status", "success", "message", "Files loaded successfully");
        } catch (Exception e) {
            return Map.of("status", "error", "message", e.getMessage());
        }
    }

    /**
     * Handles POST requests to /load-file endpoint.
     * Accepts a single file upload and triggers file processing.
     * Uses DataLoader to load the file into a vector database.
     * Returns a status and message indicating success or error.
     *
     * @param file the uploaded file
     * @return a map with status and message
     */
    @PostMapping("/load-file")
    public Map<String, String> loadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Convert MultipartFile to Resource
            Resource resource = new InputStreamResource(file.getInputStream());

            dataLoader.addResource(resource);

            return Map.of("status", "success", "message", "File loaded successfully");
        } catch (Exception e) {
            return Map.of("status", "error", "message", e.getMessage());
        }
    }


}
