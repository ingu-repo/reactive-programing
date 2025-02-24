package ingu.flux;

import ingu.flux.common.Util;
import ingu.flux.helper.NameGenerator;
import ingu.flux.subscriber.SubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * Flux can have multiple data
 *
 * create(FluxSink) :
 *  - desinged to be used for a single subscriber
 *  - thread safe
 *  - can keep emitting without thinking about whether subscriber receives or not
 *
 * generate(SynchronousSink)
 *  - loop concept implemented internally, so can't generate multiple data inside method
 *
 */
public class FluxMain {

    private static final Logger log = LoggerFactory.getLogger(FluxMain.class);

    public static void main(String[] args) {
        fileToFlux();
//        generateBySelfControl();
//        generateByCond();
//        generateBySync();
//        takeOnDemand();
//        emitOnDemand();
//        emitWithoutControl();
//        emitThreadSafe();
//        nonThreadSafeCodes();
//        emitFromBizLogic();
//        emitUntilCondition();
//        emitInLoop();
//        emitByFluxSink();
    }

    /**
     * Calling File to Flux service which returns Flux<String>
     * and subscribe it to show
     */
    public static void fileToFlux() {
        String fileName = "test.txt";
        var fileService = new FileReaderService();
        fileService.readForFlux(fileName)
            .takeUntil(s -> s.equalsIgnoreCase("line 33"))
            .subscribe(Util.subscriber());
    }
    /**
     * Stop in case either of below cases
     *  - country is Canada
     *  - or 10 records generated
     */
    public static void generateBySelfControl() {
        Flux.generate(
            () -> 0                     // initial value
            , (counter , sync) -> {     // take 2 params
                var name = Util.faker().country().name();
                sync.next(name);
                counter++;
                if (counter == 10 || name.equalsIgnoreCase("canada")) {
                    sync.complete();
                }
                return counter;
            }
        )
        .subscribe(Util.subscriber());
    }
    public static void generateByCond() {
        Flux.generate(sync -> {
            var name = Util.faker().country().name();
            sync.next(name);
            if (name.equalsIgnoreCase("israel")) {
                sync.complete();
            }
        })
        .subscribe(Util.subscriber());
        System.out.println();

        Flux.<String>generate(sync -> {
            var name = Util.faker().country().name();
            sync.next(name);
        })
        .takeUntil(c -> c.equalsIgnoreCase("congo"))
        .subscribe(Util.subscriber());
    }
    public static void generateBySync() {
        Flux.generate(sync -> {
            sync.next(1);
//            sync.next(2);   // It won't work emit more than 2times but emits infinitely by being called from subsribe
            sync.complete();   // Unless this line, it will be infinite loop
        })
        .subscribe(Util.subscriber());
    }
    public static void takeOnDemand() {
        int takingItems = 2;
        int takingMaxItems = 50000;
        Flux.range(1, 10)
            .log("take")
            .take(takingItems)
            .log("sub")
            .subscribe(Util.subscriber());
        log.info("Take only {} item(s)", takingItems);
        System.out.println();

        Flux.range(1, 10)
            .takeWhile(i -> i < takingItems)
            .subscribe(Util.subscriber());
        log.info("taking while only i < {}", takingItems);
        System.out.println();

        Flux.<String>range(1, 10)
            .takeUntil(i -> i > takingMaxItems)
            .subscribe(Util.subscriber());
        log.info("taking until i > {}", takingItems);
        System.out.println();

    }
    /**
     * Fix overflow issue
     */
    public static void emitOnDemand() {
        var subscriber = new SubscriberImpl();
        Flux.<String>create(fluxSink -> {
            fluxSink.onRequest(reqCnt -> {
                for (int i=0; i<reqCnt && !fluxSink.isCancelled(); i++) {
                    var name = Util.faker().name().firstName();
                    log.info("generated: {}", name);
                    fluxSink.next(name);
                }
            });
        })
        .subscribe(subscriber);
        subscriber.getSubscription().request(1);
        System.out.println();
        subscriber.getSubscription().request(3);
        subscriber.getSubscription().cancel();
        subscriber.getSubscription().request(10);
    }
    /**
     * Regardless of subscriber, publisher will just generate what it can do and make que up
     * It can cause overflow issue if subscriber does not receive from the que
     */
    public static void emitWithoutControl() {
        var subscriber = new SubscriberImpl();
        Flux.<String>create(fluxSink -> {
            for (int i=0; i<10; i++) {
                var name = Util.faker().name().firstName();
                log.info("generated: {}", name);
                fluxSink.next(name);
            }
            fluxSink.complete();
        })
        .subscribe(subscriber);

        subscriber.getSubscription().request(1);
        subscriber.getSubscription().cancel();
        subscriber.getSubscription().request(1);    // won't be received due to canceled
    }
    public static void emitThreadSafe() {
        var names = new ArrayList<>();
        var generator = new NameGenerator();
        var flux = Flux.create(generator);
        // Two ways of expression
//        flux.subscribe(name -> {
//            names.add(name);
//        });
//        or
        flux.subscribe(names::add);

        Runnable runnable = () -> {
            for (int i=0; i<1000; i++) {
                generator.generate();
            }
        };
        for (int i=0; i<10; i++) {
            new Thread(runnable).start();
        }

        // generator.complete();
        Util.sleepSeconds(1);
        System.out.println("Tread Safe Counter: " + names.size());
    }
    public static void nonThreadSafeCodes() {
        // ArrayList is NOT thread safe
        List<Integer> counter = new ArrayList<>();
        // Biz logics
        Runnable runnable = () -> {
            for (int i=0; i<1000; i++) {
                counter.add(i);
            }
        };
        // Create 10 threads
        for (int i=0; i<10; i++) {
            new Thread(runnable).start();           // java 17
            // Thread.ofPlatform().start(runnable); // java 21
        }
        // Check counter which would not be
        System.out.println("Tread Non Safe Counter: " + counter.size());
    }
    public static void emitFromBizLogic() {
        NameGenerator generator = new NameGenerator();
        var flux = Flux.create(generator);
        flux.subscribe(Util.subscriber());
        for (int i=0; i<3; i++) {
            generator.generate();
        }
        generator.complete();
    }
    public static void emitUntilCondition() {
        // having some conditions
        Flux.create(fluxSink -> {
                String name;
                do {
                    name = Util.faker().country().name();
                    fluxSink.next(name);
                } while (! name.equalsIgnoreCase("canada"));
                fluxSink.complete();
            })
            .subscribe(Util.subscriber());
        System.out.println();
    }
    public static void emitInLoop() {
        // emit country names
        Flux.create(fluxSink -> {
                for (int i = 0; i < 3; i++) {
                    fluxSink.next(Util.faker().country().name());
                }
                fluxSink.complete();
            })
            .subscribe(Util.subscriber());
        System.out.println();
    }
    public static void emitByFluxSink() {
        // emit numbers
        Flux.create(fluxSink -> {
                fluxSink.next(1);
                fluxSink.next(2);
                fluxSink.next(3);
                fluxSink.complete();
            })
            .subscribe(Util.subscriber());
        System.out.println();
    }

}
