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
        Produto produto = produtoRepository.buscarPorId(id);
        aplicarRegraPremium(produto);
        return produto;
    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = produtoRepository.listar();
        for (Produto produto : produtos) {
            aplicarRegraPremium(produto);
        }
        return produtos;
    }

    private void aplicarRegraPremium(Produto produto) {
        if (produto != null) {
            produto.setPremium(produto.getPreco() > 3000);
        }
    }
}