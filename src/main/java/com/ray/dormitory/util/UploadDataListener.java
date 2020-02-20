package com.ray.dormitory.util;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;

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
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    /**
     * 记录保存数据时的错误信息
     */
    private List<String> errorMsgs;
    private String time;

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private IService<T> baseService;


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param baseService
     */
    public UploadDataListener(IService baseService, String time) {
        this.baseService = baseService;
        this.time = time;
        errorMsgs = new ArrayList<>();
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        log.info("解析到一条数据:{}", data.toString());

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM

        saveData(data);

    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
//        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(T data) {
        String msg;
        try {
            baseService.save(data);

            log.info("存储数据库成功！");
        } catch (Exception e) {
            msg = "数据：[" + getData(data) + "]\n错误信息:" + e.getMessage();
            errorMsgs.add(msg);

            log.error("data:{}, cause:{}", data, e.getMessage());
            WebSocketServer.sendInfo(msg, time);
        }


    }


    private String getData(T data) {
        StringBuffer buffer = new StringBuffer();
        Field[] fields = data.getClass().getDeclaredFields();
        int index = 0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                field.setAccessible(true);
                try {
                    if (index > 0) {
                        buffer.append("，");
                    }
                    String[] name = field.getAnnotation(ExcelProperty.class).value();

                    Object value = field.get(data);
                    buffer.append(name[0]).append("：").append(value);
                    index++;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return buffer.toString();
    }


    public List<String> getErrorMsgs() {
        return this.errorMsgs;
    }
}
