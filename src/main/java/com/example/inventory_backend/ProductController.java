package com.example.inventory_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.LocalDate;


@RestController
@RequestMapping("/products")
public class ProductController {

    private List<Product> productList = new ArrayList<>();

    // POST /products - Crea un producto con validación
    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
        long newId = productList.size() + 1;
        product.setId(newId);
        product.setCreatedAt(java.time.LocalDate.now());
        product.setUpdatedAt(java.time.LocalDate.now());
        productList.add(product);
        return product;
    }

    // PUT /products/# - Para buscar productos y editarlos :D
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product updatedProduct) {

        // Busca el producto por ID
        for (Product product : productList) {
            if (product.getId().equals(id)) {
                // Actualiza los campos permitidos
                product.setName(updatedProduct.getName());
                product.setCategory(updatedProduct.getCategory());
                product.setUnitPrice(updatedProduct.getUnitPrice());
                product.setQuantityInStock(updatedProduct.getQuantityInStock());
                product.setExpirationDate(updatedProduct.getExpirationDate());
                product.setUpdatedAt(java.time.LocalDate.now());
                return ResponseEntity.ok(product);
            }
        }

        // Si no lo encuentra, responde 404
        return ResponseEntity.notFound().build();
    }

    // DELETE /product/# - Para BORRAR un producto !!!!!!!!
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        for (Product product : productList) {
            if (product.getId().equals(id)) {
                productList.remove(product);
                return ResponseEntity.noContent().build(); // 204 No Content
            }
        }
        return ResponseEntity.notFound().build();
    }

    // PUT /products/#/instock?quantity=X Marcamos un producto como en STOCK
    @PutMapping("/{id}/instock")
    public ResponseEntity<?> markProductInStock(
            @PathVariable Long id,
            @RequestParam(defaultValue = "10") Integer quantity) {
        for (Product product : productList) {
            if (product.getId().equals(id)) {
                product.setQuantityInStock(quantity);
                product.setUpdatedAt(java.time.LocalDate.now());
                return ResponseEntity.ok(product);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // POST /products/#/outofstock Para marcar que ya no hay stock de un productop
    @PostMapping("/{id}/outofstock")
    public ResponseEntity<?> markProductOutOfStock(@PathVariable Long id) {
        for (Product product : productList) {
            if (product.getId().equals(id)) {
                product.setQuantityInStock(0);
                product.setUpdatedAt(java.time.LocalDate.now());
                return ResponseEntity.ok(product);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // GET /products/metrics - Obtiene métricas generales y por categoría del inventario
    @GetMapping("/metrics")
    public Map<String, Object> getInventoryMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // Métricas generales
        int totalStock = productList.stream()
                .filter(p -> p.getQuantityInStock() != null)
                .mapToInt(Product::getQuantityInStock)
                .sum();

        double totalValue = productList.stream()
                .filter(p -> p.getUnitPrice() != null && p.getQuantityInStock() != null)
                .mapToDouble(p -> p.getUnitPrice() * p.getQuantityInStock())
                .sum();

        double avgPrice = productList.stream()
                .filter(p -> p.getQuantityInStock() != null && p.getQuantityInStock() > 0 && p.getUnitPrice() != null)
                .mapToDouble(Product::getUnitPrice)
                .average().orElse(0);

        metrics.put("totalStock", totalStock);
        metrics.put("totalValue", totalValue);
        metrics.put("avgPrice", avgPrice);

        // Métricas por categoría
        Map<String, Map<String, Object>> byCategory = new HashMap<>();
        productList.stream()
                .filter(p -> p.getCategory() != null)
                .collect(Collectors.groupingBy(Product::getCategory))
                .forEach((cat, products) -> {
                    int catTotalStock = products.stream()
                            .filter(p -> p.getQuantityInStock() != null)
                            .mapToInt(Product::getQuantityInStock)
                            .sum();
                    double catTotalValue = products.stream()
                            .filter(p -> p.getUnitPrice() != null && p.getQuantityInStock() != null)
                            .mapToDouble(p -> p.getUnitPrice() * p.getQuantityInStock())
                            .sum();
                    double catAvgPrice = products.stream()
                            .filter(p -> p.getQuantityInStock() != null && p.getQuantityInStock() > 0 && p.getUnitPrice() != null)
                            .mapToDouble(Product::getUnitPrice)
                            .average().orElse(0);

                    Map<String, Object> catMetrics = new HashMap<>();
                    catMetrics.put("totalStock", catTotalStock);
                    catMetrics.put("totalValue", catTotalValue);
                    catMetrics.put("avgPrice", catAvgPrice);

                    byCategory.put(cat, catMetrics);
                });
        metrics.put("byCategory", byCategory);

        return metrics;
    }

    // GET /products - Obtiene productos filtrados, ordenados y paginados
    @GetMapping
    public List<Product> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortBy2,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "asc") String order2,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        // 1. Filtro
        Stream<Product> stream = productList.stream();

        if (name != null && !name.isEmpty()) {
            stream = stream.filter(p -> p.getName() != null && p.getName().toLowerCase().contains(name.toLowerCase()));
        }
        if (category != null && !category.isEmpty()) {
            stream = stream.filter(p -> category.contains(p.getCategory()));
        }
        if (inStock != null) {
            stream = stream.filter(p -> inStock ? p.getQuantityInStock() > 0 : p.getQuantityInStock() == 0);
        }

        // 2. Ordenamiento (puedes ordenar hasta por 2 campos)
        Comparator<Product> comparator = null;
        if (sortBy != null) {
            comparator = getComparator(sortBy, order);
        }
        if (sortBy2 != null) {
            Comparator<Product> secondaryComparator = getComparator(sortBy2, order2);
            comparator = comparator == null ? secondaryComparator : comparator.thenComparing(secondaryComparator);
        }
        if (comparator != null) {
            stream = stream.sorted(comparator);
        }

        // 3. Paginación (page empieza en 0)
        List<Product> filteredList = stream.collect(Collectors.toList());
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, filteredList.size());
        if (fromIndex > toIndex) return new ArrayList<>();
        return filteredList.subList(fromIndex, toIndex);
    }

    // Maneja los errores de validación y regresa un JSON con los mensajes de error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    // Utilidad: Devuelve el comparator según el campo y orden
    private Comparator<Product> getComparator(String field, String order) {
        Comparator<Product> comparator;
        switch (field) {
            case "name":
                comparator = Comparator.comparing(Product::getName, Comparator.nullsLast(String::compareToIgnoreCase));
                break;
            case "category":
                comparator = Comparator.comparing(Product::getCategory, Comparator.nullsLast(String::compareToIgnoreCase));
                break;
            case "unitPrice":
                comparator = Comparator.comparing(Product::getUnitPrice, Comparator.nullsLast(Double::compareTo));
                break;
            case "quantityInStock":
                comparator = Comparator.comparing(Product::getQuantityInStock, Comparator.nullsLast(Integer::compareTo));
                break;
            case "expirationDate":
                comparator = Comparator.comparing(Product::getExpirationDate, Comparator.nullsLast(LocalDate::compareTo));
                break;
            default:
                comparator = Comparator.comparing(Product::getId);
        }
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

}
