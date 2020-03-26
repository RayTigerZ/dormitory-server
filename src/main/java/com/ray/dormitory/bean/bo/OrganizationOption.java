package com.ray.dormitory.bean.bo;

import com.ray.dormitory.bean.po.Organization;
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
            if (children != null) {
                List<OrganizationOption> optionChildren = new ArrayList<>();
                children.forEach(child -> optionChildren.add(convert(child)));
                option.setChildren(optionChildren);
            } else {
                option.setChildren(new ArrayList<>());
            }
            return option;
        }
        return null;
    }

}
