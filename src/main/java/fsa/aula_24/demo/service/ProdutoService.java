package fsa.aula_24.demo.service;

import fsa.aula_24.demo.dto.ProdutoRequest;
import fsa.aula_24.demo.dto.ProdutoResponse;
import fsa.aula_24.demo.exception.ConflitoException;
import fsa.aula_24.demo.exception.RecursoNaoEncontradoException;
import fsa.aula_24.demo.exception.RegraNegocioException;
import fsa.aula_24.demo.model.Categoria;
import fsa.aula_24.demo.model.Produto;
import fsa.aula_24.demo.repository.ProdutoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static fsa.aula_24.demo.repository.ProdutoSpecifications.daCategoria;
import static fsa.aula_24.demo.repository.ProdutoSpecifications.disponivel;
import static fsa.aula_24.demo.repository.ProdutoSpecifications.nomeContem;
import static fsa.aula_24.demo.repository.ProdutoSpecifications.precoMaximo;
import static fsa.aula_24.demo.repository.ProdutoSpecifications.precoMinimo;
import static fsa.aula_24.demo.repository.ProdutoSpecifications.premium;

@Service
public class ProdutoService {

    static final double LIMITE_PREMIUM = 3000.0;

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;
    private final EstoqueService estoqueService;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaService categoriaService,
                          EstoqueService estoqueService) {
        this.produtoRepository = produtoRepository;
        this.categoriaService = categoriaService;
        this.estoqueService = estoqueService;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar(String nome, Long categoriaId, Double precoMin, Double precoMax,
                                        Boolean disponivel, Boolean premium) {
        if (precoMin != null && precoMax != null && precoMin > precoMax) {
            throw new RegraNegocioException("precoMin não pode ser maior que precoMax");
        }

        List<Specification<Produto>> filtros = new ArrayList<>();
        if (nome != null && !nome.isBlank()) filtros.add(nomeContem(nome.trim()));
        if (categoriaId != null) filtros.add(daCategoria(categoriaId));
        if (precoMin != null) filtros.add(precoMinimo(precoMin));
        if (precoMax != null) filtros.add(precoMaximo(precoMax));
        if (disponivel != null) filtros.add(disponivel(disponivel));
        if (premium != null) filtros.add(premium(premium));

        return produtoRepository.findAll(Specification.allOf(filtros), Sort.by("id"))
                .stream().map(ProdutoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscar(Long id) {
        return ProdutoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public ProdutoResponse cadastrar(ProdutoRequest dados) {
        String nome = dados.nome().trim();
        if (produtoRepository.existsByNomeIgnoreCase(nome)) {
            throw new ConflitoException("Já existe um produto com o nome '" + nome + "'");
        }
        Categoria categoria = categoriaService.buscarEntidade(dados.categoriaId());
        validarPrecoNaCategoria(dados.preco(), categoria);

        Produto produto = new Produto(nome, dados.preco(), dados.quantidadeEstoque(), categoria);
        aplicarRegraPremium(produto);
        return ProdutoResponse.de(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest dados) {
        Produto produto = buscarEntidade(id);
        String nome = dados.nome().trim();
        if (produtoRepository.existsByNomeIgnoreCaseAndIdNot(nome, id)) {
            throw new ConflitoException("Já existe um produto com o nome '" + nome + "'");
        }
        Categoria categoria = categoriaService.buscarEntidade(dados.categoriaId());
        validarPrecoNaCategoria(dados.preco(), categoria);

        produto.setNome(nome);
        produto.setPreco(dados.preco());
        produto.setQuantidadeEstoque(dados.quantidadeEstoque());
        produto.setCategoria(categoria);
        aplicarRegraPremium(produto);
        return ProdutoResponse.de(produtoRepository.save(produto));
    }

    @Transactional
    public void excluir(Long id) {
        Produto produto = buscarEntidade(id);
        if (estoqueService.estaDisponivel(produto)) {
            throw new ConflitoException("O produto '" + produto.getNome() + "' ainda possui "
                    + produto.getQuantidadeEstoque() + " unidade(s) em estoque. Zere o estoque antes de excluir");
        }
        produtoRepository.delete(produto);
    }

    @Transactional
    public ProdutoResponse registrarEntrada(Long id, int quantidade) {
        Produto produto = buscarEntidade(id);
        estoqueService.registrarEntrada(produto, quantidade);
        return ProdutoResponse.de(produto);
    }

    @Transactional
    public ProdutoResponse registrarSaida(Long id, int quantidade) {
        Produto produto = buscarEntidade(id);
        estoqueService.registrarSaida(produto, quantidade);
        return ProdutoResponse.de(produto);
    }

    private Produto buscarEntidade(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto com id " + id + " não encontrado"));
    }

    private void validarPrecoNaCategoria(Double preco, Categoria categoria) {
        if (categoria.getPrecoMaximo() != null && preco > categoria.getPrecoMaximo()) {
            throw new RegraNegocioException("O preço R$ " + preco + " excede o máximo de R$ "
                    + categoria.getPrecoMaximo() + " permitido na categoria '" + categoria.getNome() + "'");
        }
    }

    private void aplicarRegraPremium(Produto produto) {
        produto.setPremium(produto.getPreco() > LIMITE_PREMIUM);
    }
}
