package com.ray.dormitory.schedule;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.enums.AllocateStatus;
import com.ray.dormitory.bean.enums.Sex;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.infrastructure.entity.AllocateTemp;
import com.ray.dormitory.infrastructure.entity.Answer;
import com.ray.dormitory.infrastructure.entity.Room;
import com.ray.dormitory.infrastructure.entity.Survey;
import com.ray.dormitory.infrastructure.entity.User;
import com.ray.dormitory.kmeans.Kmeans;
import com.ray.dormitory.kmeans.Point;
import com.ray.dormitory.service.*;
import com.ray.dormitory.system.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

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
    @Autowired
    private RoomService roomService;
    @Autowired
    private AllocateTempService allocateTempService;

    /**
     * 每日1点检查是否需要需要分配宿舍
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void run() {

        log.info("allocate room start ...");

        //查询需要分配的问卷调查
        //符合条件：问卷调查时间<=当前时间 且 问卷调查未进行分配
        Wrapper<Survey> wrapper = Wrappers.<Survey>lambdaQuery()
                .le(Survey::getEndTime, new Date())
                .eq(Survey::getStatus, AllocateStatus.NOT_ALLOCATE);
        List<Survey> list = surveyService.list(wrapper);

        for (Survey survey : list) {

            //问卷调查的所有学生
            Wrapper<User> userWrapper = Wrappers.<User>query()
                    .eq("LENGTH(account)", Constants.STUDENT_ACCOUNT_LEN)
                    .eq("LEFT(account," + Constants.GRADE_LEN + ")", survey.getGrade())
                    .lambda().select(User::getId, User::getAccount, User::getName, User::getSex);
            List<User> users = userService.list(userWrapper);

            //男/女数据分离，
            List<Point> boys = new ArrayList<>(), girls = new ArrayList<>();
            users.forEach(user -> {
                Sex sex = user.getSex();
                Wrapper<Answer> answerWrapper = Wrappers.<Answer>lambdaQuery()
                        .eq(Answer::getSurveyId, survey.getId())
                        .eq(Answer::getUserId, user.getId());
                Answer answer = answerService.getOne(answerWrapper);
                (Sex.MAN.equals(sex) ? boys : girls).add(new Data(user, answer));
            });

            //进行聚类
            allocate(boys, getRooms(1 + boys.size() / Constants.ROOM_MIN_SIZE, Sex.MAN), survey.getId());
            allocate(girls, getRooms(1 + girls.size() / Constants.ROOM_MIN_SIZE, Sex.WOMAN), survey.getId());
        }

        log.info("allocate room over ...");
    }


    private void allocate(List<Point> list, List<Room> rooms, int surveyId) {
        Map<String, List<Point>> map = new HashMap<>(rooms.size());
        int index = 0;
        Room room = rooms.get(index);
        int roomSize = room.getSize();
        while (list.size() > roomSize) {
            // 执行k-means算法
            Kmeans kmeans = new Kmeans(list, Constants.ROOM_MAX_SIZE);
            //获取聚类后的点簇集合
            List<List<Point>> clusters = kmeans.clustering();
            //点簇分配后剩余的点集合
            List<Point> newList = new ArrayList<>();

            //遍历点簇，将点簇中的点分配到房间中
            //当剩余点无法满足一个房间时，加入到newList
            for (List<Point> cluster : clusters) {
                int from = 0, to = from + roomSize, len = cluster.size();
                while (len - from >= roomSize) {
                    map.put(room.getNumber(), cluster.subList(from, to));
                    //下一间房
                    room = rooms.get(++index);
                    roomSize = room.getSize();
                    //cluster的下一段
                    from = to;
                    to = from + roomSize;
                }
                //剩余点数量大于等于1
                if (from <= cluster.size() - 1) {
                    newList.addAll(cluster.subList(from, cluster.size()));
                }

            }
            list = newList;
        }
        if (list.size() > 0) {
            map.put(room.getNumber(), list);
        }
        List<AllocateTemp> allocateTemps = new ArrayList<>();
        map.forEach((roomNum, points) -> {
            points.forEach(point -> {
                Data data = (Data) point;
                AllocateTemp allocateTemp = new AllocateTemp();
                allocateTemp.setName(data.getName());
                allocateTemp.setSex(data.getSex());
                allocateTemp.setStudentNum(data.getStudentNum());
                allocateTemp.setRoomNum(roomNum);
                allocateTemp.setSurveyId(surveyId);
                allocateTemps.add(allocateTemp);
            });
        });
        allocateTempService.saveBatch(surveyId, allocateTemps);
    }

    /**
     * @param num 房间数量(建议多一些)
     * @param sex 男/女
     * @return 房间列表
     */
    private List<Room> getRooms(int num, Sex sex) {
        Wrapper<Room> wrapper = Wrappers.<Room>lambdaQuery()
                .eq(Room::getType, sex.getValue())
                .orderByAsc(Room::getNumber)
                .last("limit 0," + num);

        List<Room> rooms = roomService.list(wrapper);
        if (rooms.size() < num) {

            throw new CustomException(202, "房间数量不足");
        }
        return rooms;

    }

}
