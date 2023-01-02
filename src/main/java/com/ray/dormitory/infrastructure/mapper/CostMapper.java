package com.ray.dormitory.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.bo.Count;
import com.ray.dormitory.bean.enums.CycleType;
import com.ray.dormitory.infrastructure.entity.Cost;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 账单 Mapper 接口
 * </p>
 *
 * @author Ray
 * @date 2020-2-26 14:56:49
 */
@Repository
public interface CostMapper extends BaseMapper<Cost> {

    List<Count> statistic(String charge, CycleType type, List<Integer> list);
}
