package dio.innovationone.Projeto.Academia.Spring.Boot.service;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.Aluno;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.AvaliacaoFisica;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AlunoForm;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AlunoUpdateForm;
import java.time.LocalDate;
import java.util.List;
public interface IAlunoService {

    Aluno create(AlunoForm form);

    Aluno get(Long id);

    List<Aluno> getAll(String dataDeNascimento);


    Aluno update(Long id, AlunoUpdateForm formUpdate);


    void delete(Long id);


    List<AvaliacaoFisica> getAllAvaliacaoFisicaId(Long id);
}
