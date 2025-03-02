package ingu.flux.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Reading flie contents line by line
 * and emit the lines to Flux
 */
public class FileService {
    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    private static final Path path = Path.of("/Users/appa/Documents/Projects/Java/reactive-programing/data");

    /**
     * for Mono
     */
    public Mono<String> readForMono(String fileName) {
        return Mono.fromCallable(() -> this.readFile(fileName));
    }
    public Mono<Void> writeForMono(String fileName, String content) {
        return Mono.fromRunnable(() -> this.writeFile(fileName, content));
    }
    public Mono<Void> deleteForMono(String fileName) {
        return Mono.fromRunnable(() -> this.deleteFile(fileName));
    }
    private String readFile(String fileName) {
        log.info("read file {} from path {}", fileName, path);
        try {
            return Files.readString(path.resolve(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void writeFile(String fileName, String content) {
        log.info("write file {} to path {}", fileName, path);
        try {
            Files.writeString(path.resolve(fileName), content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteFile(String fileName) {
        log.info("delete file {} from path {}", fileName, path);
        try {
            Files.deleteIfExists(path.resolve(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * for Flux
     */
    public Flux<String> readForFlux(String fileName) {
        return Flux.generate(
            () -> openFile(path)    // init
            , this::readFile        // main
            , this::closeFile       // final
        );
    }
    private BufferedReader readFile(BufferedReader reader, SynchronousSink<String> sink) {
        try {
            var line = reader.readLine();
            if (Objects.isNull(line)) {
                sink.complete();
            } else {
                log.info("reading line: {}", line);
                sink.next(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            sink.error(e);
        }
        return reader;
    }
    private BufferedReader openFile(Path path) {
        try {
            return Files.newBufferedReader(path);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    private void closeFile(BufferedReader reader) {
        try {
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
