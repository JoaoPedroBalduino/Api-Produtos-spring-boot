Desenvolver uma API REST utilizando **Spring Boot, JPA e PostgreSQL** para realizar o cadastro e gerenciamento de professores.

A atividade tem como objetivo praticar:

- Criação de tabelas no PostgreSQL;
    
- Mapeamento de entidades com `@Entity`;
    
- Criação de `Repository`;
    
- Implementação de `Service`;
    
- Criação de endpoints REST com `Controller`;
    
- Operações de CRUD;
    
- Consultas utilizando métodos derivados do Spring Data JPA;
    
- Testes dos endpoints;
    
- Documentação de uma API no GitHub.
    

---

## 1. Banco de dados

Crie no PostgreSQL uma tabela chamada `professor` com os seguintes campos:

|Campo|Tipo|Descrição|
|---|---|---|
|`id`|BIGSERIAL|Identificador do professor|
|`nome`|VARCHAR(100)|Nome completo|
|`email`|VARCHAR(150)|E-mail|
|`area`|VARCHAR(100)|Área de atuação|
|`telefone`|VARCHAR(20)|Telefone|

### Requisitos

A coluna `id` deve ser a chave primária da tabela.

Crie também alguns registros iniciais para possibilitar os testes da API.

---

# 2. Estrutura da aplicação

A aplicação deverá ser desenvolvida utilizando:

- Java;
    
- Spring Boot;
    
- Spring Web;
    
- Spring Data JPA;
    
- PostgreSQL.
    

Organize o projeto utilizando, no mínimo, as seguintes camadas:

```text
src/main/java
└── ... 
    ├── controller
    ├── service
    ├── repository
    └── model
```

A entidade `Professor` deverá ser mapeada para a tabela `professor`.

---

# 3. Endpoints obrigatórios

A API deverá possuir os seguintes endpoints.

## 3.1 Listar todos os professores

**GET**

```text
/professores
```

Deve retornar todos os professores cadastrados.

---

## 3.2 Filtrar professores por nome

**GET**

```text
/professores/nome/{nome}
```

Exemplo:

```text
GET /professores/nome/João
```

O endpoint deve retornar os professores cujo nome contenha o texto informado.

O filtro deve ser:

- parcial;
    
- case insensitive.
    

Por exemplo, uma busca por:

```text
joao
```

deve conseguir encontrar:

```text
João da Silva
João Pedro
MARIA JOANA
```

quando houver correspondência no nome.

---

## 3.3 Filtrar professores por área

**GET**

```text
/professores/area/{area}
```

Exemplo:

```text
GET /professores/area/Desenvolvimento
```

O endpoint deve retornar os professores pertencentes à área informada.

O filtro deve utilizar **case insensitive**.

---

## 3.4 Cadastrar professor

**POST**

```text
/professores
```

Exemplo de JSON:

```json
{
    "nome": "Maria Silva",
    "email": "maria@email.com",
    "area": "Desenvolvimento",
    "telefone": "86999999999"
}
```

O professor deverá ser persistido no banco de dados.

---

## 3.5 Editar professor

**PUT**

```text
/professores/{id}
```

Exemplo:

```text
PUT /professores/1
```

Exemplo de JSON:

```json
{
    "nome": "Maria Silva Santos",
    "email": "mariasantos@email.com",
    "area": "Engenharia de Software",
    "telefone": "86988888888"
}
```

O professor correspondente ao `id` informado deverá ser atualizado.

---

## 3.6 Excluir professor

**DELETE**

```text
/professores/{id}
```

Exemplo:

```text
DELETE /professores/1
```

O professor correspondente ao `id` informado deverá ser removido do banco de dados.

---

# 4. Repository

Utilize o **Spring Data JPA** para implementar as consultas.

Para os filtros de nome e área, utilize **métodos derivados do Repository**, aplicando recursos como:

- `Containing`;
    
- `IgnoreCase`.
    

Não é necessário utilizar `@Query` nesta atividade.

Exemplo conceitual:

```text
findByNomeContainingIgnoreCase(...)
findByAreaIgnoreCase(...)
```

---

# 5. Testes

Todos os endpoints deverão ser testados e deverão funcionar corretamente.

Você poderá utilizar ferramentas como:

- Postman;
    
- Insomnia;
    
- ou outra ferramenta para testes de API.

Para cada operação, realize pelo menos um teste com sucesso.

---

# 6. GitHub

O projeto deverá ser disponibilizado em um **repositório público no GitHub**.

O repositório deverá conter:

```text
/projeto
├── src/
├── pom.xml
├── README.md
└── ...
```

Não envie arquivos desnecessários, como:

```text
target/
.idea/
```

ou outros arquivos gerados automaticamente pela IDE.

---

# 7. README.md

O projeto deverá possuir um arquivo `README.md` contendo:

### 7.1 Identificação

- Nome do aluno;
    
- Nome da disciplina;
    
- Descrição breve do projeto.
    

### 7.2 Tecnologias utilizadas

Exemplo:

```text
Java
Spring Boot
Spring Data JPA
PostgreSQL
Maven
```

### 7.3 Como executar

Explique, de forma resumida, como configurar e executar o projeto.

### 7.4 Endpoints

Documente os endpoints desenvolvidos:

|Método|Endpoint|Descrição|
|---|---|---|
|GET|`/professores`|Lista todos|
|GET|`/professores/nome/{nome}`|Filtra por nome|
|GET|`/professores/area/{area}`|Filtra por área|
|POST|`/professores`|Cadastra professor|
|PUT|`/professores/{id}`|Edita professor|
|DELETE|`/professores/{id}`|Exclui professor|

---

# 8. Evidências de execução

O `README.md` deverá conter **prints comprovando a execução com sucesso de cada operação**.

Deverá existir pelo menos um print para cada caso:

### Caso 1 — Listar professores

Print da execução:

```text
GET /professores
```

### Caso 2 — Filtrar por nome

Print da execução:

```text
GET /professores/nome/{nome}
```

### Caso 3 — Filtrar por área

Print da execução:

```text
GET /professores/area/{area}
```

### Caso 4 — Cadastrar professor

Print mostrando:

```text
POST /professores
```

com o JSON enviado e a resposta da API.

### Caso 5 — Editar professor

Print mostrando:

```text
PUT /professores/{id}
```

com o resultado da alteração.

### Caso 6 — Excluir professor

Print mostrando:

```text
DELETE /professores/{id}
```

e o resultado da exclusão.

Sempre que possível, os prints devem mostrar claramente:

- método HTTP;
    
- URL;
    
- dados enviados, quando houver;
    
- status da resposta;
    
- resultado retornado pela API.
    

---

# 9. Entrega

A entrega deverá ser realizada na **Plataforma A**.

O aluno deverá enviar:

**Link do repositório GitHub contendo o projeto completo.**

Antes de realizar a entrega, certifique-se de que:

-  O projeto está no GitHub;
    
-  O repositório está acessível;
    
-  O projeto possui `README.md`;
    
-  O README possui as instruções de execução;
    
-  O README possui a documentação dos endpoints;
    
-  O README possui os prints dos testes;
    
-  O CRUD está funcionando;
    
-  O filtro por nome está funcionando;
    
-  O filtro por área está funcionando;
    
-  O projeto está conectado ao PostgreSQL.
    

---

## Resultado esperado

Ao final da atividade, o aluno deverá possuir uma **API REST completa para gerenciamento de professores**, permitindo cadastrar, consultar, filtrar, editar e excluir professores utilizando Spring Boot, JPA e PostgreSQL.