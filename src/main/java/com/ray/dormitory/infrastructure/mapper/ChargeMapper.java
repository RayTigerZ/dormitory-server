package com.ray.dormitory.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.infrastructure.entity.Charge;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 宿舍费用项 Mapper 接口
 * </p>
 *
 * @author Ray
 * @date 2020-2-26 13:07:34
 */
@Repository
public interface ChargeMapper extends BaseMapper<Charge> {

}
