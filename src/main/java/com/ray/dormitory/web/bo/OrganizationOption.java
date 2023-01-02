package com.ray.dormitory.web.bo;

import com.ray.dormitory.infrastructure.entity.Organization;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2020.01.13 20:36
 */
@Getter
@Setter
public class OrganizationOption {
    private int id;
    private String name;
    private List<OrganizationOption> children;

    /**
     * 递归将Organization转化为OrganizationOption
     *
     * @param object Organization实例
     * @return object转化后的OrganizationOption实例 或者 null
     */
    public static OrganizationOption convert(Object object) {

        if (object instanceof Organization) {
            OrganizationOption option = new OrganizationOption();
            Organization organization = (Organization) object;
            option.setId(organization.getId());
            option.setName(organization.getName());
            List<Organization> children = organization.getChildren();
            List<OrganizationOption> optionChildren = new ArrayList<>();
            if (children != null && !children.isEmpty()) {
                children.forEach(child -> optionChildren.add(convert(child)));
            }
            option.setChildren(optionChildren);
            return option;
        }
        return null;
    }

}
