package com.example.inventory_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Test GET vacío
    @Test
    public void testGetAllProducts_EmptyList() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // Test POST con producto válido
    @Test
    public void testCreateProduct_Valid() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("Pepsi");
        newProduct.setCategory("Bebida");
        newProduct.setUnitPrice(11.0);
        newProduct.setQuantityInStock(15);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Pepsi"));
    }

    // Test POST con producto inválido (sin nombre)
    @Test
    public void testCreateProduct_Invalid() throws Exception {
        Product newProduct = new Product();
        newProduct.setCategory("Bebida");
        newProduct.setUnitPrice(11.0);
        newProduct.setQuantityInStock(15);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }
}
