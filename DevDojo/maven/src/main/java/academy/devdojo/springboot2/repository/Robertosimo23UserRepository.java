package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.domain.Robertosimo23User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Robertosimo23UserRepository extends JpaRepository<Robertosimo23User,Long> {

    Robertosimo23User findByUsername(String username);
}
