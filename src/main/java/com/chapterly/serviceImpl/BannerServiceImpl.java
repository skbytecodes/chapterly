package com.chapterly.serviceImpl;

import com.chapterly.aws.AmazonClient;
import com.chapterly.dto.BannerDto;
import com.chapterly.entity.Banner;
import com.chapterly.mapper.BannerMapper;
import com.chapterly.repository.BannerRepo;
import com.chapterly.service.BannerService;
import com.chapterly.util.Utility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerRepo bannerRepo;
    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private AmazonClient amazonClient;

    @Autowired
    private Utility utility;
    Logger logger = LoggerFactory.getLogger("BannerServiceImpl");

    @Override
    public BannerDto saveBanner(MultipartFile file, String data) {
        BannerDto response = null;
        if (data != null && file != null) {
            Banner banner = null;
            try {
                ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
                banner = mapper.readValue(data, Banner.class);
            } catch (JsonProcessingException e) {
                logger.error("ERROR ", e);
                return null;
            }

            try {
//                String fileDownloadUri = amazonClient.uploadFile(file);
                String fileDownloadUri = utility.saveImage(file);
                banner.setImageUrl(fileDownloadUri);
                banner.setImageName(file.getOriginalFilename());
            } catch (Exception e){
                logger.error("ERROR", e);
                return null;
            }
            Banner saveBanner = bannerRepo.save(banner);
            response = bannerMapper.toDto(saveBanner);
        }
        return response;
    }

    @Override
    public BannerDto getBannerById(Long bannerId) {
        if(bannerId != null){
            return bannerMapper.toDto(bannerRepo.findById(bannerId).get());
        }
        return null;
    }

    @Override
    public boolean deleteBannerById(Long bannerId) {
        if(bannerId != null){
            Banner book = bannerRepo.findById(bannerId).get();
            bannerRepo.deleteById(bannerId);
            return true;
        }
        return false;
    }
}
