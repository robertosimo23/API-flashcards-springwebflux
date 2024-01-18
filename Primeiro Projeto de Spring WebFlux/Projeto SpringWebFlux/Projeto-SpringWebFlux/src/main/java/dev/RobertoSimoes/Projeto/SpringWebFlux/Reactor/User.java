package dev.RobertoSimoes.Projeto.SpringWebFlux.Reactor;

public record User(Long id,
                       String name,
                       String email,
                       String password,
                       Boolean isAdmin) {
    public String password() {
        return null;
    }

    public Boolean isAdmin() {
        return false;
    }

    }

