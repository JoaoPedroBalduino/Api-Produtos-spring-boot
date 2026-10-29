# Roteiro do vídeo de apresentação (até 10 min)

> Antes de gravar: suba o Postgres no Docker, rode `./mvnw spring-boot:run` e abra **http://localhost:8080/swagger-ui.html**.
> Deixe abertos o VS Code/IntelliJ com o projeto e o navegador com o Swagger.

| Parte | Tempo | Assunto |
|---|---|---|
| 1 | 0:00 – 0:45 | Apresentação |
| 2 | 0:45 – 2:30 | Estrutura do código |
| 3 | 2:30 – 3:00 | Swagger |
| 4 | 3:00 – 5:00 | CRUD |
| 5 | 5:00 – 6:15 | Filtros |
| 6 | 6:15 – 8:45 | Regras de negócio e erros |
| 7 | 8:45 – 9:30 | Encerramento |

---

## 1. Apresentação (0:00 – 0:45)

**Fala:**
> "Olá, eu sou o João Pedro Balduino. Este é o projeto de API REST em Spring Boot da disciplina de Back-End Java. É a API de uma loja com duas tabelas relacionadas, **Categoria** e **Produto**. Ela tem CRUD completo, filtros, regras de negócio, tratamento de erros com os status HTTP corretos e documentação com Swagger."

---

## 2. Estrutura do código (0:45 – 2:30)

Mostre rapidamente no editor:

1. **`model/Categoria.java` e `model/Produto.java`**: mostre o `@OneToMany` na Categoria e o `@ManyToOne` no Produto.
   > "Uma categoria tem vários produtos, e cada produto pertence a uma categoria. É uma relação 1 para N, feita pela coluna `categoria_id`."
2. **Pastas** `controller → service → repository`:
   > "O controller só recebe a requisição. As regras ficam nos services, e o repository conversa com o banco."
3. **`dto/ProdutoRequest.java`**: mostre as anotações `@NotBlank`, `@Positive` e `@PositiveOrZero`.
   > "A entrada é validada com Bean Validation. Se vier um dado inválido, a API responde 400."
4. **`exception/GlobalExceptionHandler.java`**:
   > "Todas as exceções passam por aqui e voltam num formato JSON padrão. Uso 404 para registro que não existe, 409 para conflito e 422 para regra de negócio violada."

---

## 3. Swagger (2:30 – 3:00)

Mostre a tela do Swagger com os dois grupos, **Categorias** e **Produtos**.
> "A documentação é gerada pelo springdoc. Cada endpoint mostra o que recebe e os status que pode devolver."

Abra um endpoint e mostre a lista de *Responses* (201, 400, 409...).

---

## 4. CRUD (3:00 – 5:00)

Em cada consulta: clique em **Try it out**, preencha os dados e clique em **Execute**.

**4.1 Listar categorias:** `GET /categorias` → **200**
> "Já tenho Eletrônicos, Higiene e Livros cadastradas. Cada uma mostra o preço máximo e o total de produtos."

**4.2 Criar categoria:** `POST /categorias` → **201**
```json
{
  "nome": "Games",
  "descricao": "Consoles e jogos",
  "precoMaximo": 6000
}
```
📌 Anote o `id` que voltar, por exemplo **4**.

**4.3 Criar produto nessa categoria:** `POST /produtos` → **201**
```json
{
  "nome": "PlayStation 5",
  "preco": 4500,
  "quantidadeEstoque": 3,
  "categoriaId": 4
}
```
> "Repare que o produto voltou com `premium: true`. Isso é calculado automaticamente porque o preço passa de R$ 3.000."

📌 Anote o `id` do produto.

**4.4 Atualizar:** `PUT /produtos/{id}` → **200**
```json
{
  "nome": "PlayStation 5 Slim",
  "preco": 2900,
  "quantidadeEstoque": 3,
  "categoriaId": 4
}
```
> "Baixei o preço para menos de 3 mil, e ele deixou de ser premium."

**4.5 Buscar pelo id:** `GET /produtos/{id}` → **200**

---

## 5. Filtros (5:00 – 6:15)

`GET /produtos` com parâmetros (todos opcionais e combináveis):

| Parâmetros | O que mostra |
|---|---|
| `nome = note` | Busca parcial, sem diferenciar maiúsculas → Notebook |
| `categoriaId = 1` e `disponivel = true` | Eletrônicos com estoque (o Mouse, com estoque 0, não aparece) |
| `precoMin = 100` e `precoMax = 1000` | Faixa de preço |
| `premium = true` | Só os produtos premium |

Também dá para mostrar `GET /categorias/{id}/produtos` com `id = 1`.
> "Esse lista os produtos de uma categoria usando o relacionamento."

---

## 6. Regras de negócio e erros (6:15 – 8:45)

Essa é a parte mais importante: mostre o **status** e a **mensagem** de cada resposta.

**6.1 Validação → 400:** `POST /produtos`
```json
{
  "nome": "",
  "preco": -10,
  "quantidadeEstoque": -1
}
```
> "A resposta lista o erro de cada campo."

**6.2 Registro inexistente → 404:** `GET /produtos/999`

**6.3 Nome duplicado → 409:** `POST /categorias`
```json
{ "nome": "eletrônicos" }
```
> "Mesmo escrito em minúsculo, o sistema detecta que essa categoria já existe."

**6.4 Preço acima do teto da categoria → 422:** `POST /produtos`
```json
{
  "nome": "PC Gamer",
  "preco": 8000,
  "quantidadeEstoque": 1,
  "categoriaId": 4
}
```
> "A categoria Games aceita produtos de no máximo R$ 6.000."

**6.5 Estoque insuficiente → 422:** `POST /produtos/{id do PS5}/estoque/saida`
```json
{ "quantidade": 10 }
```
> "Tenho só 3 unidades, então não consigo vender 10."

**6.6 Excluir produto com estoque → 409:** `DELETE /produtos/{id do PS5}`
> "Para excluir, o estoque precisa estar zerado."

Agora zere o estoque com `POST /produtos/{id}/estoque/saida`:
```json
{ "quantidade": 3 }
```
Depois rode `DELETE /produtos/{id}` de novo → **204**.

**6.7 Excluir categoria com produtos → 409:** `DELETE /categorias/1`
> "Eletrônicos tem produtos vinculados, então a exclusão é bloqueada."

Feche com `DELETE /categorias/4` → **204**, porque a Games ficou vazia.

---

## 7. Encerramento (8:45 – 9:30)

> "Então, resumindo: duas tabelas relacionadas com CRUD completo, filtros combináveis, regras de negócio como teto de preço por categoria, controle de estoque e proteção na exclusão, erros padronizados com os status HTTP corretos e tudo documentado no Swagger. Obrigado!"

---

### Dicas

- Treine uma vez antes de gravar, porque os `id`s mudam a cada cadastro.
- Se gravar mais de uma vez, apague antes a categoria "Games" e o "PlayStation 5" que ficaram da tentativa anterior. Se não apagar, o cadastro vai dar 409 por nome duplicado.
- Aumente o zoom do navegador (Ctrl +) para o JSON ficar legível no vídeo.
