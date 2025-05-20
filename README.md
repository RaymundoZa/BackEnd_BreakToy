# BreakToy Inventory Backend

Backend para la gestión de inventario de productos (BreakToy), construido con Spring Boot.

---

## Requisitos

- Java 17 o superior
- Maven 3.x

---

## Instalación y uso

1. Clona el repositorio:
   ```bash
   git clone https://github.com/RaymundoZa/BackEnd_BreakToy.git
   cd BackEnd_BreakToy
   ```
2. Compila el proyecto:
   ```bash
   mvn clean install
   ```
3. Ejecuta el backend:
   ```bash
   mvn spring-boot:run
   ```  
   El backend estará disponible en [http://localhost:9090](http://localhost:9090).

---

## Endpoints principales

### Obtener todos los productos
```http
GET /products
```  
Respuesta: lista de productos.

### Crear un producto
```http
POST /products
Content-Type: application/json
```  
```json
{
  "name": "Nombre del producto",
  "category": "Categoría",
  "unitPrice": 123.45,
  "quantityInStock": 10,
  "expirationDate": "2025-12-31"
}
```

### Actualizar un producto
```http
PUT /products/{id}
Content-Type: application/json
```  
```json
{
  "name": "Nuevo nombre",
  "category": "Nueva categoría",
  "unitPrice": 200,
  "quantityInStock": 20,
  "expirationDate": "2026-01-01"
}
```

### Eliminar un producto
```http
DELETE /products/{id}
```

### Marcar producto como en stock
```http
PUT /products/{id}/instock?quantity=10
```

### Marcar producto como agotado
```http
POST /products/{id}/outofstock
```

### Obtener métricas de inventario
```http
GET /products/metrics
```  
Respuesta: métricas generales y por categoría.

---

## Estructura del proyecto

- `src/main/java/com/example/inventory_backend/`
    - `Product.java`: modelo de producto
    - `ProductController.java`: controlador con los endpoints
- `pom.xml`: configuración de Maven

---

## Notas

- Datos en memoria (se pierden al reiniciar)
- Para pruebas o desarrollo únicamente

---

## Autor

Raymundo Daniel Zamora Juárez
