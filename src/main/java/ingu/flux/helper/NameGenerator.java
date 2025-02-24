package ingu.flux.helper;

import ingu.flux.common.Util;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

public class NameGenerator implements Consumer<FluxSink<String>> {
    private FluxSink<String> sink;

    // Constructor
    @Override
    public void accept(FluxSink<String> fluxSink) {
        this.sink = fluxSink;
    }
    public void generate() {
        this.sink.next(Util.faker().country().name());
    }
    public void complete() {
        this.sink.complete();
    }
}
