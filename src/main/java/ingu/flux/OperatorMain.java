package ingu.flux;

import ingu.flux.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 * Operator runs making another instance that is the reason needs subscribe to consume
 *
 */
public class OperatorMain {
    private static final Logger log = LoggerFactory.getLogger(OperatorMain.class);

    public static void main(String[] args) {
        runHandle();
//        runManyInstances();
    }
    public static void runHandle() {
        Flux<Integer> nums = Flux.range(1, 10);
        nums
            .filter(v -> v != 7)
            .handle((item, sink) -> {
                switch (item) {
                    case 1 -> sink.next(-2);
                    case 4 -> {}
                    case 7 -> sink.error(new RuntimeException());
                    default -> sink.next(item);
                }
            })
            .subscribe(Util.subscriber());
    }
    public static void runManyInstances() {
        Flux<Integer> nums = Flux.range(1, 10);
        Flux<Integer> numsEven = nums.filter(v -> (v % 2) == 0);
        Flux<Integer> numsMod = numsEven.map(v -> v * 9);

        log.info("consuming nums");
        nums.subscribe(Util.subscriber());
        log.info("consuming even");
        numsEven.subscribe(Util.subscriber());
        log.info("consuming modified");
        numsMod.subscribe(Util.subscriber());
    }
}
