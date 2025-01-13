package ingu.flux.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {

    public Flux<String> getNames() {
        return this.httpClient.get()
            .uri("/demo02/name/stream")
            .responseContent()
            .asString();
    }
    public Mono<String> getProductName(int id) {
        return this.httpClient.get()
            .uri("/demo/products/" + id)
            .responseContent()
            .asString()
            .next();
    }
    public Flux<Integer> getPriceChanges() {
        return this.httpClient.get()
            .uri("/demo02/stock/stream")
            .responseContent()
            .asString()
            .map(Integer::parseInt);
    }

}
