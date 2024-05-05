package com.chapterly.serviceImpl;

import com.chapterly.dto.ShippingAddressDto;
import com.chapterly.entity.ShippingAddress;
import com.chapterly.mapper.ShippingAddressMapper;
import com.chapterly.repository.ShippingAddressRepo;
import com.chapterly.service.ShippingAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {
    @Autowired
    private ShippingAddressRepo shippingAddressRepo;
    @Autowired
    private ShippingAddressMapper shippingMapper;
    Logger logger = LoggerFactory.getLogger("ShippingAddressServiceImpl");



    @Override
    public ShippingAddressDto saveShippingAddress(ShippingAddressDto shippingDto) {
        ShippingAddressDto shipping = null;
        if(shippingDto != null){
            ShippingAddress address = new ShippingAddress();
            address.setUser(shippingDto.getUser());
            address.setCity(shippingDto.getCity());
            address.setState(shippingDto.getState());
            address.setStreet(shippingDto.getStreet());
            address.setCountry(shippingDto.getCountry());
            address.setAddress(shippingDto.getHouseNo());
            address.setPinCode(shippingDto.getPinCode());
            try {
                ShippingAddress savedAddress = shippingAddressRepo.save(address);
                shipping = shippingMapper.toDto(savedAddress);
            }catch (Exception e){
                logger.error("ERROR", e);
            }
        }
        return shipping;
    }

    @Override
    public ShippingAddressDto getShippingAddressById(Long shippingAddressId) {
        if(shippingAddressId != null){
            return shippingMapper.toDto(shippingAddressRepo.findById(shippingAddressId).get());
        }
        return null;
    }

    @Override
    public ShippingAddressDto updateShippingAddress(Long shippingAddressId, ShippingAddressDto shippingAddressDto) {
        ShippingAddressDto shipping = null;
        if(shippingAddressDto != null){

            ShippingAddress shippingAddress = shippingAddressRepo.findById(shippingAddressId).get();

            shippingAddress.setUser(shippingAddressDto.getUser());
            shippingAddress.setCity(shippingAddressDto.getCity());
            shippingAddress.setState(shippingAddressDto.getState());
            shippingAddress.setStreet(shippingAddressDto.getStreet());
            shippingAddress.setCountry(shippingAddressDto.getCountry());
            shippingAddress.setAddress(shippingAddressDto.getHouseNo());
            shippingAddress.setPinCode(shippingAddressDto.getPinCode());
            try {
                ShippingAddress savedAddress = shippingAddressRepo.save(shippingAddress);
                shipping = shippingMapper.toDto(savedAddress);
            }catch (Exception e){
                logger.error("ERROR", e);
            }
        }
        return shipping;
    }

    @Override
    public boolean deleteShippingAddressById(Long shippingAddressId) {
        if(shippingAddressId != null){
            ShippingAddress shippingAddress = shippingAddressRepo.findById(shippingAddressId).get();
            if(shippingAddress != null){
                shippingAddressRepo.deleteById(shippingAddressId);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteShippingAddressByUserId(Long userId) {
        if(userId != null){
            ShippingAddress shippingAddress = shippingAddressRepo.findByUserId(userId);
            if(shippingAddress != null){
                shippingAddressRepo.deleteByUserId(userId);
                return true;
            }
            return false;
        }
        return false;
    }
}
