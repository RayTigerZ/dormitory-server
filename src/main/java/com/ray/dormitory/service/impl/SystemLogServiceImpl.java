package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.SystemLog;
import com.ray.dormitory.mapper.SystemLogMapper;
import com.ray.dormitory.service.SystemLogService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {
    private static Map<String, String> operateType;

    public static Map<String, String> getOperateType() {
        return operateType;
    }

    public static void setOperateType(Map<String, String> operateType) {
        SystemLogServiceImpl.operateType = operateType;
    }

    @Override
    public boolean save(SystemLog systemLog) {
        String requestUri = systemLog.getRequestUri();
        String type;
        if ("/user/logout".equals(requestUri)) {
            type = "退出";
        } else if ("/user/login".equals(requestUri)) {
            type = "登录";
        } else if (operateType != null && requestUri != null) {
            type = operateType.get(requestUri);
        } else {
            type = requestUri;
        }

        systemLog.setOptype(type);

        return super.save(systemLog);
    }


}
