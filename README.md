# API de Produtos — Spring Boot

API REST desenvolvida em Java + Spring Boot para a disciplina de Desenvolvimento Back-End Java, aplicando os conceitos de Controller, Service, Repository, Path Variable e regras de negócio.

## O que a API faz

Simula o backend de uma loja de produtos. Permite consultar produtos cadastrados, verificar se estão disponíveis em estoque e buscar por categoria — sem uso de banco de dados (os dados ficam em memória, numa `List`).

## Estrutura do projeto

```
src/main/java/fsa/aula_24/demo
├── controller
│   └── ProdutoController.java
├── service
│   ├── ProdutoService.java      → regras de negócio de produto (ex: premium)
│   └── EstoqueService.java      → regras de negócio de estoque/disponibilidade
├── repository
│   └── ProdutoRepository.java   → armazena os produtos em memória
└── model
    └── Produto.java
```

### Responsabilidade de cada camada

- **Controller**: recebe as requisições HTTP, não contém regra de negócio, apenas encaminha para os Services.
- **ProdutoService**: busca produtos e aplica a regra de produto premium.
- **EstoqueService**: verifica se um produto está disponível (estoque > 0). Responsabilidade separada da de `ProdutoService`.
- **ProdutoRepository**: guarda e devolve os dados dos produtos, sem lógica de negócio.

## Regras de negócio

1. **Produto premium**: todo produto com preço maior que R$ 3.000,00 é marcado automaticamente como `premium` (campo `premium` no JSON).
2. **Disponibilidade**: um produto só está disponível se sua quantidade em estoque for maior que zero.

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/produtos` | Lista todos os produtos cadastrados |
| GET | `/produto/{id}` | Busca um produto pelo ID (404 se não existir) |
| GET | `/produto/{id}/disponibilidade` | Verifica se o produto tem estoque disponível |
| GET | `/produto/{id}/{nome}` | Retorna uma mensagem combinando ID e nome |
| GET | `/produtos/categoria/{categoria}` | Retorna a categoria pesquisada |
| GET | `/produtos/{id}/categoria/{categoria}` | Retorna ID e categoria combinados |
| GET | `/bem-vindo/{nome}` | Mensagem de boas-vindas personalizada |
| GET | `/sobre` | Informações sobre a API |

### Exemplos

```
GET /produto/1
→ 200 OK
{ "id": 1, "nome": "Notebook", "preco": 3500.0, "categoria": "Eletrônicos", "premium": true, "quantidadeEstoque": 5 }

GET /produto/2/disponibilidade
→ 200 OK
"Produto Mouse disponível: false"

GET /produto/99
→ 404 Not Found
"Produto não encontrado"
```

## Como executar

Pré-requisitos: Java instalado e Maven (ou usar o `mvnw` incluso no projeto).

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

## Tecnologias

- Java
- Spring Boot
- Spring Web (`@RestController`, `@Service`, `@Repository`)
