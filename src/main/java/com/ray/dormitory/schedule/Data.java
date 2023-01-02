package com.ray.dormitory.schedule;

import com.ray.dormitory.infrastructure.enums.Sex;
import com.ray.dormitory.infrastructure.entity.Answer;
import com.ray.dormitory.infrastructure.entity.User;
import com.ray.dormitory.kmeans.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2020.03.28 0:40
 */
@Setter
@Getter
@NoArgsConstructor
public class Data implements Point {
    private String studentNum;
    private String name;
    private Sex sex;
    private List<Double> answer;

    public Data(User user, Answer answer) {
        this.studentNum = user.getAccount();
        this.name = user.getName();
        this.sex = user.getSex();
        this.answer = answer == null ? null : answer.getAnswer();
    }

    @Override
    public List<Double> getCoordinate() {
        return this.answer;
    }

    @Override
    public boolean samePosition(Point point) {

        List<Double> myCoordinate = getCoordinate();
        List<Double> coordinate = point.getCoordinate();
        int len = myCoordinate.size();
        for (int i = 0; i < len; i++) {
            if (!myCoordinate.get(i).equals(coordinate.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Point clone() {
        Data data = new Data();
        data.setName(name);
        data.setStudentNum(studentNum);
        data.setSex(sex);
        data.setAnswer(new ArrayList<>(answer));
        return data;
    }
}