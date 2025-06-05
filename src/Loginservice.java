import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Loginservice {

    public static boolean pruefeLogin(String benutzer, String passwort) {
        String sql = "SELECT COUNT(*) FROM benutzer_bereitsregistriert WHERE Benutzername = ? AND Passwort = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, benutzer);
            stmt.setString(2, passwort);  // Achtung: Passwörter lieber gehashed speichern!

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}