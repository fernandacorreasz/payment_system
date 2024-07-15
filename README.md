# Payment System

Este projeto é um sistema de pagamento desenvolvido em Java utilizando Spring Boot. A aplicação, o banco de dados e outros serviços necessários estão orquestrados utilizando Docker Compose.

### Tecnologias Utilizadas:

- **Java**
- **Spring Boot**
- **Docker**
- **Docker Compose**
- **PostgreSQL**
- **Flyway**
- **Spring Security e JWT**
- **Swagger**

### Sobre o Projeto:
Este projeto consiste em uma aplicação de pagamento que permite o registro de usuários, autenticação, gerenciamento de contas a pagar e recebimento de pagamentos. A aplicação está configurada para utilizar Spring Security com JWT para proteger os endpoints e controlar o acesso baseado em permissões.

- **Autenticação e Autorização**: O projeto utiliza Spring Security e JWT para autenticação e autorização. Usuários podem se registrar e fazer login para receber um token JWT, que deve ser usado para acessar endpoints protegidos.
- **Migrações de Banco de Dados**: Utilizamos Flyway para gerenciar as migrações de banco de dados.
- **Documentação da API**: A documentação da API é gerada automaticamente utilizando Swagger para visualizar e testar os endpoints da API diretamente no navegador.
- **Testes**: Os testes unitários foram implementados utilizando JUnit e Mockito.

---
## Demonstração em Vídeo

### Configuração da Aplicação na Máquina

[Assista ao vídeo no YouTube](https://youtu.be/aMZF3JlWxAE)

### Testes das APIs no Swagger

[Assista ao vídeo no YouTube](https://youtu.be/_S9UUaSnjOI)

---

## Instruções para Construir e Rodar a Aplicação

### Pré-requisitos

- Docker
- Docker Compose

---

### Como Construir e Rodar

1. **Clone o repositório**:
    ```sh
    git clone https://github.com/fernandacorreasz/payment_system.git
    cd payment_system
    ```

---

2. **Remova contêineres anteriores (se existirem)**:
    ```sh
    docker rm -f postgres_container postgres_test_container payment_system_app
    ```

---

3. **Construa e inicie os contêineres**:
    ```sh
    docker-compose up --build
    ```

---

4. **Verifique os logs para assegurar que tudo está funcionando corretamente**:
    ```sh
    docker-compose logs -f
    ```

---

### Verificação das Imagens Geradas

5. Para verificar se as imagens foram geradas corretamente, você pode usar o seguinte comando:
    ```sh
    docker images
    ```

---

### Serviços e Portas

- **Aplicação**: http://localhost:8080
- **PostgreSQL (Produção)**: localhost:5432
- **PostgreSQL (Teste)**: localhost:5433

---

### Acesso ao Swagger

6. Após iniciar os contêineres, você pode acessar a documentação da API através do Swagger:
    ```
    http://localhost:8080/swagger-ui/index.html#/
    ```

---

### Informações Adicionais

7. Para mais detalhes sobre o projeto e documentação completa, consulte o link abaixo:
    [Documentação Payment System](https://paint-mailbox-381.notion.site/Documenta-o-payment-system-89386274425647fab3edb224760cc2e6)


