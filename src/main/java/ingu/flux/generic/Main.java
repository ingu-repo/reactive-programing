package ingu.flux.generic;

import ingu.flux.client.ExternalServiceClient;
import ingu.flux.client.ExternalServiceClient_SEO;
import ingu.flux.generic.common.Util;
import ingu.flux.generic.model.Product;
import ingu.flux.scratch.subscriber.SubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class Main {
    private static final String className = Main.class.getSimpleName();
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
//        testFluxNumber();
//        testFluxError();
//        testDelay();
//        testRunnable();
//        testFluxList();
//        testFluxStream();
//        testFluxRange();
//        testFluxLog();
//        testListWithSleep();
//        testFluxWithSleep();
//        testNonBlockingStreamingMsg();
//        testFuxInterval();
        testClientByOne();
//        testClientByAll();
    }
    /**
     * multiple subscribing with lambda methods
     */
    public static void testFluxNumber() {
        log.info("testFluxNumber()");
        var flux = Flux.just(456, 13, 900);
        // subscribe
        flux
            .subscribe(Util.subscriber("subA"));
        // subscribe by filter
        flux
            .filter(v -> v < 500)
            .subscribe(Util.subscriber("subB"));
        // subscribe by filter && modify
        flux
            .filter(v -> v % 2 == 0)
            .map(v -> "even " + v)
            .subscribe(Util.subscriber("subC"));
    }
    public static void testFluxError() {
        log.info("testFluxError(): case for No data");
        Flux.empty()
            .subscribe(Util.subscriber());
        log.info("testFluxError(): case for w/o Error handling");
        Flux.error(new RuntimeException("Flux error"))
            .subscribe(Util.subscriber());
        log.info("testFluxError(): case for Error handling");
        Flux.error(new RuntimeException("Flux error"))
            .subscribe(
                v -> Util.subscriber()
                , err -> {}
            );
    }
    /**
     * When doing some CPU intensive works and if it is not needed right away
     * it can make it delayed until when really needed by using fromSupplier() instead of just()
     */
    public static void testDelay() {
        var list = List.of(1, 2, 3);
        log.info("creating Mono.just()");
        var monoJust = Mono.just(sum(list));
        log.info("creating Mono.fromSupplier(): delaying sum() function until subscribed");
        var monoSupplier = Mono.fromSupplier(() -> sum(list));

        log.info("run Mono.just()");
        monoJust.subscribe(Util.subscriber());
        log.info("run Mono.fromSupplier()");
        monoSupplier.subscribe(Util.subscriber());
    }
    private static int sum(List<Integer> list) {
        log.info("sum of list: {}", list);
        return list.stream().mapToInt(i -> i).sum();
    }

    /**
     * just run method without return value to create Mono/Flux
     */
    public static void testRunnable() {
        getProductName(0)
            .subscribe(Util.subscriber());
    }
    private static Mono<String> getProductName(int id) {
        if (id > 0) {
            return Mono.fromSupplier(() -> Util.faker().commerce().productName());
        }
        return Mono.fromRunnable(() -> productNameError(id));
    }
    private static void productNameError(int id) {
        log.info("missing product id: {}", id);
    }
    /**
     * creating flux from list
     */
    public static void testFluxList() {
        log.info("testFluxList()");
        var list = List.of(2, 3, 4, 98, 99);
        Flux.fromIterable(list)
                .filter(v -> v < 10)
                .subscribe(Util.subscriber("subA"));
        Integer[] arr = {3, 90, 2312};
        Flux.fromArray(arr)
                .subscribe(Util.subscriber("subB"));
    }
    /**
     * flux from stream without error
     */
    public static void testFluxStream() {
        var list = List.of(2, 99, 78, 1024);
        var stream = list.stream();
        // Error Case because Java stream can be consumed only once
        Flux.fromStream(stream).subscribe(Util.subscriber("subA"));
        Flux.fromStream(stream).subscribe(Util.subscriber("subB"));
        // Solution create a stream for each time
        var flux = Flux.fromStream(list::stream);
        flux.subscribe(Util.subscriber("subA"));
        flux.subscribe(Util.subscriber("subB"));
    }
    /**
     * creating flux with range
     */
    public static void testFluxRange() {
        Flux.range(3, 5).subscribe(Util.subscriber());
        Flux.range(3, 10)
                .map(v -> Util.faker().name().lastName())
                .subscribe(Util.subscriber());
    }
    /**
     * lambda expressions for log
     */
    public static void testFluxLog() {
        log.info("testFluxLog()");
        Flux.range(1, 5)
                .log("first-log")
                .filter(v -> v % 2 == 0)
                .map(v -> "Hello " + v)
                .log("second-log")
                .subscribe(Util.subscriber("subA"));
    }
    public static void testListWithSleep() {
        log.info("testListWithSleep()");
        var list = Util.getNamesList(3);
        log.info("{}", list);
    }

    /**
     * Flux can stop in the middle of streams
     * But List can't
     */
    public static void testFluxWithSleep() {
        log.info("testFluxWithSleep(): Using Util subscriber");
        Util.getNamesFlux(3)
                .subscribe(Util.subscriber());

        log.info("testFluxWithSleep(): Using Own implemented");
        var subscriber = new SubscriberImpl();
        Util.getNamesFlux(3)
                .subscribe(subscriber);
        subscriber.getSubscription().request(1);
        subscriber.getSubscription().cancel();
    }
    public static void testFuxInterval() {
        log.info("testFuxInterval()");
        Flux.interval(Duration.ofSeconds(1))
                .subscribe(Util.subscriber("subB"));
        Util.sleepSeconds(5);
    }
    /**
     * !! Non-Blocking IO !!
     * NOTE:
     * 1. Mono<String> is async thread
     *    so program will exit before subscribed output printed if generation process takes time
     *    so no log printed out unless sleep performed
     * 2. Subscribed output will be randomly printed
     *    , although the requests were sent orderly in for loop
     * 3. It means all 100 requests were not sequential
     *    and all 100 requests are processed in parallel at same timing by thread processing
     *    so output also can be disordered
     *    Also that is the reason no more 1 second sleep in for loop
     *    (1 sec sleep intentianally implemented in server side)
     *
     * POINT:
     *    There are 10 different threads were made by Mono or Flux
     */
    public static void testClientByOne() {
        log.info("testClientByOne()");
        ExternalServiceClient client = new ExternalServiceClient();
        client.getProductName(1)
            .subscribe(Util.subscriber());
        Util.sleepSeconds(2);

        for (int i = 0; i < 100; i++) {
            client.getProductName(i)
                .subscribe(Util.subscriber());
        }
        Util.sleepSeconds(2);

        log.info("Completed !!");
    }
    public static void testClientByAll() {
        ExternalServiceClient_SEO client = new ExternalServiceClient_SEO();
        try {
            Flux products = client.getProductNames();
        } catch (Exception e) {
            log.error("error", e);
        }
    }

}
