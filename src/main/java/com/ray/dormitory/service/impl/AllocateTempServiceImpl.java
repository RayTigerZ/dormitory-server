package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.enums.AllocateStatus;
import com.ray.dormitory.bean.po.AllocateTemp;
import com.ray.dormitory.bean.po.Room;
import com.ray.dormitory.bean.po.Survey;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.mapper.AllocateTempMapper;
import com.ray.dormitory.mapper.RoomMapper;
import com.ray.dormitory.mapper.SurveyMapper;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.service.AllocateTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 宿舍预分配结果表 服务实现类
 * </p>
 *
 * @author Ray
 * @date 2020-3-23 21:10:14
 */
@Service
public class AllocateTempServiceImpl extends ServiceImpl<AllocateTempMapper, AllocateTemp> implements AllocateTempService {

    @Autowired
    private SurveyMapper surveyMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveBatch(int surveyId, Collection<AllocateTemp> entityList) {
        saveBatch(entityList);
        Wrapper<Survey> wrapper = Wrappers.<Survey>lambdaUpdate()
                .set(Survey::getStatus, AllocateStatus.PRE_ALLOCATE)
                .eq(Survey::getId, surveyId);
        surveyMapper.update(null, wrapper);
        return true;
    }


    @Override
    public boolean updateById(AllocateTemp entity) {

        AllocateTemp allocateTemp = baseMapper.selectById(entity.getId());
        if (allocateTemp == null) {
            throw new CustomException(ErrorEnum.RECORD_NOT_EXIST);
        }

        int count = roomMapper.selectCount(Wrappers.<Room>lambdaQuery().eq(Room::getNumber, entity.getRoomNum()));
        if (count == 0) {
            throw new CustomException(ErrorEnum.ROOM_NOT_EXIST);
        }
        return super.updateById(entity);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean allocate(int surveyId) {
        Survey survey = surveyMapper.selectById(surveyId);
        if (survey == null || !survey.getStatus().equals(AllocateStatus.PRE_ALLOCATE)) {
            throw new CustomException(202, "无法进行分配");
        }
        List<AllocateTemp> temps = baseMapper.selectList(Wrappers.<AllocateTemp>lambdaQuery().eq(AllocateTemp::getSurveyId, surveyId));

        temps.forEach(temp -> userMapper.update(null, Wrappers.<User>lambdaUpdate()
                .eq(User::getAccount, temp.getStudentNum())
                .set(User::getRoomNum, temp.getRoomNum())));

        Wrapper<Survey> surveyWrapper = Wrappers.<Survey>lambdaUpdate()
                .eq(Survey::getId, surveyId)
                .set(Survey::getStatus, AllocateStatus.ALLOCATED);
        surveyMapper.update(null, surveyWrapper);
        return true;
    }
}
