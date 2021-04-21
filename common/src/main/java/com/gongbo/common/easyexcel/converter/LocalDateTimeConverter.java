package com.gongbo.common.easyexcel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.DateTimeFormatProperty;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.gongbo.common.constant.Times;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LocalDateTimeConverter implements Converter<LocalDateTime> {
    @Override
    public Class supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        String pattern = Optional.ofNullable(contentProperty)
                .map(ExcelContentProperty::getDateTimeFormatProperty)
                .map(DateTimeFormatProperty::getFormat)
                .orElse(Times.Pattern.DEFAULT_DATE_TIME);
        return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public CellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        String pattern = Optional.ofNullable(contentProperty)
                .map(ExcelContentProperty::getDateTimeFormatProperty)
                .map(DateTimeFormatProperty::getFormat)
                .orElse(Times.Pattern.DEFAULT_DATE_TIME);

        return new CellData<>(value.format(DateTimeFormatter.ofPattern(pattern)));
    }
}
