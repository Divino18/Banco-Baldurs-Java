DROP DATABASE IF EXISTS `BancoBaldurs`;

-- -----------------------------------------------------
-- Criação do Schema (Banco de Dados)
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `BancoBaldurs` DEFAULT CHARACTER SET utf8mb4 ;
USE `BancoBaldurs` ;

-- -----------------------------------------------------
-- Tabela `usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `cpf` VARCHAR(11) NOT NULL,
  `data_nascimento` DATE NOT NULL,
  `telefone` VARCHAR(15) NOT NULL,
  `tipo_usuario` ENUM('FUNCIONARIO', 'CLIENTE') NOT NULL,
  `senha_hash` VARCHAR(32) NOT NULL, -- Para MD5
  `otp_ativo` VARCHAR(6) NULL,
  `otp_expiracao` DATETIME NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC) VISIBLE);

-- -----------------------------------------------------
-- Tabela `funcionario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`funcionario` (
  `id_funcionario` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `codigo_funcionario` VARCHAR(20) NOT NULL,
  `cargo` ENUM('ESTAGIARIO', 'ATENDENTE', 'GERENTE') NOT NULL,
  `id_supervisor` INT NULL,
  PRIMARY KEY (`id_funcionario`),
  UNIQUE INDEX `codigo_funcionario_UNIQUE` (`codigo_funcionario` ASC) VISIBLE,
  INDEX `fk_funcionario_usuario_idx` (`id_usuario` ASC) VISIBLE,
  INDEX `fk_funcionario_supervisor_idx` (`id_supervisor` ASC) VISIBLE,
  CONSTRAINT `fk_funcionario_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `BancoBaldurs`.`usuario` (`id_usuario`),
  CONSTRAINT `fk_funcionario_supervisor`
    FOREIGN KEY (`id_supervisor`)
    REFERENCES `BancoBaldurs`.`funcionario` (`id_funcionario`));

-- -----------------------------------------------------
-- Tabela `cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`cliente` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `score_credito` DECIMAL(5,2) DEFAULT 0.00,
  PRIMARY KEY (`id_cliente`),
  INDEX `fk_cliente_usuario_idx` (`id_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_cliente_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `BancoBaldurs`.`usuario` (`id_usuario`));

-- -----------------------------------------------------
-- Tabela `endereco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`endereco` (
  `id_endereco` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `cep` VARCHAR(10) NOT NULL,
  `local` VARCHAR(100) NOT NULL,
  `numero_casa` INT NOT NULL,
  `bairro` VARCHAR(50) NOT NULL,
  `cidade` VARCHAR(50) NOT NULL,
  `estado` CHAR(2) NOT NULL,
  `complemento` VARCHAR(50) NULL,
  PRIMARY KEY (`id_endereco`),
  INDEX `fk_endereco_usuario_idx` (`id_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_endereco_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `BancoBaldurs`.`usuario` (`id_usuario`));

-- -----------------------------------------------------
-- Tabela `agencia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`agencia` (
  `id_agencia` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  `codigo_agencia` VARCHAR(10) NOT NULL,
  `endereco_id` INT NULL, 
  PRIMARY KEY (`id_agencia`),
  UNIQUE INDEX `codigo_agencia_UNIQUE` (`codigo_agencia` ASC) VISIBLE,
  INDEX `fk_agencia_endereco_idx` (`endereco_id` ASC) VISIBLE,
  CONSTRAINT `fk_agencia_endereco`
    FOREIGN KEY (`endereco_id`)
    REFERENCES `BancoBaldurs`.`endereco` (`id_endereco`));

-- -----------------------------------------------------
-- Tabela `conta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`conta` (
  `id_conta` INT NOT NULL AUTO_INCREMENT,
  `numero_conta` VARCHAR(20) NOT NULL,
  `id_agencia` INT NOT NULL,
  `saldo` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `tipo_conta` ENUM('POUPANCA', 'CORRENTE', 'INVESTIMENTO') NOT NULL,
  `id_cliente` INT NOT NULL,
  `data_abertura` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` ENUM('ATIVA', 'ENCERRADA', 'BLOQUEADA') NOT NULL DEFAULT 'ATIVA',
  PRIMARY KEY (`id_conta`),
  UNIQUE INDEX `numero_conta_UNIQUE` (`numero_conta` ASC) VISIBLE,
  INDEX `fk_conta_agencia_idx` (`id_agencia` ASC) VISIBLE,
  INDEX `fk_conta_cliente_idx` (`id_cliente` ASC) VISIBLE,
  CONSTRAINT `fk_conta_agencia`
    FOREIGN KEY (`id_agencia`)
    REFERENCES `BancoBaldurs`.`agencia` (`id_agencia`),
  CONSTRAINT `fk_conta_cliente`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `BancoBaldurs`.`cliente` (`id_cliente`));

-- -----------------------------------------------------
-- Tabela `conta_poupanca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`conta_poupanca` (
  `id_conta_poupanca` INT NOT NULL AUTO_INCREMENT,
  `id_conta` INT NOT NULL,
  `taxa_rendimento` DECIMAL(5,2) NOT NULL,
  `ultimo_rendimento` DATETIME NULL,
  PRIMARY KEY (`id_conta_poupanca`),
  UNIQUE INDEX `id_conta_UNIQUE` (`id_conta` ASC) VISIBLE,
  CONSTRAINT `fk_poupanca_conta`
    FOREIGN KEY (`id_conta`)
    REFERENCES `BancoBaldurs`.`conta` (`id_conta`));

-- -----------------------------------------------------
-- Tabela `conta_corrente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`conta_corrente` (
  `id_conta_corrente` INT NOT NULL AUTO_INCREMENT,
  `id_conta` INT NOT NULL,
  `limite` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `data_vencimento` DATE NOT NULL,
  `taxa_manutencao` DECIMAL(5,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id_conta_corrente`),
  UNIQUE INDEX `id_conta_UNIQUE` (`id_conta` ASC) VISIBLE,
  CONSTRAINT `fk_corrente_conta`
    FOREIGN KEY (`id_conta`)
    REFERENCES `BancoBaldurs`.`conta` (`id_conta`));

-- -----------------------------------------------------
-- Tabela `conta_investimento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`conta_investimento` (
  `id_conta_investimento` INT NOT NULL AUTO_INCREMENT,
  `id_conta` INT NOT NULL,
  `perfil_risco` ENUM('BAIXO', 'MEDIO', 'ALTO') NOT NULL,
  `valor_minimo` DECIMAL(15,2) NOT NULL,
  `taxa_rendimento_base` DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (`id_conta_investimento`),
  UNIQUE INDEX `id_conta_UNIQUE` (`id_conta` ASC) VISIBLE,
  CONSTRAINT `fk_investimento_conta`
    FOREIGN KEY (`id_conta`)
    REFERENCES `BancoBaldurs`.`conta` (`id_conta`));

-- -----------------------------------------------------
-- Tabela `transacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`transacao` (
  `id_transacao` INT NOT NULL AUTO_INCREMENT,
  `id_conta_origem` INT NULL,
  `id_conta_destino` INT NULL,
  `tipo_transacao` ENUM('DEPOSITO', 'SAQUE', 'TRANSFERENCIA', 'TAXA', 'RENDIMENTO') NOT NULL,
  `valor` DECIMAL(15,2) NOT NULL,
  `data_hora` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `descricao` VARCHAR(100) NULL,
  PRIMARY KEY (`id_transacao`),
  INDEX `fk_transacao_conta_origem_idx` (`id_conta_origem` ASC) VISIBLE,
  INDEX `fk_transacao_conta_destino_idx` (`id_conta_destino` ASC) VISIBLE,
  INDEX `idx_data_hora` (`data_hora` ASC) VISIBLE,
  CONSTRAINT `fk_transacao_conta_origem`
    FOREIGN KEY (`id_conta_origem`)
    REFERENCES `BancoBaldurs`.`conta` (`id_conta`),
  CONSTRAINT `fk_transacao_conta_destino`
    FOREIGN KEY (`id_conta_destino`)
    REFERENCES `BancoBaldurs`.`conta` (`id_conta`));

-- -----------------------------------------------------
-- Tabela `auditoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`auditoria` (
  `id_auditoria` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NULL,
  `acao` VARCHAR(50) NOT NULL,
  `data_hora` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `detalhes` TEXT NULL,
  PRIMARY KEY (`id_auditoria`),
  INDEX `fk_auditoria_usuario_idx` (`id_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_auditoria_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `BancoBaldurs`.`usuario` (`id_usuario`));

-- -----------------------------------------------------
-- Tabela `relatorio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BancoBaldurs`.`relatorio` (
  `id_relatorio` INT NOT NULL AUTO_INCREMENT,
  `id_funcionario` INT NOT NULL,
  `tipo_relatorio` VARCHAR(50) NOT NULL,
  `data_geracao` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `conteudo` TEXT NOT NULL,
  PRIMARY KEY (`id_relatorio`),
  INDEX `fk_relatorio_funcionario_idx` (`id_funcionario` ASC) VISIBLE,
  CONSTRAINT `fk_relatorio_funcionario`
    FOREIGN KEY (`id_funcionario`)
    REFERENCES `BancoBaldurs`.`funcionario` (`id_funcionario`));

-- -----------------------------------------------------
-- GATILHOS (TRIGGERS)
-- -----------------------------------------------------
DELIMITER $$

CREATE TRIGGER `atualizar_saldo_AFTER_INSERT_ON_transacao` AFTER INSERT ON `transacao` FOR EACH ROW
BEGIN
    IF NEW.tipo_transacao IN ('SAQUE', 'TAXA') THEN
        UPDATE conta SET saldo = saldo - NEW.valor WHERE id_conta = NEW.id_conta_origem;
    ELSEIF NEW.tipo_transacao = 'DEPOSITO' THEN
        UPDATE conta SET saldo = saldo + NEW.valor WHERE id_conta = NEW.id_conta_origem;
    ELSEIF NEW.tipo_transacao = 'TRANSFERENCIA' THEN
        UPDATE conta SET saldo = saldo - NEW.valor WHERE id_conta = NEW.id_conta_origem;
        UPDATE conta SET saldo = saldo + NEW.valor WHERE id_conta = NEW.id_conta_destino;
    END IF;
END$$

CREATE TRIGGER `limite_deposito_BEFORE_INSERT_ON_transacao` BEFORE INSERT ON `transacao` FOR EACH ROW
BEGIN
    DECLARE total_dia DECIMAL(15,2);
    IF NEW.tipo_transacao = 'DEPOSITO' THEN
        SELECT IFNULL(SUM(valor), 0) INTO total_dia
        FROM transacao
        WHERE id_conta_origem = NEW.id_conta_origem
          AND tipo_transacao = 'DEPOSITO'
          AND DATE(data_hora) = CURDATE();

        IF (total_dia + NEW.valor) > 10000 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Limite diário de depósito (R$ 10.000,00) excedido.';
        END IF;
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- PROCEDIMENTOS ARMAZENADOS (STORED PROCEDURES)
-- -----------------------------------------------------
DELIMITER $$

CREATE PROCEDURE `gerar_otp`(IN p_id_usuario INT)
BEGIN
    DECLARE novo_otp VARCHAR(6);
    SET novo_otp = LPAD(FLOOR(RAND() * 1000000), 6, '0');
    
    UPDATE usuario 
    SET otp_ativo = novo_otp, otp_expiracao = NOW() + INTERVAL 5 MINUTE
    WHERE id_usuario = p_id_usuario;
    
    SELECT novo_otp;
END$$

CREATE PROCEDURE `calcular_score_credito`(IN p_id_cliente INT)
BEGIN
    DECLARE total_trans DECIMAL(15,2);
    DECLARE media_trans DECIMAL(15,2);

    SELECT IFNULL(SUM(valor), 0), IFNULL(AVG(valor), 0) INTO total_trans, media_trans
    FROM transacao t
    JOIN conta c ON (t.id_conta_origem = c.id_conta OR t.id_conta_destino = c.id_conta)
    WHERE c.id_cliente = p_id_cliente AND t.tipo_transacao IN ('DEPOSITO', 'SAQUE', 'TRANSFERENCIA');

    UPDATE cliente 
    SET score_credito = LEAST(100.00, (IFNULL(total_trans,0) / 1000) + (IFNULL(media_trans,0) / 100))
    WHERE id_cliente = p_id_cliente;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- VISÕES (VIEWS)
-- -----------------------------------------------------
CREATE OR REPLACE VIEW `vw_resumo_contas` AS
SELECT 
    c.id_cliente,
    u.nome AS nome_cliente,
    u.cpf AS cpf_cliente,
    COUNT(co.id_conta) AS total_contas,
    SUM(co.saldo) AS saldo_total_consolidado
FROM
    cliente c
        JOIN
    usuario u ON c.id_usuario = u.id_usuario
        JOIN
    conta co ON c.id_cliente = co.id_cliente
GROUP BY c.id_cliente , u.nome, u.cpf;


CREATE OR REPLACE VIEW `vw_movimentacoes_recentes` AS
SELECT 
    t.id_transacao,
    (SELECT c.numero_conta FROM conta c WHERE c.id_conta = t.id_conta_origem) AS conta_origem,
    (SELECT c.numero_conta FROM conta c WHERE c.id_conta = t.id_conta_destino) AS conta_destino,
    (SELECT u.nome FROM usuario u JOIN cliente cl ON u.id_usuario = cl.id_usuario JOIN conta c ON cl.id_cliente = c.id_cliente WHERE c.id_conta = t.id_conta_origem OR c.id_conta = t.id_conta_destino LIMIT 1) AS nome_cliente_principal,
    t.tipo_transacao,
    t.valor,
    t.data_hora,
    t.descricao
FROM
    transacao t
WHERE
    t.data_hora >= NOW() - INTERVAL 90 DAY
ORDER BY t.data_hora DESC;

-- -----------------------------------------------------
-- DADOS DE TESTE INICIAIS
-- -----------------------------------------------------

INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash) 
VALUES ('Cliente Teste', '12345678900', '1990-01-01', '61999999999', 'CLIENTE', '81dc9bdb52d04dc20036dbd8313ed055');

INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash) 
VALUES ('Funcionario Admin', '11122233344', '1985-05-10', '61988888888', 'FUNCIONARIO', '21232f297a57a5a743894a0e4a801fc3');

INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash) 
VALUES ('Cliente Destino', '98765432100', '1992-03-15', '61977777777', 'CLIENTE', '0192023a7bbd73250516f069df18b500'); -- senha 'senha'

INSERT INTO cliente (id_usuario, score_credito) 
SELECT id_usuario, 50.00 FROM usuario WHERE cpf = '12345678900';
INSERT INTO cliente (id_usuario, score_credito) 
SELECT id_usuario, 60.00 FROM usuario WHERE cpf = '98765432100';

INSERT INTO funcionario (id_usuario, codigo_funcionario, cargo) 
SELECT id_usuario, 'FUNC-00001', 'GERENTE' FROM usuario WHERE cpf = '11122233344';

INSERT INTO agencia (nome, codigo_agencia) VALUES ('Agência Principal', '001');
INSERT INTO agencia (nome, codigo_agencia) VALUES ('Agência Digital', '002');

SET @idClienteTeste = (SELECT id_cliente FROM cliente WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE cpf = '12345678900'));
SET @idClienteDestino = (SELECT id_cliente FROM cliente WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE cpf = '98765432100'));
SET @idAgenciaPrincipal = (SELECT id_agencia FROM agencia WHERE codigo_agencia = '001');

INSERT INTO conta (numero_conta, id_agencia, saldo, tipo_conta, id_cliente, status)
VALUES ('10001-1', @idAgenciaPrincipal, 1000.00, 'CORRENTE', @idClienteTeste, 'ATIVA');
SET @idContaClienteTeste = LAST_INSERT_ID();
INSERT INTO conta_corrente (id_conta, limite, data_vencimento, taxa_manutencao)
VALUES (@idContaClienteTeste, 500.00, '2025-12-31', 15.00);

INSERT INTO conta (numero_conta, id_agencia, saldo, tipo_conta, id_cliente, status)
VALUES ('20002-2', @idAgenciaPrincipal, 500.00, 'POUPANCA', @idClienteDestino, 'ATIVA');
SET @idContaClienteDestino = LAST_INSERT_ID();
INSERT INTO conta_poupanca (id_conta, taxa_rendimento)
VALUES (@idContaClienteDestino, 0.05);

SELECT 'SCRIPT SQL COMPLETO EXECUTADO E DADOS DE TESTE INSERIDOS!' AS  status_final;