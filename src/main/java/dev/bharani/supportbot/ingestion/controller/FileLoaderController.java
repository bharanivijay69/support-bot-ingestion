package dev.bharani.supportbot.ingestion.controller;

import dev.bharani.supportbot.ingestion.loader.DataLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
