package com.ray.dormitory.web.bo;

import com.ray.dormitory.infrastructure.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author : Ray
 * @date : 2020.03.28 22:57
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyOption implements Serializable {
    private int id;
    private String name;

    public static SurveyOption convert(Object object) {
        if (object instanceof Survey) {
            Survey survey = (Survey) object;
            return new SurveyOption(survey.getId(), survey.getName());
        }
        return null;
    }
}
