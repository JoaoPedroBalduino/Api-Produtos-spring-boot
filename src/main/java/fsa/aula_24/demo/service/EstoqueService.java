package fsa.aula_24.demo.service;

import fsa.aula_24.demo.model.Produto;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    public boolean estaDisponivel(Produto produto) {
        return produto.getQuantidadeEstoque() > 0;
    }
}