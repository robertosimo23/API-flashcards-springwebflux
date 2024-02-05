package dev.RobertoSimoes.reactiveflashcards.domain.service.query;

import dev.RobertoSimoes.reactiveflashcards.domain.repository.UserRepository;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.UserRepositoryImpl;
import dev.RobertoSimoes.reactiveflashcards.domain.service.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith((MockitoExtension.class))
public class UserQueryServiceTest {
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  UserRepositoryImpl userRepositoryImpl;

    private UserQueryService userQueryService;

    @BeforeEach
    void setup(){
        userQueryService = new UserQueryService(userRepository,userRepositoryImpl);
    }
}
