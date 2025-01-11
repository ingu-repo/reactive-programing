
### Theory
*Traditional Communication Pattern*
* request vs response pattern
* facing issues when multiple requesters / reqularly updates / blocking
* which is not efficient

*Reactive Programing Communication Pattern*
* publisher vs subscriber model
* asynchronous design solving non-blocking methods
* broadcasting rather than request everytime

*Reactive Package namespace*
* org.reactivestreams.Publisher;
* org.reactivestreams.Subscriber;
* org.reactivestreams.Subscription;

*Procedure : overall, publisher creating subscription and connect to subscriber for the subscription*
> main
    -> create Subscriber instance
    -> create Publisher instance 
        -> call Publisher.subscribe passing subscriber instance
            -> Publisher.subscribe do 
                -> creating subscription isntance passing subscriber 
                -> call subsciber.onSubscribe passing subscription 
                    -> subscriber.onSubscribe do 
                        -> set subscription variable 
        -> call subscriber.getSubscription().request(count)

### Publisher can have Mono or Flux
*Mono*
* publish one itme and then complete right away because Mono is for just one item
* Used for one data expectaion such as primary key item
* So no need implement Stream / Back pressure / etc
* So it is just request and response model
> Simplest way to create Mono publisher is just() method

*Flux* 
* publish n items such as price chagnes intraday etc 
* Need Stream / Back pressure / extra mothods handling stream processing 

 


