package com.ray.dormitory.web.bo;

import com.ray.dormitory.infrastructure.entity.Questionnaire;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 问卷选项
 *
 * @author : Ray
 * @date : 2020.03.26 0:04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireOption {
    private int id;
    private String title;

    public static QuestionnaireOption convert(Object object) {
        if (object instanceof Questionnaire) {
            Questionnaire questionnaire = (Questionnaire) object;
            return new QuestionnaireOption(questionnaire.getId(), questionnaire.getTitle());
        }
        return null;
    }
}
