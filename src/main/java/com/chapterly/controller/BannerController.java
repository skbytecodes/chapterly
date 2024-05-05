package com.chapterly.controller;

import com.chapterly.dto.BannerDto;
import com.chapterly.service.BannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    Logger logger = LoggerFactory.getLogger("BannerController");

    @PostMapping("/addBanner")
    public ResponseEntity<Object> addBanner(@Param("file")MultipartFile file, String data){
        try {
            BannerDto bannerDto = bannerService.saveBanner(file, data);
            return new ResponseEntity<>(bannerDto,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/banner/{id}")
    public ResponseEntity<Object> getBannerById(@PathVariable("id") Long bannerId){
        try {
            BannerDto bannerDto = bannerService.getBannerById(bannerId);
            return new ResponseEntity<>(bannerDto,HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/banner/{id}")
    public ResponseEntity<Object> deleteBannerById(@PathVariable("id") Long bannerId){
        try {
            boolean res = bannerService.deleteBannerById(bannerId);
            if(res)
                return new ResponseEntity<>("SUCCESSFUL",HttpStatus.OK);
            return new ResponseEntity<>("FAILED",HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
