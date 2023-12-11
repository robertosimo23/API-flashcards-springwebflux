package dio.innovationone.Projeto.Academia.Spring.Boot.service;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.Matricula;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.MatriculaForm;
import java.util.List;
public interface IMatriculaService {


    Matricula create(MatriculaForm form);


    Matricula get(Long id);


    List<Matricula> getAll(String bairro);


    void delete(Long id);
}
