package com.ray.dormitory.upload;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 账单周期转化
 * Date《--》String
 *
 * @author : Ray
 * @date : 2020.04.10 19:44
 */
public class CycleConverter implements Converter<Date> {
    private String dateFormat = "yyyy-MM";

    @Override
    public Class supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Date convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new SimpleDateFormat(dateFormat).parse(cellData.getStringValue());
    }

    @Override
    public CellData convertToExcelData(Date date, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String cycle = new SimpleDateFormat(dateFormat).format(date);
        return new CellData(cycle);
    }
}
