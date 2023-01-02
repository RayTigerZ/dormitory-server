package com.ray.dormitory.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Map;

/**
 * @author Ray
 * @date 2019/12/04 15:38
 */
public class JwtUtil {

    /**
     * redis缓存中帐号登陆 账户值的key前缀
     */
    private static final String SESSION_USER_PREFIX = "USER_";

    /**
     * 设置私钥
     */
    private static final String SECRET = "ray_dormitory";

    public static String getSecret() {
        return SECRET;
    }

    public static String getAccountUserKey(String account) {
        return SESSION_USER_PREFIX + account;
    }


    /**
     * 校验token是否有效
     *
     * @param token
     * @return
     */
    public static boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("name").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("account").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    public static int getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt();
        } catch (JWTDecodeException e) {
            return 0;
        }
    }


    /**
     * 通过Token解析出roles
     *
     * @param token
     * @return
     */
    public static String getRoles(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("roles").asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

}