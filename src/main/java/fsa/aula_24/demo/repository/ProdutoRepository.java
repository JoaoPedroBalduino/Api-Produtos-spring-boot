package fsa.aula_24.demo.repository;

import fsa.aula_24.demo.model.Produto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoRepository {

    private final List<Produto> produtos = new ArrayList<>();

    public ProdutoRepository() {
        produtos.add(new Produto(1L, "Notebook", 3500.0, "Eletrônicos", 5));
        produtos.add(new Produto(2L, "Mouse", 80.0, "Eletrônicos", 0));
        produtos.add(new Produto(3L, "Teclado", 150.0, "Eletrônicos", 10));
    }

    public List<Produto> listar() {
        return produtos;
    }

    public Produto buscarPorId(Long id) {
        for (Produto produto : produtos) {
            if (produto.getId().equals(id)) {
                return produto;
            }
        }
        return null;
    }
}