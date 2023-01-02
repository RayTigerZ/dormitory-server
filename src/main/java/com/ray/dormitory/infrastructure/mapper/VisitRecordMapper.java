package com.ray.dormitory.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.web.bo.Count;
import com.ray.dormitory.infrastructure.enums.CycleType;
import com.ray.dormitory.infrastructure.entity.VisitRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 来访记录表 Mapper 接口
 * </p>
 *
 * @author Ray
 * @date 2020-2-14 17:23:00
 */
@Repository
public interface VisitRecordMapper extends BaseMapper<VisitRecord> {
    List<Count> statistic(CycleType type, List<Integer> list);

}
