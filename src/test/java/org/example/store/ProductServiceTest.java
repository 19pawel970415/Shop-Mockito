package org.example.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Long ID = 1L;
    private static final Long NONEXISTENT_ID = 0L;
    private static final Product PRODUCT = new Product(ID, "Bitcoin ZenGo wallet", 299.99);
    private static final Product INVALID_PRODUCT = new Product(NONEXISTENT_ID, "", 0.0);

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
        when(productRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(NONEXISTENT_ID))
                .isExactlyInstanceOf(NoSuchElementException.class)
                .hasMessage("Product not found")
                .hasNoCause();
        verify(productRepository).findById(NONEXISTENT_ID);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
    }

    @Test
    void shouldAddValidProduct() {
        when(productValidator.isProductValid(PRODUCT)).thenReturn(true);
        // doNothing().when(productRepository).addProduct(PRODUCT); - optional, Mockito does nothing with it anyway

        productService.addProduct(PRODUCT);

        verify(productValidator).isProductValid(PRODUCT);
        verify(productRepository).addProduct(PRODUCT);
        verifyNoMoreInteractions(productValidator, productRepository);
    }

    @Test
    void shouldNotAddInvalidProductAndThrowIllegalArgumentException() {
        when(productValidator.isProductValid(INVALID_PRODUCT)).thenReturn(false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.addProduct(INVALID_PRODUCT))
                .withMessage("Product is invalid")
                .withNoCause();
        verify(productValidator).isProductValid(INVALID_PRODUCT);
        verifyNoMoreInteractions(productValidator);
        verifyNoInteractions(productRepository);
    }

    @Test
    void shouldDeleteProductExistingInRepoById() {
    }

    @Test
    void shouldNotDeleteProductNotExistingInRepoByIdAndThrowNoSuchElementException() {
    }
}