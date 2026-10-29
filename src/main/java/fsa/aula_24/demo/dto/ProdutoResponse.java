package fsa.aula_24.demo.dto;

import fsa.aula_24.demo.model.Categoria;
import fsa.aula_24.demo.model.Produto;

public record ProdutoResponse(
        Long id,
        String nome,
        Double preco,
        boolean premium,
        int quantidadeEstoque,
        boolean disponivel,
        Long categoriaId,
        String categoriaNome
) {

    public static ProdutoResponse de(Produto produto) {
        Categoria categoria = produto.getCategoria();
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.isPremium(),
                produto.getQuantidadeEstoque(),
                produto.getQuantidadeEstoque() > 0,
                categoria != null ? categoria.getId() : null,
                categoria != null ? categoria.getNome() : null);
    }
}
