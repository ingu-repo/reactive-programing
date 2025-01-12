package ingu.flux.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {
    public Flux<String> getNames() {
        return this.httpClient.get()
                .uri("/demo/name/stream")
                .responseContent()
                .asString();
    }
}
