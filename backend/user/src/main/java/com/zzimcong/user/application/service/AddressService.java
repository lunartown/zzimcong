package com.zzimcong.user.application.service;

import com.zzimcong.user.application.dto.AddressCreateRequest;
import com.zzimcong.user.application.dto.AddressResponse;
import com.zzimcong.user.application.dto.AddressUpdateRequest;
import com.zzimcong.user.common.exception.ErrorCode;
import com.zzimcong.user.common.exception.NotFoundException;
import com.zzimcong.user.domain.entity.Address;
import com.zzimcong.user.domain.mapper.AddressMapper;
import com.zzimcong.user.domain.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    public AddressService(AddressMapper addressMapper, AddressRepository addressRepository) {
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
    }

    public AddressResponse createAddress(Long userId, AddressCreateRequest request) {
        Address address = addressMapper.toEntity(request);
        address.setUserId(userId);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    public AddressResponse getAddress(Long userId, Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));
        if (!address.getUserId().equals(userId)) {
            throw new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        return addressMapper.toDto(address);
    }

    public List<AddressResponse> getUserAddresses(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        return addressMapper.toDtoList(addresses);
    }

    public AddressResponse updateAddress(Long userId, Long id, AddressUpdateRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주소를 찾을 수 없습니다."));
        if (!address.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 주소에 접근할 수 없습니다.");
        }
        addressMapper.updateEntityFromDto(request, address);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    public void deleteAddress(Long userId, Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주소를 찾을 수 없습니다."));
        if (!address.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 주소에 접근할 수 없습니다.");
        }
        addressRepository.delete(address);
    }

    @Transactional
    public AddressResponse setDefaultAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));

        addressRepository.resetDefaultForUser(userId);

        address.setIsDefault(true);
        Address savedAddress = addressRepository.save(address);

        return addressMapper.toDto(savedAddress);
    }

    public AddressResponse getDefaultAddress(Long userId) {
        Address address = addressRepository.findAllByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));

        return addressMapper.toDto(address);
    }
}
