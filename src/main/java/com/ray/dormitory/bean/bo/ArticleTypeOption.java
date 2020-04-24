package com.ray.dormitory.bean.bo;

import com.ray.dormitory.bean.po.ArticleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Ray
 * @date : 2020.03.26 3:02
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTypeOption {
    private int id;
    private String name;

    public static ArticleTypeOption convert(Object object) {
        if (object instanceof ArticleType) {
            ArticleType type = (ArticleType) object;
            return new ArticleTypeOption(type.getId(), type.getName());
        }
        return null;
    }
}
