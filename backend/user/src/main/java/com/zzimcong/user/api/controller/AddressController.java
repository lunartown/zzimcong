package com.zzimcong.user.api.controller;

import com.zzimcong.user.api.response.ApiResponse;
import com.zzimcong.user.application.dto.AddressCreateRequest;
import com.zzimcong.user.application.dto.AddressResponse;
import com.zzimcong.user.application.dto.AddressUpdateRequest;
import com.zzimcong.user.application.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "ADDRESS_CONTROLLER")
@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @RequestHeader("X-AUTH-USER-ID") Long userId,
            @RequestBody AddressCreateRequest request) {
        log.info("Creating new address for user: {}", userId);
        AddressResponse createdAddress = addressService.createAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdAddress, "주소가 성공적으로 생성되었습니다."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getUserAddresses(
            @RequestHeader("X-AUTH-USER-ID") Long userId) {
        log.info("Fetching all addresses for user with id: {}", userId);
        List<AddressResponse> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(ApiResponse.success(addresses, "사용자의 모든 주소 조회에 성공하였습니다."));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddress(
            @RequestHeader("X-AUTH-USER-ID") Long userId,
            @PathVariable Long id) {
        log.info("Fetching address with id: {} for user: {}", id, userId);
        AddressResponse address = addressService.getAddress(userId, id);
        return ResponseEntity.ok(ApiResponse.success(address, "주소 조회에 성공하였습니다."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @RequestHeader("X-AUTH-USER-ID") Long userId,
            @PathVariable Long id,
            @RequestBody AddressUpdateRequest request) {
        log.info("Updating address with id: {} for user: {}", id, userId);
        AddressResponse updatedAddress = addressService.updateAddress(userId, id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedAddress, "주소가 성공적으로 업데이트되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @RequestHeader("X-AUTH-USER-ID") Long userId,
            @PathVariable Long id) {
        log.info("Deleting address with id: {} for user: {}", id, userId);
        addressService.deleteAddress(userId, id);
        return ResponseEntity.ok(ApiResponse.success(null, "주소가 성공적으로 삭제되었습니다."));
    }

    @PutMapping("/{id}/set-default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @RequestHeader("X-AUTH-USER-ID") Long userId,
            @PathVariable Long id) {
        log.info("Setting address with id: {} as default for user: {}", id, userId);
        AddressResponse updatedAddress = addressService.setDefaultAddress(userId, id);
        return ResponseEntity.ok(ApiResponse.success(updatedAddress, "기본 주소가 성공적으로 설정되었습니다."));
    }

    @GetMapping("/default")
    public ResponseEntity<ApiResponse<AddressResponse>> getDefaultAddress(
            @RequestHeader("X-AUTH-USER-ID") Long userId) {
        log.info("Fetching default address for user with id: {}", userId);
        AddressResponse address = addressService.getDefaultAddress(userId);
        return ResponseEntity.ok(ApiResponse.success(address, "기본 주소 조회에 성공하였습니다."));
    }
}