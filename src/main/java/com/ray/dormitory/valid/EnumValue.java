package com.ray.dormitory.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 验证是否为 target 中的值
 *
 * @author : Ray
 * @date : 2020.03.25 16:31
 */


@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidatorForString.class})
public @interface EnumValue {

    String message() default "";


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 目标枚举类
     */
    Class<?> target() default Class.class;

    /**
     * 是否忽略空值
     */
    boolean ignoreEmpty() default false;
}
