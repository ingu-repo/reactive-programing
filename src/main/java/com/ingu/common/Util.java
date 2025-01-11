package com.ingu.common;

import org.reactivestreams.Subscriber;

public class Util {

    public static <T> Subscriber<T> getSubscriber() {
        return new DefaultSubscriber<>("");
    }
    public static <T> Subscriber<T> getSubscriber(String name) {
        return new DefaultSubscriber<>(name);
    }

}
