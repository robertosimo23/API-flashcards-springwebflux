package dio.innovationone.Projeto.Academia.Spring.Boot.service.impl;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.Aluno;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.Matricula;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.MatriculaForm;
import dio.innovationone.Projeto.Academia.Spring.Boot.repository.AlunoRepository;
import  dio.innovationone.Projeto.Academia.Spring.Boot.repository.MatriculaRepository;
import dio.innovationone.Projeto.Academia.Spring.Boot.service.IMatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatriculaServiceImpl implements IMatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public Matricula create(MatriculaForm form) {
        Matricula matricula = new Matricula();
        Aluno aluno = alunoRepository.findById(form.getAlunoId()).get();

        matricula.setAluno(aluno);

        return matriculaRepository.save(matricula);
    }

    @Override
    public Matricula get(Long id) {
        return matriculaRepository.findById(id).get();
    }

    @Override
    public List<Matricula> getAll(String bairro) {

        if(bairro == null){
            return matriculaRepository.findAll();
        }else{
            return matriculaRepository.findAlunosMatriculadosBairro(bairro);
        }

    }

    @Override
    public void delete(Long id) {}
}
