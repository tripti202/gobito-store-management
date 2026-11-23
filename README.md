# GOBITO — Store Management & E-commerce Backend

A Java-based backend application designed to simplify store operations through secure authentication, product management, and expiry-aware inventory tracking.

GOBITO aims to make inventory management smarter by tracking product batches, monitoring expiry dates, and supporting time-sensitive discount strategies to help stores reduce product wastage.

---

## Overview

Traditional inventory systems often treat all available stock as identical. However, products belonging to different batches may have different expiry dates.

GOBITO addresses this challenge through an inventory lot-based approach, allowing stock to be managed at the batch level rather than treating inventory as a single quantity.

The system is being developed with a focus on clean backend architecture, secure APIs, data consistency, and practical business workflows.

## Key Features

### Authentication & Authorization

- User registration and login.
- JWT-based authentication.
- Secure password handling.
- Role-based access control.
- Protected API endpoints.

### Product & Inventory Management

- Product and store inventory management.
- Inventory lot-based stock tracking.
- Separate tracking of product batches.
- Expiry-date monitoring.
- Remaining shelf-life calculation.

### Expiry-Aware Discount Management

- Identify inventory approaching its expiry date.
- Calculate the remaining days before expiry.
- Support time-sensitive discount strategies.
- Apply higher discounts as products approach expiry to encourage timely sales and reduce wastage.

## Technology Stack

| Layer             | Technology                  |
| ----------------- | --------------------------- |
| Language          | Java 21                     |
| Backend Framework | Spring Boot                 |
| API Architecture  | REST APIs                   |
| Database          | PostgreSQL                  |
| ORM               | Spring Data JPA / Hibernate |
| Authentication    | Spring Security, JWT        |
| Build Tool        | Maven                       |
| Development IDE   | Eclipse                     |
| Version Control   | Git & GitHub                |

## Architecture

The application follows a layered backend architecture to separate business logic, API handling, and data persistence.

```text
Client
  |
  v
REST Controllers
  |
  v
Service Layer
  |
  v
Repository Layer
  |
  v
PostgreSQL Database
```

**Responsibilities:**

- **Controller Layer:** Handles incoming HTTP requests and API responses.
- **Service Layer:** Contains business logic, validations, and application workflows.
- **Repository Layer:** Manages database operations using Spring Data JPA.
- **Database:** Stores users, products, and inventory-related information.

## Getting Started

### Prerequisites

Make sure you have the following installed:

- Java 21 or later
- PostgreSQL
- Maven
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/tripti202/gobito-store-management.git
```

### 2. Navigate to the Project

```bash
cd gobito-store-management
```

### 3. Create the Database

Create a PostgreSQL database:

```sql
CREATE DATABASE gobito_db;
```

### 4. Configure Environment Variables

The application uses environment variables to keep sensitive configuration outside the source code.

Configure the following variables:

| Variable      | Description                     |
| ------------- | ------------------------------- |
| `DB_URL`      | PostgreSQL JDBC connection URL  |
| `DB_USERNAME` | PostgreSQL username             |
| `DB_PASSWORD` | PostgreSQL password             |
| `JWT_SECRET`  | Secret key used for JWT signing |

Example configuration:

```text
DB_URL=jdbc:postgresql://localhost:5432/gobito_db
DB_USERNAME=postgres
DB_PASSWORD=your_database_password
JWT_SECRET=your_secure_base64_secret
```

**Important:** Replace the example values with your own local configuration. Never commit real credentials, passwords, or JWT secrets to GitHub.

### 5. Build the Application

```bash
mvn clean install
```

### 6. Run the Application

```bash
mvn spring-boot:run
```

The application runs at:

```text
http://localhost:8080
```

## Configuration

The application uses Spring Boot configuration with environment-variable placeholders.

Example:

```properties
spring.application.name=GOBITO
server.port=8080

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/gobito_db}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

## Engineering Concepts

This project provides practical implementation experience with:

- Layered application architecture.
- REST API development.
- JWT authentication and authorization.
- Role-based access control.
- Object-relational mapping.
- Relational database design.
- Inventory lot management.
- Business-rule implementation.
- Environment-based configuration.
- Git-based version control.

## Project Roadmap

The application is being developed incrementally, with planned improvements including:

- [ ] Complete product and inventory workflows.
- [ ] Advanced expiry-based discount automation.
- [ ] Inventory movement and stock adjustment tracking.
- [ ] Order and checkout management.
- [ ] Automated unit and integration testing.
- [ ] API documentation.
- [ ] Docker-based deployment.
- [ ] Production deployment and monitoring.

## Project Repository

**GitHub:** [https://github.com/tripti202/gobito-store-management](https://github.com/tripti202/gobito-store-management)

---

## Author

**Tripti Chaturvedi**

Associate Software Engineer | Java Backend Development

GitHub: [@tripti202](https://github.com/tripti202)

---

*GOBITO — Building smarter store management through technology.*
