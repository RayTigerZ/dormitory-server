package com.ray.dormitory.bean.bo;

import com.ray.dormitory.bean.enums.Sex;
import com.ray.dormitory.infrastructure.entity.Building;
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
    private Sex type;

    public static BuildingOption convert(Object object) {
        if (object instanceof Building building) {
            return new BuildingOption(building.getId(), building.getName(), building.getType());
        }
        return null;
    }
}
