package com.gongbo.test.base;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback
@Transactional(rollbackFor = Exception.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractTest<T> {


    @Autowired(required = false)
    protected T target;

    /**
     * 登录用户名
     */
    protected String getUserName() {
        return "wgx";
    }

    /**
     * 登录密码
     */
    protected String getPassword() {
        return "123456";
    }

    /**
     * 登录
     */
    @BeforeEach
    protected void login() {
    }

    /**
     * 清除登录信息
     */
    @AfterEach
    protected void clear() {
    }

    /**
     * 电场ID参数
     */
    public static Collection<Collection<String>> wpIds() {
        List<Collection<String>> list = Lists.newArrayList();
        list.add(Lists.newArrayList("632500"));
        list.add(Lists.newArrayList(""));
        list.add(Lists.newArrayList((String) null));
        return list;
    }

    /**
     * 数据库中所有电厂id
     */
    public static Collection<String> allWpIds() {
        return Arrays.asList("630063", "632500", "632524", "632533", "632535", "632536", "632538", "632549", "632551", "632552", "632553", "632554", "632555", "632556", "632557", "632558", "632803", "632821", "632853", "632855", "632856", "632858", "632859", "632902", "632904");
    }

    public static Collection<String> wpId() {
        return Lists.newArrayList("632500");
    }

    public static LocalDate startDate() {
        return LocalDate.parse("2021-01-13");
    }

    public static LocalDate endDate() {
        return LocalDate.parse("2021-01-17");
    }

    /**
     * 区域ID
     */
    public static Collection<String> orgId() {
        return Lists.newArrayList("080bfc0ffe4ce6feeab194bc3f3bfd0f");
    }

    /**
     * 今年的所有月份
     */
    public static Collection<YearMonth> currentYearMonth() {
        return Arrays.stream(Month.values())
                .map(m -> YearMonth.of(Year.now().getValue(), m))
                .collect(Collectors.toList());
    }

    public static Collection<LocalDate> localDate() {
        List<LocalDate> list = Lists.newArrayList();
        list.add(LocalDate.now());
        return list;
    }
}
