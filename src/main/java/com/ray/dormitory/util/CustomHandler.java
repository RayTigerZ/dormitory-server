package com.ray.dormitory.util;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.dormitory.bean.bo.Question;

import java.io.IOException;
import java.util.List;

/**
 * @author : Ray
 * @date : 2020.03.27 15:37
 */
public class CustomHandler extends JacksonTypeHandler {

    private static ObjectMapper mapper = new ObjectMapper();
    private JavaType type;


    public CustomHandler(Class<Object> type) {
        super(type);
        this.type = mapper.getTypeFactory().constructParametricType(List.class, Question.class);
    }


    @Override
    protected Object parse(String json) {
        try {
            return mapper.readValue(json, this.type);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    @Override
    protected String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper should not be null", new Object[0]);
        CustomHandler.mapper = objectMapper;
    }
}
