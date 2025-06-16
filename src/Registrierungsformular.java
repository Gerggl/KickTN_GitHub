import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Registrierungsformular extends JFrame {
    JLabel lbl_ueberschrift;
    JTextField txt_vname, txt_nname, txt_benutzer, txt_email, txt_gebdatum, txt_adresse;
    JPasswordField txt_passwort, txt_passwort2;
    JCheckBox chk_agb;
    RoundedButton btn_absenden, btn_zuruecksetzen, btn_abbrechen;

    Color HINTERGRUND = new Color(245, 250, 255);
    Color BUTTONFARBE = new Color(15, 105, 140);
    Color HOVERFARBE = new Color(10, 85, 115);
    Color ROTEFARBE = new Color(200, 50, 50);
    Color ROTHOVERFARBE = new Color(160, 40, 40);

    public Registrierungsformular() {
        setTitle("Registrierungsformular");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.getContentPane().setBackground(HINTERGRUND);

        Font font_title = new Font("Segoe UI", Font.BOLD, 34);
        Font font_label = new Font("Segoe UI", Font.PLAIN, 16);

        // Überschrift
        lbl_ueberschrift = new JLabel("Benutzerregistrierung");
        lbl_ueberschrift.setFont(font_title);
        lbl_ueberschrift.setForeground(BUTTONFARBE);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(25, 10, 20, 10);
        c.anchor = GridBagConstraints.CENTER;
        this.add(lbl_ueberschrift, c);

        // Formularfelder
        c.gridwidth = 1;
        int row = 1;
        row = addLabelAndField("Vorname:", txt_vname = new JTextField(16), font_label, c, row);
        row = addLabelAndField("Nachname:", txt_nname = new JTextField(16), font_label, c, row);
        row = addLabelAndField("Benutzername:", txt_benutzer = new JTextField(16), font_label, c, row);
        row = addLabelAndField("Passwort:", txt_passwort = new JPasswordField(16), font_label, c, row);
        row = addLabelAndField("Passwort bestätigen:", txt_passwort2 = new JPasswordField(16), font_label, c, row);
        row = addLabelAndField("E-Mail:", txt_email = new JTextField(16), font_label, c, row);
        row = addLabelAndField("Geburtsdatum (JJJJ-MM-TT):", txt_gebdatum = new JTextField(16), font_label, c, row);
        row = addLabelAndField("Adresse:", txt_adresse = new JTextField(16), font_label, c, row);

        // AGB Checkbox
        chk_agb = new JCheckBox("Ich akzeptiere die AGB");
        chk_agb.setFont(font_label);
        chk_agb.setBackground(HINTERGRUND);
        c = position(c, 0, row++);
        c.gridwidth = 2;
        this.add(chk_agb, c);

        // Buttons
        btn_absenden = new RoundedButton("Registrieren", BUTTONFARBE, HOVERFARBE);
        btn_zuruecksetzen = new RoundedButton("Zurücksetzen", BUTTONFARBE, HOVERFARBE);
        btn_abbrechen = new RoundedButton("Abbrechen", ROTEFARBE, ROTHOVERFARBE);

        c = position(c, 0, row);
        this.add(btn_absenden, c);

        c = position(c, 1, row++);
        this.add(btn_zuruecksetzen, c);

        c = position(c, 0, row);
        c.gridwidth = 2;
        this.add(btn_abbrechen, c);

        // Hinweis: Schon registriert? Jetzt anmelden
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setBackground(HINTERGRUND);
        JLabel lblSchonRegistriert = new JLabel("Schon registriert?");
        lblSchonRegistriert.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton btnLogin = new JButton("Jetzt anmelden");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(BUTTONFARBE);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setFocusPainted(false);

        btnLogin.addActionListener(e -> {
            dispose();
            new Login(); // Deine Login-Klasse
        });

        loginPanel.add(lblSchonRegistriert);
        loginPanel.add(btnLogin);

        c = position(c, 0, ++row);
        c.gridwidth = 2;
        this.add(loginPanel, c);

        // Listener

        btn_absenden.addActionListener(e -> {
            if (!chk_agb.isSelected()) {
                JOptionPane.showMessageDialog(null, "Bitte akzeptiere die AGB.");
                return;
            }
            String p1 = new String(txt_passwort.getPassword());
            String p2 = new String(txt_passwort2.getPassword());
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(null, "Die Passwörter stimmen nicht überein.");
                return;
            }

            // Geburtsdatum parsen
            Date geburtsdatum;
            try {
                geburtsdatum = new SimpleDateFormat("yyyy-MM-dd").parse(txt_gebdatum.getText());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Bitte gib das Geburtsdatum im Format JJJJ-MM-TT ein.");
                return;
            }

            // Rolle festlegen (z.B. "Benutzer")
            String rolle = "Benutzer";

            boolean success = registriereBenutzer(
                    txt_benutzer.getText(),
                    p1,
                    txt_vname.getText(),
                    txt_nname.getText(),
                    txt_email.getText(),
                    geburtsdatum,
                    txt_adresse.getText(),
                    rolle
            );

            if (success) {
                JOptionPane.showMessageDialog(null, "Registrierung erfolgreich!");
                dispose();
                new Login();
            } else {
                JOptionPane.showMessageDialog(null, "Registrierung fehlgeschlagen! Bitte überprüfe deine Eingaben.");
            }
        });

        btn_zuruecksetzen.addActionListener(e -> {
            txt_vname.setText("");
            txt_nname.setText("");
            txt_benutzer.setText("");
            txt_passwort.setText("");
            txt_passwort2.setText("");
            txt_email.setText("");
            txt_gebdatum.setText("");
            txt_adresse.setText("");
            chk_agb.setSelected(false);
        });

        btn_abbrechen.addActionListener(e -> dispose());
    }

    private int addLabelAndField(String label, JTextField field, Font font, GridBagConstraints c, int row) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(font);
        field.setFont(font);
        c = position(c, 0, row);
        this.add(lbl, c);
        c = position(c, 1, row);
        this.add(field, c);
        return row + 1;
    }

    private GridBagConstraints position(GridBagConstraints c, int x, int y) {
        c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        return c;
    }

    public boolean registriereBenutzer(String benutzername, String passwort, String vorname, String nachname,
                                       String email, Date geburtsdatum, String adresse, String rolle) {
        String sql = "INSERT INTO benutzer_bereitsregistriert (benutzername, passwort, vorname, nachname, email, geburtsdatum, adresse, rolle) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, benutzername);
            pstmt.setString(2, passwort);  // ACHTUNG: besser vorher hashen!
            pstmt.setString(3, vorname);
            pstmt.setString(4, nachname);
            pstmt.setString(5, email);
            pstmt.setDate(6, new java.sql.Date(geburtsdatum.getTime()));
            pstmt.setString(7, adresse);
            pstmt.setString(8, rolle);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3307/kicktn_db"; // DB-URL anpassen
        String user = "root"; // DB-User anpassen
        String pass = ""; // DB-Passwort anpassen
        return DriverManager.getConnection(url, user, pass);
    }

    // ----------- ROUNDED BUTTON CLASS ----------- //
    class RoundedButton extends JButton {
        private final Color normalColor;
        private final Color hoverColor;
        private boolean hovered = false;

        public RoundedButton(String text, Color normalColor, Color hoverColor) {
            super(text);
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setPreferredSize(new Dimension(0, 45));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? hoverColor : normalColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Kein sichtbarer Rand
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Registrierungsformular::new);
    }
}