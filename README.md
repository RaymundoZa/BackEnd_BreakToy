# BreakToy Inventory – Backend

This is the backend API for the **BreakToy Inventory Management** system. Built with **Spring Boot 3.4.5** and **Java 21**, it provides RESTful endpoints for managing product data and inventory metrics.

---

## ⚙️ Requirements

- Java 17+
- Maven 3.x

---

## 🚀 Setup & Run

1. **Clone the repository**

```bash
git clone https://github.com/RaymundoZa/BackEnd_BreakToy.git
cd BackEnd_BreakToy
   ```
   
The backend will be available in [http://localhost:9090](http://localhost:9090).

---

## 📦 REST API Overview

### Get all products
```http
GET /products
```
Returns a list of all products (with optional filters for name, category, and stock).

### Create a new product
```http
POST /products
Content-Type: application/json
```  
```json
{
  "name": "Name of the product",
  "category": "Category name",
  "unitPrice": 123.45,
  "quantityInStock": 10,
}
```

### Update an existing product
```http
PUT /products/{id}
Content-Type: application/json
```  
```json
{
  "name": "New Name",
  "category": "New Category",
  "unitPrice": 200,
  "quantityInStock": 20,
}
```

### Delete a product
```http
DELETE /products/{id}
```

### Mark product as in stock
```http
PUT /products/{id}/instock?quantity=10
```

### Mark product as out of stoc
```http
POST /products/{id}/outofstock
```

### Get inventory metrics
```http
GET /products/metrics
```  
Returns total stock, total value, average price, and category breakdowns.


---

## 🧠 Project Structure

- `src/main/java/com/example/inventory_backend/`
    - `Product.java`: Product Model
    - `ProductController.java`: HTTP Endpoints (/products)
- `pom.xml`: Maven configuration

---

## 🛡️ Validation & Error Handling

- Uses Jakarta Bean Validation annotations (@NotNull, @Size, etc.)
- Global @ExceptionHandler returns structured field-level errors

---

## 🔧 CORS Configuration

To allow cross-origin request from the frontend (http://localhost:8080), we enabled a basico CORS filter in Spring Boot.

---

## ✅ Testing

- JUnit 5 for unit tests (service layer)
- WebMvcTest for controller endpoint testing
- Uses Mockito and AssertJ for assertions and mocking

```bash
mvn test
   ```

---

## 📌 Notes

- Data is stored in memory (no database required)
- Designed for prototyping and local development
- Easily extendable with a real database using JPA

---

## 👨‍💻 Author

Raymundo Daniel Zamora Juárez
Encora SPARK Program · 2025
