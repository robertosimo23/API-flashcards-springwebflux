package dio.innovationone.Projeto.Academia.Spring.Boot.controller;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.Aluno;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.AvaliacaoFisica;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AlunoForm;
import dio.innovationone.Projeto.Academia.Spring.Boot.service.impl.AlunoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping ("/alunos")
public class AlunoController {
    @Autowired
    private AlunoServiceImpl service;

    @PostMapping
    public Aluno create(@Valid @RequestBody AlunoForm form){
        return service.create(form);
    }
    @GetMapping ("/avaliacoes/{id}")
    public List<AvaliacaoFisica> getAllAvaliacaoFisicaId(@PathVariable Long id){
        return service.getAllAvaliacaoFisicaId(id);
    }
    @GetMapping
    public List<Aluno> getAll(@RequestParam(value = "dataDeNAscimento" , required = false)String dataDeNascimento){
        String dataDeNacimento = null;
        return service.getAll(dataDeNacimento);
    }
}
