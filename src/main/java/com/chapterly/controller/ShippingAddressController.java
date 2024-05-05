package com.chapterly.controller;


import com.chapterly.dto.BookDto;
import com.chapterly.dto.ShippingAddressDto;
import com.chapterly.service.ShippingAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class ShippingAddressController {
    @Autowired
    private ShippingAddressService shippingService;
    Logger logger = LoggerFactory.getLogger("ShippingAddressController");



    @PostMapping("/saveUserShippingAddress")
    public ResponseEntity<Object> addNewBook(ShippingAddressDto shippingAddressDto) {
        try {
            ShippingAddressDto shippingAddress = shippingService.saveShippingAddress(shippingAddressDto);
            if (shippingAddress != null)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/shippingAddress/{id}")
    public ResponseEntity<?> getShippingAddress(@PathVariable("id") Long shippingId) {
        if (shippingId != null) {
            ShippingAddressDto shippingAddress = null;
            try {
                shippingAddress = shippingService.getShippingAddressById(shippingId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(shippingAddress, HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @PutMapping("/shippingAddress/{id}")
    public ResponseEntity<?> updateShipping(@PathVariable("id") Long shippingId, ShippingAddressDto shippingRequest) {
        if (shippingId != null) {
            ShippingAddressDto shippingAddress = null;
            try {
                shippingAddress = shippingService.updateShippingAddress(shippingId, shippingRequest);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(shippingAddress, HttpStatus.OK);

        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @DeleteMapping("/shippingAddress/{id}")
    public ResponseEntity<?> deleteShippingById(@PathVariable("id") Long shippingId) {
        if (shippingId != null) {
            boolean res = false;
            try {
                res = shippingService.deleteShippingAddressById(shippingId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (res)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("FAILED", HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/shippingAddressByUser/{id}")
    public ResponseEntity<?> deleteShippingByUserId(@PathVariable("id") Long userId) {
        if (userId != null) {
            boolean res = false;
            try {
                res = shippingService.deleteShippingAddressByUserId(userId);
            } catch (Exception e) {
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (res)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("FAILED", HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
