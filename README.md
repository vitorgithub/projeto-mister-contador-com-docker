Aplicação Spring Boot para processamento de extratos bancários em PDF, permitindo a visualização das transações através de uma interface web simples.

## Pré-requisitos

- Docker
- Docker Compose

## Como Executar

1. Clone o repositório:
   ```
   git clone [URL_DO_REPOSITÓRIO]
   ```
2. Entre no diretório do projeto:
   ```
   cd pdf-extract-api
   ```
3. Utilize o Docker Compose para construir e iniciar a aplicação:
   ```
   docker-compose up --build
   ```

Após os contêineres estarem em funcionamento, a aplicação estará acessível em `http://localhost:8080`.

## Uso

- Acesse `http://localhost:8080` em seu navegador para carregar a interface de usuário.
- Faça o upload de um arquivo PDF contendo o extrato bancário.
- Visualize os dados das transações extraídas na tabela apresentada na interface.

## Tecnologias Utilizadas

- Java 11
- Spring Boot
- Apache PDFBox
- PostgreSQL
- Docker
