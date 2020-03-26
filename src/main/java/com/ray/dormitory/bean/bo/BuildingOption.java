package com.ray.dormitory.bean.bo;

import com.ray.dormitory.bean.po.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Ray
 * @date : 2020.03.26 22:39
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingOption {
    private int id;
    private String name;
    private String type;

    public static BuildingOption convert(Object object) {
        if (object != null && object instanceof Building) {
            Building building = (Building) object;
            return new BuildingOption(building.getId(), building.getName(), building.getType());
        }
        return null;
    }
}
