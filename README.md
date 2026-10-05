# API de Produtos e Professores — Spring Boot

API REST desenvolvida em Java + Spring Boot para a disciplina de Desenvolvimento Back-End Java, aplicando os conceitos de Controller, Service, Repository, JPA, PostgreSQL e CRUD.

- **Aluno:** João Pedro Balduino L.
- **Disciplina:** Desenvolvimento Back-End Java

## O que a API faz

- **Produtos**: simula o backend de uma loja, permitindo consultar produtos cadastrados, verificar disponibilidade em estoque e buscar por categoria.
- **Professores**: permite cadastrar, listar, filtrar (por nome e por área), editar e excluir professores, persistidos no PostgreSQL via Spring Data JPA.

## Estrutura do projeto

```
src/main/java/fsa/aula_24/demo
├── controller
│   ├── ProdutoController.java
│   └── ProfessorController.java
├── service
│   ├── ProdutoService.java      → regras de negócio de produto (ex: premium)
│   ├── EstoqueService.java      → regras de negócio de estoque/disponibilidade
│   └── ProfessorService.java    → CRUD e filtros de professor
├── repository
│   ├── ProdutoRepository.java
│   └── ProfessorRepository.java → consultas derivadas (nome/área)
└── model
    ├── Produto.java
    └── Professor.java
```

### Responsabilidade de cada camada

- **Controller**: recebe as requisições HTTP, não contém regra de negócio, apenas encaminha para os Services.
- **ProdutoService**: busca produtos e aplica a regra de produto premium.
- **EstoqueService**: verifica se um produto está disponível (estoque > 0). Responsabilidade separada da de `ProdutoService`.
- **ProfessorService**: aplica o CRUD de professores e os filtros por nome/área.
- **Repository**: guarda e devolve os dados via Spring Data JPA, sem lógica de negócio.

## Regras de negócio

1. **Produto premium**: todo produto com preço maior que R$ 3.000,00 é marcado automaticamente como `premium` (campo `premium` no JSON).
2. **Disponibilidade**: um produto só está disponível se sua quantidade em estoque for maior que zero.

## Endpoints — Produtos

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

## Endpoints — Professores

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/professores` | Lista todos os professores cadastrados |
| GET | `/professores/nome/{nome}` | Filtra professores cujo nome contenha o texto informado (parcial, case insensitive) |
| GET | `/professores/area/{area}` | Filtra professores pela área informada (case insensitive) |
| POST | `/professores` | Cadastra um novo professor |
| PUT | `/professores/{id}` | Edita o professor correspondente ao ID |
| DELETE | `/professores/{id}` | Exclui o professor correspondente ao ID |

### Exemplos

```
GET /professores/nome/joao
→ 200 OK
[{ "id": 1, "nome": "João da Silva", "email": "joao.silva@email.com", "area": "Desenvolvimento", "telefone": "86999990001" }]

POST /professores
Body: { "nome": "Maria Silva", "email": "maria@email.com", "area": "Desenvolvimento", "telefone": "86999999999" }
→ 201 Created
{ "id": 5, "nome": "Maria Silva", "email": "maria@email.com", "area": "Desenvolvimento", "telefone": "86999999999" }

PUT /professores/5
Body: { "nome": "Maria Silva Santos", "email": "mariasantos@email.com", "area": "Engenharia de Software", "telefone": "86988888888" }
→ 200 OK

DELETE /professores/5
→ 204 No Content

GET /professores/99
→ 404 Not Found (quando aplicável, ex.: PUT/DELETE em ID inexistente)
```

## Banco de dados

A tabela `professor` é criada automaticamente pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`) a partir da entidade `Professor`, com as colunas `id` (BIGSERIAL/identity, chave primária), `nome` (VARCHAR 100), `email` (VARCHAR 150), `area` (VARCHAR 100) e `telefone` (VARCHAR 20).

Registros iniciais para teste são inseridos automaticamente na subida da aplicação pelo script `src/main/resources/data.sql`.

Configure a conexão em `src/main/resources/application.properties` (`spring.datasource.url`, `username`, `password`) de acordo com o seu PostgreSQL local.

## Como executar

Pré-requisitos: Java instalado, Maven (ou usar o `mvnw` incluso no projeto) e um PostgreSQL rodando localmente com o banco configurado em `application.properties`.

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. Ao iniciar, o Hibernate cria/atualiza a tabela `professor` e os registros iniciais são inseridos via `data.sql`.

## Tecnologias

- Java
- Spring Boot
- Spring Web (`@RestController`, `@Service`, `@Repository`)
- Spring Data JPA
- PostgreSQL
- Maven
