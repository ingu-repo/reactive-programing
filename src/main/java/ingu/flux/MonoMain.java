package ingu.flux;

import ingu.flux.common.Util;
import reactor.core.publisher.Mono;

/**
 * Mono can have 1 or 0
 */
public class MonoMain {

    public static void main(String[] args) {
//        readFile();
//        writeFile();
        deleteFile();
    }
    public static void readFile() {
        var fileService = new FileReaderService();
        String fileName = "test.txt";
        Mono<String> mono = fileService.readForMono(fileName);
        mono.subscribe(Util.subscriber());
    }
    public static void writeFile() {
        var fileService = new FileReaderService();
        String fileName = "new.txt";
        String content = "hello there\n";
        Mono<Void> mono = fileService.writeForMono(fileName, content);
        mono.subscribe(Util.subscriber());
    }
    public static void deleteFile() {
        var fileService = new FileReaderService();
        String fileName = "new.txt";
        Mono<Void> mono = fileService.deleteForMono(fileName);
        mono.subscribe(Util.subscriber());
    }

}
