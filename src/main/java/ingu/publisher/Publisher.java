package ingu.publisher;

import org.reactivestreams.Subscriber;

public class Publisher implements org.reactivestreams.Publisher {
    @Override
    public void subscribe(Subscriber subscriber) {
        Subscription subscription = new Subscription();
        subscriber.onSubscribe(subscription);
    }
}
