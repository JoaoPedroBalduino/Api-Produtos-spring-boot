package fsa.aula_24.demo.service;

import fsa.aula_24.demo.dto.CategoriaRequest;
import fsa.aula_24.demo.dto.CategoriaResponse;
import fsa.aula_24.demo.dto.ProdutoResponse;
import fsa.aula_24.demo.exception.ConflitoException;
import fsa.aula_24.demo.exception.RecursoNaoEncontradoException;
import fsa.aula_24.demo.exception.RegraNegocioException;
import fsa.aula_24.demo.model.Categoria;
import fsa.aula_24.demo.repository.CategoriaRepository;
import fsa.aula_24.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar(String nome) {
        List<Categoria> categorias = (nome == null || nome.isBlank())
                ? categoriaRepository.findAll()
                : categoriaRepository.findByNomeContainingIgnoreCase(nome.trim());
        return categorias.stream().map(CategoriaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscar(Long id) {
        return CategoriaResponse.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarProdutos(Long id) {
        buscarEntidade(id);
        return produtoRepository.findByCategoriaId(id).stream().map(ProdutoResponse::de).toList();
    }

    @Transactional
    public CategoriaResponse cadastrar(CategoriaRequest dados) {
        String nome = dados.nome().trim();
        if (categoriaRepository.existsByNomeIgnoreCase(nome)) {
            throw new ConflitoException("Já existe uma categoria com o nome '" + nome + "'");
        }
        Categoria categoria = new Categoria(nome, dados.descricao(), dados.precoMaximo());
        return CategoriaResponse.de(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest dados) {
        Categoria categoria = buscarEntidade(id);
        String nome = dados.nome().trim();
        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(nome, id)) {
            throw new ConflitoException("Já existe uma categoria com o nome '" + nome + "'");
        }

        // O novo teto de preço não pode deixar produtos já cadastrados acima do limite
        if (dados.precoMaximo() != null) {
            Double maiorPreco = produtoRepository.maiorPrecoDaCategoria(id);
            if (maiorPreco != null && maiorPreco > dados.precoMaximo()) {
                throw new RegraNegocioException("Preço máximo de R$ " + dados.precoMaximo()
                        + " é menor que o preço de produtos já cadastrados na categoria (maior: R$ " + maiorPreco + ")");
            }
        }

        categoria.setNome(nome);
        categoria.setDescricao(dados.descricao());
        categoria.setPrecoMaximo(dados.precoMaximo());
        return CategoriaResponse.de(categoriaRepository.save(categoria));
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscarEntidade(id);
        if (produtoRepository.existsByCategoriaId(id)) {
            throw new ConflitoException("A categoria '" + categoria.getNome()
                    + "' possui produtos vinculados e não pode ser excluída");
        }
        categoriaRepository.delete(categoria);
    }

    Categoria buscarEntidade(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria com id " + id + " não encontrada"));
    }
}
