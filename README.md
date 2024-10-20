# Vending Machine API

## Overview
This repository contains a Spring Boot application that simulates a vending machine. The application provides a CRUD API for managing products and allows users to buy drinks based on the inserted denominations.

## Features
- CRUD operations for managing vending machine products.
- Validation for incoming requests.
- Swagger documentation for API endpoints.
- Checkstyle integration for code quality and adherence to coding conventions.

## Technologies Used
- Spring Boot
- MongoDB
- Maven
- Checkstyle
- Swagger

## API Endpoints
### Product Endpoints
- **Create Product**: `POST /api/products`
- **Get All Products**: `GET /api/products`
- **Get Product by ID**: `GET /api/products/{id}`
- **Update Product**: `PUT /api/products`
- **Delete Product**: `DELETE /api/products/{id}`
- **Buy Drinks**: `POST /api/vending/buy`
    - **Request Body**:
      ```array
      [2000, 5000]
      ```
    - **Response**:
      ```array
      [
        "1 Cola"
      ]
      ```

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven
- MongoDB

### Clone the Repository
```bash
git clone https://github.com/fredericklungken/vending-machine.git
cd vending-machine
```

### Configuration
1. **MongoDB**: Ensure that MongoDB is running on your local machine or configure the connection settings in `application.properties`.
2. **Swagger Configuration**: Access the Swagger UI at `http://localhost:8080/swagger-ui.html` after starting the application.

### Build and Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

## Code Quality
### Checkstyle
This project uses Checkstyle for enforcing coding conventions. The configuration file is located at `src/main/resources/checkstyle.xml`.

To run Checkstyle:
```bash
mvn checkstyle:check
```
This command checks the code against the rules defined in the Checkstyle configuration.

### Code Formatting
Ensure you follow the following coding standards:
- Indentation: 4 spaces
- Line length: Max 120 characters
- Method and variable naming conventions (camelCase)

## Testing
Unit tests are included in the project to ensure the functionality of the application. To run the tests, use:
```bash
mvn test
```

## Documentation
API documentation is automatically generated using Swagger. Access it at:
```
http://localhost:8080/swagger-ui.html
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author
Frederick Lungken - [LinkedIn](https://www.linkedin.com/in/fredericklungken)
