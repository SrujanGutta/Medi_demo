# Medi_demo Application

## Overview

The **Medi_demo** application is a RESTful service API for managing member data and claims. This project includes functionality for creating, retrieving, updating, and deleting member and claim records. It also supports searching and grouping claims based on specific criteria.

## Features

- Create, retrieve, update, and delete member records.
- Add new claims to existing members.
- Update claim status from `SUBMITTED` to `APPROVED` or `DENIED`.
- Search for member records with claims after a specific date.
- Group and retrieve submitted claims by member, medication, and pharmacy.
- API documentation using Swagger for interactive testing and documentation.

## Technologies

- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger/OpenAPI**
- **Lombok**

## Getting Started

### Prerequisites

- Java 17
- PostgreSQL database
- Gradle

### Setup Instructions

1. **Clone the repository**:

   ```bash
   git clone https://github.com/your-username/Medi_demo.git
   cd Medi_demo
   ```

2. **Set up the PostgreSQL database**:

   - Ensure PostgreSQL is installed and running.
   - Create a new database named `medication_db`.

     ```sql
     CREATE DATABASE medication_db;
     ```

   - Create the required tables:

     ```sql
     CREATE TABLE member (
         id SERIAL PRIMARY KEY,
         first_name VARCHAR(255),
         last_name VARCHAR(255),
         phone_number VARCHAR(20),
         dob DATE,
         demographics VARCHAR(255)
     );

     CREATE TABLE claim (
         id SERIAL PRIMARY KEY,
         claim_date DATE,
         medication VARCHAR(255),
         pharmacy_name VARCHAR(255),
         status VARCHAR(20),
         member_id INTEGER REFERENCES member(id)
     );
     ```

3. **Load initial data**:

   - (Optional) You can load initial data into the tables for testing. Refer to the provided `medication_db.sql` script in the project directory.

4. **Configure the application properties**:

   - Open the `application.properties` file located in `src/main/resources` and set your PostgreSQL database connection details.

     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/medication_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```

5. **Build and run the application**:

   ```bash
   ./gradlew bootRun
   ```

6. **Access the Swagger UI**:

   - Open your web browser and go to `http://localhost:8080/swagger-ui.html` to interact with the API.

## API Endpoints

### Members

- **GET /members**: Retrieve all member records with claims after `01/01/2024`.
- **POST /members**: Create a new member.
- **PUT /members/{id}**: Update an existing member's information.
- **DELETE /members/{id}**: Delete an existing member.

### Claims

- **GET /claims**: Retrieve all claims.
- **POST /claims**: Add a new claim to an existing member.
- **PUT /claims/{id}**: Update the status of a claim.
- **DELETE /claims/{id}**: Delete an existing claim.
- **GET /submitted**: Retrieve claims that are in `SUBMITTED` status, grouped by member, medication, and pharmacy.

## Testing

- Use Swagger or Postman to test the API endpoints.
- Use the provided `medication_db.sql` to load initial data for testing.

## License

This project is licensed under the MIT License.

