package fsa.aula_24.demo;
import fsa.aula_24.demo.model.Produto;
import fsa.aula_24.demo.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner seedProdutos(ProdutoRepository produtoRepository) {
		return args -> {
			if (produtoRepository.count() == 0) {
				produtoRepository.save(new Produto("Notebook", 3500.0, "Eletrônicos", 5));
				produtoRepository.save(new Produto("Mouse", 80.0, "Eletrônicos", 0));
				produtoRepository.save(new Produto("Teclado", 150.0, "Eletrônicos", 10));
			}
		};
	}
}