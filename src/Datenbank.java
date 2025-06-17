import java.sql.*;

public class Datenbank {
    private static final String URL = "jdbc:mysql://localhost:3307/kicktn_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}