//package com.zzimcong.order.application.service;
//
//import com.zzimcong.order.api.client.ProductServiceClient;
//import com.zzimcong.order.application.dto.ReleaseInventoryRequest;
//import com.zzimcong.order.application.dto.ReserveInventoryRequest;
//import com.zzimcong.order.application.dto.ReserveInventoryResponse;
//import feign.FeignException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class ProductServiceTest {
//
//    @Mock
//    private ProductServiceClient productServiceClient;
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
//    void testReserveInventorySuccess() {
//        when(productServiceClient.reserveInventory(anyLong(), any(ReserveInventoryRequest.class)))
//                .thenReturn(new ReserveInventoryResponse(true, "Success"));
//
//        boolean result = productService.reserveInventory(1L, 5);
//
//        assertTrue(result);
//        verify(productServiceClient).reserveInventory(eq(1L), any(ReserveInventoryRequest.class));
//    }
//
//    @Test
//    void testReserveInventoryFailure() {
//        when(productServiceClient.reserveInventory(anyLong(), any(ReserveInventoryRequest.class)))
//                .thenReturn(new ReserveInventoryResponse(false, "Failed"));
//
//        boolean result = productService.reserveInventory(1L, 5);
//
//        assertFalse(result);
//        verify(productServiceClient).reserveInventory(eq(1L), any(ReserveInventoryRequest.class));
//    }
//
//    @Test
//    void testReserveInventoryException() {
//        when(productServiceClient.reserveInventory(anyLong(), any(ReserveInventoryRequest.class)))
//                .thenThrow(FeignException.class);
//
//        assertThrows(RuntimeException.class, () -> productService.reserveInventory(1L, 5));
//    }
//
//    @Test
//    void testReleaseInventory() {
//        doNothing().when(productServiceClient).releaseInventory(anyLong(), any(ReleaseInventoryRequest.class));
//
//        assertDoesNotThrow(() -> productService.releaseInventory(1L, 5));
//        verify(productServiceClient).releaseInventory(eq(1L), any(ReleaseInventoryRequest.class));
//    }
//}