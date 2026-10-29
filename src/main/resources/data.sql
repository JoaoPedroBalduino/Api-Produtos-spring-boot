-- Categorias iniciais
INSERT INTO categoria (nome, descricao, preco_maximo)
SELECT 'Eletrônicos', 'Aparelhos eletrônicos e acessórios', 15000
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = 'Eletrônicos');

INSERT INTO categoria (nome, descricao, preco_maximo)
SELECT 'Higiene', 'Produtos de higiene pessoal', 500
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = 'Higiene');

INSERT INTO categoria (nome, descricao, preco_maximo)
SELECT 'Livros', 'Livros e apostilas', NULL
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = 'Livros');

-- Produtos iniciais
INSERT INTO produto (nome, preco, premium, quantidade_estoque, categoria_id)
SELECT 'Notebook', 3500.0, true, 5, (SELECT id FROM categoria WHERE nome = 'Eletrônicos')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Notebook');

INSERT INTO produto (nome, preco, premium, quantidade_estoque, categoria_id)
SELECT 'Mouse', 80.0, false, 0, (SELECT id FROM categoria WHERE nome = 'Eletrônicos')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Mouse');

INSERT INTO produto (nome, preco, premium, quantidade_estoque, categoria_id)
SELECT 'Teclado', 150.0, false, 10, (SELECT id FROM categoria WHERE nome = 'Eletrônicos')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Teclado');

INSERT INTO produto (nome, preco, premium, quantidade_estoque, categoria_id)
SELECT 'Shampoo', 25.9, false, 30, (SELECT id FROM categoria WHERE nome = 'Higiene')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Shampoo');

INSERT INTO produto (nome, preco, premium, quantidade_estoque, categoria_id)
SELECT 'Clean Code', 120.0, false, 3, (SELECT id FROM categoria WHERE nome = 'Livros')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome = 'Clean Code');

-- Produtos antigos (de antes do relacionamento) ficam em Eletrônicos
UPDATE produto SET categoria_id = (SELECT id FROM categoria WHERE nome = 'Eletrônicos')
WHERE categoria_id IS NULL;

-- Recalcula o premium de registros antigos (preço > 3000)
UPDATE produto SET premium = (preco > 3000) WHERE premium <> (preco > 3000);
