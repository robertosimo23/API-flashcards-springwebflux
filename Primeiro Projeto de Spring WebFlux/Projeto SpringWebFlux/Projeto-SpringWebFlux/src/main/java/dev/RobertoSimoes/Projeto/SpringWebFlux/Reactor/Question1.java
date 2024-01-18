package dev.RobertoSimoes.Projeto.SpringWebFlux.Reactor;
import reactor.core.publisher.Flux;

import java.util.List;

public class Question1 {

    /*
    Recebe uma lista de longs, incrementa 1 nos valores e retorna um flux dos resultados
     */
    public Flux<Long> inc(final List<Long> numbers){
        return Flux.fromIterable(numbers)
                .map(num -> num + 1);
    }

}