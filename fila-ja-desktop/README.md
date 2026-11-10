# Fila Já — Sistema de Gerenciamento de Filas

Sistema desktop desenvolvido em Java para gerenciamento de filas e atendimentos em Unidades Básicas de Saúde (UBS).

## Sobre o projeto

O **Fila Já** tem como objetivo organizar o fluxo de atendimento em unidades de saúde, permitindo o gerenciamento de pacientes, unidades, serviços, usuários, filas e atendimentos.

O projeto foi desenvolvido como atividade prática acadêmica, aplicando conceitos de Programação Orientada a Objetos (POO), arquitetura MVC, JDBC e integração com banco de dados MySQL.

## Funcionalidades

- **Autenticação:** login de usuários com validação de credenciais e senhas protegidas por hash.
- **Pacientes:** cadastro, consulta, atualização e exclusão.
- **Unidades de Saúde:** gerenciamento das UBS cadastradas.
- **Serviços:** gerenciamento dos serviços oferecidos pelas unidades.
- **Usuários:** cadastro e gerenciamento de usuários e grupos.
- **Gerenciamento de filas:** cadastro de pacientes, geração de senhas, chamada do próximo paciente, início, finalização e cancelamento de atendimentos.
- **Relatórios:** consulta de atendimentos por período, com informações de pacientes, unidades, serviços e profissionais.

## Tecnologias utilizadas

- Java 21
- Java Swing
- MySQL 8.0
- JDBC
- MySQL Connector/J 9.5.0
- Apache Maven

## Arquitetura

O sistema utiliza o padrão MVC (Model-View-Controller), com separação de responsabilidades:

- **Model:** representação das entidades do sistema.
- **View:** interfaces gráficas desenvolvidas com Java Swing.
- **Controller:** coordenação das ações e regras da aplicação.
- **DAO:** acesso e manipulação dos dados no banco de dados.

## Estrutura do repositório

```text
FilaJa-POO/
├── database/
│   ├── backup/
│   │   └── fila_ja_poo.sql
│   └── scripts/
├── fila-ja-desktop/
│   ├── src/
│   ├── pom.xml
│   ├── README.md
│   └── .gitignore
├── .gitignore
└── README.md
```

## Requisitos

- JDK 21 ou superior.
- Apache Maven.
- MySQL Server 8.0 ou superior.

## Configuração do banco de dados

O banco utilizado pela aplicação é `fila_ja_poo`.

O