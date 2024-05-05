package com.chapterly.service;

import com.chapterly.dto.ShippingAddressDto;
import com.chapterly.entity.ShippingAddress;
import org.springframework.aot.hint.*;

public interface ShippingAddressService {
    public ShippingAddressDto saveShippingAddress(ShippingAddressDto shippingAddressDto);
    public ShippingAddressDto getShippingAddressById(Long shippingAddressId);
    public ShippingAddressDto updateShippingAddress(Long shippingAddressId, ShippingAddressDto shippingAddressDto);
    public boolean deleteShippingAddressById(Long shippingAddressId);
    public boolean deleteShippingAddressByUserId(Long userId);
}
