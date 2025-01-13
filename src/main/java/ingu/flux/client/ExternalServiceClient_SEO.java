package ingu.flux.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ingu.flux.generic.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

public class ExternalServiceClient_SEO extends AbstractHttpClient_SEO {
    private static final Logger log = LoggerFactory.getLogger(ExternalServiceClient_SEO.class);

    public Mono<Product> getProductName(int id) {
        return Mono.fromSupplier(() -> sendRequestById(id));
    }
    public Product sendRequestById(int id) {
        Product product;
        try {
            String strJSON = this.request(id);
            ObjectMapper mapper = new ObjectMapper();
            product = mapper.readValue(strJSON, new TypeReference<Product>(){});

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return product;
    }

    public Flux<Product> getProductNames() {
        return Flux.fromStream(sendRequestForAll());
    }
    public Stream<Product> sendRequestForAll() {
        List<Product> products;
        try {
            String strJSON = this.request();
            ObjectMapper mapper = new ObjectMapper();
            products = mapper.readValue(strJSON, new TypeReference<List<Product>>(){});

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return products.stream();
    }

}
