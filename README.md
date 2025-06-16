# Sistema de Gerenciamento Bancário "Banco Baldur's"

## 1. Objetivo do Projeto

Este projeto foi desenvolvido como parte do trabalho de Laboratório de Banco de Dados. O objetivo foi criar uma aplicação bancária simulada, o "Banco Baldur's", construída em Java com interface gráfica Swing e utilizando um banco de dados MySQL para a persistência dos dados. O foco foi a aplicação de conceitos de Programação Orientada a Objetos (POO), a criação de uma estrutura de banco de dados relacional complexa e a implementação de operações avançadas como gatilhos, procedimentos armazenados e visões.

## 2. Arquitetura e Tecnologias

* **Linguagem de Programação:** Java (JDK OpenJDK 24)
* **Interface Gráfica (GUI):** Java Swing
* **Banco de Dados:** MySQL 8.0
* **Conexão Java-BD:** JDBC (via MySQL Connector/J)
* **Padrão de Arquitetura:** Model-View-Controller (MVC)
    * **Model:** Representa as entidades do banco de dados (`Usuario`, `Conta`, etc.).
    * **View:** Camada de apresentação responsável pela interface gráfica do usuário (`LoginView`, `ClienteView`, etc.).
    * **Controller:** Orquestra a lógica de negócios, mediando a interação entre a View e o Model/DAO.
    * **DAO (Data Access Object):** Camada de acesso aos dados, responsável por toda a comunicação com o banco de dados MySQL.
    * **Util:** Classes utilitárias para tarefas como criptografia de senhas.

## 3. Diagramas do Sistema

#### 3.1. Diagrama de Caso de Uso

O diagrama ilustra as funcionalidades disponíveis para os atores **Cliente** e **Funcionário**.

![Captura de tela 2025-06-15 231141](https://github.com/user-attachments/assets/7ce482c9-9798-495f-b66c-948b526f156c)


* **Ator Cliente:** Pode se autenticar, realizar operações financeiras, consultar informações da conta e encerrar sua sessão.
* **Ator Funcionário:** Pode se autenticar, gerenciar contas de clientes, cadastrar novos funcionários, alterar dados e gerar relatórios.

#### 3.2. Diagrama de Classes

O diagrama de classes representa a estrutura das entidades do sistema e seus relacionamentos. A classe `Usuario` é a base para `Cliente` e `Funcionario`, e a classe `Conta` é a base para as especializações `ContaCorrente`, `ContaPoupanca` e `ContaInvestimento`.

![Imagem do WhatsApp de 2025-06-15 à(s) 17 33 27_efa6fc0d](https://github.com/user-attachments/assets/23e2110a-0288-48b2-a2dc-31721f76a8d6)


#### 3.3. Diagrama de Sequência (Exemplo: Transferência)

Este diagrama detalha o fluxo de interações para uma operação de transferência.

![Imagem do WhatsApp de 2025-06-04 à(s) 18 19 04_1d6f2084](https://github.com/user-attachments/assets/2524e407-c34f-41cb-afee-afb84317636d)


**Fluxo da Operação:** O Cliente insere os dados na Aplicação (App). A App envia a requisição ao Servidor (lógica do Controller/DAO), que executa as operações de débito e crédito no Banco de Dados. O Banco de Dados retorna a confirmação, o Servidor informa o status para a App, que exibe o resultado final ao Cliente.

## 4. Configuração do Ambiente

#### 4.1. Banco de Dados

1.  **Pré-requisitos:** É necessário ter um servidor MySQL em execução.
2.  **Criação do Banco:** Execute o script SQL completo fornecido com o projeto. Ele criará o schema `BancoBaldurs`, todas as tabelas, e populará o banco com os dados de teste iniciais.

#### 4.2. Aplicação Java

1.  **Pré-requisitos:** Projeto importado em uma IDE Java (como IntelliJ IDEA) e o driver JDBC (MySQL Connector/J) adicionado às dependências.
2.  **Configuração:** Ajuste as credenciais de acesso ao banco de dados (URL, usuário e senha) no arquivo `dao/ConnectionFactory.java`.
3.  **Execução:** A aplicação é iniciada executando o método `main` da classe `view/MainApp.java`.

## 5. Credenciais de Teste

* **Cliente:**
    * **CPF:** `12345678900`
    * **Senha:** `1234` (ou a senha que foi alterada durante os testes)
* **Funcionário (Gerente):**
    * **CPF:** `11122233344`
    * **Senha:** `admin`

**Observação:** O login requer a geração de um OTP na interface da aplicação.

## 6. Conclusão

O projeto "Banco Baldur's" foi concluído com sucesso, atendendo a todos os requisitos funcionais e não funcionais especificados no documento do trabalho. A aplicação demonstra uma arquitetura MVC funcional, com uma clara separação de responsabilidades, e uma forte integração com um banco de dados relacional robusto que utiliza recursos avançados como triggers, procedimentos armazenados e visões para garantir a integridade dos dados e a implementação de regras de negócio complexas. As funcionalidades implementadas fornecem uma base sólida e completa para um sistema de gerenciamento bancário, cumprindo os objetivos propostos.
