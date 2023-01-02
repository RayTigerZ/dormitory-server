package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.infrastructure.entity.Operation;
import com.ray.dormitory.infrastructure.mapper.OperationMapper;
import com.ray.dormitory.service.OperationService;
import org.springframework.stereotype.Service;

/**
 * @author : Ray
 * @date : 2019.12.05 15:52
 */
@Service
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements OperationService {
}
