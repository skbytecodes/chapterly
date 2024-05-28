package com.chapterly.serviceImpl;

import com.chapterly.dto.AddressDto;
import com.chapterly.entity.Address;
import com.chapterly.entity.User;
import com.chapterly.mapper.AddressMapper;
import com.chapterly.repository.AddressRepo;
import com.chapterly.repository.UserRepo;
import com.chapterly.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepo userRepo;

    Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Override
    public AddressDto savePermanentAddressOfUser(AddressDto addressRequest) {
        Address address = new Address();
        if (addressRequest != null) {
            address.setHouseNo(addressRequest.getHouseNo());
            address.setStreet(addressRequest.getStreet());
            address.setCity(addressRequest.getCity());
            address.setState(addressRequest.getState());
            address.setCountry(addressRequest.getCountry());
            address.setPinCode(addressRequest.getPinCode());
            address.setUser(addressRequest.getUser());
            addressRepo.save(address);
        }
        return addressRequest;
    }

    @Override
    public Address saveAddress(Address address) {
        try {
            return addressRepo.save(address);
        }catch (Exception e) {
            logger.error("Error saving address"+e.getMessage());
            throw new RuntimeException("SOMETHING WENT WRONG");
        }
    }

    @Override
    public AddressDto getUserPermanentAddressById(Long addressId) {
        if (addressId != null) {
            Address address = addressRepo.findById(addressId).get();
            return addressMapper.toDto(address);
        } else
            return null;
    }

    @Override
    public AddressDto updateUserPermanentAddressById(Long addressId, AddressDto addressRequest) {
        if (addressRequest != null) {
            Address address = addressRepo.findById(addressId).get();
            address.setStreet(addressRequest.getStreet());
            address.setCity(addressRequest.getCity());
            address.setState(addressRequest.getState());
            address.setCountry(addressRequest.getState());
            address.setHouseNo(addressRequest.getHouseNo());
            address.setPinCode(addressRequest.getPinCode());
            addressRepo.save(address);
            return addressRequest;
        } else
            return null;
    }

    @Override
    public void deleteUserPermanentAddressById(Long addressId) {
        Address address = addressRepo.findById(addressId).get();
        if (address != null) {
            try {
                addressRepo.deleteById(addressId);
            }catch (EmptyResultDataAccessException e) {
                logger.warn("No record found for user ID: " + addressId);
            }
        }
    }

    @Override
    public void deleteUserPermanentAddressByUserId(Long userId) {
        Address address = addressRepo.findById(userId).get();
        if (address != null) {
            addressRepo.deleteById(userId);
        }
    }

    @Override
    public List<AddressDto> getAllPermanentAddressesOfUsers() {
        List<Address> all = addressRepo.findAll();
        List<AddressDto> allAddresses = all.stream().map(obj -> {
            return addressMapper.toDto(obj);
        }).collect(Collectors.toList());

        return allAddresses;
    }

    @Override
    public AddressDto getPersonalAddress() {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }
        if(username != null && username.length() > 0){
            Optional<User> user = userRepo.findByEmail(username);
            if(user.isPresent()){
                Address address = addressRepo.findById(user.get().getAddress().getAddressId()).orElseThrow(() -> new IllegalStateException("ADDRESS NOT FOUND"));
                AddressDto addressDto = new AddressDto();
                addressDto.setHouseNo(address.getHouseNo());
                addressDto.setStreet(address.getStreet());
                addressDto.setCity(address.getCity());
                addressDto.setState(address.getState());
                addressDto.setCountry(address.getCountry());
                addressDto.setPinCode(address.getPinCode());
                return addressDto;
            }
            return null;
        }
        return null;
    }

    @Override
    public boolean updatePersonalAddress(AddressDto addressRequest) {
        String username = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                username = authentication.getName();
            }
            Optional<User> byEmail = userRepo.findByEmail(username);
            boolean present = byEmail.isPresent();
            if(present){
                User user = byEmail.get();
                Address address = addressRepo.findById(user.getAddress().getAddressId()).orElseThrow(() -> new IllegalStateException("ADDRESS NOT FOUND"));
                address.setHouseNo(addressRequest.getHouseNo());
                address.setStreet(addressRequest.getStreet());
                address.setCity(addressRequest.getCity());
                address.setState(addressRequest.getState());
                address.setCountry(addressRequest.getCountry());
                address.setPinCode(addressRequest.getPinCode());
                addressRepo.save(address);
                return true;
            }else
                return false;
        }catch (Exception e){
            return false;
        }
    }
}
