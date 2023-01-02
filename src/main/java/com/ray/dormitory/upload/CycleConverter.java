package com.ray.dormitory.upload;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


/**
 * 账单周期转化
 * Date《--》String
 *
 * @author : Ray
 * @date : 2020.04.10 19:44
 */
public class CycleConverter implements Converter<YearMonth> {
    private String dateFormat = "yyyy-MM";

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);

    @Override
    public Class<?> supportJavaTypeKey() {
        return YearMonth.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public YearMonth convertToJavaData(ReadCellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return YearMonth.parse(cellData.getStringValue(), dateTimeFormatter);
    }

    @Override
    public WriteCellData<?> convertToExcelData(YearMonth date, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String cycle = date.format(dateTimeFormatter);
        return new WriteCellData<>(cycle);
    }
}
