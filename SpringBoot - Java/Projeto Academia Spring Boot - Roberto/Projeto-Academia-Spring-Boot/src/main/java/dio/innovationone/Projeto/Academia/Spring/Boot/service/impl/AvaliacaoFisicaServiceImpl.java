package dio.innovationone.Projeto.Academia.Spring.Boot.service.impl;

import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AvaliacaoFisicaUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import dio.innovationone.Projeto.Academia.Spring.Boot.entity.Aluno;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.AvaliacaoFisica;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AvaliacaoFisicaForm;
import dio.innovationone.Projeto.Academia.Spring.Boot.repository.AlunoRepository;
import dio.innovationone.Projeto.Academia.Spring.Boot.repository.AvaliacaoFisicaRepository;
import dio.innovationone.Projeto.Academia.Spring.Boot.service.IAvaliacaoFisicaService;

@Service
public class AvaliacaoFisicaServiceImpl implements IAvaliacaoFisicaService {

    @Autowired
    private AvaliacaoFisicaRepository avaliacaoFisicaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public AvaliacaoFisica create(AvaliacaoFisicaForm form) {
        AvaliacaoFisica avaliacaoFisica = new AvaliacaoFisica();
        Aluno aluno = alunoRepository.findById(form.getAlunoId()).get();

        avaliacaoFisica.setAluno(aluno);
        avaliacaoFisica.setPeso(form.getPeso());
        avaliacaoFisica.setAltura(form.getAltura());

        return avaliacaoFisicaRepository.save(avaliacaoFisica);
    }

    @Override
    public AvaliacaoFisica get(Long id) {
        return null;
    }

    @Override
    public List<AvaliacaoFisica> getAll() {

        return avaliacaoFisicaRepository.findAll();
    }

    @Override
    public AvaliacaoFisica update(Long id, AvaliacaoFisicaUpdateForm formUpdate) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
