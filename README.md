# Todo Summary Assistant

## Todo Summary Assistant is a full-stack web application for managing todo tasks, summarizing pending todos using Cohere's language model API, and sending the summary to a Slack channel using a webhook.

### Technologies Used

### Backend:

-Java

-Spring Boot

-RESTful APIs

-PostgreSQL

-JPA/Hibernate

### Frontend:

-React.js

-Axios (for HTTP requests)

### Integrations:

-Cohere API for summarization

-Slack Webhook for sending summaries

### Features:

-Add and delete todo tasks

-View all todos and pending todos

-Summarize pending todos using Cohere

-Send task summary to Slack via webhook

-Organized frontend-backend project structure

### Application Properties

spring.datasource.url=jdbc:postgresql://localhost:5432/tododb

spring.datasource.username=your_postgres_username

spring.datasource.password=your_postgres_password

spring.jpa.hibernate.ddl-auto=update

cohere.api.key=your_cohere_api_key

slack.webhook.url=https://hooks.slack.com/services/your/webhook/url

### Notes

Make sure PostgreSQL is running and the database is created.

Ensure the Cohere API key and Slack webhook URL are valid.

CORS is enabled in the backend to support React frontend integration.
