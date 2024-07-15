# Payment System

Este projeto é um sistema de pagamento desenvolvido em Java utilizando Spring Boot. A aplicação, o banco de dados e outros serviços necessários estão orquestrados utilizando Docker Compose.

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

---
