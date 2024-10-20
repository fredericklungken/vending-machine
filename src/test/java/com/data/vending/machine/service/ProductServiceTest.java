package com.data.vending.machine.service;

import com.data.vending.machine.exception.DuplicateEntityException;
import com.data.vending.machine.exception.InvalidDenominationException;
import com.data.vending.machine.exception.ResourceNotFoundException;
import com.data.vending.machine.model.Product;
import com.data.vending.machine.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product aqua;
    private Product sosro;
    private Product cola;
    private Product milo;
    private Product coffee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        aqua = new Product("Aqua", 2000);
        sosro = new Product("Sosro", 5000);
        cola = new Product("Cola", 7000);
        milo = new Product("Milo", 9000);
        coffee = new Product("Coffee", 12000);
    }

        @Test
        void createProduct_successful() {
            when(productRepository.findById(aqua.getName())).thenReturn(Optional.empty());

            productService.createProduct(aqua);
            verify(productRepository, times(1)).save(aqua);
        }

    @Test
    void createProduct_duplicateName() {
        when(productRepository.findById(aqua.getName())).thenReturn(Optional.of(aqua));

        assertThrows(DuplicateEntityException.class, () -> {
            productService.createProduct(aqua);
        });
    }

    @Test
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(aqua, sosro));

        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    void getProductById_successful() {
        when(productRepository.findById("Aqua")).thenReturn(Optional.of(aqua));

        Product foundProduct = productService.getProductById("Aqua");
        assertEquals("Aqua", foundProduct.getName());
    }

    @Test
    void getProductById_notFound() {
        when(productRepository.findById("NonExistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById("NonExistent");
        });
    }

    @Test
    void updateProduct_successful() {
        when(productRepository.existsById(aqua.getName())).thenReturn(true);

        Product updatedProduct = productService.updateProduct(aqua);
        verify(productRepository, times(1)).save(aqua);
    }

    @Test
    void updateProduct_notFound() {
        when(productRepository.existsById(aqua.getName())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(aqua);
        });
    }

    @Test
    void deleteProduct_successful() {
        when(productRepository.existsById("Aqua")).thenReturn(true);

        assertDoesNotThrow(() -> {
            productService.deleteProduct("Aqua");
        });
    }

    @Test
    void deleteProduct_notFound() {
        when(productRepository.existsById("Aqua")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct("Aqua");
        });
    }

    @Test
    void buyProduct_invalidDenomination() {
        List<Integer> insertedMoney = Arrays.asList(1000);

        assertThrows(InvalidDenominationException.class, () -> {
            productService.buyProduct(insertedMoney);
        });
    }

    @Test
    void buyProduct_success_Aqua() {
        List<Integer> insertedMoney = Arrays.asList(2000);

        when(productRepository.findAll()).thenReturn(Arrays.asList(aqua));

        List<String> purchasedProducts = productService.buyProduct(insertedMoney);
        assertEquals(1, purchasedProducts.size());
        assertEquals("1 Aqua", purchasedProducts.get(0));
    }

    @Test
    void buyProduct_success_multipleAqua() {
        List<Integer> insertedMoney = Arrays.asList(2000, 2000);

        when(productRepository.findAll()).thenReturn(Arrays.asList(aqua));

        List<String> purchasedProducts = productService.buyProduct(insertedMoney);
        assertEquals(1, purchasedProducts.size());
        assertEquals("2 Aqua", purchasedProducts.get(0));
    }

    @Test
    void buyProduct_success_Cola() {
        List<Integer> insertedMoney = Arrays.asList(5000, 2000);

        when(productRepository.findAll()).thenReturn(Arrays.asList(aqua, cola));

        List<String> purchasedProducts = productService.buyProduct(insertedMoney);
        assertEquals(1, purchasedProducts.size());
        assertEquals("1 Cola", purchasedProducts.get(0));
    }

    @Test
    void buyProduct_success_Milo() {
        List<Integer> insertedMoney = Arrays.asList(5000, 5000);

        when(productRepository.findAll()).thenReturn(Arrays.asList(milo));

        List<String> purchasedProducts = productService.buyProduct(insertedMoney);
        assertEquals(1, purchasedProducts.size());
        assertEquals("1 Milo", purchasedProducts.get(0));
    }

    @Test
    void buyProduct_success_CoffeeAndSosro() {
        List<Integer> insertedMoney = Arrays.asList(5000, 5000, 5000, 2000);

        when(productRepository.findAll()).thenReturn(Arrays.asList(coffee, sosro, aqua));

        List<String> purchasedProducts = productService.buyProduct(insertedMoney);
        assertEquals(2, purchasedProducts.size());
        assertEquals("1 Coffee", purchasedProducts.get(0));
        assertEquals("1 Sosro", purchasedProducts.get(1));
    }
}