package ingu.flux.generic;

import ingu.flux.client.ExternalServiceClient;
import ingu.flux.generic.common.Util;
import ingu.flux.scratch.subscriber.SubscriberImpl;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class Main {
    private static final String className = Main.class.getSimpleName();
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
//        testFluxNumber();
//        testFluxList();
//        testFluxStream();
//        testFluxRange();
//        testFluxLog();
//        testListWithSleep();
//        testFluxWithSleep();
//        testNonBlockingStreamingMsg();
        testFuxInterval();
    }
    /**
     * multiple subscribing with lambda methods
     */
    public static void testFluxNumber() {
        System.out.println(className + ".testFluxNumber()");
        var flux = Flux.just(456, 13, 900);
        flux.subscribe(Util.subscriber("subA"));
        flux.filter(v -> v < 100)
                .subscribe(Util.subscriber("subB"));
        flux.filter(v -> v % 2 == 0)
                .map(v -> "filtered " + v)
                .subscribe(Util.subscriber("subC"));
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
    /**
     * Make sure external service is up and running
     */
    public static void testNonBlockingStreamingMsg() {
        log.info("testNonBlockingStreamingMsg()");
        var client = new ExternalServiceClient();
        client.getNames()
                .subscribe(Util.subscriber("subA"));
    }
    public static void testFuxInterval() {
        log.info("testFuxInterval()");
//        Flux.range(1, 2)
//                //.map(i -> Util.generateName())
//                .subscribe(Util.subscriber("subA"));

        Flux.interval(Duration.ofSeconds(1))
                .subscribe(Util.subscriber("subB"));

        Util.sleepSeconds(5);
    }
}
