# Clinic Schedule API

Sistema backend para gerenciamento de clínica médica, desenvolvido com Java + Spring Boot, com foco em boas práticas de arquitetura, autenticação e regras reais de negócio.

## 🚀 Objetivo

Construir uma aplicação profissional para permitir:

* Cadastro e autenticação de usuários
* Cadastro de pacientes
* Cadastro e gestão de médicos
* Configuração de agendas médicas
* Agendamento de consultas
* Histórico de atendimentos
* Controle de acesso por perfil (ADMIN, DOCTOR, PATIENT)

---

## 🛠️ Tecnologias Utilizadas

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* Lombok
* H2 Database (desenvolvimento)
* PostgreSQL (produção/futuro)
* Maven
* JWT (em implementação)

---

## 📁 Estrutura do Projeto

Arquitetura modular por domínio:

```text
user/
patient/
doctor/
appointment/
schedule/
auth/
config/
exception/
```

Cada módulo contém suas próprias camadas:

```text
controller/
service/
repository/
dto/
model/
```

---

## ✅ Funcionalidades Implementadas

### Usuários

* Estrutura base de usuários
* Roles com ENUM:

    * ADMIN
    * DOCTOR
    * PATIENT
* Senha criptografada com BCrypt

### Pacientes

* Cadastro unificado de paciente
* Criação automática de User + Patient
* Validação de e-mail único
* Validação de CPF único
* Transação atômica com `@Transactional`

Endpoint:

```http
POST /patients/register
```

---

## 📌 Próximas Implementações

* Login com JWT
* Rotas protegidas por perfil
* Cadastro de médicos
* Agenda médica
* Slots disponíveis
* Agendamento de consultas
* Swagger/OpenAPI
* Docker + PostgreSQL
* Testes automatizados
* CI/CD

---

## ▶️ Como Executar

```bash
./mvnw spring-boot:run
```

Ou execute pela IDE.

Aplicação padrão:

```text
http://localhost:8080
```

---

## 👨‍💻 Autor

Vinicius Oliveira Souza

Projeto construído para estudo prático e portfólio profissional.
