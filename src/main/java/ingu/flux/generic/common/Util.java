package ingu.flux.generic.common;
import com.github.javafaker.Faker;
import ingu.flux.generic.subscriber.DefaultSubscriber;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

public class Util {
    private static final String className = Util.class.getSimpleName();
    private static final Logger log = LoggerFactory.getLogger(Util.class);
    private static final Faker faker = Faker.instance();

    public static <T> Subscriber<T> subscriber() {
        log.debug("subscriber()");
        return new DefaultSubscriber<>("");
    }
    public static <T> Subscriber<T> subscriber(String name) {
        log.debug("subscriber({})", name);
        return new DefaultSubscriber<>(name);
    }
    public static Faker faker() {
        return faker;
    }
    public static List<String> getNamesList(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Util.generateName())
                .toList();
    }
    public static Flux<String> getNamesFlux(int count) {
        return Flux.range(1, count)
                .map(i -> Util.generateName());
    }
    public static String generateName() {
        Util.sleepSeconds(2);
        return Util.faker().name().firstName();
    }
    public static void sleepSeconds(long seconds){
        try {
//            System.out.println("sleeping " + seconds);
//            Thread.sleep(Duration.ofSeconds(seconds));
            Thread.sleep (seconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
