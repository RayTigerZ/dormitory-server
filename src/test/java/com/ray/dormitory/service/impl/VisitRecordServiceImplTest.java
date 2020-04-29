package com.ray.dormitory.service.impl;

import com.ray.dormitory.bean.enums.CycleType;
import com.ray.dormitory.service.VisitRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : Ray
 * @date : 2020.04.25 19:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VisitRecordServiceImplTest {

    @Autowired
    private VisitRecordService visitRecordService;

    @Test
    public void test() {
        System.out.println(visitRecordService.statistic(CycleType.MONTH, 6));
    }

}