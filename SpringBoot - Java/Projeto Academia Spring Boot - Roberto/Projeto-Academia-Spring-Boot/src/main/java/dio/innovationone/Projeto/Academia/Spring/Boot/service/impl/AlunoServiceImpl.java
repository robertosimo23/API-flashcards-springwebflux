package dio.innovationone.Projeto.Academia.Spring.Boot.service.impl;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.Aluno;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.AvaliacaoFisica;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AlunoForm;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AlunoUpdateForm;
import dio.innovationone.Projeto.Academia.Spring.Boot.infra.utils.JavaTimeUtils;
import dio.innovationone.Projeto.Academia.Spring.Boot.repository.AlunoRepository;
import dio.innovationone.Projeto.Academia.Spring.Boot.service.IAlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class AlunoServiceImpl implements IAlunoService {

    @Autowired
    private AlunoRepository repository;

    @Override
    public Aluno create(AlunoForm form) {
        Aluno aluno = new Aluno();
        aluno.setNome(form.getNome());
        aluno.setCpf(form.getCpf());
        aluno.setBairro(form.getBairro());
        aluno.setDataDeNascimento(form.getDataDeNascimento());

        return repository.save(aluno);
    }

    @Override
    public Aluno get(Long id) {
        return null;
    }

    @Override
    public List<Aluno> getAll(String dataDeNascimento) {

        if(dataDeNascimento == null) {
            return repository.findAll();
        } else {
            LocalDate localDate = LocalDate.parse(dataDeNascimento, JavaTimeUtils.LOCAL_DATE_FORMATTER);
            return repository.findByDataDeNascimento(localDate);
        }

    }

    @Override
    public Aluno update(Long id, AlunoUpdateForm formUpdate) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public List<AvaliacaoFisica> getAllAvaliacaoFisicaId(Long id) {

        Aluno aluno = repository.findById(id).get();

        return aluno.getAvaliacoes();

    }
}
