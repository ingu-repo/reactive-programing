package ingu.flux.subscriber;

import ingu.flux.model.Product;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProductSubscriber<T> implements Subscriber<T> {
    private static final Logger log = LoggerFactory.getLogger(ProductSubscriber.class);
    private static final int REQUEST_MAX_COUNT = 10000;

    private String subscriberName = "";
    public ProductSubscriber(String name) {
        this.subscriberName = name;
    }
    @Override
    public void onSubscribe(Subscription subscription) {
        log.info("onSubscribe({})", this.subscriberName);
        subscription.request(REQUEST_MAX_COUNT);
    }
    @Override
    public void onNext(T t) {
        Product p = (Product)t;
        log.info("received {}", p.getProductName());
    }
    @Override
    public void onError(Throwable throwable) {

    }
    @Override
    public void onComplete() {

    }
}
