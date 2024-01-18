package dev.RobertoSimoes.Projeto.SpringWebFlux.Reactor;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;

public class Question2 {

    /*
    Recebe uma lista de usuários e retorna a quantos usuários admin tem na lista
     */
    public Mono<Long> countAdmins(final @NotNull List<User> users){
        return Mono.just(users.stream()
                .filter(User::isAdmin)
                .count());
    }

}