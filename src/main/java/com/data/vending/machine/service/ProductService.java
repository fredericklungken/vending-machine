package com.data.vending.machine.service;

import com.data.vending.machine.exception.DuplicateEntityException;
import com.data.vending.machine.exception.InvalidDenominationException;
import com.data.vending.machine.exception.ResourceNotFoundException;
import com.data.vending.machine.model.Product;
import com.data.vending.machine.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Create a new product
    public Product createProduct(Product product) {
        // Check for duplicate product name
        if (productRepository.findById(product.getName()).isPresent()) {
            throw new DuplicateEntityException("Product name already exists.");
        }
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get a product by ID (name)
    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    // Update a product
    public Product updateProduct(Product product) {
        if (!productRepository.existsById(product.getName())) {
            throw new ResourceNotFoundException("Product not found");
        }
        return productRepository.save(product);
    }

    // Delete a product
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }


    public List<String> buyProduct(List<Integer> insertedMoney) {
        // Validate the denominations
        for (Integer money : insertedMoney) {
            if (money != 2000 && money != 5000) {
                throw new InvalidDenominationException("Invalid denomination: " + money);
            }
        }

        // Calculate total inserted money
        int totalMoney = insertedMoney.stream().mapToInt(Integer::intValue).sum();

        // Fetch all available products sorted by price (most expensive first)
        List<Product> availableProducts = productRepository.findAll();
        availableProducts.sort((p1, p2) -> p2.getPrice().compareTo(p1.getPrice())); // Sort by price descending

        Map<String, Integer> purchasedProductsMap = new LinkedHashMap<>();

        // Select products from the most expensive to the cheapest
        for (Product product : availableProducts) {
            while (totalMoney >= product.getPrice()) {
                purchasedProductsMap
                        .put(product.getName(), purchasedProductsMap.getOrDefault(product.getName(), 0) + 1);
                totalMoney -= product.getPrice();
            }
        }

        if (purchasedProductsMap.isEmpty()) {
            throw new IllegalArgumentException("Not enough money to buy any products");
        }

        // Format the output to "1 Coffee, 2 Sosro"
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : purchasedProductsMap.entrySet()) {
            result.add(entry.getValue() + " " + entry.getKey());
        }

        return result;
    }
}
