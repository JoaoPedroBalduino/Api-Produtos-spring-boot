package fsa.aula_24.demo.controller;

import fsa.aula_24.demo.dto.MovimentacaoEstoqueRequest;
import fsa.aula_24.demo.dto.ProdutoRequest;
import fsa.aula_24.demo.dto.ProdutoResponse;
import fsa.aula_24.demo.exception.ErroResponse;
import fsa.aula_24.demo.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Cadastro de produtos e movimentação de estoque")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    @Operation(summary = "Lista produtos com filtros", description = "Todos os filtros são opcionais e combináveis")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    @ApiResponse(responseCode = "400", description = "Parâmetro com formato inválido",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "422", description = "precoMin maior que precoMax",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public List<ProdutoResponse> listar(
            @Parameter(description = "Parte do nome") @RequestParam(required = false) String nome,
            @Parameter(description = "Id da categoria") @RequestParam(required = false) Long categoriaId,
            @Parameter(description = "Preço mínimo") @RequestParam(required = false) Double precoMin,
            @Parameter(description = "Preço máximo") @RequestParam(required = false) Double precoMax,
            @Parameter(description = "true = com estoque, false = sem estoque") @RequestParam(required = false) Boolean disponivel,
            @Parameter(description = "Somente premium (preço > R$ 3.000)") @RequestParam(required = false) Boolean premium) {
        return produtoService.listar(nome, categoriaId, precoMin, precoMax, disponivel, premium);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um produto pelo id")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ProdutoResponse buscar(@PathVariable Long id) {
        return produtoService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra um produto",
            description = "O preço não pode exceder o preço máximo da categoria. Produtos acima de R$ 3.000 viram premium")
    @ApiResponse(responseCode = "201", description = "Produto criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "409", description = "Já existe produto com esse nome",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "422", description = "Preço acima do máximo da categoria",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @RequestBody ProdutoRequest dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrar(dados));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto")
    @ApiResponse(responseCode = "200", description = "Produto atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "404", description = "Produto ou categoria não encontrados",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "409", description = "Já existe produto com esse nome",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "422", description = "Preço acima do máximo da categoria",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest dados) {
        return produtoService.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um produto", description = "Só é permitido se o estoque estiver zerado")
    @ApiResponse(responseCode = "204", description = "Produto excluído")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "409", description = "Produto ainda tem estoque",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/estoque/entrada")
    @Operation(summary = "Registra entrada de unidades no estoque")
    @ApiResponse(responseCode = "200", description = "Estoque atualizado")
    @ApiResponse(responseCode = "400", description = "Quantidade inválida",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ProdutoResponse entradaEstoque(@PathVariable Long id, @Valid @RequestBody MovimentacaoEstoqueRequest dados) {
        return produtoService.registrarEntrada(id, dados.quantidade());
    }

    @PostMapping("/{id}/estoque/saida")
    @Operation(summary = "Registra saída (venda) de unidades do estoque")
    @ApiResponse(responseCode = "200", description = "Estoque atualizado")
    @ApiResponse(responseCode = "400", description = "Quantidade inválida",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "422", description = "Estoque insuficiente",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ProdutoResponse saidaEstoque(@PathVariable Long id, @Valid @RequestBody MovimentacaoEstoqueRequest dados) {
        return produtoService.registrarSaida(id, dados.quantidade());
    }
}
