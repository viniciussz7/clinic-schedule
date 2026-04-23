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
config/security
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
### 🔐 Autenticação

* Login por e-mail e senha
* Validação de credenciais
* Geração de token JWT
* Token com:
  * userId
  * email
  * role
  * expiração
* Filtro JWT global
* Rotas protegidas com Spring Security

Endpoints:

```http
POST /auth/login
GET /auth/me
```

---

### 👤 Endpoint /auth/me

Retorna dados do usuário autenticado através do token JWT.

Exemplo:

```json
{
"id": "...",
"name": "Vinicius Souza",
"email": "vinicius@email.com",
"role": "PATIENT",
"active": true
}
```

---

### ADMIN

Ao subir a aplicação, um administrador padrão é criado automaticamente (se não existir).
```text
email: admin@clinic.com
password: admin123
role: ADMIN
```

---

### Médicos

* Cadastro de médicos realizado por ADMIN.
* Validação de CRM único
* Associação de médicos a especialidades

Endpoints:

```http
POST /admin/doctors
GET /doctors
GET /doctor/dashboard
```
---

### Espacialidades

* Cadastro de especialidades médicas realizado por ADMIN.
* Validação de nome único

Endpoints:

```http
POST /admin/specialties
GET /specialties
```

---

### 👥 Controle de Acesso por Perfil (RBAC)

* ADMIN: Acesso total a todos os endpoints
* DOCTOR: Acesso a endpoints relacionados a médicos (exceto criação, feito exclusivamente pelo admin) e consultas
* PATIENT: Acesso a endpoints relacionados a pacientes e agendamento

---

## 📌 Próximas Implementações

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
