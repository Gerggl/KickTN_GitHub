import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Login extends JFrame {
    // Attribute
    JLabel lbl_ueberschrift, lbl_benutzer, lbl_passwort;
    JTextField txt_benutzer;
    JPasswordField txt_passwort;
    RoundedButton btn_login, btn_zuruecksetzen, btn_beenden, btn_abmelden;
    RoundedButton toggleModeButton;

    private Color buttonColor;
    private Color hoverColor;
    private Color redColor;
    private Color redHoverColor;
    private Color bgColor;
    private Color textColor;

    private boolean darkMode = false;

    // Konstruktor
    public Login() {
        initColors();

        UIManager.put("control", bgColor);
        UIManager.put("info", bgColor);
        UIManager.put("nimbusBase", bgColor);
        UIManager.put("nimbusLightBackground", bgColor);
        UIManager.put("text", textColor);

        this.setTitle("KickTN Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(bgColor);

        initComponents();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(450, 640));
    }

    private void initColors() {
        if (darkMode) {
            bgColor = new Color(40, 44, 52);
            textColor = new Color(230, 230, 230);
            buttonColor = new Color(45, 156, 219);
            hoverColor = new Color(35, 130, 180);
            redColor = new Color(200, 60, 60);
            redHoverColor = new Color(160, 40, 40);
        } else {
            bgColor = new Color(245, 250, 255);
            textColor = Color.BLACK;
            buttonColor = new Color(15, 105, 140);
            hoverColor = new Color(10, 85, 115);
            redColor = new Color(200, 50, 50);
            redHoverColor = new Color(160, 40, 40);
        }
    }

    private void styleTextField(JTextField field) {
        RoundedLineBorder rounded15Border = new RoundedLineBorder(buttonColor, 1, 15);
        RoundedLineBorder focus15Border = new RoundedLineBorder(hoverColor, 1, 15);
        Border padding = BorderFactory.createEmptyBorder(8, 12, 8, 12);

        field.setBorder(BorderFactory.createCompoundBorder(rounded15Border, padding));
        field.setBackground(bgColor);
        field.setForeground(textColor);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setCaretColor(textColor);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(focus15Border, padding));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(rounded15Border, padding));
            }
        });
    }

    private void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        toggleModeButton = new RoundedButton(darkMode ? "Light Mode" : "Dark Mode", buttonColor, hoverColor);
        toggleModeButton.setPreferredSize(new Dimension(120, 40));
        toggleModeButton.addActionListener(e -> toggleTheme());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor);
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

        lbl_ueberschrift = new JLabel("Willkommen bei KickTN");
        lbl_ueberschrift.setFont(titleFont);
        lbl_ueberschrift.setForeground(buttonColor);
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 10, 30, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(lbl_ueberschrift, gbc);

        lbl_benutzer = new JLabel("Benutzername:");
        lbl_benutzer.setFont(labelFont);
        lbl_benutzer.setForeground(textColor);
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        this.add(lbl_benutzer, gbc);

        txt_benutzer = new JTextField(16);
        styleTextField(txt_benutzer);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(txt_benutzer, gbc);

        lbl_passwort = new JLabel("Passwort:");
        lbl_passwort.setFont(labelFont);
        lbl_passwort.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        this.add(lbl_passwort, gbc);

        txt_passwort = new JPasswordField(16);
        styleTextField(txt_passwort);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(txt_passwort, gbc);

        btn_login = new RoundedButton("Login", buttonColor, hoverColor);
        btn_zuruecksetzen = new RoundedButton("Zurücksetzen", buttonColor, hoverColor);
        btn_beenden = new RoundedButton("Beenden", redColor, redHoverColor);
        btn_abmelden = new RoundedButton("Abmelden", redColor, redHoverColor);

        btn_login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_zuruecksetzen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_beenden.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_abmelden.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleModeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn_login.addActionListener(new MyActionListener());
        btn_zuruecksetzen.addActionListener(new MyActionListener());
        btn_beenden.addActionListener(new MyActionListener());
        btn_abmelden.addActionListener(new MyActionListener());

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        this.add(btn_login, gbc);

        gbc.gridx = 1;
        this.add(btn_zuruecksetzen, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        this.add(btn_beenden, gbc);

        gbc.gridy = 6;
        this.add(btn_abmelden, gbc);

        JPanel registrierenPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registrierenPanel.setBackground(bgColor);

        JLabel lblNochNichtRegistriert = new JLabel("Noch nicht registriert?");
        lblNochNichtRegistriert.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNochNichtRegistriert.setForeground(textColor);

        JButton btnRegistrieren = new JButton("Hier registrieren");
        btnRegistrieren.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrieren.setForeground(buttonColor);
        btnRegistrieren.setBorderPainted(false);
        btnRegistrieren.setContentAreaFilled(false);
        btnRegistrieren.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrieren.setFocusPainted(false);
        btnRegistrieren.addActionListener(e -> {
            dispose();
            new Registrierungsformular();
        });

        registrierenPanel.add(lblNochNichtRegistriert);
        registrierenPanel.add(btnRegistrieren);

        gbc.gridy = 7;
        this.add(registrierenPanel, gbc);

        this.getRootPane().setDefaultButton(btn_login);
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        initColors();
        this.getContentPane().setBackground(bgColor);

        lbl_ueberschrift.setForeground(buttonColor);
        lbl_benutzer.setForeground(textColor);
        lbl_passwort.setForeground(textColor);

        btn_login.updateColors(buttonColor, hoverColor);
        btn_zuruecksetzen.updateColors(buttonColor, hoverColor);
        btn_beenden.updateColors(redColor, redHoverColor);
        btn_abmelden.updateColors(buttonColor, hoverColor);
        toggleModeButton.setText(darkMode ? "Light Mode" : "Dark Mode");
        toggleModeButton.updateColors(buttonColor, hoverColor);

        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setBackground(bgColor);
                for (Component c : panel.getComponents()) {
                    if (c instanceof JLabel lbl) lbl.setForeground(textColor);
                    if (c instanceof JButton btn && btn != toggleModeButton)
                        btn.setForeground(buttonColor);
                }
            }
        }

        styleTextField(txt_benutzer);
        styleTextField(txt_passwort);
        this.repaint();
    }

    private class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btn_login) {
                String benutzer = txt_benutzer.getText().trim();
                char[] passwortChars = txt_passwort.getPassword();
                String passwort = new String(passwortChars);
                java.util.Arrays.fill(passwortChars, '0');

                if (benutzer.isEmpty() || passwort.isEmpty()) {
                    JOptionPane.showMessageDialog(Login.this, "Bitte Benutzername und Passwort eingeben!", "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                btn_login.setEnabled(false);
                new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() {
                        return Loginservice.pruefeLogin(benutzer, passwort);
                    }

                    @Override
                    protected void done() {
                        btn_login.setEnabled(true);
                        try {
                            boolean loginErfolgreich = get();
                            if (loginErfolgreich) {
                                JOptionPane.showMessageDialog(Login.this, "Login erfolgreich! Sie werden nun zur Spielerliste weitergeleitet.");
                                dispose();
                                new SpielerGUI();
                            } else {
                                JOptionPane.showMessageDialog(Login.this,
                                        "Benutzername oder Passwort falsch! Prüfen Sie die Eingaben!");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(Login.this, "Fehler bei der Anmeldung!");
                        }
                    }
                }.execute();

            } else if (e.getSource() == btn_zuruecksetzen) {
                txt_benutzer.setText("");
                txt_passwort.setText("");
            } else if (e.getSource() == btn_beenden) {
                dispose();
            } else if (e.getSource() == btn_abmelden) {
                txt_benutzer.setText("");
                txt_passwort.setText("");
                JOptionPane.showMessageDialog(Login.this, "Abmeldung durchgeführt.");
                dispose();
            }
        }
    }

    class RoundedButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
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
            setPreferredSize(new Dimension(0, 50));

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

        public void updateColors(Color normal, Color hover) {
            this.normalColor = normal;
            this.hoverColor = hover;
            repaint();
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
        }
    }

    static class RoundedLineBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        public RoundedLineBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            for (int i = 0; i < thickness; i++) {
                g2.drawRoundRect(x + i, y + i, width - 1 - 2 * i, height - 1 - 2 * i, radius, radius);
            }
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = thickness;
            return insets;
        }
    }
}