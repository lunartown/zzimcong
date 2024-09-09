package com.zzimcong.user.domain.mapper;

import com.zzimcong.user.application.dto.AddressCreateRequest;
import com.zzimcong.user.application.dto.AddressResponse;
import com.zzimcong.user.application.dto.AddressUpdateRequest;
import com.zzimcong.user.domain.entity.Address;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateEntityFromDto(AddressUpdateRequest request, @MappingTarget Address address);

    AddressResponse toDto(Address address);

    List<AddressResponse> toDtoList(List<Address> addresses);

    @AfterMapping
    default void setDefaults(@MappingTarget Address address) {
        if (address.getIsDefault() == null) {
            address.setIsDefault(false);
        }
    }
}