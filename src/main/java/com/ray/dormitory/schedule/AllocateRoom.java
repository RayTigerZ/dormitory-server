package com.ray.dormitory.schedule;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.po.Answer;
import com.ray.dormitory.bean.po.Room;
import com.ray.dormitory.bean.po.Survey;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.kmeans.Kmeans;
import com.ray.dormitory.kmeans.Point;
import com.ray.dormitory.service.AnswerService;
import com.ray.dormitory.service.SurveyService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.system.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.stream.Collectors;

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
    public void run() {


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

            //男生、女生分开
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

            allocate(boys, getRooms(boys.size() / Constants.ROOM_MIN_SIZE, true));
            allocate(girls, getRooms(girls.size() / Constants.ROOM_MIN_SIZE, false));


        }

    }


    private void allocate(List<Data> list, List<Room> rooms) {


        Map<String, List<Data>> map = new HashMap<>();
        int index = 0;
        Room room = rooms.get(index);
        int size = room.getSize();
        while (list.size() > size) {
            // 执行k-means算法
            Kmeans<Data> boyKmeans = new Kmeans<Data>(list, Constants.ROOM_MAX_SIZE);

            List<Data> newList = new ArrayList<>();

            List<List<Data>> clusters = boyKmeans.clustering();


            for (List<Data> cluster : clusters) {
                int from = 0, to = from + size;
                while (cluster.size() - from >= size) {
                    map.put(room.getNumber(), cluster.subList(from, to));
                    room = rooms.get(++index);
                    size = room.getSize();
                    from = to;
                    to = from + size;
                }
                if (from <= cluster.size() - 1) {
                    newList.addAll(cluster.subList(from, cluster.size()));
                }

            }
            list = newList;
        }
        if (list.size() > 0) {
            map.put(room.getNumber(), list);
        }
        map.forEach((n, arr) -> {
            System.out.println(n);
            arr.forEach(a -> {
                System.out.println(a.getName() + "  " + a.getCoordinate());
            });
            System.out.println();
        });


    }

    /**
     * @param num  房间数量(建议多一些)
     * @param flag true：男，false：女
     * @return 房间列表
     */
    private List<Room> getRooms(int num, boolean flag) {
        return null;
    }

    @Setter
    @Getter
    public static class Data implements Point {
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


    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        List<Data> boys = new ArrayList<>(), girls = new ArrayList<>();

        int num = 100;

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int x = random.nextInt(4) + 1;
            int y = random.nextInt(4) + 1;
            int z = random.nextInt(4) + 1;
            int w = random.nextInt(4) + 1;
            Answer answer = new Answer();
            Double[] arr = {new Double(x), new Double(y), new Double(z), new Double(w)};
            answer.setAnswer(Arrays.asList(arr));
            if (i % 2 == 0) {
                answer.setAnswer(null);
            }

            User user = new User();
            String str = String.valueOf(i + 1);
            user.setAccount(str);
            user.setName(str);

            user.setSex("男");
            boys.add(new AllocateRoom.Data(user, answer));

        }
        System.out.println(boys.size());

        List<Room> rooms = new ArrayList<>();
        //int len = boys.size() / 4 + 1;
        for (int i = 0; i < 30; i++) {
            int size = 4;
            Room room = new Room();
            room.setNumber("男" + (i + 1));
            room.setSize(size);
            rooms.add(room);
        }

        Map<String, List<Data>> map = new HashMap<>();
        int index = 0;
        Room room = rooms.get(index);
        int size = room.getSize();
        while (boys.size() > size) {
            // 执行k-means算法
            Kmeans<Data> boyKmeans = new Kmeans<Data>(boys, Constants.ROOM_MAX_SIZE);

            List<Data> newBoys = new ArrayList<>();

            List<List<Data>> centers = boyKmeans.clustering();


            for (List<Data> list : centers) {
                int from = 0, to = from + size;
                while (list.size() - from >= size) {
                    map.put(room.getNumber(), list.subList(from, to));
                    room = rooms.get(++index);
                    size = room.getSize();
                    from = to;
                    to = from + size;
                }
                if (from <= list.size() - 1) {
                    newBoys.addAll(list.subList(from, list.size()));
                }


            }
            boys = newBoys;


        }
        if (boys.size() > 0) {
            map.put(room.getNumber(), boys);
        }
        map.forEach((n, arr) -> {
            System.out.println(n);
            arr.forEach(a -> {
                System.out.println(a.getName() + "  " + a.getCoordinate());
            });
            System.out.println();
        });
        final int[] c = {0};
        List<String> list = new ArrayList<>();
        map.forEach((k, v) -> {
            list.addAll(v.stream().map(one -> one.getName()).collect(Collectors.toList()));
            c[0] += v.size();
        });
        System.out.println(c[0]);
        list.sort(String::compareTo);
        System.out.print(list + "   ");

        System.out.println(System.currentTimeMillis() - start);


    }
}
