//package com.zzimcong.user.application.service;
//
//import com.zzimcong.user.application.dto.AddressCreateRequest;
//import com.zzimcong.user.application.dto.AddressResponse;
//import com.zzimcong.user.application.dto.AddressUpdateRequest;
//import com.zzimcong.user.common.exception.NotFoundException;
//import com.zzimcong.user.domain.entity.Address;
//import com.zzimcong.user.domain.mapper.AddressMapper;
//import com.zzimcong.user.domain.repository.AddressRepository;
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
//import static org.mockito.Mockito.*;
//
//class AddressServiceTest {
//
//    @Mock
//    private AddressRepository addressRepository;
//
//    @Mock
//    private AddressMapper addressMapper;
//
//    @InjectMocks
//    private AddressService addressService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateAddress() {
//        AddressCreateRequest request = new AddressCreateRequest("John Doe", "123 Main St", "Apt 4", "12345", "1234567890", "Leave at door", true);
//        Address address = new Address();
//        address.setId(1L);
//
//        when(addressMapper.toEntity(request)).thenReturn(address);
//        when(addressRepository.save(address)).thenReturn(address);
//        when(addressMapper.toDto(address)).thenReturn(new AddressResponse(1L, "John Doe", "123 Main St", "Apt 4", "12345", "1234567890", "Leave at door", true));
//
//        AddressResponse result = addressService.createAddress(1L, request);
//
//        assertNotNull(result);
//        assertEquals(1L, result.id());
//        assertEquals("John Doe", result.name());
//
//        verify(addressMapper).toEntity(request);
//        verify(addressRepository).save(address);
//        verify(addressMapper).toDto(address);
//    }
//
//    @Test
//    void testGetAddress() {
//        Address address = new Address();
//        address.setId(1L);
//        address.setUserId(1L);
//
//        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
//        when(addressMapper.toDto(address)).thenReturn(new AddressResponse(1L, "John Doe", "123 Main St", "Apt 4", "12345", "1234567890", "Leave at door", true));
//
//        AddressResponse result = addressService.getAddress(1L, 1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.id());
//
//        verify(addressRepository).findById(1L);
//        verify(addressMapper).toDto(address);
//    }
//
//    @Test
//    void testGetAddressNotFound() {
//        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> addressService.getAddress(1L, 1L));
//
//        verify(addressRepository).findById(1L);
//    }
//
//    @Test
//    void testGetUserAddresses() {
//        Address address1 = new Address();
//        address1.setId(1L);
//        Address address2 = new Address();
//        address2.setId(2L);
//        List<Address> addresses = Arrays.asList(address1, address2);
//
//        when(addressRepository.findAllByUserId(1L)).thenReturn(addresses);
//        when(addressMapper.toDtoList(addresses)).thenReturn(Arrays.asList(
//                new AddressResponse(1L, "John Doe", "123 Main St", "Apt 4", "12345", "1234567890", "Leave at door", true),
//                new AddressResponse(2L, "Jane Doe", "456 Elm St", "Suite 2", "67890", "9876543210", "Ring bell", false)
//        ));
//
//        List<AddressResponse> result = addressService.getUserAddresses(1L);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//
//        verify(addressRepository).findAllByUserId(1L);
//        verify(addressMapper).toDtoList(addresses);
//    }
//
//    @Test
//    void testUpdateAddress() {
//        // 테스트 데이터 준비
//        Long userId = 1L;
//        Long addressId = 1L;
//        AddressUpdateRequest request = new AddressUpdateRequest("John Doe Updated", "456 Elm St", "Suite 2", "67890", "9876543210", "Ring bell", false);
//
//        // 기존 주소 객체 생성
//        Address existingAddress = new Address();
//        existingAddress.setId(addressId);
//        existingAddress.setUserId(userId);
//
//        // Mock 객체 설정
//        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
//
//        // updateEntityFromDto 메소드 동작 설정
//        doAnswer(invocation -> {
//            Address address = invocation.getArgument(1);
//            address.setName(request.name());
//            address.setStreetAddress(request.streetAddress());
//            // 나머지 필드들도 설정...
//            return null;
//        }).when(addressMapper).updateEntityFromDto(eq(request), any(Address.class));
//
//        // toDto 메소드 동작 설정
//        when(addressMapper.toDto(any(Address.class))).thenAnswer(invocation -> {
//            Address address = invocation.getArgument(0);
//            return new AddressResponse(
//                    address.getId(),
//                    address.getName(),
//                    address.getStreetAddress(),
//                    address.getAddressDetail(),
//                    address.getZipcode(),
//                    address.getPhone(),
//                    address.getMessage(),
//                    address.getIsDefault()
//            );
//        });
//
//        // 테스트 실행
//        AddressResponse result = addressService.updateAddress(userId, addressId, request);
//
//        // 결과 검증
//        assertNotNull(result, "결과가 null이면 안됩니다.");
//        assertEquals("John Doe Updated", result.name(), "이름이 올바르게 업데이트되어야 합니다.");
//        assertEquals("456 Elm St", result.streetAddress(), "주소가 올바르게 업데이트되어야 합니다.");
//
//        // Mock 객체 호출 검증
//        verify(addressRepository).findById(addressId);
//        verify(addressMapper).updateEntityFromDto(eq(request), any(Address.class));
//        verify(addressRepository).save(existingAddress);
//        verify(addressMapper).toDto(existingAddress);
//    }
//
//    @Test
//    void testDeleteAddress() {
//        Address address = new Address();
//        address.setId(1L);
//        address.setUserId(1L);
//
//        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
//
//        addressService.deleteAddress(1L, 1L);
//
//        verify(addressRepository).findById(1L);
//        verify(addressRepository).delete(address);
//    }
//
//    @Test
//    void testSetDefaultAddress() {
//        Address address = new Address();
//        address.setId(1L);
//        address.setUserId(1L);
//
//        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
//        when(addressMapper.toDto(address)).thenReturn(new AddressResponse(1L, "John Doe", "123 Main St", "Apt 4", "12345", "1234567890", "Leave at door", true));
//
//        AddressResponse result = addressService.setDefaultAddress(1L, 1L);
//
//        assertNotNull(result);
//        assertTrue(result.isDefault());
//
//        verify(addressRepository).findById(1L);
//        verify(addressRepository).resetDefaultForUser(1L);
//        verify(addressRepository).save(address);
//        verify(addressMapper).toDto(address);
//    }
//
//    @Test
//    void testGetDefaultAddress() {
//        Address address = new Address();
//        address.setId(1L);
//        address.setUserId(1L);
//        address.setIsDefault(true);
//
//        when(addressRepository.findAllByUserIdAndIsDefaultTrue(1L)).thenReturn(Optional.of(address));
//        when(addressMapper.toDto(address)).thenReturn(new AddressResponse(1L, "John Doe", "123 Main St", "Apt 4", "12345", "1234567890", "Leave at door", true));
//
//        AddressResponse result = addressService.getDefaultAddress(1L);
//
//        assertNotNull(result);
//        assertTrue(result.isDefault());
//
//        verify(addressRepository).findAllByUserIdAndIsDefaultTrue(1L);
//        verify(addressMapper).toDto(address);
//    }
//}