package org.example.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Long ID = 1L;
    private static final Long NONEXISTENT_ID = 0L;
    private static final Long ID_2 = 2L;
    private static final Product PRODUCT = new Product(ID, "Bitcoin ZenGo wallet", 299.99);
    private static final Product INVALID_PRODUCT = new Product(NONEXISTENT_ID, "", 0.0);
    private static final Product PRODUCT_2 = new Product(ID_2, "ETH pendent", 39.99);

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductService productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

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
    void shouldAddValidNewProduct() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());
        when(productValidator.isProductValid(PRODUCT)).thenReturn(true);
        doNothing().when(productRepository).addProduct(PRODUCT); // - optional, Mockito does nothing with it anyway

        productService.addProduct(PRODUCT);

        InOrder inOrder = inOrder(productRepository);
        inOrder.verify(productRepository).findById(ID);
        verify(productValidator).isProductValid(PRODUCT);
        inOrder.verify(productRepository).addProduct(PRODUCT);
        verifyNoMoreInteractions(productValidator, productRepository);
    }

    @Test
    void shouldNotAddExistingProduct() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.addProduct(PRODUCT))
                .withMessage("Product already in repository")
                .withNoCause();
        verify(productRepository).findById(ID);
        verifyNoInteractions(productValidator);
    }

    @Test
    void shouldNotAddNewInvalidProduct() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());
        when(productValidator.isProductValid(PRODUCT)).thenReturn(false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.addProduct(PRODUCT))
                .withMessage("Product is invalid")
                .withNoCause();
        verify(productRepository).findById(ID);
        verify(productValidator).isProductValid(PRODUCT);
    }

    @Test
    void shouldDeleteProductExistingInRepoById() {
        when(productRepository.findById(ID)).thenReturn(Optional.of(PRODUCT));
        doNothing().when(productRepository).deleteProduct(ID);

        productService.deleteProduct(ID);

        final InOrder inOrder = inOrder(productRepository);
        inOrder.verify(productRepository).findById(ID);
        inOrder.verify(productRepository).deleteProduct(ID);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
    }

    @Test
    void shouldNotDeleteProductNotExistingInRepoByIdAndThrowNoSuchElementException() {
        when(productRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> productService.deleteProduct(NONEXISTENT_ID))
                .withMessage("Cannot delete non-existent product")
                .withNoCause();
        verify(productRepository).findById(NONEXISTENT_ID);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
    }

    @Test
    void shouldNotUpdateNonExistentProduct() {
        when(productRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

        NoSuchElementException actualNoSuchElementException = assertThrows(NoSuchElementException.class, () -> productService.updateProductPrice(NONEXISTENT_ID, 111.1));
        assertThat(actualNoSuchElementException)
                .hasMessage("Cannot update price of non-existent product")
                .hasNoCause();
        verify(productRepository).findById(NONEXISTENT_ID);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
    }

    @Test
    void shouldNotUpdateExistingProductWithPriceSmallerThanZero() {
        when(productRepository.findById(ID)).thenReturn(Optional.of(PRODUCT));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.updateProductPrice(ID, -0.1))
                .withMessage("Price must be greater than 0")
                .withNoCause();
        verify(productRepository).findById(ID);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
    }

    @Test
    void shouldNotUpdateExistingProductWithPriceEqualToZero() {
        when(productRepository.findById(ID_2)).thenReturn(Optional.of(PRODUCT_2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.updateProductPrice(ID_2, 0))
                .withMessage("Price must be greater than 0")
                .withNoCause();
        verify(productRepository).findById(ID_2);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
    }

    @Test
    void shouldUpdateExistingProductWithPriceGreaterThanZero() {
        when(productRepository.findById(ID)).thenReturn(Optional.of(PRODUCT));

        productService.updateProductPrice(ID, 0.1);

        InOrder inOrder = inOrder(productRepository);
        inOrder.verify(productRepository).findById(ID);
        inOrder.verify(productRepository).addProduct(productCaptor.capture());
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
        Product capturedProduct = productCaptor.getValue();
        assertAll(
                () -> assertEquals(capturedProduct.getId(), PRODUCT.getId()),
                () -> assertEquals(capturedProduct.getName(), PRODUCT.getName()),
                () -> assertEquals(capturedProduct.getPrice(), 0.1)
                );

    }

    @Test
    void shouldGetEmptyListWhenNoProductsInRepository() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> actualAllProducts = productService.getAllProducts();

        assertThat(actualAllProducts)
                .isEmpty();
        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
    }

    @Test
    void shouldGetAListOfAllProductsInRepository() {
        when(productRepository.findAll()).thenReturn(List.of(PRODUCT, PRODUCT_2));

        List<Product> actualAllProducts = productService.getAllProducts();

        assertThat(actualAllProducts)
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(PRODUCT, PRODUCT_2);

        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productValidator);
    }
}