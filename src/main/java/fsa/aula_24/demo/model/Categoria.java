package fsa.aula_24.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    // Teto de preço para produtos desta categoria (opcional)
    private Double precoMaximo;

    @OneToMany(mappedBy = "categoria")
    private List<Produto> produtos = new ArrayList<>();

    protected Categoria() {
    }

    public Categoria(String nome, String descricao, Double precoMaximo) {
        this.nome = nome;
        this.descricao = descricao;
        this.precoMaximo = precoMaximo;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getPrecoMaximo() { return precoMaximo; }
    public void setPrecoMaximo(Double precoMaximo) { this.precoMaximo = precoMaximo; }

    public List<Produto> getProdutos() { return produtos; }
}
