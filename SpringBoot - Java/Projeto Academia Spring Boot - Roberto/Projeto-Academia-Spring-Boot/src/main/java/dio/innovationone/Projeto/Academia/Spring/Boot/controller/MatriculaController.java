package dio.innovationone.Projeto.Academia.Spring.Boot.controller;
 import dio.innovationone.Projeto.Academia.Spring.Boot.entity.Matricula;
 import  dio.innovationone.Projeto.Academia.Spring.Boot.entity.form.MatriculaForm;
 import dio.innovationone.Projeto.Academia.Spring.Boot.service.impl.MatriculaServiceImpl;
 import jakarta.validation.Valid;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;
 import java.util.List;

 @RestController
 @RequestMapping("/matriculas")

public class MatriculaController {

     @Autowired
     private MatriculaServiceImpl service;

     @PostMapping
     public Matricula create(@Valid @RequestBody MatriculaForm form) {
         return service.create(form);
     }

     @GetMapping
     public List<Matricula> getAll(@RequestParam(value = "bairro", required = false) String bairro) {
         return service.getAll(bairro);
     }

}
