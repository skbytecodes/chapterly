package com.chapterly.controller;

import com.chapterly.dto.CategoryDto;
import com.chapterly.entity.Category;
import com.chapterly.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    Logger logger = LoggerFactory.getLogger("CategoryController");

    @PostMapping("/addNewCategory")
    public ResponseEntity<Object> addNewCategory(@RequestParam("file") MultipartFile file, @RequestParam("data") @Valid String data) {
        try {
            categoryService.addCategory(file, data);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error ", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/categoryById/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable("id") Long categoryId) {
        try {
            CategoryDto categoryById = categoryService.getCategoryById(categoryId);
            if (categoryById != null) {
                return new ResponseEntity<>(categoryById, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("ERROR ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable("id") Long categoryId, @RequestBody CategoryDto categoryRequest) {
        try {
            categoryService.updateCategoryById(categoryId, categoryRequest);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR ", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteCategoryById/{id}")
    public ResponseEntity<Object> deleteCategoryById(@PathVariable("id") Long categoryId) {
        try {
            categoryService.deleteCategoryByCategoryId(categoryId);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR ", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getAllCategories() {
        try {
            List<CategoryDto> categories = categoryService.getAllCategories();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR ", e);
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categoryKey/{key}")
    public ResponseEntity<Object> category(@PathVariable("key") String key){
       try {
           CategoryDto categoryByKey = null;
            if(key != null && key.length() > 0){
                categoryByKey = categoryService.getCategoryByKey(key);
            }
            return new ResponseEntity<>(categoryByKey,HttpStatus.OK);
       }catch (Exception e){
           return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

}
