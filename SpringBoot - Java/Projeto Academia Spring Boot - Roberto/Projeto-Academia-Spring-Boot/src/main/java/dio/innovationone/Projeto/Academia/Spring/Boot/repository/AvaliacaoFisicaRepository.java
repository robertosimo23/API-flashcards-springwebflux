package dio.innovationone.Projeto.Academia.Spring.Boot.repository;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.AvaliacaoFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AvaliacaoFisicaRepository extends JpaRepository<AvaliacaoFisica, Long>  {


}
