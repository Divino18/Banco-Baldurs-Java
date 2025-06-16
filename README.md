# Sistema de Gerenciamento Bancário "Banco Baldur's"

## 1. Objetivo do Projeto

Este projeto foi desenvolvido como parte do trabalho de Programação Laboratorial de Banco de Dados e tem como objetivo a criação de uma aplicação bancária simulada chamada "Banco Baldur's". O sistema gerencia contas bancárias (Poupança, Corrente e Investimento), clientes e funcionários, com foco em Programação Orientada a Objetos (POO), interface gráfica em Java Swing e persistência de dados em um banco de dados MySQL. O projeto visa demonstrar o domínio em design de banco de dados relacional, consultas SQL, gatilhos, procedimentos armazenados e a integração de uma aplicação Java com o banco. [cite: 10, 11, 12]

## 2. Tecnologias Utilizadas

* **Linguagem de Programação:** Java (JDK OpenJDK 24 implícito)
* **Interface Gráfica (GUI):** Java Swing
* **Banco de Dados:** MySQL 8.0
* **Conexão Java-BD:** JDBC (MySQL Connector/J)
* **IDE:** IntelliJ IDEA (implícito pelo contexto)

## 3. Funcionalidades Implementadas

O sistema é dividido em módulos para Cliente e Funcionário, cada um com suas respectivas funcionalidades:

### Módulo Cliente:
* **Autenticação Segura:** Login com CPF, senha (hash MD5) e sistema de OTP (One-Time Password) funcional no backend (procedure `gerar_otp` no MySQL) com solicitação e validação implementada na interface Java. [cite: 17, 42]
* **Consulta de Saldo:** Visualização do saldo atual da conta. [cite: 54]
* **Realizar Depósito:** Permite depósitos com validação de limite diário (R$10.000,00) via trigger no banco. [cite: 55, 113]
* **Realizar Saque:** Permite saques com validação de saldo disponível. [cite: 55, 114]
* **Realizar Transferência:** Transferência de valores entre contas, com validação de conta destino e saldo. [cite: 56, 115]
* **Consultar Extrato:** Exibição das últimas 50 transações da conta. [cite: 56, 116]
* **Consultar Limite da Conta:** Visualização do limite para contas correntes. [cite: 57, 117]
* **Alterar Própria Senha:** Permite ao cliente alterar sua senha de acesso, com validação de força da nova senha. [cite: 104]

### Módulo Funcionário (Gerente):
* **Autenticação Segura:** Login com CPF, senha (hash MD5) e sistema de OTP. [cite: 17, 42]
* **Abertura de Nova Conta para Cliente:** Cadastro completo de novo cliente e abertura de contas (Poupança, Corrente, Investimento) com geração automática de número de conta. [cite: 18, 45, 91] Validação de senha forte para o novo cliente.
* **Encerrar Conta de Cliente:** Permite o encerramento de contas ativas com saldo zero, com validação da senha do funcionário logado. [cite: 18, 47, 96]
* **Cadastrar Novo Funcionário:** Cadastro de novos funcionários no sistema, com geração de código de funcionário. [cite: 19, 51, 106] Validação de senha forte para o novo funcionário.
* **Alterar Dados do Cliente:** Permite a alteração de telefone e endereço de clientes cadastrados, com validação da senha do funcionário e registro de auditoria detalhado. [cite: 50, 104]
* **Alterar Dados da Conta Corrente:** Permite a alteração de limite e taxa de manutenção de contas correntes, com validação da senha do funcionário. [cite: 50, 102]
* **Consultas de Dados:**
   * Visualização do "Resumo de Contas por Cliente" (via `vw_resumo_contas`). [cite: 48, 101, 79]
   * Visualização das "Movimentações Recentes" (via `vw_movimentacoes_recentes`). [cite: 48, 101, 79]
* **Geração de Relatórios Exportáveis:** Geração dos relatórios acima com opção de exportação para formato CSV. [cite: 19, 53, 108]
* **Auditoria:** Registro de ações críticas como logins (sucesso e falha com motivos), abertura de conta, encerramento de conta, cadastro de funcionário e alterações de dados do cliente na tabela `auditoria`. [cite: 43, 46, 51, 76]

## 4. Configuração do Banco de Dados

1.  **Pré-requisitos:** Ter um servidor MySQL (versão 8.x recomendada) instalado e em execução. [cite: 62]
2.  **Criação do Banco:**
   * Abra o MySQL Workbench (ou outro cliente MySQL).
   * Copie o conteúdo completo do arquivo `script_banco_completo.sql` (o script SQL que te enviei por último, que inclui `DROP DATABASE IF EXISTS BancoBaldurs;`, `CREATE SCHEMA`, todas as tabelas, triggers, procedures, views e dados de teste).
   * Execute o script completo. Isso criará o banco de dados `BancoBaldurs`, todas as estruturas necessárias e os dados de teste iniciais.

## 5. Executando a Aplicação Java

1.  **IDE:** Importe o projeto para o IntelliJ IDEA (ou outra IDE Java de sua preferência).
2.  **Bibliotecas (Dependências):**
   * Certifique-se de que o **MySQL Connector/J** (driver JDBC para MySQL) está configurado no projeto. Se estiver usando Maven, ele deve estar no `pom.xml`. Caso contrário, adicione o JAR manualmente às bibliotecas do projeto.
   * Outras dependências (como `opencsv` se você tivesse implementado a exportação para CSV usando essa biblioteca, mas nossa `CSVExporter` é manual) devem ser listadas aqui. Para a versão atual, apenas o MySQL Connector/J é essencial.
3.  **Configuração da Conexão:**
   * Verifique e, se necessário, ajuste os dados de conexão com o banco no arquivo `dao/ConnectionFactory.java`:
       ```java
       private static final String URL = "jdbc:mysql://localhost:3306/BancoBaldurs";
       private static final String USER = "seu_usuario_mysql"; // Ex: root
       private static final String PASSWORD = "sua_senha_mysql";
       ```
4.  **Executar:**
   * Encontre a classe `view.MainApp.java`.
   * Execute o método `main` desta classe para iniciar a aplicação.

## 6. Credenciais de Teste (Padrão do Script SQL)

* **Cliente Teste:**
   * CPF: `12345678900`
   * Senha: `Banco$eguro25` (ou `1234` se você não alterou via sistema)
* **Funcionário Admin (Gerente):**
   * CPF: `11122233344`
   * Senha: `admin`
* **Cliente Destino (para transferências):**
   * CPF: `98765432100`
   * Senha: `senha`
   * Conta (Poupança): `20002-2`

Lembre-se de usar o botão "Gerar OTP" na tela de login.

---