package fsa.aula_24.demo.controller;

import fsa.aula_24.demo.model.Produto;
import fsa.aula_24.demo.service.EstoqueService;
import fsa.aula_24.demo.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProdutoController {

    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public ProdutoController(ProdutoService produtoService, EstoqueService estoqueService){
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    @GetMapping("/bem-vindo/{nome}")
    public String bemVindo(@PathVariable String nome){
        return "Bem-vindo á nossa loja, " + nome + "!";
    }

    @GetMapping("/sobre")
    public String sobre(){
        return "API de Produto - Balduino";
    }

    @GetMapping("/lista")
    public List<String> listaNomes(){
        List<String> nomes = new ArrayList<>();
        nomes.add("Perfume");
        nomes.add("Shampoo");
        nomes.add("Sabonete");
        nomes.add("Pente de Cabelo");
        return nomes;
    }

    @GetMapping("/produtos")
    public List<Produto> listarProdutos(){
        return produtoService.listarProdutos();
    }

    @GetMapping("/produto/{id}")
    public ResponseEntity<Object> buscarProduto(@PathVariable Long id){
        Produto produto = produtoService.buscarProduto(id);
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/produto/{id}/disponibilidade")
    public ResponseEntity<Object> verificarDisponibilidade(@PathVariable Long id){
        Produto produto = produtoService.buscarProduto(id);
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        boolean disponivel = estoqueService.estaDisponivel(produto);
        return ResponseEntity.ok("Produto " + produto.getNome() + " disponível: " + disponivel);
    }

    @GetMapping("/produto/{id}/{nome}")
    public String buscarProdutoComNome(@PathVariable int id, @PathVariable String nome){
        return "Produto: " + id + " - Nome: " + nome;
    }

    @GetMapping("/produtos/categoria/{categoria}")
    public String buscarPorCategoria(@PathVariable String categoria){
        return "Categoria pesquisada: " + categoria;
    }

    @GetMapping("/produtos/{id}/categoria/{categoria}")
    public String buscarProdutoPorIdECategoria(@PathVariable Long id, @PathVariable String categoria){
        return "Produto ID: " + id + " - Categoria: " + categoria;
    }
}