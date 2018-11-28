package com.hongguo.jdbc;

import com.hongguo.jdbc.domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;

/**
 * @auther: chenghongguo
 * @date: 2018-11-13
 * @description:
 */
public class SimpleApp {

    private Connection connection;

    private Statement statement;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    @Before
    public void before() {
        connection = JDBCUtil.getConnection();
    }

    @After
    public void after() {
        JDBCUtil.closeResources(connection, statement, preparedStatement, resultSet);
    }

    @Test
    public void testQuery() {
        try {
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            boolean ok = statement.execute("select* from t_user");
            if (ok) {
                resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    System.out.println(resultSet.getObject(1) + ", " + resultSet.getObject(2)
                            + ", " + resultSet.getObject(3) + ", " + resultSet.getObject(4)
                            + ", " + resultSet.getObject(5));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPreparedStatement() {
        try {
//            preparedStatement = connection.prepareStatement("select * from t_user where id > ?",
//                    ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_UPDATABLE);
            preparedStatement = connection.prepareStatement("select * from t_user where id > ?");
            preparedStatement.setObject(1, "40");
            boolean ok = preparedStatement.execute();
            if (ok) {
                resultSet = preparedStatement.getResultSet();
                System.out.println(resultSet.first());
                while (resultSet.next()) {
                    System.out.println(resultSet.getObject(1) + ", " + resultSet.getObject(2)
                            + ", " + resultSet.getObject(3) + ", " + resultSet.getObject(4)
                            + ", " + resultSet.getObject(5));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBatchUpdate() {
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.addBatch("insert into t_user (`user_name`, `user_gender`, `user_salary`, `user_birthday`) values ('test01', '1', '100.00', now())");
            statement.addBatch("insert into t_user (`user_name`, `user_gender`, `user_salary`, `user_birthday`) values ('test02', '0', '100.00', now())");
            statement.addBatch("insert into t_user (`user_name`, `user_gender`, `user_salary`, `user_birthday`) values ('test03', '1', '100.00', now())");
            int[] batch = statement.executeBatch();
            for (int i = 0; i < batch.length; i++) {
                System.out.println(batch[i]);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectOne() {
        CommonQuery query = new CommonQuery();
        String sql = "SELECT id, user_name userName, user_gender userGender, user_salary, user_birthday userBirthday" +
                " FROM t_user where id = ?";
        User user = query.selectOne(User.class, sql, "22");
        System.out.println(user);
    }

    @Test
    public void testSelectList() {
        CommonQuery query = new CommonQuery();
        String sql = "SELECT id, user_name userName, user_gender userGender, user_salary userSalary, user_birthday userBirthday" +
                " FROM t_user where id > ?";
        List<User> users = query.selectList(User.class, sql, "40");
        System.out.println(users);
    }
}
