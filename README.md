# Fila Já — Sistema Desktop

**Sistema de gerenciamento de filas de atendimento em Unidades Básicas de Saúde (UBS), desenvolvido em Java Swing, utilizando Programação Orientada a Objetos, arquitetura MVC e integração com MySQL.**

## Sobre o projeto

O **Fila Já** é um projeto acadêmico desenvolvido como parte da disciplina **Programação Orientada a Objetos**, do curso de **Engenharia de Software**, durante o 4º semestre.

A proposta do sistema é facilitar o gerenciamento de filas de atendimento em Unidades Básicas de Saúde (UBS), permitindo organizar pacientes, unidades de saúde, serviços e atendimentos por meio de uma aplicação desktop.

O desenvolvimento teve como foco a aplicação prática dos conceitos de Programação Orientada a Objetos, organização em camadas, arquitetura MVC e integração com banco de dados MySQL.

## Objetivos e conhecimentos desenvolvidos

Durante o desenvolvimento do Fila Já, foram aplicados os seguintes conceitos e tecnologias:

- **Programação Orientada a Objetos (POO):** utilização de classes, objetos, encapsulamento, construtores e organização de responsabilidades.
- **Arquitetura MVC:** separação da aplicação em Model, View e Controller, promovendo organização e manutenção do código.
- **DAO (Data Access Object):** implementação de classes responsáveis pelo acesso e manipulação dos dados.
- **Java Swing:** desenvolvimento de interfaces gráficas desktop, utilizando componentes como JFrame, JPanel, JTable, JTextField e JButton.
- **JDBC:** integração entre a aplicação Java e o banco de dados MySQL.
- **Operações CRUD:** implementação de operações de cadastro, consulta, atualização e exclusão de registros.
- **Consultas SQL:** utilização de comandos SQL para manipulação e recuperação de informações.
- **Relacionamentos e JOINs:** realização de consultas envolvendo diferentes tabelas do banco de dados.
- **Segurança:** implementação de autenticação de usuários e armazenamento de senhas com hash utilizando PBKDF2.
- **Tratamento de exceções:** utilização de mecanismos para tratar erros durante as operações da aplicação.
- **Maven:** gerenciamento de dependências e configuração do projeto.

O desenvolvimento permitiu consolidar conhecimentos sobre organização de aplicações Java, separação de responsabilidades, persistência de dados e construção de sistemas desktop integrados a bancos de dados relacionais.

## Funcionalidades

- Autenticação de usuários.
- Cadastro e gerenciamento de pacientes.
- Cadastro e gerenciamento de Unidades Básicas de Saúde (UBS).
- Cadastro e gerenciamento de serviços oferecidos pelas unidades.
- Cadastro e gerenciamento de usuários.
- Organização de pacientes em filas de atendimento.
- Geração automática de senhas de atendimento.
- Chamada do próximo paciente.
- Registro e finalização de atendimentos.
- Consulta ao histórico de movimentações da fila.
- Visualização do tempo de espera e da quantidade de pacientes aguardando.
- Geração de relatórios de atendimentos por período.

## Tecnologias utilizadas

- **Java 21** — linguagem de programação.
- **Java Swing** — desenvolvimento da interface gráfica.
- **MySQL 8** — gerenciamento do banco de dados.
- **JDBC** — comunicação entre Java e MySQL.
- **Maven** — gerenciamento de dependências e construção do projeto.
- **IntelliJ IDEA** — ambiente de desenvolvimento.
- **Git e GitHub** — versionamento e hospedagem do código-fonte.

## Arquitetura do projeto

A aplicação utiliza o padrão arquitetural MVC (Model-View-Controller), com a camada DAO responsável pela comunicação com o banco de dados.

- **Model:** representa as entidades e os dados utilizados pela aplicação.
- **View:** contém as interfaces gráficas desenvolvidas com Java Swing.
- **Controller:** realiza a intermediação entre as interfaces e as operações do sistema.
- **DAO:** centraliza as operações de acesso e persistência dos dados.

Essa organização contribui para a separação de responsabilidades e facilita a manutenção e evolução do sistema.

## Estrutura do repositório

```text
Fila-Ja-Desktop/
├── database/
│   ├── backup/
│   │   └── fila_ja_poo.sql
│   └── scripts/
│       ├── 01_criacao_tabelas.sql
│       ├── 02_dados_iniciais.sql
│       ├── 03_funcoes.sql
│       ├── 04_indices.sql
│       ├── 05_views.sql
│       ├── 06_triggers.sql
│       ├── 07_procedures.sql
│       └── 08_usuarios_permissoes.sql
│
├── fila-ja-desktop/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── br/com/filaja/
│   │               ├── controller/
│   │               ├── dao/
│   │               ├── model/
│   │               ├── view/
│   │               └── Main.java
│   ├── pom.xml
│   └── README.md
│
├── .gitignore
└── README.md
```

## Como executar o projeto

### Pré-requisitos

- Java JDK 21 ou superior compatível com a configuração do projeto.
- MySQL 8 instalado e configurado.
- Maven instalado.
- IDE de preferência, como IntelliJ IDEA.

### 1. Clonar o repositório

```bash
git clone https://github.com/phgarc1aa/Fila-Ja-Desktop.git
```

### 2. Configurar o banco de dados

O projeto disponibiliza os scripts SQL e um backup completo do banco de dados na pasta `database/`.

Para preparar o ambiente:

1. Abra o MySQL Workbench.
2. Importe o arquivo `database/backup/fila_ja_poo.sql`.
3. Verifique se o banco de dados `fila_ja_poo` foi criado corretamente.

### 3. Configurar as credenciais

A aplicação utiliza variáveis de ambiente para estabelecer a conexão com o MySQL.

Configure as seguintes variáveis:

```text
DB_USER=fila_poo
DB_PASSWORD=sua_senha
```

O usuário informado deve possuir as permissões necessárias para acessar o banco de dados.

### 4. Executar a aplicação

Entre na pasta do projeto desktop:

```bash
cd Fila-Ja-Desktop/fila-ja-desktop
```

Compile o projeto:

```bash
mvn compile
```

Execute a aplicação:

```bash
mvn exec:java "-Dexec.mainClass=br.com.filaja.Main"
```

Após a execução, a tela de autenticação será apresentada.

## Contexto acadêmico

**Curso:** Engenharia de Software  
**Semestre:** 4º semestre  
**Disciplina:** Programação Orientada a Objetos  
**Projeto:** Fila Já — Sistema Desktop

---

Projeto desenvolvido com fins acadêmicos, aplicando na prática os conceitos de Programação Orientada a Objetos, arquitetura MVC, desenvolvimento de interfaces gráficas com Java Swing e integração com banco de dados MySQL.
