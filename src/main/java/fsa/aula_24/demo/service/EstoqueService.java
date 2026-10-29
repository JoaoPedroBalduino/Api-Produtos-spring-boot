package fsa.aula_24.demo.service;

import fsa.aula_24.demo.exception.RegraNegocioException;
import fsa.aula_24.demo.model.Produto;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    public boolean estaDisponivel(Produto produto) {
        return produto.getQuantidadeEstoque() > 0;
    }

    public void registrarEntrada(Produto produto, int quantidade) {
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
    }

    public void registrarSaida(Produto produto, int quantidade) {
        if (quantidade > produto.getQuantidadeEstoque()) {
            throw new RegraNegocioException("Estoque insuficiente para '" + produto.getNome()
                    + "': solicitado " + quantidade + ", disponível " + produto.getQuantidadeEstoque());
        }
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
    }
}
