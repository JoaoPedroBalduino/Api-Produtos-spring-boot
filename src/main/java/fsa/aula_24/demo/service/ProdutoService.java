package fsa.aula_24.demo.service;

import fsa.aula_24.demo.model.Produto;
import fsa.aula_24.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto buscarProduto(Long id) {
        if (id <= 0) {
            return null;
        }
        return produtoRepository.buscarPorId(id);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.listar();
    }
}