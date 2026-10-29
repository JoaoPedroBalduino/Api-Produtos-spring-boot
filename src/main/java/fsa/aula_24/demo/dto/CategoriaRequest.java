package fsa.aula_24.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @Schema(example = "Eletrônicos")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Schema(example = "Aparelhos eletrônicos e acessórios")
        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String descricao,

        @Schema(description = "Preço máximo permitido para produtos da categoria (opcional)", example = "10000")
        @Positive(message = "O preço máximo deve ser maior que zero")
        Double precoMaximo
) {
}
