package dev.RobertoSimoes.reactiveflashcards.utils.request;

import lombok.AllArgsConstructor;
import org.springframework.cglib.core.internal.Function;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;
@AllArgsConstructor
public class NoBodyRequestBuilder<B> {
    private WebTestClient webTestClient;
    private Function<UriBuilder, URI> uriFunction;
    private Map<String, Set<String>> headers;
    private final Objects body;


    public EmptyBodyAsserUtils doDelete() {
        var preResponse = webTestClient.delete()
                .uri(uriFunction.toString())
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody()
                .isEmpty();
        return new EmptyBodyAsserUtils(response);

    }
    public EmptyBodyAsserUtils doPost() {
        var preResponse = webTestClient.post()
                .uri(uriFunction.toString())
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody()
                .returnResult();
        if (Objects.nonNull(body)){
            return new EmptyBodyAsserUtils(preResponse.bodyValue(body).exchange().expectBody().isEmpty());
        }
        return new EmptyBodyAsserUtils(preResponse.exchange().expectBody().isEmpty());

    }
    public EmptyBodyAsserUtils doPut() {
        var preResponse = webTestClient.put()
                .uri(uriFunction.toString())
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody()
                .returnResult();
        if (Objects.nonNull(body)){
            return new EmptyBodyAsserUtils(preResponse.bodyValue(body).exchange().expectBody().isEmpty());
        }
        return new EmptyBodyAsserUtils(preResponse.exchange().expectBody().isEmpty());

    }
}
