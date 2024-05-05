package com.chapterly.mapper;

import com.chapterly.dto.UserDto;
import com.chapterly.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "password", source = "user.pwd")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "address", source = "user.address")
//    @Mapping(target = "account_creation_date", source = "user.account_creation_date")
    @Mapping(target = "shippingAddress", source = "user.shippingAddress")
    @Mapping(target = "imageUrl", source = "user.imageUrl")
    @Mapping(target = "imageName", source = "user.imageName")

    UserDto toDto(User user);
}
