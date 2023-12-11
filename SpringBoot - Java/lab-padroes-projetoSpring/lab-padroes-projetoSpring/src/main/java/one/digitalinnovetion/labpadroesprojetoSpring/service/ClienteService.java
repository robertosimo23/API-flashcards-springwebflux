package one.digitalinnovetion.labpadroesprojetoSpring.service;
import one.digitalinnovetion.labpadroesprojetoSpring.model.Cliente;
public interface ClienteService {
    Iterable<Cliente> buscarTodos();
    Cliente buscarPorId(Long id);
    void inserir(Cliente cliente);
    void atualizar(Long id, Cliente cliente);
    void deletar(Long id);
}
