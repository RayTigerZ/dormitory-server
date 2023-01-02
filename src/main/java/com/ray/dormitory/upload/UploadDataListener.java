package com.ray.dormitory.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.valid.group.SaveByFileValid;
import com.ray.dormitory.web.socket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.20 23:14
 * 模板的读取类
 * 有个很重要的点 UploadDataListener 不能被Spring管理，
 * 要每次读取excel都要new,然后里面用到Spring可以构造方法传进去
 */


@Slf4j
public class UploadDataListener<T> extends AnalysisEventListener<T> {

    /**
     * 记录保存数据时的错误信息
     */
    private final List<String> errorMsgList;
    private final String token;

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private final IService<T> baseService;


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param baseService
     * @param token
     */
    public UploadDataListener(IService<T> baseService, String token) {
        this.baseService = baseService;
        this.token = token;
        errorMsgList = new ArrayList<>();
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        saveData(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        WebSocketServer.sendInfo(token, "#over");
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(@Validated({SaveByFileValid.class}) T data) {
        String msg;
        try {
            boolean result = baseService.save(data);
            if (result) {
                log.info("存储数据库成功！");
            } else {
                log.error("存储数据库失败！！！");
            }

        } catch (Exception e) {
            msg = "数据：[" + getData(data) + "]\n错误信息:" + e.getMessage();
            errorMsgList.add(msg);

            log.error("data:{}, cause:{}", data, e.getMessage());
            WebSocketServer.sendInfo(token, msg);
        }


    }


    private String getData(T data) {
        StringBuilder stringBuilder = new StringBuilder();
        Field[] fields = data.getClass().getDeclaredFields();
        int index = 0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                field.setAccessible(true);
                try {
                    if (index > 0) {
                        stringBuilder.append("，");
                    }
                    String[] name = field.getAnnotation(ExcelProperty.class).value();

                    Object value = field.get(data);
                    stringBuilder.append(name[0]).append("：").append(value);
                    index++;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return stringBuilder.toString();
    }


}
