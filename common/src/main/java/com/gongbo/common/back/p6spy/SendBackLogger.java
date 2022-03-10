
package com.gongbo.common.back.p6spy;


import com.gongbo.common.back.BackHandler;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.P6Logger;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "p6spy")
@NoArgsConstructor
public class SendBackLogger implements P6Logger {

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        BackHandler.handler(category, sql);
    }

    @Override
    public void logException(Exception e) {
    }

    @Override
    public void logText(String text) {
    }

    public boolean isCategoryEnabled(Category category) {
        return true;
    }
}
