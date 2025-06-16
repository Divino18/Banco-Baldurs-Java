# Sistema de Gerenciamento Bancário "Banco Baldur's"

## 1. Objetivo do Projeto

Este projeto foi desenvolvido como parte do trabalho de Laboratório de Banco de Dados. [cite_start]O objetivo foi criar uma aplicação bancária simulada, o "Banco Baldur's", construída em Java com interface gráfica Swing e utilizando um banco de dados MySQL para a persistência dos dados. [cite_start]O foco foi a aplicação de conceitos de Programação Orientada a Objetos (POO), a criação de uma estrutura de banco de dados relacional complexa e a implementação de operações avançadas como gatilhos, procedimentos armazenados e visões.

## 2. Arquitetura e Tecnologias

* [cite_start]**Linguagem de Programação:** Java (JDK OpenJDK 24) 
* [cite_start]**Interface Gráfica (GUI):** Java Swing 
* [cite_start]**Banco de Dados:** MySQL 8.0 
* [cite_start]**Conexão Java-BD:** JDBC (via MySQL Connector/J) 
* [cite_start]**Padrão de Arquitetura:** Model-View-Controller (MVC) 
    * [cite_start]**Model:** Representa as entidades do banco de dados (`Usuario`, `Conta`, etc.). 
    * [cite_start]**View:** Camada de apresentação responsável pela interface gráfica do usuário (`LoginView`, `ClienteView`, etc.). 
    * [cite_start]**Controller:** Orquestra a lógica de negócios, mediando a interação entre a View e o Model/DAO. 
    * [cite_start]**DAO (Data Access Object):** Camada de acesso aos dados, responsável por toda a comunicação com o banco de dados MySQL. 
    * [cite_start]**Util:** Classes utilitárias para tarefas como criptografia de senhas. 

## 3. Diagramas do Sistema

#### 3.1. Diagrama de Caso de Uso

O diagrama ilustra as funcionalidades disponíveis para os atores **Cliente** e **Funcionário**.

![Captura de tela 2025-06-15 231141](https://github.com/user-attachments/assets/a3d5995e-38a9-4267-bfc3-a5c2af2407fe)



* [cite_start]**Ator Cliente:** Pode se autenticar, realizar operações financeiras, consultar informações da conta e encerrar sua sessão.
* [cite_start]**Ator Funcionário:** Pode se autenticar, gerenciar contas de clientes, cadastrar novos funcionários, alterar dados e gerar relatórios.

#### 3.2. Diagrama de Classes

O diagrama de classes representa a estrutura das entidades do sistema e seus relacionamentos. [cite_start]A classe `Usuario` é a base para `Cliente` e `Funcionario`, e a classe `Conta` é a base para as especializações `ContaCorrente`, `ContaPoupanca` e `ContaInvestimento`.

![Imagem do WhatsApp de 2025-06-15 à(s) 17 33 27_efa6fc0d](https://github.com/user-attachments/assets/a0116ced-bf10-4aab-b36f-c2f28dfb6325)


#### 3.3. Diagrama de Sequência (Exemplo: Transferência)

Este diagrama detalha o fluxo de interações para uma operação de transferência.

![Imagem do WhatsApp de 2025-06-04 à(s) 18 19 04_1d6f2084](https://github.com/user-attachments/assets/282c63d4-c189-4b23-b840-e83938540ee3)


**Fluxo da Operação:** O Cliente insere os dados na Aplicação (App). A App envia a requisição ao Servidor (lógica do Controller/DAO), que executa as operações de débito e crédito no Banco de Dados. [cite_start]O Banco de Dados retorna a confirmação, o Servidor informa o status para a App, que exibe o resultado final ao Cliente.

## 4. Configuração do Ambiente

#### 4.1. Banco de Dados

1.  **Pré-requisitos:** É necessário ter um servidor MySQL em execução.
2.  **Criação do Banco:** Execute o script SQL completo fornecido com o projeto. [cite_start]Ele criará o schema `BancoBaldurs`, todas as tabelas, e populará o banco com os dados de teste iniciais.

#### 4.2. Aplicação Java

1.  [cite_start]**Pré-requisitos:** Projeto importado em uma IDE Java (como IntelliJ IDEA) e o driver JDBC (MySQL Connector/J) adicionado às dependências.
2.  [cite_start]**Configuração:** Ajuste as credenciais de acesso ao banco de dados (URL, usuário e senha) no arquivo `dao/ConnectionFactory.java`.
3.  [cite_start]**Execução:** A aplicação é iniciada executando o método `main` da classe `view.MainApp.java`.

## 5. Credenciais de Teste

* **Cliente:**
    * **CPF:** `12345678900`
    * [cite_start]**Senha:** `1234` (ou a senha que foi alterada durante os testes) 
* **Funcionário (Gerente):**
    * **CPF:** `11122233344`
    * [cite_start]**Senha:** `admin` 

**Observação:** O login requer a geração de um OTP na interface da aplicação.

## 6. Conclusão

[cite_start]O projeto "Banco Baldur's" foi concluído com sucesso, atendendo a todos os requisitos funcionais e não funcionais especificados no documento do trabalho. [cite_start]A aplicação demonstra uma arquitetura MVC funcional, com uma clara separação de responsabilidades, e uma forte integração com um banco de dados relacional robusto que utiliza recursos avançados como triggers, procedimentos armazenados e visões para garantir a integridade dos dados e a implementação de regras de negócio complexas. [cite_start]As funcionalidades implementadas fornecem uma base sólida e completa para um sistema de gerenciamento bancário, cumprindo os objetivos propostos.
