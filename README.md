
## Theory
**Traditional Communication Pattern**
* request vs response pattern
* facing issues when multiple requesters / reqularly updates / blocking
* which is not efficient

**Reactive Programing Communication Pattern**
* publisher vs subscriber model
* asynchronous design solving non-blocking methods
* broadcasting rather than request everytime

**Reactive Package namespace**
* org.reactivestreams.Publisher;
* org.reactivestreams.Subscriber;
* org.reactivestreams.Subscription;

**Process Flow**
* Main / Publisher / Subscriber / Subscription
> main
<br> create Subscriber instance
<br> create Publisher instance 
<br> call Publisher.subscribe() with passing subscriber instance
>> Publisher.subscribe 
<br> create subscription instance
<br> call subsciber.onSubscribe() with passing subscription
>>> subscriber.onSubscribe 
<br> set passed subscription to local subscription

>main
<br> call subscriber.getSubscription().request(count)

## Mono vs Flux
*Below lines are all same meaning*
```java
Mono<String> mono = Mono.just("john");
Publisher<String> mono = Mono.just("john");
var mono = Mono.just("john");
```
**Mono**
* publish one item and finish right away because Mono is just for one item
* Used for one data expectaion such as primary key item
* So no need implement Stream / Back pressure / etc
* So it is just request and response model
* Simplest way to create Mono of Publisher is just() method

**Flux** 
* publish n items such as price chagnes intraday etc 
* Need Stream / Back pressure / extra mothods handling stream processing 

**Appendix**
* Process : unit of resources
* Thread  : unit of execution
* Heap memory : store objects that we create them dynamically i.e. ArrayList/HashMap
* Stac memory : store local references i.e. local variable / function called information
> CPU / Memory are expensive so reactive programing design comes up as part of thinking how to save such expensive costs
* Sync  : blocking model     :
* Async : non-blocking model : use resources more efficiently

 


