package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.infrastructure.entity.StayApply;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.infrastructure.mapper.StayApplyMapper;
import com.ray.dormitory.service.StayApplyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * <p>
 * 留校申请 服务实现类
 * </p>
 *
 * @author Ray
 * @date 2020-2-18 19:24:33
 */
@Service
public class StayApplyServiceImpl extends ServiceImpl<StayApplyMapper, StayApply> implements StayApplyService {


    @Override
    public boolean check(int id, boolean result) {
        StayApply stayApply = baseMapper.selectById(id);
        //判断是否存在该记录
        if (stayApply != null) {
            //判断记录是否已处理
            if (stayApply.getProcessTime() == null) {
                Wrapper<StayApply> wrapper = Wrappers.<StayApply>lambdaUpdate().eq(StayApply::getId, id).set(StayApply::getIsConsent, result).set(StayApply::getProcessTime, LocalDateTime.now());
                return update(wrapper);
            } else {
                throw new CustomException(204, "该记录已审核");
            }
        } else {
            throw new CustomException(204, "记录不存在");
        }
    }


}
