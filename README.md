# Desafio Soft Design

Guia rápido para criar, testar, executar e usar esta aplicação.

## Clone do projeto e preparação do ambiente

```shell script
$ https://github.com/MateusWMachado/softdesign-challenge.git
$ cd softdesign-challenge
$ docker-compose up -d
```

## Como fazer a conexão com o banco PostgreSQL

A aplicação estará escutando por padrão na porta HTTP 8080.
Após rodar o comando docker compose, digite a url "localhost:16543" e faça login com o usuário "test@test.com.br" e senha "root", em seguida vá em "Adicionar Novo Servidor" e digite os seguintes dados:
- Host name/address: teste-postgres-compose
- Port: 5432
- Maintenance Database: postgres
- Username: postgres
- Password: Postgres2023! <br />

Então você precisará comentar uma linha de código em application.properties e descomentar o restante sobre o banco de dados. <br />
Seu código irá parecer com isso <br />
```shell script
 #spring.datasource.url=${JDBC_DATASOURCE-URL}
 
 spring.database.driverClassName=org.postgresql.Driver
 spring.datasource.url=jdbc:postgresql://localhost:15432/postgres
 spring.datasource.username=postgres
 spring.datasource.password=Postgres2023!
```
Caso você não queira fazer toda a conexão com o PostgreSQL localmente, também pode utilizar o banco em mémoria H2 para testes. <br /> 
A dependência já está incluída no POM.xml, basta ajustar o application.properties e a parte da conexão ficará mais ou menos assim <br /> 
```shell script
 #spring.datasource.url=${JDBC_DATASOURCE-URL}

 spring.datasource.url=jdbc:h2:mem:testdb
 spring.datasource.driverClassName=org.h2.Driver
 spring.datasource.username=sa
 spring.datasource.password=
 spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

 spring.h2.console.path=/h2-console
 spring.h2.console.settings.trace=false
 spring.h2.console.settings.web-allow-others=false

 spring.h2.console.enabled=true
```
Depois acesse a URL http://localhost:8080/h2-console/ e coloque em JDBC URL o valor "jdbc:h2:mem:testdb", username = "sa" e password vazio.


## Como buildar a aplicação

```shell script
$ mvn install
$ mvn spring-boot:run
```

## Como rodar os testes

```shell script
$ mvn test
```

## Breve documentação dos Endpoints

### Schedule API
#### Create new schedule
```http request
POST http://localhost:8080/api/v1/schedule/
{
  "subject": "Test new subject",
  "votes": []
}
``` 

#### Get the result of a schedule
```http request
GET http://localhost:8080/api/v1/schedule/{id}
``` 

### Voting Sessions API
#### Open a voting session
```http request
POST http://localhost:8080/api/v1/vote-session/
{
  "duration": 3,
  "idSchedule": 1
}
``` 

### Votes Session API
#### Add new vote
```http request
POST http://localhost:8080/api/v1/vote/
{
  "cpf": "12312312300",
  "idSchedule": 1,
  "vote": "SIM"
}
```

### Users Session API
#### Add new user
```http request
POST http://localhost:8080/api/v1/users
{
  "cpf": "12312312300"
}
```

#### Validate CPF 
```http request
GET http://localhost:8080/api/v1/users/{cpf}
```

Em um fluxo normal de trabalho o usuário criará um novo User para ser utilizado, em seguida, criará uma nova Schedule e abrirá uma nova sessão de votação, então, com o usuário criado poderá votar nessa sessão apenas uma vez, por fim, quando a sessão de votação se encerrar o usuário poderá verificar o resultado final da sessão buscando pelo endpoint correspondente.

## Swagger
Esta API é construída com o gerador de documentação Swagger. A documentação completa da API pode ser acessada em:
```http request
http://localhost:8080/swagger-ui.html
``` 

## Considerações importantes

- O aplicativo faz um post no kafka toda vez que um usuário busca o resultado de uma schedule mas isso não está no Heroku pois precisa de um plano pago então a implementação do Kafka está comentada no código e suas dependencias também. Caso queira fazer os testes utilizando Kafka basta descomentar o código e também as dependências.
- Não encontrei um serviço externo de consulta de cpf que não estivesse fora do ar ou precisasse de criação de token de autenticação então utilizei uma lib externa para fazer a validação de CPF, você pode chamar o endpoint /api/v1/users/{cpf} para verificar se um CPF é válido, caso ele não sejá válido você ainda poderá criar um usuário com aquele CPF, porém, ele não conseguirá votar em uma schedule.
- Usei o lombok no desenvolvimento, então para o aplicativo funcionar é recomendado usar o IDE IntelliJ.

## Sobre a implementação

- Para desenvolvimento da API: Java 11, Spring Framework, banco de dados PostgreSQL e Kafka.
- Para a conversão dos DTOs, optei por utilizar a lib ModelMapper, que abstrai essa lógica, tornando o código mais legível.
- Lombok foi usado para acelerar a escrita e a legibilidade do código

## Heroku APP URL

https://desafio-votos-softdesign-5d91aacd8924.herokuapp.com/api/v1/

## Swagger

https://desafio-votos-softdesign-5d91aacd8924.herokuapp.com/swagger-ui.html#/
