package ingu;

import ingu.publisher.Publisher;
import ingu.subscriber.Subscriber;

public class Main {
    public static void main(String[] args) {
        Publisher pub = new Publisher();
        Subscriber sub = new Subscriber();
        pub.subscribe(sub);
        sub.getSubscription().request(100);
    }
}
