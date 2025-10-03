package dev.bharani.supportbot.ingestion.loader;

import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class DataLoader {

    //Inject autoconfigured PgVectorStore
    private final PgVectorStore vectorStore;

    public DataLoader(PgVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
         * Processes all regular files in the specified directory and adds them as resources
         * to the vector store. Each file is read, split into tokens, and ingested.
         *
         * @param directoryPath the path to the directory containing files to process
         * @throws IOException if an I/O error occurs accessing the directory or files
         */
    public void processFilesInDirectory(String directoryPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                    .forEach(this::addResource);
        }
    }

    /**
     * Adds a file resource to the vector store.
     * Reads the file, processes its content using TikaDocumentReader,
     * splits the text into tokens, and ingests the result into the vector store.
     *
     * @param path the path to the file to be added
     */
    public void addResource(Path path) {
        Resource resource = new FileSystemResource(path.toFile());
        TikaDocumentReader tikaDocumentReaderConfig = new TikaDocumentReader(resource);
        TokenTextSplitter textSplitter = new TokenTextSplitter();
        vectorStore.accept(textSplitter.apply(tikaDocumentReaderConfig.get()));
    }

    /**
     * Adds a file resource to the vector store using a Spring Resource.
     * Reads the file, processes its content using TikaDocumentReader,
     * splits the text into tokens, and ingests the result into the vector store.
     *
     * @param resource the Spring Resource representing the file to be added
     */
    public void addResource(Resource resource) {
        TikaDocumentReader tikaDocumentReaderConfig = new TikaDocumentReader(resource);
        TokenTextSplitter textSplitter = new TokenTextSplitter();
        vectorStore.accept(textSplitter.apply(tikaDocumentReaderConfig.get()));
    }

}

