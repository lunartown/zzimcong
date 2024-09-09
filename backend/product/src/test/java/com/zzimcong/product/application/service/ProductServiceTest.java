//package com.zzimcong.product.application.service;
//
//import com.zzimcong.product.application.dto.*;
//import com.zzimcong.product.domain.entity.Category;
//import com.zzimcong.product.domain.entity.Product;
//import com.zzimcong.product.domain.repository.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class ProductServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private CategoryService categoryService;
//
//    @InjectMocks
//    private ProductService productService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateProduct() {
//        ProductRequestDto requestDto = new ProductRequestDto();
//        requestDto.setCategoryId(1L);
//        requestDto.setName("Test Product");
//        requestDto.setPrice(100);
//        requestDto.setSale(10);
//        requestDto.setContent("Test Content");
//        requestDto.setImage("test.jpg");
//        requestDto.setAvailableQuantity(50);
//
//        Category category = new Category();
//        category.setId(1L);
//
//        Product product = new Product(requestDto, category);
//        product.setId(1L);
//
//        when(categoryService.getCategoryById(1L)).thenReturn(category);
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        ProductResponseDto responseDto = productService.createProduct(requestDto);
//
//        assertNotNull(responseDto);
//        assertEquals(1L, responseDto.getProductId());
//        assertEquals("Test Product", responseDto.getName());
//        assertEquals(100, responseDto.getPrice());
//        assertEquals(10, responseDto.getSale());
//        assertEquals("Test Content", responseDto.getContent());
//        assertEquals("test.jpg", responseDto.getImage());
//        assertEquals(50, responseDto.getAvailableQuantity());
//
//        verify(categoryService, times(1)).getCategoryById(1L);
//        verify(productRepository, times(1)).save(any(Product.class));
//    }
//
//    @Test
//    void testGetProductById() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setName("Test Product");
//        product.setPrice(100);
//        product.setSale(10);
//        product.setContent("Test Content");
//        product.setImage("test.jpg");
//        product.setAvailableQuantity(50);
//
//        Category category = new Category();
//        category.setId(1L);
//        product.setCategory(category);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        ProductResponseDto responseDto = productService.getProductById(1L);
//
//        assertNotNull(responseDto);
//        assertEquals(1L, responseDto.getProductId());
//        assertEquals("Test Product", responseDto.getName());
//        assertEquals(100, responseDto.getPrice());
//        assertEquals(10, responseDto.getSale());
//        assertEquals("Test Content", responseDto.getContent());
//        assertEquals("test.jpg", responseDto.getImage());
//        assertEquals(50, responseDto.getAvailableQuantity());
//
//        verify(productRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetProductByIdNotFound() {
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertNull(productService.getProductById(1L));
//
//        verify(productRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testUpdateProduct() {
//        ProductRequestDto requestDto = new ProductRequestDto();
//        requestDto.setCategoryId(2L);
//        requestDto.setName("Updated Product");
//        requestDto.setPrice(200);
//        requestDto.setSale(20);
//        requestDto.setContent("Updated Content");
//        requestDto.setImage("updated.jpg");
//        requestDto.setAvailableQuantity(100);
//
//        Product existingProduct = new Product();
//        existingProduct.setId(1L);
//        existingProduct.setName("Test Product");
//        existingProduct.setPrice(100);
//        existingProduct.setSale(10);
//        existingProduct.setContent("Test Content");
//        existingProduct.setImage("test.jpg");
//        existingProduct.setAvailableQuantity(50);
//
//        Category category = new Category();
//        category.setId(2L);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
//        when(categoryService.getCategoryById(2L)).thenReturn(category);
//        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        ProductResponseDto responseDto = productService.updateProduct(1L, requestDto);
//
//        assertNotNull(responseDto);
//        assertEquals(1L, responseDto.getProductId());
//        assertEquals("Updated Product", responseDto.getName());
//        assertEquals(200, responseDto.getPrice());
//        assertEquals(20, responseDto.getSale());
//        assertEquals("Updated Content", responseDto.getContent());
//        assertEquals("updated.jpg", responseDto.getImage());
//        assertEquals(100, responseDto.getAvailableQuantity());
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(categoryService, times(1)).getCategoryById(2L);
//        verify(productRepository, times(1)).save(any(Product.class));
//    }
//
//    @Test
//    void testUpdateProductNotFound() {
//        ProductRequestDto requestDto = new ProductRequestDto();
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertNull(productService.updateProduct(1L, requestDto));
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(productRepository, never()).save(any(Product.class));
//    }
//
//    @Test
//    void testGetProducts() {
//        Product product1 = new Product();
//        product1.setId(1L);
//        product1.setName("Product 1");
//
//        Product product2 = new Product();
//        product2.setId(2L);
//        product2.setName("Product 2");
//
//        List<Product> productList = Arrays.asList(product1, product2);
//        Page<Product> productPage = new PageImpl<>(productList);
//
//        when(productRepository.findAllWithFilters(anyString(), anyList(), any(Pageable.class))).thenReturn(productPage);
//
//        List<ProductResponseDto> result = productService.getProducts(0, 10, "search", 1L);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals("Product 1", result.get(0).getName());
//        assertEquals("Product 2", result.get(1).getName());
//
//        verify(productRepository, times(1)).findAllWithFilters(anyString(), anyList(), any(Pageable.class));
//    }
//
//    @Test
//    void testDeleteProduct() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setDeleted(false);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        productService.deleteProduct(1L);
//
//        assertTrue(product.isDeleted());
//        verify(productRepository, times(1)).findById(1L);
//        verify(productRepository, times(1)).save(product);
//    }
//
//    @Test
//    void testReserveInventory() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setAvailableQuantity(10);
//        product.setReservedQuantity(0);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        ReserveInventoryRequest request = new ReserveInventoryRequest();
//        request.setQuantity(5);
//
//        ReserveInventoryResponse response = productService.reserveInventory(1L, request);
//
//        assertTrue(response.isSuccess());
//        assertEquals("재고 예약 성공", response.getMessage());
//        assertEquals(5, product.getAvailableQuantity());
//        assertEquals(5, product.getReservedQuantity());
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(productRepository, times(1)).save(product);
//    }
//
//    @Test
//    void testReserveInventoryInsufficientStock() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setAvailableQuantity(3);
//        product.setReservedQuantity(0);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        ReserveInventoryRequest request = new ReserveInventoryRequest();
//        request.setQuantity(5);
//
//        ReserveInventoryResponse response = productService.reserveInventory(1L, request);
//
//        assertFalse(response.isSuccess());
//        assertEquals("가용 재고가 부족합니다.", response.getMessage());
//        assertEquals(3, product.getAvailableQuantity());
//        assertEquals(0, product.getReservedQuantity());
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(productRepository, never()).save(any(Product.class));
//    }
//
//    @Test
//    void testReleaseInventory() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setAvailableQuantity(5);
//        product.setReservedQuantity(5);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        ReleaseInventoryRequest request = new ReleaseInventoryRequest();
//        request.setQuantity(3);
//
//        productService.releaseInventory(1L, request);
//
//        assertEquals(8, product.getAvailableQuantity());
//        assertEquals(2, product.getReservedQuantity());
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(productRepository, times(1)).save(product);
//    }
//
//    @Test
//    void testReleaseInventoryInsufficientReservedStock() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setAvailableQuantity(5);
//        product.setReservedQuantity(2);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        ReleaseInventoryRequest request = new ReleaseInventoryRequest();
//        request.setQuantity(3);
//
//        assertThrows(IllegalStateException.class, () -> productService.releaseInventory(1L, request));
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(productRepository, never()).save(any(Product.class));
//    }
//}