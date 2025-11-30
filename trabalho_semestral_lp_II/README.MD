
# RestoUni – REST API em Java 17 sem frameworks

Aplicação REST em **Java 17** usando apenas o **JDK** (HTTP Server via `com.sun.net.httpserver`), **JDBC** e **H2** como banco embarcado. Implementa CRUD para **Curso** e **Aula**, com relacionamento **1..N** (um Curso possui várias Aulas).

## Tecnologias e decisões
- Java 17 (OpenJDK)
- Sem frameworks como Spring/Quarkus/Micronaut
- H2 Embedded (arquivo em `./data/appdb`)
- JSON via Jackson
- Build com Maven
- Padrões e conceitos OO: abstração, encapsulamento, herança, polimorfismo
- Camadas: Controller → Service → Repository → DB

## Endpoints

### Cursos
- `GET /api/courses` — lista todos
- `GET /api/courses/{id}` — busca por id
- `POST /api/courses` — cria
- `PUT /api/courses/{id}` — atualiza
- `DELETE /api/courses/{id}` — remove

**Payload exemplo (POST/PUT)**:
```json
{
  "name": "Algoritmos I",
  "code": "ALG001",
  "description": "Introdução",
  "status": "ACTIVE"
}
