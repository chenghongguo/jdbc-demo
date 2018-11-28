package com.hongguo.jdbc;

import org.apache.commons.beanutils.BeanUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther: chenghongguo
 * @date: 2018-11-19
 * @description:
 */
public class CommonQuery {

    public <T> T selectOne(Class<T> clazz, String sql, Object... params) {
        List<T> result = selectList(clazz, sql, params);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }


    public <T> List<T> selectList(Class<T> clazz, String sql, Object... params) {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> values = handleResultSetToMapList(resultSet);
            list = transferMapListToBeanList(clazz, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResources(connection, null, preparedStatement, resultSet);
        }
        return list;
    }

    private <T> List<T> transferMapListToBeanList(Class<T> clazz, List<Map<String, Object>> values) throws Exception {
        List<T> result = new ArrayList<>();
        T bean = null;
        if (values.size() > 0) {
            for (Map<String, Object> m : values) {
                bean = clazz.newInstance();
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    String propertyName = entry.getKey();
                    Object value = entry.getValue();
                    BeanUtils.setProperty(bean, propertyName, value);
                }
                result.add(bean);
            }
        }
        return result;
    }

    private List<Map<String, Object>> handleResultSetToMapList(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> values = new ArrayList<>();
        List<String> labels = getColumnLabels(resultSet);
        Map<String, Object> map = null;
        while (resultSet.next()) {
            map = new HashMap<>();
            for (String columnLabel : labels) {
                map.put(columnLabel, resultSet.getObject(columnLabel));
            }
            values.add(map);
        }
        return values;
    }

    private List<String> getColumnLabels(ResultSet resultSet) throws SQLException {
        List<String> labels = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            labels.add(metaData.getColumnLabel(i + 1));
        }
        return labels;
    }
}
