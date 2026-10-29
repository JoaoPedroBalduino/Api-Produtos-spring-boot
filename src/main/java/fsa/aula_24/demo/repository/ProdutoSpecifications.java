package fsa.aula_24.demo.repository;

import fsa.aula_24.demo.model.Produto;
import org.springframework.data.jpa.domain.Specification;

// Filtros opcionais da listagem de produtos; cada um só é aplicado se o valor vier preenchido
public final class ProdutoSpecifications {

    private ProdutoSpecifications() {
    }

    public static Specification<Produto> nomeContem(String nome) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Produto> daCategoria(Long categoriaId) {
        return (root, query, cb) -> cb.equal(root.get("categoria").get("id"), categoriaId);
    }

    public static Specification<Produto> precoMinimo(Double precoMin) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("preco"), precoMin);
    }

    public static Specification<Produto> precoMaximo(Double precoMax) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("preco"), precoMax);
    }

    public static Specification<Produto> disponivel(boolean disponivel) {
        return (root, query, cb) -> disponivel
                ? cb.greaterThan(root.get("quantidadeEstoque"), 0)
                : cb.equal(root.get("quantidadeEstoque"), 0);
    }

    public static Specification<Produto> premium(boolean premium) {
        return (root, query, cb) -> cb.equal(root.get("premium"), premium);
    }
}
