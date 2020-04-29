package com.ray.dormitory.config.mvc.converter;


import com.baomidou.mybatisplus.core.enums.IEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * String转IEnum
 *
 * @author : Ray
 * @date : 2020.03.29 14:43
 */
@Component
public class IEnumConverterFactory implements ConverterFactory<String, IEnum> {
    @Override
    public <T extends IEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new IEnumConverter<>(targetType);
    }


    private static class IEnumConverter<T extends IEnum> implements Converter<String, T> {
        private final Class<T> targetType;

        public IEnumConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (StringUtils.isEmpty(source)) {
                return null;
            }
            return (T) IEnumConverterFactory.getEnum(this.targetType, source);
        }
    }

    public static <T extends IEnum> Object getEnum(Class<T> target, String source) {
        for (T enumObj : target.getEnumConstants()) {
            if (source.equals(String.valueOf(enumObj.getValue()))) {
                return enumObj;
            }
        }
        return null;
    }

}
