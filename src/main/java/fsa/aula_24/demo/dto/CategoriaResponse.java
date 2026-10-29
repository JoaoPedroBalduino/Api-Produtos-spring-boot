package fsa.aula_24.demo.dto;

import fsa.aula_24.demo.model.Categoria;

public record CategoriaResponse(Long id, String nome, String descricao, Double precoMaximo, int totalProdutos) {

    public static CategoriaResponse de(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getPrecoMaximo(),
                categoria.getProdutos().size());
    }
}
