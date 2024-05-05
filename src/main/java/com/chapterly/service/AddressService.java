package com.chapterly.service;

import com.chapterly.dto.AddressDto;
import com.chapterly.dto.UserDto;
import com.chapterly.entity.Address;
import com.chapterly.entity.User;

import java.util.List;

public interface AddressService {
    public AddressDto savePermanentAddressOfUser(AddressDto address);

    public AddressDto getUserPermanentAddressById(Long addressId);

    public AddressDto updateUserPermanentAddressById(Long addressId, AddressDto addressDto);

    public void deleteUserPermanentAddressById(Long addressId);

    public void deleteUserPermanentAddressByUserId(Long userId);

    public List<AddressDto> getAllPermanentAddressesOfUsers();

    AddressDto getPersonalAddress();

    boolean updatePersonalAddress(AddressDto addressRequest);
}
