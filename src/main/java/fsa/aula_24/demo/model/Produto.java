package fsa.aula_24.demo.model;

public class Produto {

    private Long id;
    private String nome;
    private Double preco;
    private String categoria;
    private boolean premium;
    private int quantidadeEstoque;

    public Produto(Long id, String nome, Double preco, String categoria, int quantidadeEstoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Double getPreco() { return preco; }
    public String getCategoria() { return categoria; }
    public boolean isPremium() { return premium; }
    public void setPremium(boolean premium) { this.premium = premium; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
}