package com.gongbo.common.back;

import cn.hutool.core.util.StrUtil;
import com.gongbo.common.back.core.BackContext;
import com.gongbo.common.back.core.BackContextHolder;
import com.gongbo.common.back.core.BackService;
import com.gongbo.common.back.exception.BackException;
import com.p6spy.engine.logging.Category;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/**
 * 回传
 */
public class BackHandler {

    @Autowired
    private BackService backService;
    private static BackHandler INSTANCE;

    @PostConstruct
    private void init() {
        INSTANCE = this;
    }

    public static BackHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 是否开启回传
     */
    public static boolean isEnableBack() {
        return getInstance().backService.isEnable();
    }

    /**
     * 获取默认延时时间
     */
    public static int getDefaultDelayMs() {
        //FIXME
        return 0;
    }

    /**
     * 以回传的方式执行
     */
    public static void withBack(Integer delayMs, Runnable runnable, Object... extra) {
        if (!isEnableBack()) {
            runnable.run();
            return;
        }

        start(delayMs, extra);
        try {
            runnable.run();
        } catch (Exception e) {
            rollbackCurrent();
            throw e;
        } finally {
            stop();
        }
    }

    /**
     * 以回传的方式执行
     */
    public static void withBack(Runnable runnable, Object... extra) {
        withBack(0, runnable, extra);
    }

    public static void withBackSync(Runnable runnable, Object... extra) {
        withBack(0, runnable, extra);
    }


    /**
     * 记录执行sql（排除查询）
     */
    public static List<String> record(Runnable runnable) {
        start(0);
        try {
            runnable.run();
            BackContext backContext = BackContextHolder.currentBackContext();
            return new ArrayList<>(backContext.getCurrentSqls());
        } finally {
            rollbackCurrent();
            stop();
        }
    }


    /**
     * 开启回传记录
     */
    public static void start(Integer delayMs, Object... extra) {
        BackContext backContext = BackContextHolder.currentBackContext();
        if (backContext == null) {
            backContext = BackContextHolder.newBackContext(getInstance().backService, getInstance().backService.isTransaction());
        }
        backContext.start(delayMs, extra);
    }

    /**
     * 关闭回传记录
     */
    public static void stop() {
        BackContext backContext = BackContextHolder.currentBackContext();
        if (backContext == null) {
            throw new BackException("not start");
        }
        backContext.stop();
    }

    /**
     * 存在事务时回退当前事务（清空当前已记录的sql）
     */
    public static void rollbackCurrent() {
        BackContextHolder.currentBackContext().clearCurrent();
    }

    /**
     * sql监听
     */
    public static void handler(Category category, String sql) {
        BackContext backContext = BackContextHolder.currentBackContext();
        //若当前线程没有开启过，则跳过
        if (backContext == null) {
            return;
        }

        if (category == Category.COMMIT) {
            backContext.commit();
        } else if (category == Category.ROLLBACK) {
            backContext.rollback();
        } else if (StrUtil.isNotEmpty(sql)) {
            if (!sql.startsWith("SELECT") && !sql.startsWith("select")) {
                backContext.addWhenBacking(sql);
            }
        }
    }
}
