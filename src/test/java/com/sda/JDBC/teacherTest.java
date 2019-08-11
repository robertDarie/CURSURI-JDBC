package com.sda.JDBC;

import org.junit.Test;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class teacherTest {

//    CREATE TABLE IF NOT EXISTS teacher (
//            id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
//            first_name VARCHAR(255) NOT NULL,
//    last_name VARCHAR(255) NOT NULL,
//    hire_date DATE


    private String user = "root";
    private String password = "02201999r";
    private String url = "jdbc:mysql://localhost:3306/courses?useSSL=false";


    @Test
    public void createTeacher() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("d-MMM-yyyy");
        LocalDate birthDate = format
                .parse("12-Feb-1985")
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        String sqlString = "insert into teacher(first_name, last_name, hire_date)" +
                " values ('Ionel', 'Popescu',  '2007-09-23')";
        //in varianta  try with resources ,resursele sunt inchise automat
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement statment = conn.createStatement()) {
            //get transaction-change default behaviour of transaction
            conn.setAutoCommit(false);
            //cate randuri sunt afectate
            int res = statment.executeUpdate(sqlString);
            //commit transaction
            conn.commit();
        }
    }

    @Test
    public void deleteTeacher() throws SQLException {
        String sqlString = "DELETE FROM teacher WHERE id = ?";

        //utilizam try-ul pentru a exemplifica incheierea resurselor


        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sqlString);
            conn.setAutoCommit(false);
            stmt.setInt(1, 2);
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
        String sqlMaxId = "Select max(id) from teacher";
        String deletedLastRow = "delete from teacher where id =?";
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
        String sqlString = "select * from teacher";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            //pentru SELECT(READ) nu este nevoie de o tranzactie
            ResultSet rs = stmt.executeQuery(sqlString);
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                Date date = rs.getDate("hire_date");

                System.out.println("Prof: " + id + " " + firstName + " " + lastName + " " + date);
            }
        }
    }
}
