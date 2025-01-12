package ingu.flux.generic.common;

import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSubscriber<T> implements Subscriber<T> {
    private static final String className = DefaultSubscriber.class.getSimpleName();
    private static final Logger log = LoggerFactory.getLogger(DefaultSubscriber.class);
    private final String name;
    public DefaultSubscriber(String name) {
        log.debug("DefaultSubscriber({})", name);
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        log.debug("onSubscribe({})", this.name);
        subscription.request(10);
    }

    @Override
    public void onNext(Object o) {
        log.info("{}:onNext():received {}", this.name, o);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(className + ".onError()");
    }

    @Override
    public void onComplete() {
        System.out.println(className + ".onComplete()");
    }
}
