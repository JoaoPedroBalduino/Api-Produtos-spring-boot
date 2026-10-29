package fsa.aula_24.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiLoja() {
        return new OpenAPI().info(new Info()
                .title("API Loja - Produtos e Categorias")
                .description("CRUD de produtos e categorias com filtros, regras de negócio e controle de estoque")
                .version("1.0")
                .contact(new Contact().name("João Pedro Balduino L.")));
    }
}
