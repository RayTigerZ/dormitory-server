package com.ray.dormitory.util;

import java.time.format.DateTimeFormatter;

/**
 * @author : Ray
 * @date : 2020.02.19 23:39
 */
public class DateUtils {

    public final static String EXPORT_FILE_DATE_FORMAT = "yyyyMMdd";

    public final static DateTimeFormatter EXPORT_FILE_DATE_FORMATTER= DateTimeFormatter.ofPattern(EXPORT_FILE_DATE_FORMAT);

}
