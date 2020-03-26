package com.ray.dormitory.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * EnumValue验证器
 *
 * @author : Ray
 * @date : 2020.03.25 16:33
 */
public class EnumValidatorForString implements ConstraintValidator<EnumValue, String> {

    /**
     * 枚举校验注解
     */
    private EnumValue annotation;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;

        Class<?> enumClass = annotation.target();
        boolean ignoreEmpty = annotation.ignoreEmpty();

        // target为枚举，并且value有值，或者不忽视空值，才进行校验
        if (enumClass.isEnum() && (value != null || !ignoreEmpty)) {

            Object[] objects = enumClass.getEnumConstants();
            for (Object obj : objects) {
                // 使用此注解的枚举类需要重写toString方法，改为需要验证的值
                if (obj.toString().equals(value)) {
                    result = true;
                    break;
                }
            }
        } else {
            result = true;
        }
        return result;
    }
}
