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
public class SimpleRequestBuilder<B> {

    private WebTestClient webTestClient;
    private Function<UriBuilder, URI> uriFunction;
    private Map<String, Set<String>> headers;
    private Object body;
    private Class<B> responseClass;

    public SimpleBodyAsserUtils<B> doGet() {
        var preResponse = webTestClient.get()
                .uri(uriFunction.toString())
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody(responseClass)
                .returnResult();
        return new SimpleBodyAsserUtils<>(response);
    }

    public SimpleBodyAsserUtils<B>  doDelete() {
        var preResponse = webTestClient.delete()
                .uri(uriFunction.toString())
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody(responseClass)
                .returnResult();
        return new SimpleBodyAsserUtils<>(response);

    }
    public SimpleBodyAsserUtils<B> doPost() {
        var preResponse = webTestClient.post()
                .uri(uriFunction.toString())
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody(responseClass)
                .returnResult();
        if (Objects.nonNull(body)){
            return new SimpleBodyAsserUtils<>(preResponse.bodyValue(body).exchange().expectBody(responseClass).returnResult());
        }
        return new SimpleBodyAsserUtils<>(preResponse.exchange().expectBody(responseClass).returnResult());

    }
    public SimpleBodyAsserUtils<B> doPut() {
        var preResponse = webTestClient.put()
                .uri(uriFunction.toString())
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody(responseClass)
                .returnResult();
        if (Objects.nonNull(body)){
            return new SimpleBodyAsserUtils<>(preResponse.bodyValue(body).exchange().expectBody(responseClass).returnResult());
        }
        return new SimpleBodyAsserUtils<>(preResponse.exchange().expectBody(responseClass).returnResult());

    }
}
