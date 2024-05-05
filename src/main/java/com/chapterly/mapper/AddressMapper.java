package com.chapterly.mapper;

import com.chapterly.dto.AddressDto;
import com.chapterly.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "addressId", source = "address.addressId")
    @Mapping(target = "houseNo", source = "address.houseNo")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "state", source = "address.state")
    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "pinCode", source = "address.pinCode")
    @Mapping(target = "user", source = "address.user")
    AddressDto toDto(Address address);
}
