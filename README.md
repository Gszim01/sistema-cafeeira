# Sistema Cafeeira 2 Irmãos

Aplicação web para controle de notas de secagem e beneficiamento de café da Cafeeira 2 Irmãos, em São Miguel do Guaporé - RO.

## Funcionalidades

- Emissão, consulta, edição e exclusão de notas.
- Pesquisa de notas por número, nome e data.
- Painel com totais de notas e sacas.
- Geração de nota em PDF.

## Tecnologias

- Java 25
- Spring Boot 3.5
- Spring MVC e Thymeleaf
- Spring Data JPA e banco de dados H2
- Maven

## Como executar

É necessário ter o JDK 25 instalado. Na raiz do projeto, execute:

```bash
mvn spring-boot:run
```

Depois, acesse [http://localhost:8080](http://localhost:8080).

O banco H2 é criado localmente na pasta `data/` durante a execução. Essa pasta e os arquivos do banco não são versionados.
