package org.example.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Long ID = 1L;
    private static final Product PRODUCT = new Product(ID, "Bitcoin ZenGo wallet", 299.99);

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    ProductService productService;

    @Test
    void shouldGetProductExistingInRepoById() {
    }

    @Test
    void shouldNotGetProductNotExistingInRepoByIdAndThrowNoSuchElementException() {
    }

    @Test
    void shouldAddValidProduct() {
    }

    @Test
    void shouldNotAddInvalidProductAndThrowIllegalArgumentException() {
    }

    @Test
    void shouldDeleteProductExistingInRepoById() {
    }

    @Test
    void shouldNotDeleteProductNotExistingInRepoByIdAndThrowNoSuchElementException() {
    }
}