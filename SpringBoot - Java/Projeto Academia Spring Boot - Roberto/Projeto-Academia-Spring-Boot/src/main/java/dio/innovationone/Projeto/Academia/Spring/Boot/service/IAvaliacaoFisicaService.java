package dio.innovationone.Projeto.Academia.Spring.Boot.service;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.AvaliacaoFisica;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AvaliacaoFisicaForm;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AvaliacaoFisicaUpdateForm;
import java.util.List;
public interface IAvaliacaoFisicaService {



    AvaliacaoFisica create(AvaliacaoFisicaForm form);


    AvaliacaoFisica get(Long id);


    List<AvaliacaoFisica> getAll();


    AvaliacaoFisica update(Long id, AvaliacaoFisicaUpdateForm formUpdate);


    void delete(Long id);
}
