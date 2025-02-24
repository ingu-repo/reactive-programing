package ingu.flux.scratch;

import ingu.flux.scratch.common.Util;
import ingu.flux.scratch.publisher.PublisherImpl;
import ingu.flux.subscriber.SubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        log.info("Main started");
//        demoGenericSubscriber();
//        demoMonoNativeSubscribe();
//        demoMono();
//        demoStreamForLazyConcept();
//        demoRequestMoreThanMax();
//        demoCancelAndContinueToRequest();
//        demoNotYetRequestToShowNothing();
        demoRequestToShow();
//        demoRequestOverMax();
    }
    private static void demoGenericSubscriber() {
        var mono = Mono.just("hello");
        mono.subscribe(Util.getSubscriber("ingu/flux/generic"));
    }
    /**
     * native method subscribe() implemented in Mono
     *  - it will call request automatically so emit value even without calling request
     *  - whereas using subscriber implemented by us needs to call request to emit actual value
     *    i.e. demoMono
     */
    private static void demoMonoNativeSubscribe() {
        // Successful case
        var mono = Mono.just("john");
        /**
         * below codes all possible having same meaning
         */
//        mono.subscribe(
//                item -> log.info("received: {}", item)
//        );
//        mono.subscribe(
//                item -> log.info("received: {}", item)
//                , err -> log.error("error: {}", err)
//                , () -> log.info("completed")
//        );
        mono.subscribe(
          item -> log.info("received: {}", item)
          , err -> log.error("error: {}", err)
          , () -> log.info("completed")
          , subscription -> subscription.request(900)
        );

        // Error case
        var mono2 = Mono.just(1)
                .map(i -> i/0);
        mono2.subscribe(
          i -> log.info("received: " + i)
          , err -> log.error("error:", err)
        );
    }
    private static void demoMono() {
        /* All same meaning
         *  Mono<String> mono = Mono.just("john");
         *  Publisher<String> mono = Mono.just("john");
         */
        var mono = Mono.just("john");
        /* Not printing actual value at this point because it is not yet subscribed */
        System.out.println(mono);
        /* Not printing actual value at this point because it is not yet requested */
        var subscriber = new SubscriberImpl();
        mono.subscribe(subscriber);
        /* printing value */
        subscriber.getSubscription().request(100);
        subscriber.getSubscription().cancel();
        subscriber.getSubscription().request(100);
    }
    private static void demoStreamForLazyConcept() {
        Stream.of(1)
                .forEach(i -> log.info("received: " + i));
    }
    private static void demoRequestMoreThanMax() {
        var subscriber = new SubscriberImpl();
        var publisher = new PublisherImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(400);
    }
    private static void demoCancelAndContinueToRequest() throws InterruptedException {
        var subscriber = new SubscriberImpl();
        var publisher = new PublisherImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(4);
        Thread.sleep(1000);
        subscriber.getSubscription().cancel();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(4);
    }
    private static void demoNotYetRequestToShowNothing() {
        var subscriber = new SubscriberImpl();
        var publisher = new PublisherImpl();
        publisher.subscribe(subscriber);
    }
    private static void demoRequestToShow() {
        var subscriber = new SubscriberImpl();
        var publisher = new PublisherImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(4);
    }
    private static void demoRequestOverMax() throws InterruptedException {
        var subscriber = new SubscriberImpl();
        var publisher = new PublisherImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(4);
        Thread.sleep(1000);
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(8);
    }
}
