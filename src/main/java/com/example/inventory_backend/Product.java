package com.example.inventory_backend;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class Product {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 120, message = "Name must not exceed 120 characters")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    private Double unitPrice;

    @NotNull(message = "Quantity in stock is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantityInStock;

    private LocalDate expirationDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Product() {
        // Empty constructor required for Jackson and Spring
    }

    // Getters and Setters
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
