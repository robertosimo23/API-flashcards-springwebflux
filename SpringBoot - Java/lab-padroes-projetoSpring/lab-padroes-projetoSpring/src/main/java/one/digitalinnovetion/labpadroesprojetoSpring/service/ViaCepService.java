package one.digitalinnovetion.labpadroesprojetoSpring.service;
import one.digitalinnovetion.labpadroesprojetoSpring.model.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "Viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {

    @RequestMapping(method = RequestMethod.GET, value = "/{cep}/json/")
    static Endereco consultarCep(@PathVariable("cep") String cep);
}
