package ingu.flux.scratch.common;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSubscriber<T> implements Subscriber<T> {
    private static final Logger log = LoggerFactory.getLogger(DefaultSubscriber.class);
    private static final int MAX_ITEMS = 10;
    private final String name;
    public DefaultSubscriber(String name) {
        this.name = name;
    }
    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(MAX_ITEMS);
    }
    @Override
    public void onNext(T item) {
        log.info("{} received: ", this.name, item);
    }
    @Override
    public void onError(Throwable throwable) {
        log.error("{} faced error:", this.name, throwable);
    }
    @Override
    public void onComplete() {
        log.info("{} completed", this.name);
    }
}
