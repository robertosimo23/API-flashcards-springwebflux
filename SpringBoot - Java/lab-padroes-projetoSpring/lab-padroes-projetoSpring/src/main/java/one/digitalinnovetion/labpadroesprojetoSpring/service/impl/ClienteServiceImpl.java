package one.digitalinnovetion.labpadroesprojetoSpring.service.impl;
import one.digitalinnovetion.labpadroesprojetoSpring.model.Cliente;
import one.digitalinnovetion.labpadroesprojetoSpring.model.ClienteRepository;
import one.digitalinnovetion.labpadroesprojetoSpring.model.Endereco;
import one.digitalinnovetion.labpadroesprojetoSpring.model.EnderecoRepository;
import one.digitalinnovetion.labpadroesprojetoSpring.service.ClienteService;
import one.digitalinnovetion.labpadroesprojetoSpring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findAllById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        SalvarClienteComCep(cliente);


    }

    private void SalvarClienteComCep(Cliente cliente) {
        String cep= cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(Long.valueOf(cep)).orElseGet(() ->{
           Endereco novoEndereco = ViaCepService.consultarCep(cep);
           enderecoRepository.save(novoEndereco);
             return novoEndereco;
         });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()){
            SalvarClienteComCep(cliente);

        }

    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);

    }
}
