package com.chapterly.service;

import com.chapterly.dto.BannerDto;
import org.springframework.web.multipart.MultipartFile;

public interface BannerService {
    BannerDto saveBanner(MultipartFile file, String data);
    BannerDto getBannerById(Long bannerId);
    boolean deleteBannerById(Long bannerId);
}
