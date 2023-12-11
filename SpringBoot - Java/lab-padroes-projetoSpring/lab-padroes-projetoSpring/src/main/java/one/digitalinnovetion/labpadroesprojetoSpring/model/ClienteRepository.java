package one.digitalinnovetion.labpadroesprojetoSpring.model;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {


    Optional<Cliente> findAllById(Long id);
}
