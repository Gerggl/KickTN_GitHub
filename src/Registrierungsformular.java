import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.*;

public class Registrierungsformular extends JFrame {
    // Attribute
    JLabel lbl_ueberschrift, lbl_vorname, lbl_nachname, lbl_benutzer, lbl_passwort, lbl_passwortBestaetigen,
            lbl_email, lbl_geburtsdatum;
    JTextField txt_vorname, txt_nachname, txt_benutzer, txt_email, txt_geburtsdatum;
    JPasswordField txt_passwort, txt_passwortBestaetigen;
    JCheckBox chk_agb;
    RoundedButton btn_registrieren, btn_zuruecksetzen, btn_beenden, toggleModeButton;
    JPanel agbPanel;
    JLabel agbText;
    JLabel lblNochNichtRegistriert;
    JButton btn_bereitsRegistriert;

    // Farben für den Style
    private Color buttonColor;
    private Color hoverColor;
    private Color redColor;
    private Color redHoverColor;
    private Color bgColor;
    private Color textColor;
    private Color headerBgColor;

    private boolean darkMode = false;

    // individueller Konstruktor
    public Registrierungsformular() {
        initColors();

        UIManager.put("control", bgColor);
        UIManager.put("info", bgColor);
        UIManager.put("nimbusBase", bgColor);
        UIManager.put("nimbusLightBackground", bgColor);
        UIManager.put("text", textColor);

        this.setTitle("KickTN Registrierung");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(bgColor);

        initComponents();

        this.pack();
        this.validate();
        this.repaint();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(540, 700)); // etwas größer als Login
    }

    private void initColors() {
        if (darkMode == true) {
            bgColor = new Color(40, 44, 52);
            headerBgColor = new Color(40, 44, 52);
            textColor = new Color(230, 230, 230);
            buttonColor = new Color(45, 156, 219);
            hoverColor = new Color(35, 130, 180);
            redColor = new Color(200, 60, 60);
            redHoverColor = new Color(160, 40, 40);
        } else {
            bgColor = new Color(245, 250, 255);
            headerBgColor = new Color(245, 250, 255);
            textColor = Color.BLACK;
            buttonColor = new Color(15, 105, 140);
            hoverColor = new Color(10, 85, 115);
            redColor = new Color(200, 50, 50);
            redHoverColor = new Color(160, 40, 40);
        }
    }

    // Style für die Textfelder
    private void styleTextField(JTextField field) {
        RoundedLineBorder rounded15Border = new RoundedLineBorder(buttonColor, 1, 15);
        RoundedLineBorder focus15Border = new RoundedLineBorder(hoverColor, 1, 15);
        Border padding = BorderFactory.createEmptyBorder(8, 12, 8, 12);

        field.setBorder(BorderFactory.createCompoundBorder(
                rounded15Border,
                padding));
        field.setBackground(bgColor);
        field.setForeground(textColor);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setCaretColor(textColor);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        focus15Border,
                        padding));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        rounded15Border,
                        padding));
            }
        });
    }

    // Methode initComponents() legt die Formularfelder fest
    private void initComponents() {
        this.setLayout(new GridBagLayout());
        // GridBagConstraints für den Abstand zwischen den Elementen
        GridBagConstraints gbc = new GridBagConstraints();

        toggleModeButton = new RoundedButton(darkMode ? "Light Mode" : "Dark Mode", buttonColor, hoverColor);
        toggleModeButton.setPreferredSize(new Dimension(120, 40));
        toggleModeButton.addActionListener(e -> toggleTheme());
        toggleModeButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(headerBgColor);
        headerPanel.add(toggleModeButton, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 10, 0, 10);
        this.add(headerPanel, gbc);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 32);

        lbl_ueberschrift = new JLabel("Registrieren bei KickTN");
        lbl_ueberschrift.setFont(titleFont);
        lbl_ueberschrift.setForeground(buttonColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 30, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(lbl_ueberschrift, gbc);

        // Vorname
        lbl_vorname = new JLabel("Vorname:");
        lbl_vorname.setFont(labelFont);
        lbl_vorname.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        this.add(lbl_vorname, gbc);

        txt_vorname = new JTextField(14);
        styleTextField(txt_vorname);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(txt_vorname, gbc);

        // Nachname
        lbl_nachname = new JLabel("Nachname:");
        lbl_nachname.setFont(labelFont);
        lbl_nachname.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        this.add(lbl_nachname, gbc);

        txt_nachname = new JTextField(16);
        styleTextField(txt_nachname);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(txt_nachname, gbc);

        // Benutzername
        lbl_benutzer = new JLabel("Benutzername:");
        lbl_benutzer.setFont(labelFont);
        lbl_benutzer.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        this.add(lbl_benutzer, gbc);

        txt_benutzer = new JTextField(16);
        styleTextField(txt_benutzer);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(txt_benutzer, gbc);

        // Passwort
        lbl_passwort = new JLabel("Passwort:");
        lbl_passwort.setFont(labelFont);
        lbl_passwort.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        this.add(lbl_passwort, gbc);

        txt_passwort = new JPasswordField(16);
        styleTextField(txt_passwort);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(txt_passwort, gbc);

        // Passwort bestätigen
        lbl_passwortBestaetigen = new JLabel("Passwort bestätigen:");
        lbl_passwortBestaetigen.setFont(labelFont);
        lbl_passwortBestaetigen.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        this.add(lbl_passwortBestaetigen, gbc);

        txt_passwortBestaetigen = new JPasswordField(16);
        styleTextField(txt_passwortBestaetigen);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(txt_passwortBestaetigen, gbc);

        // E-Mail
        lbl_email = new JLabel("E-Mail:");
        lbl_email.setFont(labelFont);
        lbl_email.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        this.add(lbl_email, gbc);

        txt_email = new JTextField(16);
        styleTextField(txt_email);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(txt_email, gbc);

        // Geburtsdatum
        lbl_geburtsdatum = new JLabel("Geburtsdatum (JJJJ-MM-TT):");
        lbl_geburtsdatum.setFont(labelFont);
        lbl_geburtsdatum.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        this.add(lbl_geburtsdatum, gbc);

        txt_geburtsdatum = new JTextField(16);
        styleTextField(txt_geburtsdatum);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(txt_geburtsdatum, gbc);

        // AGB Checkbox
        agbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        agbPanel.setBackground(bgColor);
        chk_agb = new JCheckBox();
        chk_agb.setBackground(bgColor);
        agbText = new JLabel("Ich akzeptiere die AGB");
        agbText.setFont(labelFont);
        agbText.setForeground(textColor);
        agbPanel.add(chk_agb);
        agbPanel.add(agbText);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        this.add(agbPanel, gbc);

        // Buttons
        btn_registrieren = new RoundedButton("Registrieren", buttonColor, hoverColor);
        btn_zuruecksetzen = new RoundedButton("Zurücksetzen", buttonColor, hoverColor);
        btn_beenden = new RoundedButton("Beenden", redColor, redHoverColor);

        Dimension buttonSize = new Dimension(0, 50);
        btn_registrieren.setPreferredSize(buttonSize);
        btn_zuruecksetzen.setPreferredSize(buttonSize);
        btn_beenden.setPreferredSize(buttonSize);

        btn_beenden.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn_zuruecksetzen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn_registrieren.setFont(new Font("Segoe UI", Font.BOLD, 14));

        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 11;
        this.add(btn_registrieren, gbc);

        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 1;
        gbc.gridy = 11;
        this.add(btn_zuruecksetzen, gbc);

        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        this.add(btn_beenden, gbc);

        // Button "Bereits registriert"
        btn_bereitsRegistriert = new JButton("Bereits registriert");
        btn_bereitsRegistriert.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn_bereitsRegistriert.setForeground(buttonColor);
        btn_bereitsRegistriert.setBorderPainted(false);
        btn_bereitsRegistriert.setContentAreaFilled(false);
        btn_bereitsRegistriert.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_bereitsRegistriert.setFocusPainted(false);

        lblNochNichtRegistriert = new JLabel("Noch nicht registriert?");
        lblNochNichtRegistriert.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNochNichtRegistriert.setForeground(textColor);

        JPanel registriertPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registriertPanel.setBackground(bgColor);
        registriertPanel.add(lblNochNichtRegistriert);
        registriertPanel.add(btn_bereitsRegistriert);

        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 10, 20, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(registriertPanel, gbc);

        // Label "Noch nicht registriert?"
        lblNochNichtRegistriert = new JLabel("Noch nicht registriert?");
        lblNochNichtRegistriert.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNochNichtRegistriert.setForeground(textColor);
        lblNochNichtRegistriert.setOpaque(true);
        lblNochNichtRegistriert.setBackground(bgColor);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 10, 10, 5);
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(lblNochNichtRegistriert, gbc);

        // Button "Bereits registriert"
        lblNochNichtRegistriert = new JLabel("Noch nicht registriert?");
        lblNochNichtRegistriert.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNochNichtRegistriert.setForeground(textColor);
        lblNochNichtRegistriert.setOpaque(true);
        lblNochNichtRegistriert.setBackground(bgColor);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 10, 10, 5);
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(lblNochNichtRegistriert, gbc);

        // ActionListener für den "Bereits registriert"-Button
        btn_bereitsRegistriert.addActionListener(e -> {
            new Login();
        });

        // ActionListener für Beenden Button
        btn_beenden.addActionListener(e -> System.exit(0));

        // ActionListener für Zurücksetzen Button
        btn_zuruecksetzen.addActionListener(e -> {
            txt_vorname.setText("");
            txt_nachname.setText("");
            txt_benutzer.setText("");
            txt_passwort.setText("");
            txt_passwortBestaetigen.setText("");
            txt_email.setText("");
            txt_geburtsdatum.setText("");
            chk_agb.setSelected(false);
        });

        // ActionListener für Registrieren Button
        btn_registrieren.addActionListener(e -> {
            if (chk_agb.isSelected() == false) {
                JOptionPane.showMessageDialog(this, "Bitte akzeptiere die AGB!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String vorname = txt_vorname.getText();
            String nachname = txt_nachname.getText();
            String benutzer = txt_benutzer.getText();
            String passwort = new String(txt_passwort.getPassword());
            String passwortBestaetigen = new String(txt_passwortBestaetigen.getPassword());
            String email = txt_email.getText();
            String geburtsdatum = txt_geburtsdatum.getText();

            if (!passwort.equals(passwortBestaetigen)) {
                JOptionPane.showMessageDialog(this, "Die Passwörter stimmen nicht überein!", "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Prüfung, ob die Felder leer sind oder nicht -> wenn ja, dann Fehlermeldung
            // ausgeben
            if (vorname.isEmpty() || nachname.isEmpty() || benutzer.isEmpty() || email.isEmpty()
                    || geburtsdatum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte leere Felder ausfüllen!", "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Daten in die Datenbank speichern
            try {
                LocalDate geburtsdatumParsed = LocalDate.parse(txt_geburtsdatum.getText());

                char[] passwortArray = txt_passwort.getPassword();
                passwort = new String(passwortArray);

                BenutzerDAO.registriereBenutzer(
                        txt_vorname.getText(),
                        txt_nachname.getText(),
                        txt_benutzer.getText(),
                        passwort,
                        txt_email.getText(),
                        geburtsdatumParsed);

                Arrays.fill(passwortArray, ' ');

                JOptionPane.showMessageDialog(this, "Registrierung erfolgreich!", "Erfolg",
                        JOptionPane.INFORMATION_MESSAGE);

                // Formularfelder zurücksetzen
                txt_vorname.setText("");
                txt_nachname.setText("");
                txt_benutzer.setText("");
                txt_email.setText("");
                txt_geburtsdatum.setText("");
                txt_passwort.setText("");
                txt_passwortBestaetigen.setText("");
                chk_agb.setSelected(false);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Registrierung: " + ex.getMessage(),
                        "Datenbankfehler", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Geburtsdatum ist ungültig (Format: JJJJ-MM-TT)", "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
        JRootPane rootPane = this.getRootPane();
        rootPane.setDefaultButton(btn_registrieren);

    }

    // Dark-Mode
    private void toggleTheme() {
        darkMode = !darkMode;
        initColors();

        this.getContentPane().setBackground(bgColor);

        lbl_ueberschrift.setForeground(buttonColor);

        lbl_vorname.setForeground(textColor);
        lbl_nachname.setForeground(textColor);
        lbl_benutzer.setForeground(textColor);
        lbl_passwort.setForeground(textColor);
        lbl_passwortBestaetigen.setForeground(textColor);
        lbl_email.setForeground(textColor);
        lbl_geburtsdatum.setForeground(textColor);

        JTextField[] fields = { txt_vorname, txt_nachname, txt_benutzer, txt_email, txt_geburtsdatum };
        for (JTextField f : fields) {
            f.setBackground(bgColor);
            f.setForeground(textColor);
            f.setCaretColor(textColor);
        }

        JPasswordField[] passFields = { txt_passwort, txt_passwortBestaetigen };
        for (JPasswordField pf : passFields) {
            pf.setBackground(bgColor);
            pf.setForeground(textColor);
            pf.setCaretColor(textColor);
        }

        chk_agb.setBackground(bgColor);
        chk_agb.setForeground(textColor);

        btn_registrieren.setBackground(buttonColor);
        btn_registrieren.setForeground(Color.WHITE);

        btn_zuruecksetzen.setBackground(buttonColor);
        btn_zuruecksetzen.setForeground(Color.WHITE);

        btn_beenden.setBackground(redColor);
        btn_beenden.setForeground(Color.WHITE);

        btn_bereitsRegistriert.setForeground(buttonColor);
        btn_bereitsRegistriert.setBackground(bgColor);

        toggleModeButton.setText(darkMode ? "Light Mode" : "Dark Mode");
        toggleModeButton.setBackground(buttonColor);
        toggleModeButton.setForeground(Color.WHITE);

        if (getContentPane().getComponentCount() > 0) {
            Component comp = getContentPane().getComponent(0);
            if (comp instanceof JPanel) {
                comp.setBackground(headerBgColor);
            }
        }

        for (JTextField f : fields) {
            styleTextField(f);
        }
        for (JPasswordField pf : passFields) {
            styleTextField(pf);
        }

        this.repaint();

        if (agbText != null) {
            agbText.setForeground(textColor);
            agbPanel.setBackground(bgColor);
        }

        lblNochNichtRegistriert.setBackground(Color.BLACK);
        btn_bereitsRegistriert.setBackground(Color.BLACK);

        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setBackground(bgColor);
                for (Component c : panel.getComponents()) {
                    if (c instanceof JLabel lbl)
                        lbl.setForeground(textColor);
                    if (c instanceof JButton btn && btn != toggleModeButton)
                        btn.setForeground(buttonColor);
                }
            }
        }

    }

    private static class RoundedButton extends JButton {
        private Color normalColor;
        private Color hoverColor;

        public RoundedButton(String text, Color normalColor, Color hoverColor) {
            super(text);
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;

            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 20));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    setBackground(normalColor);
                    repaint();
                }
            });

            setBackground(normalColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            normalColor = bg;
        }
    }

    private static class RoundedLineBorder extends AbstractBorder {
        private Color color;
        private int thickness = 1;
        private int radius = 15;

        public RoundedLineBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x + thickness / 2, y + thickness / 2,
                    width - thickness, height - thickness, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness + radius / 4, thickness + radius / 4, thickness + radius / 4,
                    thickness + radius / 4);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = thickness + radius / 4;
            return insets;
        }
    }
}