package com.example.inventory_backend;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class Product {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no debe exceder 120 caracteres")
    private String name;

    @NotBlank(message = "La categoría es obligatoria")
    private String category;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private Double unitPrice;

    @NotNull(message = "La cantidad en inventario es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer quantityInStock;

    private LocalDate expirationDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Product() {
        // Constructor vacío necesario para Jackson y Spring
    }

// Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public Integer getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(Integer quantityInStock) { this.quantityInStock = quantityInStock; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}
