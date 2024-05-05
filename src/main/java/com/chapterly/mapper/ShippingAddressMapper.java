package com.chapterly.mapper;


import com.chapterly.dto.ShippingAddressDto;
import com.chapterly.entity.ShippingAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingAddressMapper {
    @Mapping(target = "shippingAddressId", source = "shippingAddress.shippingAddressId")
//    @Mapping(target = "houseNo", source = "shippingAddress.houseNo")
    @Mapping(target = "street", source = "shippingAddress.street")
    @Mapping(target = "city", source = "shippingAddress.city")
    @Mapping(target = "state", source = "shippingAddress.state")
    @Mapping(target = "country", source = "shippingAddress.country")
    @Mapping(target = "pinCode", source = "shippingAddress.pinCode")
    @Mapping(target = "user", source = "shippingAddress.user")
    ShippingAddressDto toDto(ShippingAddress shippingAddress);
}
