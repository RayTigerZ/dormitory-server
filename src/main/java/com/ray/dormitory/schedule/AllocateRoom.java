package com.ray.dormitory.schedule;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.po.Answer;
import com.ray.dormitory.bean.po.Survey;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.kmeans.Kmeans;
import com.ray.dormitory.kmeans.Point;
import com.ray.dormitory.service.AnswerService;
import com.ray.dormitory.service.SurveyService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 根据问卷结果在结束时间后自动预分配宿舍
 *
 * @author : Ray
 * @date : 2020.03.23 20:10
 */
@Slf4j
@Configuration
@EnableScheduling
public class AllocateRoom {

    @Autowired
    private SurveyService surveyService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserService userService;

    /**
     * 每日1点检查是否需要需要分配宿舍
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void allocate() {
        log.info("allocate room start ...");
        //查询需要分配的问卷调查
        //符合条件：问卷调查时间<=当前时间 且问卷调查未进行分配
        Wrapper<Survey> wrapper = Wrappers.<Survey>lambdaQuery()
                .le(Survey::getEndTime, new Date())
                .eq(Survey::getAllocated, false);
        List<Survey> list = surveyService.list(wrapper);

        for (Survey survey : list) {

            //问卷调查的所有学生
            Wrapper<User> userWrapper = Wrappers.<User>query()
                    .eq("LENGTH(account)", Constants.STUDENT_ACCOUNT_LEN)
                    .eq("LEFT(account," + Constants.GRADE_LEN + ")", survey.getGrade())
                    .lambda().select(User::getId, User::getAccount, User::getName, User::getSex);
            List<User> users = userService.list(userWrapper);

            List<Data> boys = new ArrayList<>(), girls = new ArrayList<>();

            for (User user : users) {
                String sex = user.getSex();
                Wrapper<Answer> answerWrapper = Wrappers.<Answer>lambdaQuery()
                        .eq(Answer::getSurveyId, survey.getId())
                        .eq(Answer::getUserId, user.getId());
                Answer answer = answerService.getOne(answerWrapper);
                if ("男".equals(sex)) {
                    boys.add(new Data(user, answer));
                } else {
                    girls.add(new Data(user, answer));
                }

            }

            // 执行k-means算法
            int size = 4, num = boys.size();
            Kmeans kmeans = new Kmeans<Data>(boys, size, num / size) {

                @Override
                public double getDistance(Data center, Data point) {
                    double sum = 0;
                    int length = center.getAnswer().size();
                    for (int i = 0; i < length; i++) {
                        sum += Math.pow(center.getAnswer().get(i) - point.getAnswer().get(i), 2);
                    }

                    return sum;
                }

                @Override
                public void updateCenterPoint(List<Data> list, Data center) {

                    int size = center.getAnswer().size();
                    for (int i = 0; i < size; i++) {
                        double sum = 0;
                        int length = list.size();
                        for (int j = 0; j < length; j++) {
                            sum += list.get(j).getAnswer().get(i);
                        }

                        center.getAnswer().set(i, sum / length);
                    }


                }
            };


            List<List<Answer>> centers = kmeans.clustering();

        }

    }

    @Setter
    @Getter
    private class Data implements Point {
        private String studentNum;
        private String name;
        private String sex;
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
    }
}
