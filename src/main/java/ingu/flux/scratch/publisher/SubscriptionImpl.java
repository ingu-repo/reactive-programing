package ingu.flux.scratch.publisher;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionImpl implements Subscription {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionImpl.class);
    private static final int MAX_ITEMS = 10;
    private final Subscriber<? super String> subscriber;
    private final Faker faker;
    private static boolean isCanceled = false;
    private static int counter = 0;

    public SubscriptionImpl(Subscriber<? super String> subscriber) {
        this.subscriber = subscriber;
        this.faker = Faker.instance();
    }
    @Override
    public void request(long requestedNo) {
        if (this.isCanceled) return;
        log.info("subscriber has requested {} items", requestedNo);
        if (requestedNo > MAX_ITEMS) {
            this.subscriber.onError(new RuntimeException("requested more than MAX " + MAX_ITEMS));
            this.isCanceled = true;
            return;
        }
        for (int i = 0; i < requestedNo && counter < MAX_ITEMS; i++) {
            counter++;
            log.info("total requested count: {}", counter );
            subscriber.onNext(this.faker.internet().emailAddress());
        }
        if (counter == MAX_ITEMS) {
            log.info("subscribing items reached max count");
            subscriber.onComplete();
            this.isCanceled = true;
        }
    }
    @Override
    public void cancel() {
        log.info("subscriber has canceled");
        this.isCanceled = true;
    }
}
