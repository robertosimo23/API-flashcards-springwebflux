package dio.innovationone.Projeto.Academia.Spring.Boot.controller;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.AvaliacaoFisica;
import dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.AvaliacaoFisicaForm;
import dio.innovationone.Projeto.Academia.Spring.Boot.service.impl.AvaliacaoFisicaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/avaliacoes")

public class AvaliacaoFisicaController {

    @Autowired
    private AvaliacaoFisicaServiceImpl service;

    @PostMapping
    public AvaliacaoFisica create(@RequestBody AvaliacaoFisicaForm form) {
        return service.create(form);
    }

    @GetMapping
    public List<AvaliacaoFisica> getAll(){
        return service.getAll();
    }

}
