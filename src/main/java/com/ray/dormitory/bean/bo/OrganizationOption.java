package com.ray.dormitory.bean.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : Ray
 * @date : 2020.01.13 20:36
 */
@Getter
@Setter
@JsonIgnoreProperties({"handler"})
public class OrganizationOption {
    private int id;
    private String name;
    private List<OrganizationOption> children;
}
