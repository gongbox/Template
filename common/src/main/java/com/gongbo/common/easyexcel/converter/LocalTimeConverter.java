package com.gongbo.common.easyexcel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.DateTimeFormatProperty;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.gongbo.common.constant.Times;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LocalTimeConverter implements Converter<LocalTime> {
    @Override
    public Class supportJavaTypeKey() {
        return LocalTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalTime convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        String pattern = Optional.ofNullable(contentProperty)
                .map(ExcelContentProperty::getDateTimeFormatProperty)
                .map(DateTimeFormatProperty::getFormat)
                .orElse(Times.Pattern.DEFAULT_TIME);
        return LocalTime.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public CellData<String> convertToExcelData(LocalTime value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        String pattern = Optional.ofNullable(contentProperty)
                .map(ExcelContentProperty::getDateTimeFormatProperty)
                .map(DateTimeFormatProperty::getFormat)
                .orElse(Times.Pattern.DEFAULT_TIME);

        return new CellData<>(value.format(DateTimeFormatter.ofPattern(pattern)));
    }
}
