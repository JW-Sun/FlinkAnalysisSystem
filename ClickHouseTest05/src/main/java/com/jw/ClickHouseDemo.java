package com.jw;

import java.sql.*;

public class ClickHouseDemo {
    public static void main(String[] args) {
        String address = "jdbc:clickhouse://192.168.159.102:8123/default";
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
            connection = DriverManager.getConnection(address);
            String sql = "INSERT INTO youfantest (id, name, create_date) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "bbb");
            preparedStatement.setString(3, "2020-05-14");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }
}
