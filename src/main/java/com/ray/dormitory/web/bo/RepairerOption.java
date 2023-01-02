package com.ray.dormitory.web.bo;

import com.ray.dormitory.infrastructure.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Ray
 * @date : 2020.04.08 17:36
 */
@Getter
@Setter
public class RepairerOption {
    private String account;
    private String name;

    public static RepairerOption convert(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            RepairerOption option = new RepairerOption();
            option.setAccount(user.getAccount());
            option.setName(user.getName());
            return option;
        }
        return null;
    }
}
