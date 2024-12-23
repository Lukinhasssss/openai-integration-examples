# Documentação para Configuração e Execução do Projeto Kotlin com Integração OpenAI

Este documento fornece as etapas necessárias para configurar e executar o projeto Kotlin que integra com a OpenAI utilizando o Secrets Manager no LocalStack.

## Requisitos

Certifique-se de que seu ambiente atenda aos seguintes requisitos antes de prosseguir:

- **Java 21** instalado.
- **Docker** e **Docker Compose** instalados.
- **AWS CLI** instalado.

## Configuração do Ambiente

1. **Configurar LocalStack**:
    - O LocalStack será utilizado para simular o AWS Secrets Manager.
    - O projeto já inclui um arquivo `docker-compose.yaml` para facilitar a inicialização do LocalStack.

2. **Configurar credenciais AWS**:
    - O LocalStack está configurado para usar os seguintes valores como credenciais:
      ```
      AWS_ACCESS_KEY_ID=localstack
      AWS_SECRET_ACCESS_KEY=localstack
      ```
    - Você pode configurar um perfil específico para o LocalStack utilizando o seguinte comando:
      ```bash
      aws configure --profile localstack
      ```
      E insira os valores abaixo quando solicitado:
      ```
      AWS Access Key ID [None]: localstack
      AWS Secret Access Key [None]: localstack
      Default region name [None]: sa-east-1
      Default output format [None]: json
      ```
    - Ou altere o arquivo de credenciais do AWS CLI (`~/.aws/credentials`) para incluir o seguinte:
      ```
      [localstack]
      aws_access_key_id=localstack
      aws_secret_access_key=localstack
      ```

3. **Nome do Secret**:
    - O nome do secret utilizado é `openai-integration-secrets`.

## Inicialização do LocalStack

1. Navegue até o diretório raiz do projeto.
2. Execute o comando para iniciar o LocalStack via Docker Compose:
   ```bash
   docker compose up -d
   ```
3. Verifique se o LocalStack está funcionando:
   ```bash
   docker logs localstack_container_name
   ```
   Substitua `localstack_container_name` pelo nome correto do contêiner (configurado no `docker-compose.yaml`).

## Criar o Secret no LocalStack

Com o LocalStack em execução, você precisa criar o secret para armazenar a API Key da OpenAI.

1. Instale e configure o AWS CLI para usar o perfil do LocalStack:
   ```bash
   aws configure --profile localstack
   ```

2. Use o seguinte comando para criar o secret no Secrets Manager:
   ```bash
   aws --endpoint-url=http://localhost:4566 --profile localstack secretsmanager create-secret \
     --name openai-integration-secrets \
     --secret-string '{"apiKey":"sua-api-key-aqui"}'
   ```

## Execução do Projeto

1. Compile o projeto Kotlin:
   ```bash
   ./gradlew build
   ```
2. Execute o projeto:
   ```bash
   ./gradlew run
   ```

A aplicação deverá se conectar ao Secrets Manager no LocalStack, recuperar a API Key da OpenAI e utilizá-la conforme implementado no código.

## Dicas de Debugging

- Caso o secret não seja encontrado, verifique se ele foi criado corretamente:
  ```bash
  aws --endpoint-url=http://localhost:4566 --profile localstack secretsmanager list-secrets
  ```
- Caso a aplicação não consiga se conectar ao LocalStack, verifique os logs do contêiner e a configuração do endpoint no código.

## Encerramento do LocalStack

Para parar e remover o LocalStack:
```bash
docker compose down
```

