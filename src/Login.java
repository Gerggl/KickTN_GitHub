import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Login extends JFrame {
    JLabel lbl_ueberschrift, lbl_benutzer, lbl_passwort;
    JTextField txt_benutzer;
    JPasswordField txt_passwort;
    RoundedButton btn_login, btn_zuruecksetzen, btn_beenden;

    private final Color buttonColor = new Color(15, 105, 140);     // #0F698C
    private final Color hoverColor = new Color(10, 85, 115);       // dunklere Variante

    public Login() {
        this.setTitle("KickTN Login");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(245, 250, 255));

        initComponents();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 34);

        // Überschrift
        lbl_ueberschrift = new JLabel("Willkommen bei KickTN");
        lbl_ueberschrift.setFont(titleFont);
        lbl_ueberschrift.setForeground(buttonColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 20, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(lbl_ueberschrift, gbc);

        // Benutzername
        lbl_benutzer = new JLabel("Benutzername:");
        lbl_benutzer.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 5, 10);
        this.add(lbl_benutzer, gbc);

        txt_benutzer = new JTextField(16);
        txt_benutzer.setFont(labelFont);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(txt_benutzer, gbc);

        // Passwort
        lbl_passwort = new JLabel("Passwort:");
        lbl_passwort.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(lbl_passwort, gbc);

        txt_passwort = new JPasswordField(16);
        txt_passwort.setFont(labelFont);
        gbc.gridx = 1;
        gbc.gridy = 2;
        this.add(txt_passwort, gbc);

        // Buttons
        btn_login = new RoundedButton("Login", buttonColor, hoverColor);
        btn_zuruecksetzen = new RoundedButton("Zurücksetzen", buttonColor, hoverColor);
        btn_beenden = new RoundedButton("Beenden", buttonColor, hoverColor);

        gbc.insets = new Insets(15, 10, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(btn_login, gbc);

        gbc.gridx = 1;
        this.add(btn_zuruecksetzen, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        this.add(btn_beenden, gbc);

        // Listener
        MyActionListener listener = new MyActionListener();
        btn_login.addActionListener(listener);
        btn_zuruecksetzen.addActionListener(listener);
        btn_beenden.addActionListener(listener);
    }

    private class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btn_login) {
                String benutzer = txt_benutzer.getText();
                char[] passwortChars = txt_passwort.getPassword();
                String passwort = new String(passwortChars);

                // Passwort-Array direkt löschen (Sicherheit)
                java.util.Arrays.fill(passwortChars, '0');

                // Login im Hintergrund prüfen
                btn_login.setEnabled(false);  // Button deaktivieren während Prüfung
                new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() {
                        return Loginservice.pruefeLogin(benutzer, passwort);
                    }

                    @Override
                    protected void done() {
                        btn_login.setEnabled(true);  // Button wieder aktivieren
                        try {
                            boolean loginErfolgreich = get();
                            if (loginErfolgreich) {
                                JOptionPane.showMessageDialog(Login.this, "Login erfolgreich!");
                            } else {
                                JOptionPane.showMessageDialog(Login.this, "Login fehlgeschlagen!");
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
                System.exit(0);
            }
        }
    }

    // Echte runde Buttons
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
            setPreferredSize(new Dimension(150, 40));

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
            // kein Rand
        }
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(Login::new);
        new SpielerGUI();
    }
}