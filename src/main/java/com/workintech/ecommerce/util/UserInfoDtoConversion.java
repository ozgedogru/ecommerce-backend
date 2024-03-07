package com.workintech.ecommerce.util;

import com.workintech.ecommerce.dto.UserInfoDto;
import com.workintech.ecommerce.entity.ApplicationUser;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDtoConversion {

    public static UserInfoDto convertUser(ApplicationUser user) {
        return new UserInfoDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                AddressDtoConversion.convertAddressList(user.getAddresses()));
    }

    public static List<UserInfoDto> convertUserList(List<ApplicationUser> users) {
        List<UserInfoDto> userInfoDtoList = new ArrayList<>();
        for (ApplicationUser user : users) {
            userInfoDtoList.add(convertUser(user));
        }
        return userInfoDtoList;
    }
}
