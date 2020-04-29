package com.ray.dormitory.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.util.DigestUtils;

/**
 * md5加密和获取加密盐
 *
 * @author Ray Z
 * @date 2019.10.27 20:37
 */
public class Md5Util {

    public static String getMD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static String getSalt() {
        return new SecureRandomNumberGenerator().nextBytes().toString();
    }

}
