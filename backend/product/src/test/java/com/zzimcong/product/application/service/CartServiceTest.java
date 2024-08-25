//package com.zzimcong.product.application.service;
//
//import com.zzimcong.product.application.dto.CartItemDto;
//import com.zzimcong.product.common.exception.BadRequestException;
//import com.zzimcong.product.common.exception.ConflictException;
//import com.zzimcong.product.common.exception.NotFoundException;
//import com.zzimcong.product.domain.entity.CartItem;
//import com.zzimcong.product.domain.entity.Product;
//import com.zzimcong.product.domain.repository.CartRepository;
//import com.zzimcong.product.domain.repository.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//class CartServiceTest {
//
//    @Mock
//    private CartRepository cartRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private CartService cartService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetCartItemsForUser() {
//        CartItem cartItem1 = new CartItem(1L, new Product(), 2);
//        CartItem cartItem2 = new CartItem(1L, new Product(), 3);
//
//        when(cartRepository.findByUserId(1L)).thenReturn(Arrays.asList(cartItem1, cartItem2));
//
//        List<CartItemDto> result = cartService.getCartItemsForUser(1L);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//
//        verify(cartRepository, times(1)).findByUserId(1L);
//    }
//
//    @Test
//    void testAddProductToCart() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setAvailableQuantity(10);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
//        when(cartRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        cartService.addProductToCart(1L, 1L, 2);
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(cartRepository, times(1)).findByUserIdAndProductId(1L, 1L);
//        verify(cartRepository, times(1)).save(any(CartItem.class));
//        verify(productRepository, times(1)).save(product);
//    }
//
//    @Test
//    void testAddProductToCartExistingItem() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setAvailableQuantity(10);
//
//        CartItem existingCartItem = new CartItem(1L, product, 2);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(existingCartItem));
//        when(cartRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        cartService.addProductToCart(1L, 1L, 3);
//
//        assertEquals(5, existingCartItem.getCount());
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(cartRepository, times(1)).findByUserIdAndProductId(1L, 1L);
//        verify(cartRepository, times(1)).save(existingCartItem);
//        verify(productRepository, times(1)).save(product);
//    }
//
//    @Test
//    void testAddProductToCartInvalidQuantity() {
//        assertThrows(BadRequestException.class, () -> cartService.addProductToCart(1L, 1L, 0));
//
//        verify(productRepository, never()).findById(anyLong());
//        verify(cartRepository, never()).findByUserIdAndProductId(anyLong(), anyLong());
//        verify(cartRepository, never()).save(any(CartItem.class));
//    }
//
//    @Test
//    void testAddProductToCartInsufficientStock() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setAvailableQuantity(5);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        assertThrows(ConflictException.class, () -> cartService.addProductToCart(1L, 1L, 10));
//
//        verify(productRepository, times(1)).findById(1L);
//        verify(cartRepository, never()).findByUserIdAndProductId(anyLong(), anyLong());
//        verify(cartRepository, never()).save(any(CartItem.class));
//    }
//
//    @Test
//    void testUpdateProductCount() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setAvailableQuantity(10);
//
//        CartItem cartItem = new CartItem(1L, product, 2);
//
//        when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(cartItem));
//        when(cartRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        cartService.updateProductCount(1L, 1L, 5);
//
//        assertEquals(5, cartItem.getCount());
//
//        verify(cartRepository, times(1)).findByUserIdAndProductId(1L, 1L);
//        verify(cartRepository, times(1)).save(cartItem);
//    }
//
//    @Test
//    void testUpdateProductCountInvalidQuantity() {
//        assertThrows(BadRequestException.class, () -> cartService.updateProductCount(1L, 1L, 0));
//
//        verify(cartRepository, never()).findByUserIdAndProductId(anyLong(), anyLong());
//        verify(cartRepository, never()).save(any(CartItem.class));
//    }
//
//    @Test
//    void testUpdateProductCountInsufficientStock() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setAvailableQuantity(5);
//
//        CartItem cartItem = new CartItem(1L, product, 2);
//
//        when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(cartItem));
//
//        assertThrows(ConflictException.class, () -> cartService.updateProductCount(1L, 1L, 10));
//
//        verify(cartRepository, times(1)).findByUserIdAndProductId(1L, 1L);
//        verify(cartRepository, never()).save(any(CartItem.class));
//    }
//
//    @Test
//    void testDeleteProductFromCart() {
//        CartItem cartItem = new CartItem(1L, new Product(), 2);
//
//        when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(cartItem));
//
//        cartService.deleteProductFromCart(1L, 1L);
//
//        verify(cartRepository, times(1)).findByUserIdAndProductId(1L, 1L);
//        verify(cartRepository, times(1)).delete(cartItem);
//    }
//
//    @Test
//    void testDeleteProductFromCartNotFound() {
//        when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> cartService.deleteProductFromCart(1L, 1L));
//
//        verify(cartRepository, times(1)).findByUserIdAndProductId(1L, 1L);
//        verify(cartRepository, never()).delete(any(CartItem.class));
//    }
//}