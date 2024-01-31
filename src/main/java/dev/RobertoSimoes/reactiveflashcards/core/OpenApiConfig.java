package dev.RobertoSimoes.reactiveflashcards.core;


import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Reactive Flashcards",
                description = "API reativa de estudo de flashcards",
                version = "1.0.0"
        ),
        servers = {
                @Server(url = "http://localhost:8080/reactive-flashcards")
        }
)
public class OpenApiConfig {
}
