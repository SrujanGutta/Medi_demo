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
   git clone https://github.com/SrujanGutta/Medi_demo.git
   cd Medi_demo
   ```

2. **Set up PostgreSQL**:

   - **Install PostgreSQL**:
     - Download and install PostgreSQL from [the official website](https://www.postgresql.org/download/).
     - Follow the installation instructions for your operating system.

   - **Start PostgreSQL**:
     - On Windows, you can start PostgreSQL using `pgAdmin` or by running the PostgreSQL server from the Start menu. Just search `psql` in the Start menu.
     - On macOS or Linux, you can start PostgreSQL using the terminal with the command:
       ```bash
       sudo service postgresql start
       ```

   - **Load initial data**:
     - Use the `medication_db.sql` script provided in the project to load initial data into the tables. To load the data, you can use the following command in the terminal:
       ```bash
       psql -U your_username -d medication_db -f path/to/medication_db.sql
       ```
       Replace `your_username` with your PostgreSQL username and `path/to/medication_db.sql` with the actual path to your `medication_db.sql` file.

       For example, if `medication_db.sql` is in the project root directory, you might run:
       ```bash
       psql -U your_username -d medication_db -f medication_db.sql
       ```

   - **Create a new database** (Optional):
     - Open a terminal or `pgAdmin` and run the following commands to create a database:
       ```sql
       CREATE DATABASE medication_db;
       ```
     - To connect to the database and use SQL commands, run:
       ```bash
       psql -U your_username -d medication_db
       ```
       Replace `your_username` with your PostgreSQL username.

   - **Create required tables** (Optional):
     - Once connected to the `medication_db` database, create the tables by running:
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



3. **Configure the application properties**:

   - Open the `application.properties` file located in `src/main/resources` and set your PostgreSQL database connection details.

     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/medication_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```
     Replace `your_username` and `your_password` with your actual PostgreSQL credentials.

4. **Build and run the application**:

   ```bash
   ./gradlew bootRun
   ```

5. **Access the Swagger UI**:

   - Open your web browser and go to `http://localhost:8080/swagger-ui.html` to interact with the API.

## API Endpoints

### Members

- **GET /members**: Retrieve all member records with claims after `01/01/2024`.
- **PUT /members**: Create a new member.

### Claims

- **POST /claims**: Add a new claim to an existing member.
- **PUT /claims/{id}**: Update the status of a claim.
- **DELETE /claims/{id}**: Delete an existing claim.
- **GET /submitted**: Retrieve claims that are in `SUBMITTED` status, grouped by member, medication, and pharmacy.

## Testing

- Use Swagger or Postman to test the API endpoints.
- Use the provided `medication_db.sql` to load initial data for testing.
