# Hospital Management System - Tech Challenge FIAP (Fase 3)

Este projeto é um sistema de gestão hospitalar focado no agendamento de consultas, desenvolvido como parte da Fase 3 do Tech Challenge da pós-graduação em Arquitetura de Software da FIAP. 

A solução utiliza uma arquitetura baseada em microsserviços, focando em segurança, controle de acesso e comunicação assíncrona.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 
* **Framework:** Spring Boot
* **API:** Spring para GraphQL
* **Mensageria:** RabbitMQ
* **Banco de Dados:** MySQL
* **Mock SMTP:** Mailpit
* **Infraestrutura:** Docker e Docker Compose

## 🏗️ Arquitetura e Serviços

Para atender ao requisito de um backend simplificado e modular, o projeto foi estruturado utilizando a **Arquitetura em Camadas** (Layered Architecture/MVC).
O sistema é dividido em dois microsserviços autônomos:
1. **Appointment API:** Serviço principal responsável pelas regras de negócio, persistência de dados e exposição da interface GraphQL.
2. **Notification API:** Serviço secundário (consumidor) que escuta eventos do RabbitMQ e dispara e-mails assincronamente.

## 🚀 Como Executar (Quickstart)

O projeto foi 100% dockerizado para facilitar a execução. Não é necessário instalar nenhum banco de dados ou broker localmente.

1. Clone o repositório e acesse a pasta raiz (`hospital`).
2. Execute o comando do Docker Compose:
   ```bash
   docker-compose up -d --build
