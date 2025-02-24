package ingu.flux.subscriber;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subscribing contents of generic type
 * @param <T>
 */
public class DefaultSubscriber<T> implements Subscriber<T> {
    private static final String className = DefaultSubscriber.class.getSimpleName();
    private static final Logger log = LoggerFactory.getLogger(DefaultSubscriber.class);
    private static final int REQUEST_MAX_COUNT = 10000;
    private final String name;
    public DefaultSubscriber(String name) {
        log.debug("DefaultSubscriber({})", name);
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        log.debug("onSubscribe({})", this.name);
        subscription.request(REQUEST_MAX_COUNT);
    }

    @Override
    public void onNext(T item) {
        log.info("{}:onNext():received {}", this.name, item);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("{}:onError()", this.name, throwable);
    }

    @Override
    public void onComplete() {
        log.debug("{}:onComplete()", this.name);
    }
}
