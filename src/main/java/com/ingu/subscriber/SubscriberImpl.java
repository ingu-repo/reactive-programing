package com.ingu.subscriber;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * public class SubscriberImpl<T> implements Subscriber<T> {
 * if do like above, compile error in
 * public void onNext(T item) {
 *  log.info(item);
 */
public class SubscriberImpl implements Subscriber<String> {
    private static final Logger log = LoggerFactory.getLogger(SubscriberImpl.class);
    private Subscription subscription;

    public Subscription getSubscription() {
        return subscription;
    }
    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
    }
    @Override
    public void onNext(String s) {
        log.info("received: " + s);
    }
    @Override
    public void onError(Throwable throwable) {
        log.error("error: ", throwable);
    }
    @Override
    public void onComplete() {
        log.info("completed");
    }
}
