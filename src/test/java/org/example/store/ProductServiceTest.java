package org.example.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

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
        when(productRepository.findById(ID)).thenReturn(Optional.of(PRODUCT));

        Product actualProductById = productService.getProductById(ID);

        assertEquals(PRODUCT, actualProductById);
        verify(productRepository).findById(ID);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
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