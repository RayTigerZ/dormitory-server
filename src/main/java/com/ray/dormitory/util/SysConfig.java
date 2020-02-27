package com.ray.dormitory.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : Ray
 * @date : 2019.11.19 20:37
 */
@Component
@ConfigurationProperties(prefix = "sys-config")
@Getter
@Setter
public class SysConfig {
    private String templatePath;
    private String tokenName;
    private long tokenTimeout;
    private int repairerRoleId;


}
