package fsa.aula_24.demo.controller;

import fsa.aula_24.demo.dto.CategoriaRequest;
import fsa.aula_24.demo.dto.CategoriaResponse;
import fsa.aula_24.demo.dto.ProdutoResponse;
import fsa.aula_24.demo.exception.ErroResponse;
import fsa.aula_24.demo.service.CategoriaService;
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
@RequestMapping("/categorias")
@Tag(name = "Categorias", description = "Cadastro de categorias de produtos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Lista categorias", description = "Filtra opcionalmente por parte do nome")
    public List<CategoriaResponse> listar(
            @Parameter(description = "Parte do nome (sem diferenciar maiúsculas)") @RequestParam(required = false) String nome) {
        return categoriaService.listar(nome);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma categoria pelo id")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public CategoriaResponse buscar(@PathVariable Long id) {
        return categoriaService.buscar(id);
    }

    @GetMapping("/{id}/produtos")
    @Operation(summary = "Lista os produtos de uma categoria")
    @ApiResponse(responseCode = "200", description = "Produtos da categoria")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public List<ProdutoResponse> listarProdutos(@PathVariable Long id) {
        return categoriaService.listarProdutos(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra uma categoria")
    @ApiResponse(responseCode = "201", description = "Categoria criada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "409", description = "Já existe categoria com esse nome",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ResponseEntity<CategoriaResponse> cadastrar(@Valid @RequestBody CategoriaRequest dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.cadastrar(dados));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma categoria",
            description = "O novo preço máximo não pode ficar abaixo do preço de produtos já cadastrados")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "409", description = "Já existe categoria com esse nome",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "422", description = "Preço máximo menor que o de produtos existentes",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public CategoriaResponse atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest dados) {
        return categoriaService.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma categoria", description = "Só é permitido se não houver produtos vinculados")
    @ApiResponse(responseCode = "204", description = "Categoria excluída")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "409", description = "Categoria possui produtos vinculados",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
