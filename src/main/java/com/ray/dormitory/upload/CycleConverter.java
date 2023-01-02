package com.ray.dormitory.upload;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
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
    public Date convertToJavaData(ReadCellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new SimpleDateFormat(dateFormat).parse(cellData.getStringValue());
    }

    @Override
    public WriteCellData<?> convertToExcelData(Date date, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String cycle = new SimpleDateFormat(dateFormat).format(date);
        return new WriteCellData<>(cycle);
    }
}
