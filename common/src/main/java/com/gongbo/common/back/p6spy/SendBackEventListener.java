package com.gongbo.common.back.p6spy;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.logging.LoggingEventListener;

import java.sql.SQLException;

/**
 * 监听事件
 */
public class SendBackEventListener extends LoggingEventListener {

    @Override
    public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos, int[] updateCounts, SQLException e) {
        //忽略批量执行结果
    }

    @Override
    public void onAfterCommit(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
        super.onAfterCommit(connectionInformation, timeElapsedNanos, e);
    }

    @Override
    public void onAfterRollback(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
        super.onAfterRollback(connectionInformation, timeElapsedNanos, e);
    }
}
