package com.chapterly.serviceImpl;

import com.chapterly.aws.AmazonClient;
import com.chapterly.dto.CategoryDto;
import com.chapterly.entity.Category;
import com.chapterly.mapper.CategoryMapper;
import com.chapterly.repository.CategoryRepo;
import com.chapterly.service.CategoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private AmazonClient amazonClient;
    @Autowired
    private CategoryRepo categoryRepo;

    @Value("${s3.endpointUrl}")
    private String s3Endpoint;

    @Autowired
    private CategoryMapper categoryMapper;
    Logger logger = LoggerFactory.getLogger("CategoryServiceImpl");
    @Override
    public CategoryDto addCategory(MultipartFile file, String data) throws JsonMappingException, JsonParseException {
        CategoryDto response = null;
        if (data != null && file != null) {
            Category category = null;
            try {
                ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
                category = mapper.readValue(data, Category.class);
            } catch (JsonMappingException e) {
                logger.error("ERROR ", e);
                throw new JsonMappingException("ERROR");
            } catch (JsonProcessingException e) {
                logger.error("ERROR ", e);
                throw new JsonParseException("BAD REQUEST");
            }

            try {
                String fileDownloadUri = amazonClient.uploadFile(file);
                assert category != null;
                category.setImageUrl(fileDownloadUri);
                category.setImageName(file.getOriginalFilename());
            } catch (Exception e) {
                logger.error("ERROR", e);
            }
            assert category != null;
            category.setBooksCount(0);
            categoryRepo.save(category);

            response = categoryMapper.toDto(category);
            response.setImageUrl(s3Endpoint+"/"+category.getImageUrl());
        }
        return response;
    }

    @Override
    @Cacheable(value = "categoryById", key = "#p0")
    public CategoryDto getCategoryById(Long categoryId) {
        CategoryDto category = null;
        if(categoryId != null){
            Category categoryById = categoryRepo.findById(categoryId).get();
            category = categoryMapper.toDto(categoryById);
            category.setImageUrl(s3Endpoint+"/"+category.getImageUrl());
        }
        return category;
    }

    @Override
    @CachePut(value = "updateCategory", key = "#p0")
    public CategoryDto updateCategoryById(Long categoryId, CategoryDto categoryDto) {
        CategoryDto category = null;
        if(categoryDto != null){
            Category categoryById = categoryRepo.findById(categoryId).get();
            category = categoryMapper.toDto(categoryById);
            category.setImageUrl(s3Endpoint+"/"+category.getImageUrl());
        }
        return null;
    }

    @Override
    @CacheEvict(value = "categoryById", key = "#p0")
    public void deleteCategoryByCategoryId(Long categoryId) {
        if(categoryId != null){
            Category category = categoryRepo.findById(categoryId).get();
            if(category != null){
                categoryRepo.deleteById(categoryId);
            }
        }
    }

    @Override
//    @Cacheable(value = "categories", key = "'cache_'+#root.methodName")
    public List<CategoryDto> getAllCategories() {
        List<Category> allCategories = categoryRepo.findAll();
        List<CategoryDto> categories = allCategories.stream().map(cat -> {
            CategoryDto category = categoryMapper.toDto(cat);
            category.setImageUrl(s3Endpoint+"/"+category.getImageUrl());
            return category;
        }).collect(Collectors.toList());
        return categories;
    }

    @Override
    @CacheEvict(value = "categoryByKey", key = "#p0")
    public CategoryDto getCategoryByKey(String key) {
        CategoryDto category = null;
        try {
            category = categoryMapper.toDto(categoryRepo.findCategoryByKeyword(key));
        } catch (Exception e) {
            logger.error("ERROR", e);
            return null;
        }
        category.setImageUrl(s3Endpoint +"/"+ category.getImageUrl());
        return category;
    }

    @Override
    @CacheEvict(value = "categoryById", key = "#p0")
    public Category findCategoryById(Long categoryId) {
        try {
            return categoryRepo.findById(categoryId).get();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    @CacheEvict(value = "categoryByName", key = "#p0")
    public Category findCategoryByName(String categoryName) {
        Category category = null;
        try {
            category = categoryRepo.findByCategoryName(categoryName);
        }catch (Exception e){
            logger.error("ERROR", e);
            return null;
        }
        return category;
    }

    @Override
    @CacheEvict(value = "categoryById", key = "#p0")
    public Category getCategoryByName(String categoryName) {
        Category category = null;
        try {
            category = categoryRepo.findByCategoryName(categoryName);
        }catch (Exception e){
            logger.error("ERROR",e);
            return null;
        }
        return category;
    }
}
