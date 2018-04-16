package org.quickweb.modal.handler;

import org.quickweb.QuickWeb;
import org.quickweb.modal.*;
import org.quickweb.modal.param.ResultType;
import org.quickweb.modal.param.SqlParam;
import org.quickweb.modal.param.SqlParamHelper;
import org.quickweb.modal.param.StmtHelper;
import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.EmptyUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryHandler {

    public static void find(QuickSession quickSession, String paramName,
                            SqlParam sqlParam) throws SQLException {
        findAction(quickSession, sqlParam, (stmt, rs) -> {
            List<Map<String, Object>> rowList = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object obj = rs.getObject(i);
                    rowMap.put(metaData.getColumnLabel(i), obj);
                }
                rowList.add(rowMap);
            }
            quickSession.putParam(paramName, rowList);
        });
    }

    public static void find(QuickSession quickSession, SqlParam sqlParam,
                            OnQueryResult onQueryResult) throws SQLException {
        findAction(quickSession, sqlParam, (stmt, rs) ->
                onQueryResult.onResult(rs, quickSession));
    }

    public static void findFirst(QuickSession quickSession, String paramName,
                                 SqlParam sqlParam) throws SQLException {
        findAction(quickSession, sqlParam, (stmt, rs) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            Map<String, Object> rowMap = new HashMap<>();
            if (rs.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object obj = rs.getObject(i);
                    rowMap.put(metaData.getColumnLabel(i), obj);
                }
            }
            quickSession.putParam(paramName, rowMap);
        });
    }

    public static void count(QuickSession quickSession, String paramName,
                             SqlParam sqlParam) throws SQLException {
        aggregateAction(quickSession, paramName, "*", sqlParam, "COUNT", ResultType.INT);
    }

    public static void avg(QuickSession quickSession, String paramName, String column,
                           SqlParam sqlParam) throws SQLException {
        aggregateAction(quickSession, paramName, column, sqlParam, "AVG", ResultType.DOUBLE);
    }

    public static void max(QuickSession quickSession, String paramName, String column,
                           SqlParam sqlParam, ResultType resultType) throws SQLException {
        aggregateAction(quickSession, paramName, column, sqlParam, "MAX", resultType);
    }

    public static void min(QuickSession quickSession, String paramName, String column,
                           SqlParam sqlParam, ResultType resultType) throws SQLException {
        aggregateAction(quickSession, paramName, column, sqlParam, "MIN", resultType);
    }

    public static void sum(QuickSession quickSession, String paramName, String column,
                           SqlParam sqlParam, ResultType resultType) throws SQLException {
        aggregateAction(quickSession, paramName, column, sqlParam, "SUM", resultType);
    }

    /**
     * 处理查询操作
     */
    private static void findAction(QuickSession quickSession, SqlParam sqlParam,
                                   QueryAction action) throws SQLException {
        if (EmptyUtils.isEmpty(sqlParam.getColumns()))
            sqlParam.setColumns(new String[]{"*"});

        SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);

        String sql = QuickWeb.getSqlBuilder().find(helper);

        DataHandler.handle(quickSession, sql, (stmt) -> {
            new StmtHelper(quickSession, stmt)
                    .setParams(helper.getColumnValues())
                    .setParams(helper.getConditionValues())
                    .setParams(helper.getOrderValues());

            ResultSet rs = stmt.executeQuery();
            action.act(stmt, rs);
            rs.close();
        });
    }

    /**
     * 处理聚合函数操作
     */
    private static void aggregateAction(QuickSession quickSession, String paramName,
                                        String column, SqlParam sqlParam,
                                        String method, ResultType resultType) throws SQLException {
        column = TemplateExpr.getString(quickSession, column);

        sqlParam.setColumns(new String[]{method + "(" + column + ")"});

        findAction(quickSession, sqlParam, (stmt, rs) -> {
            if (rs.next()) {
                Object value;
                if (resultType == null)
                    value = rs.getObject(1);
                else
                    value = rs.getObject(1, resultType.getType());

                quickSession.putParam(paramName, value);
            }
        });
    }
}
