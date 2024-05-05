package com.chapterly.mapper;

import com.chapterly.dto.BannerDto;
import com.chapterly.entity.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BannerMapper {

    @Mapping(target = "id", source = "banner.bannerId")
    @Mapping(target = "imageUrl", source = "banner.imageUrl")
    @Mapping(target = "imageName", source = "banner.imageName")
    BannerDto toDto(Banner banner);
}
