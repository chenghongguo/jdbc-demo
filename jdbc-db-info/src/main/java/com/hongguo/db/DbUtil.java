package com.hongguo.db;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author: chenghongguo
 * @date: 2018-11-27
 * @description:
 */
public class DbUtil {

    public static Connection getConnection() {
        InputStream resource = DbTest.class.getResourceAsStream("/jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(resource);
            Class.forName(properties.getProperty("jdbc.driver-class"));
            Connection connection = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<TableInfo> getTableInfoList() {
        List<TableInfo> tableInfoList = null;
        Connection connection = getConnection();
        ResultSet databaseResultSet = null;
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            databaseResultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
            tableInfoList = new ArrayList<>();
            while (databaseResultSet.next()) {
                String tableName = databaseResultSet.getString("TABLE_NAME");
                TableInfo tableInfo = TableInfo.builder().name(tableName).columnInfoList(getColumnInfoList(connection, tableName))
                        .primaryKeyColumnInfoList(getPrimaryKeyColumnInfo(databaseMetaData, tableName))
                        .indexColumnInfoList(getIndexColumnInfo(databaseMetaData, tableName)).build();
                tableInfoList.add(tableInfo);
            }
            return tableInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(connection, null, databaseResultSet);
        }
        return tableInfoList;
    }

    /**
     * 获取表中所有主键
     *
     * @param databaseMetaData
     * @param tableName
     * @return
     */
    private static List<PrimaryKeyColumnInfo> getPrimaryKeyColumnInfo(DatabaseMetaData databaseMetaData, String tableName) {
        List<PrimaryKeyColumnInfo> columnInfoList = new ArrayList<>();
        ResultSet primaryKeys = null;
        try {
            primaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName);
            while (primaryKeys.next()) {
                PrimaryKeyColumnInfo columnInfo = PrimaryKeyColumnInfo.builder().columnName(primaryKeys.getString("COLUMN_NAME")).build();
                columnInfoList.add(columnInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, null, primaryKeys);
        }
        return columnInfoList;
    }

    /**
     * 获取表中所有索引
     *
     * @param databaseMetaData
     * @param tableName
     * @return
     */
    private static List<IndexColumnInfo> getIndexColumnInfo(DatabaseMetaData databaseMetaData, String tableName) {
        List<IndexColumnInfo> columnInfoList = new ArrayList<>();
        ResultSet indexResultSet = null;
        try {
            indexResultSet = databaseMetaData.getIndexInfo(null, null, tableName, false, false);
            while (indexResultSet.next()) {
                IndexColumnInfo columnInfo = IndexColumnInfo.builder().columnName(indexResultSet.getString("COLUMN_NAME")).build();
                columnInfoList.add(columnInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, null, indexResultSet);
        }
        return columnInfoList;
    }

    /**
     * 获取所有普通列信息
     *
     * @param connection
     * @param tableName
     * @return
     */
    private static List<ColumnInfo> getColumnInfoList(Connection connection, String tableName) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement preparedStatement = null;
        ResultSet tableResultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            tableResultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = tableResultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                ColumnInfo columnInfo = ColumnInfo.builder().name(resultSetMetaData.getColumnName(i + 1)).typeId(Integer.valueOf(resultSetMetaData.getColumnType(i + 1)))
                        .typeName(resultSetMetaData.getColumnTypeName(i + 1)).size(Integer.valueOf(resultSetMetaData.getColumnDisplaySize(i + 1))).build();
                columnInfoList.add(columnInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, preparedStatement, tableResultSet);
        }
        return columnInfoList;

    }

    public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet... resultSets) {
        if (resultSets.length != 0) {
            for (int i = 0; i < resultSets.length; i++) {
                try {
                    resultSets[i].close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
