INSERT INTO professor (nome, email, area, telefone)
SELECT 'João da Silva', 'joao.silva@email.com', 'Desenvolvimento', '86999990001'
WHERE NOT EXISTS (SELECT 1 FROM professor WHERE email = 'joao.silva@email.com');

INSERT INTO professor (nome, email, area, telefone)
SELECT 'Maria Joana', 'maria.joana@email.com', 'Banco de Dados', '86999990002'
WHERE NOT EXISTS (SELECT 1 FROM professor WHERE email = 'maria.joana@email.com');

INSERT INTO professor (nome, email, area, telefone)
SELECT 'Pedro Santos', 'pedro.santos@email.com', 'Engenharia de Software', '86999990003'
WHERE NOT EXISTS (SELECT 1 FROM professor WHERE email = 'pedro.santos@email.com');

INSERT INTO professor (nome, email, area, telefone)
SELECT 'Ana Beatriz', 'ana.beatriz@email.com', 'Redes de Computadores', '86999990004'
WHERE NOT EXISTS (SELECT 1 FROM professor WHERE email = 'ana.beatriz@email.com');
