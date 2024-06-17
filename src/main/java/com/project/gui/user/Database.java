package com.project.gui.user;

import com.project.constants.Constants;
import com.project.gui.dashboard.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.DataSource;
import java.sql.*;
import java.time.YearMonth;

public class Database{
    private static DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Constants.JDBC_URL);
        config.setUsername(Constants.JDBC_USER);
        config.setPassword(Constants.JDBC_PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(30);
        dataSource = new HikariDataSource(config);
    }

    public static boolean addUser(String email, String password) {
        String hashedPassword = PasswordManager.hashPassword(password);
        String query = "INSERT INTO users (email, password) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addFaceId(String email, String faceId) {
        String query = "SELECT id FROM users WHERE email = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                String insertQuery = "INSERT INTO facerecord (faceid, user_id) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, faceId);
                    insertStmt.setInt(2, userId);
                    insertStmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String predictFace(String faceId) {
        String query = "SELECT email FROM users JOIN facerecord ON users.id = facerecord.user_id WHERE faceid = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, faceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");
                System.out.println("Predicted email: " + email);
                return email;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void markAttendance(String faceId) {
        String query = "SELECT id FROM users JOIN facerecord ON users.id = facerecord.user_id WHERE faceid = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, faceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                String insertQuery = "INSERT INTO AttendanceRecords (user_id, check_in) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    insertStmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createUsersTable(Connection con ,Statement statement){

        try {
            statement = con.createStatement();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "Users", null);
            String query = "CREATE TABLE Users (id INT AUTO_INCREMENT PRIMARY KEY, email VARCHAR(100), password VARCHAR(100) , isAdmin TINYINT(1) NOT NULL DEFAULT 0)";
            if(tables.next()){
                System.out.println("Table already exists");
                return;
            } else {
                statement.execute(query);
                System.out.println("Table created");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int getUserId(String email) {
        String query = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getEmail(int id) {
        String query = "SELECT email FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int verifyUser(String email, String password){
        System.out.println("verifyUser called");
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if(PasswordManager.verifyPassword(password, hashedPassword)){
                    if(rs.getBoolean("isAdmin")){
                        System.out.println("User is verified admin");
                        return 2;
                    } else {
                        System.out.println("User is verified client");
                        return 1;
                    }
                }else {
                    System.out.println("User is not verified");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
    }
        return -1;
}

    public static boolean addAdmin (String email, String password){
        String hashedPassword = PasswordManager.hashPassword(password);
        String query = "INSERT INTO users (email, password, isAdmin) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            stmt.setBoolean(3, true);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean changePassword(String email, String password) {
        System.out.println("changePassword called");
        String hashedPassword = PasswordManager.hashPassword(password);
        String query = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //cal
    public static double calculateAttendancePercentage(int userId, int year, int month) {

            YearMonth yearMonthObject = YearMonth.of(year, month);
            int daysInMonth = yearMonthObject.lengthOfMonth();

            String query = "SELECT COUNT(DISTINCT DATE(check_in)) AS days_present FROM AttendanceRecords " +
                    "WHERE user_id = ? AND MONTH(check_in) = ? AND YEAR(check_in) = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int daysPresent = rs.getInt("days_present");
                    double attendancePercentage = (double) daysPresent / daysInMonth * 100;
                    System.out.printf("Employee ID %d has an attendance percentage of %.2f%% in %d/%d.\n",
                            userId, attendancePercentage, month, year);
                    return attendancePercentage;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static ObservableList<User> getAttendanceRecords(int year, int month) {
        String query = "SELECT users.email, COUNT(DISTINCT DATE(check_in)) AS days_present " +
                "FROM AttendanceRecords JOIN users ON users.id = AttendanceRecords.user_id " +
                "WHERE MONTH(check_in) = ? AND YEAR(check_in) = ? " +
                "GROUP BY users.email";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

            ObservableList<User> users = FXCollections.observableArrayList();

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String email = rs.getString("email");
                    int daysPresent = rs.getInt("days_present");
                    double attendancePercentage = (double) daysPresent / YearMonth.of(year, month).lengthOfMonth() * 100;
                    users.add(new User(email, attendancePercentage));
                    System.out.printf("Employee %s has an attendance percentage of %.2f%% in %d/%d.\n",
                            email, attendancePercentage, month, year);
                }
            }

            return users;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    // main
    public static void main(String[] args) {
           verifyUser("www.vyomkumar@gmail.com","vyom7482");
           calculateAttendancePercentage(1,2023,4);
    }

}



