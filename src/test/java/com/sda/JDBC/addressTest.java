package com.sda.JDBC;

import org.junit.Test;

import java.sql.*;
import java.util.Date;

public class addressTest {


    private String user = "root";
    private String password = "02201999r";
    private String url = "jdbc:mysql://localhost:3306/courses?useSSL=false";


    @Test
    public void createAddress() throws SQLException {

        String sqlString = "insert into address(city, street )" +
                " values ('Bucuresti', 'Bd. Eroilor')";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement statment = conn.createStatement()) {
            conn.setAutoCommit(false);
            int res = statment.executeUpdate(sqlString);
            conn.commit();
        }
    }

    @Test
    public void deleteAdress() throws SQLException {
        String sqlString = "DELETE FROM address WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sqlString);
            conn.setAutoCommit(false);
            stmt.setInt(1, 3);
            int res = stmt.executeUpdate();
            conn.commit();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testDeleteLastRow() throws SQLException {
        String sqlMaxId = "Select max(id) from address";
        String deletedLastRow = "delete from address where id =?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sqlMaxId);
             PreparedStatement stmt2 = conn.prepareStatement(deletedLastRow)
        ) {
            int maxId = 0;
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                maxId = rs.getInt(1);
            }
            stmt2.setInt(1, maxId);
            int res = stmt2.executeUpdate();
            System.out.println("no affect rows =" + res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testSelect() throws SQLException {
        String sqlString = "select * from address";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sqlString);
            while (rs.next()) {
                int id = rs.getInt("id");
                String city = rs.getString("city");
                String street = rs.getString("street");

                System.out.println("Adresa: " + id + " " + city + " " + street);
            }
        }
    }
}
