package ingu.subscriber;

import lombok.Getter;
import org.reactivestreams.Subscription;

public class Subscriber implements org.reactivestreams.Subscriber {
    @Getter
    private org.reactivestreams.Subscription subscription;
    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("onSubscribe started");
        this.subscription = subscription;
    }

    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
