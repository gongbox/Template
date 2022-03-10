package com.gongbo.common.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gongbo.common.dynamicresult.DynamicResult;
import com.gongbo.common.utils.BeanMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 通用表字段
 * 注意：
 * 1，所有val96/288/1440表结构包含的字段都应该添加到该实体类中，这样查询返回的结果中才会包含该字段
 * 2，部分字段某些表结构不存在，查询后的数据为null，实际使用时请忽略
 */
@Getter
@Setter
@ToString
public class CommonValView implements DynamicResult, BeanMap {
    /**
     * id字段，不能命名为id，否则会当做主键ID
     */
    @TableField("id")
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    protected String _id;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    protected String wpId;

    protected String foreDate;

    protected String recDate;

    protected String versionId;

    protected String modelId;

    protected String qxyId;

    protected String syzId;

    protected String cftId;

    protected Integer isShow;

    protected String beginHourMinute;

    protected BigDecimal val01;
    protected BigDecimal val02;
    protected BigDecimal val03;
    protected BigDecimal val04;
    protected BigDecimal val05;
    protected BigDecimal val06;
    protected BigDecimal val07;
    protected BigDecimal val08;
    protected BigDecimal val09;
    protected BigDecimal val10;
    protected BigDecimal val11;
    protected BigDecimal val12;
    protected BigDecimal val13;
    protected BigDecimal val14;
    protected BigDecimal val15;
    protected BigDecimal val16;
    protected BigDecimal val17;
    protected BigDecimal val18;
    protected BigDecimal val19;
    protected BigDecimal val20;
    protected BigDecimal val21;
    protected BigDecimal val22;
    protected BigDecimal val23;
    protected BigDecimal val24;
    protected BigDecimal val25;
    protected BigDecimal val26;
    protected BigDecimal val27;
    protected BigDecimal val28;
    protected BigDecimal val29;
    protected BigDecimal val30;
    protected BigDecimal val31;
    protected BigDecimal val32;
    protected BigDecimal val33;
    protected BigDecimal val34;
    protected BigDecimal val35;
    protected BigDecimal val36;
    protected BigDecimal val37;
    protected BigDecimal val38;
    protected BigDecimal val39;
    protected BigDecimal val40;
}
