package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.bo.Count;
import com.ray.dormitory.bean.enums.CycleType;
import com.ray.dormitory.bean.po.ViolationRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 违规记录表 Mapper 接口
 * </p>
 *
 * @author Ray
 * @date 2020-2-15 16:37:24
 */
@Repository
public interface ViolationRecordMapper extends BaseMapper<ViolationRecord> {
    List<Count> statistic(CycleType type, List<Integer> list);
}
