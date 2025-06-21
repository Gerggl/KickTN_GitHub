import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class BenutzerDAO {

    public static void registriereBenutzer(String vorname, String nachname, String benutzername,
                                           String passwort, String email, LocalDate geburtsdatum) throws SQLException {
        String sql = "INSERT INTO benutzerkicktn (vorname, nachname, benutzername, passwort, email, geburtsdatum) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vorname);
            stmt.setString(2, nachname);
            stmt.setString(3, benutzername);
            stmt.setString(4, passwort);
            stmt.setString(5, email);
            stmt.setDate(6, Date.valueOf(geburtsdatum));

            stmt.executeUpdate();
        }
    }
}