# API Loja — Produtos e Categorias (Spring Boot)

API REST desenvolvida em Java + Spring Boot para a disciplina de Desenvolvimento Back-End Java, com CRUD de duas tabelas relacionadas, filtros, regras de negócio, tratamento de exceções e documentação Swagger.

- **Aluno:** João Pedro Balduino L.
- **Disciplina:** Desenvolvimento Back-End Java

## 🎥 Vídeo de apresentação

[![Vídeo de apresentação do projeto](https://img.youtube.com/vi/IQnRKRJfN1o/maxresdefault.jpg)](https://www.youtube.com/watch?v=IQnRKRJfN1o)

No vídeo eu apresento a estrutura do projeto, o relacionamento entre Categoria e Produto, o CRUD, os filtros, as regras de negócio e o tratamento de erros, com testes feitos pelo Swagger.

▶️ [Assistir no YouTube](https://www.youtube.com/watch?v=IQnRKRJfN1o)

## Modelo de dados

```
categoria (1) ────< (N) produto
```

| Tabela | Campos |
|---|---|
| `categoria` | `id`, `nome` (único), `descricao`, `preco_maximo` (opcional) |
| `produto` | `id`, `nome`, `preco`, `premium`, `quantidade_estoque`, `categoria_id` (FK → categoria) |

As tabelas são criadas pelo Hibernate (`ddl-auto=update`) e os registros iniciais são inseridos pelo `src/main/resources/data.sql`.

## Estrutura do projeto

```
src/main/java/fsa/aula_24/demo
├── config        → OpenApiConfig (informações do Swagger)
├── controller    → CategoriaController, ProdutoController
├── dto           → objetos de entrada (com validação) e de saída
├── exception     → exceções da aplicação e GlobalExceptionHandler
├── model         → entidades Categoria e Produto
├── repository    → repositórios JPA e ProdutoSpecifications (filtros)
└── service       → CategoriaService, ProdutoService, EstoqueService
```

## Regras de negócio

1. **Nome único**: não pode haver duas categorias ou dois produtos com o mesmo nome (sem diferenciar maiúsculas) → `409`.
2. **Preço máximo da categoria**: o preço de um produto não pode passar do `precoMaximo` da categoria dele → `422`.
3. **Redução do teto**: ao alterar o `precoMaximo` de uma categoria, ele não pode ficar abaixo do preço de produtos já cadastrados nela → `422`.
4. **Exclusão de categoria**: uma categoria com produtos vinculados não pode ser excluída → `409`.
5. **Exclusão de produto**: só é permitida com o estoque zerado → `409`.
6. **Saída de estoque**: não é possível retirar mais unidades do que há em estoque → `422`.
7. **Produto premium**: produtos com preço acima de R$ 3.000,00 são marcados como `premium` automaticamente.
8. **Disponibilidade**: o produto está `disponivel` quando o estoque é maior que zero.
9. **Validação de entrada**: nome obrigatório, preço > 0, estoque ≥ 0, quantidade de movimentação > 0 → `400`.

## Endpoints — Categorias

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/categorias?nome=` | Lista categorias (filtro opcional por nome) | 200 |
| GET | `/categorias/{id}` | Busca categoria | 200, 404 |
| GET | `/categorias/{id}/produtos` | Produtos da categoria | 200, 404 |
| POST | `/categorias` | Cadastra | 201, 400, 409 |
| PUT | `/categorias/{id}` | Atualiza | 200, 400, 404, 409, 422 |
| DELETE | `/categorias/{id}` | Exclui | 204, 404, 409 |

## Endpoints — Produtos

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/produtos` | Lista com filtros combináveis (abaixo) | 200, 400, 422 |
| GET | `/produtos/{id}` | Busca produto | 200, 404 |
| POST | `/produtos` | Cadastra | 201, 400, 404, 409, 422 |
| PUT | `/produtos/{id}` | Atualiza | 200, 400, 404, 409, 422 |
| DELETE | `/produtos/{id}` | Exclui | 204, 404, 409 |
| POST | `/produtos/{id}/estoque/entrada` | Entrada de estoque | 200, 400, 404 |
| POST | `/produtos/{id}/estoque/saida` | Saída (venda) de estoque | 200, 400, 404, 422 |

Filtros de `GET /produtos` (todos opcionais): `nome`, `categoriaId`, `precoMin`, `precoMax`, `disponivel`, `premium`.

```
GET /produtos?categoriaId=1&precoMin=100&disponivel=true
```

### Exemplos

```
POST /produtos
{ "nome": "Monitor", "preco": 4000, "quantidadeEstoque": 2, "categoriaId": 1 }
→ 201 Created
{ "id": 6, "nome": "Monitor", "preco": 4000.0, "premium": true, "quantidadeEstoque": 2,
  "disponivel": true, "categoriaId": 1, "categoriaNome": "Eletrônicos" }

POST /produtos/6/estoque/saida
{ "quantidade": 5 }
→ 422 Unprocessable Content
{ "timestamp": "...", "status": 422, "erro": "Unprocessable Content",
  "mensagem": "Estoque insuficiente para 'Monitor': solicitado 5, disponível 2",
  "caminho": "/produtos/6/estoque/saida" }
```

## Tratamento de erros

Todos os erros são tratados no `GlobalExceptionHandler` e retornam o mesmo formato JSON (`timestamp`, `status`, `erro`, `mensagem`, `caminho` e, nos erros de validação, `campos`).

| Status | Quando |
|---|---|
| 400 | Dados inválidos, JSON mal formado ou parâmetro com tipo errado |
| 404 | Registro ou rota inexistente |
| 405 | Método HTTP não suportado |
| 409 | Nome duplicado ou exclusão bloqueada por vínculo/estoque |
| 422 | Violação de regra de negócio |
| 500 | Erro inesperado |

## Documentação (Swagger)

Com a aplicação rodando:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Como executar

Pré-requisitos: Java 17 e um PostgreSQL com o banco `api_loja` (credenciais em `application.properties`).

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

## Tecnologias

- Java 17
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- PostgreSQL
- springdoc-openapi (Swagger)
- Maven
