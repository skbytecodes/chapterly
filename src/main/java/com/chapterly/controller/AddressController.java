package com.chapterly.controller;

import com.chapterly.dto.AddressDto;
import com.chapterly.dto.UserDto;
import com.chapterly.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class AddressController {
    @Autowired
    private AddressService addressService;
    Logger logger = LoggerFactory.getLogger("AddressController");


    @PostMapping("/saveAddress")
    public ResponseEntity<Object> addAddress(AddressDto addressRequest) {
        try {
            AddressDto addressResponse = addressService.savePermanentAddressOfUser(addressRequest);
            if (addressResponse != null) {
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception e) {
            logger.error("ERROR",e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/addressById/{id}")
    public ResponseEntity<AddressDto> getUserAddressById(@PathVariable("id") Long addressId){
        try {
            AddressDto userPermanentAddressById = addressService.getUserPermanentAddressById(addressId);
            if(userPermanentAddressById != null)
                return new ResponseEntity<>(userPermanentAddressById, HttpStatus.OK);
            else
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/updateAddressById/{id}")
    public ResponseEntity<Object> updateAddressById(@PathVariable("id") Long addressId, AddressDto addressRequest){
        try {
            addressService.updateUserPermanentAddressById(addressId, addressRequest);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAddressById/{id}")
    public ResponseEntity<Object> deleteAddressById(@PathVariable("id") Long addressId){
        try {
            addressService.deleteUserPermanentAddressById(addressId);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAddressByUserId/{id}")
    public ResponseEntity<Object> deleteAddressByUserId(@PathVariable("id") Long addressId){
        try {
            addressService.deleteUserPermanentAddressById(addressId);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/address/personalAddress")
    public ResponseEntity<?> personalAddress(){
        AddressDto address = null;
        try {
            address = addressService.getPersonalAddress();
        }catch (IllegalStateException e){
            return new ResponseEntity<>("USER NOT FOUND", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(address != null)
            return new ResponseEntity<>(address, HttpStatus.OK);
        else
            return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
    }




    @PutMapping("/address/updatePersonalAddress")
    public ResponseEntity<?> updatePersonalAddress(@RequestBody AddressDto addressRequest){
        if(addressRequest != null){
            boolean response = false;
            try {
                response = addressService.updatePersonalAddress(addressRequest);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (IllegalStateException e){
                return new ResponseEntity<>("ADDRESS NOT FOUND", HttpStatus.NOT_FOUND);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("BAD REQUEST", HttpStatus.NOT_FOUND);
    }
}
