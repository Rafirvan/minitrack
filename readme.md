
# MiniTrack

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen)](https://spring.io/projects/spring-boot)  
[![Java](https://img.shields.io/badge/Java-17-blue)](https://openjdk.org/projects/jdk/17/)  
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14.3-blue)](https://www.postgresql.org/)  

## Overview
**MiniTrack** is a Spring Boot-based application designed to track the total of your taxes along with recording its payment

---

## Features
- **Feature 1**: Count your taxes based on tax types and review them
- **Feature 2**: Review and check tax payments
- **Feature 3**: User Register, login and profile system
- **Feature 4**: save and edit tax types (for Admin role)

- 

---

## Table of Contents
1. [Getting Started](#getting-started)
2. [Technologies Used](#technologies-used)
3. [Setup and Installation](#setup-and-installation)
4. [Database Configuration](#database-configuration)
5. [Usage](#usage)
6. [Endpoints](#endpoints)
7. [Contact](#contact)

---

## Getting Started
To get started with this project, ensure the following tools are installed on your system:
- [Java 17+](https://openjdk.org/projects/jdk/17/)
- [Maven 3.8+](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)
- (Optional) [Docker](https://www.docker.com/)

---

## Technologies Used
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Testing**: JUnit, Mockito
- **Containerization**: Docker (compose)

---

## Setup and Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Rafirvan/minitrack
   cd project-name
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. (Optional) Run using Docker:
   ```bash
   docker build -t project-name .
   docker run -p 8080:8080 project-name
   ```

---

## Database Configuration
Update the `application.properties` or `application.yml` file to configure the database connection:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your-database
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

(Optional) If using Docker Compose, update the `docker-compose.yml` file with the required environment variables.

---

## Usage
1. Access the application at:
   ```
   http://localhost:8080
   ```

2.  Default login credentials:
    - **Admin**: `username: admin` / `password: admin123`
    - **User**: `username: user` / `password: user123`

---

## Endpoints
Here’s a list of key API endpoints:


### Public Endpoints

| Method | Endpoint               | Description              |
|--------|------------------------|--------------------------|
| POST   | `/api/users/register`   | Register a new user account. |
| POST   | `/api/users/login`      | User login.              |

### Authenticated Endpoints

| Method | Endpoint                          | Description                                     |
|--------|-----------------------------------|-------------------------------------------------|
| GET    | `/api/payment-records/{paymentRecordId}` | Get payment record by ID.                      |
| PUT    | `/api/payment-records/{paymentRecordId}/mark-paid` | Change payment status to paid.                |
| GET    | `/api/tax-records/user/{userId}`  | Get all tax records by user account ID.         |
| GET    | `/api/tax-records/user/{userId}/total-unpaid` | Get the total amount of unpaid tax records by user ID. |
| GET    | `/api/tax-records/user/{userId}/total` | Get the total amount of all tax records by user ID. |
| POST   | `/api/tax-records/user/{userId}`  | Create a tax record for a user.                 |
| POST   | `/api/tax-types`                 | Create a new tax type.                         |
| PUT    | `/api/tax-types/{id}`            | Update an existing tax type by ID.              |
| GET    | `/api/tax-types`                 | Get a tax type by name.                        |
| GET    | `/api/profiles/{userAccountId}`  | Get user profile by user account ID.            |
| PUT    | `/api/profiles/{userAccountId}`  | Update user profile.                          |
"""



### Swagger


or optionally, use Swagger at 
   ```
   http://localhost:8080/swagger-ui/index.html
   ```
after running the application to check all endpoints with a nice UI

---



## Contact
For inquiries or support, please reach out:
- **Name**: Rafirvan
- **Email**: rafirvan@gmail.com

