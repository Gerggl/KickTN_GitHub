import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registrierungsformular extends JFrame {
    JLabel lbl_ueberschrift, lbl_vname, lbl_nname, lbl_benutzer, lbl_passwort, lbl_passwort2,
            lbl_email, lbl_gebdatum, lbl_adresse;
    JTextField txt_vname, txt_nname, txt_benutzer, txt_email, txt_gebdatum, txt_adresse;
    JPasswordField txt_passwort, txt_passwort2;
    JCheckBox chk_agb;
    JButton btn_absenden, btn_zuruecksetzen, btn_abbrechen;

    Color HINTERGRUND = new Color(225, 240, 255); // sanftes Hellblau
    Color BUTTONFARBE = new Color(70, 130, 180); // Steel Blue
    Color TEXTFELD_HINTERGRUND = Color.WHITE;
    Color TEXTFELD_RAND = new Color(100, 149, 237); // Cornflower Blue
    Color TEXT = Color.BLACK;
    Font font_beschriftung = new Font("Sans Serif", Font.PLAIN, 12);
    Font font_button = new Font("Sans Serif", Font.BOLD, 14);

    public Registrierungsformular() {
        setTitle("Registrierungsformular");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.getContentPane().setBackground(HINTERGRUND);

        Font font_title = new Font("Sans Serif", Font.BOLD, 22);
        Font font_label = new Font("Sans Serif", Font.PLAIN, 14);
        Font font_button = new Font("Sans Serif", Font.BOLD, 14);

        // Überschrift
        lbl_ueberschrift = new JLabel("Benutzerregistrierung");
        lbl_ueberschrift.setFont(font_title);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(20, 10, 20, 10);
        this.add(lbl_ueberschrift, c);

        // Vorname
        lbl_vname = new JLabel("Vorname:");
        lbl_vname.setFont(font_label);
        c = position(c, 0, 1);
        this.add(lbl_vname, c);
        txt_vname = new JTextField(15);
        c = position(c, 1, 1);
        this.add(txt_vname, c);

        // Nachname
        lbl_nname = new JLabel("Nachname:");
        lbl_nname.setFont(font_label);
        c = position(c, 0, 2);
        this.add(lbl_nname, c);
        txt_nname = new JTextField(15);
        c = position(c, 1, 2);
        this.add(txt_nname, c);

        // Benutzername
        lbl_benutzer = new JLabel("Benutzername:");
        lbl_benutzer.setFont(font_label);
        c = position(c, 0, 3);
        this.add(lbl_benutzer, c);
        txt_benutzer = new JTextField(15);
        c = position(c, 1, 3);
        this.add(txt_benutzer, c);

        // Passwort
        lbl_passwort = new JLabel("Passwort:");
        lbl_passwort.setFont(font_label);
        c = position(c, 0, 4);
        this.add(lbl_passwort, c);
        txt_passwort = new JPasswordField(15);
        c = position(c, 1, 4);
        this.add(txt_passwort, c);

        // Passwort bestätigen
        lbl_passwort2 = new JLabel("Passwort bestätigen:");
        lbl_passwort2.setFont(font_label);
        c = position(c, 0, 5);
        this.add(lbl_passwort2, c);
        txt_passwort2 = new JPasswordField(15);
        c = position(c, 1, 5);
        this.add(txt_passwort2, c);

        // E-Mail
        lbl_email = new JLabel("E-Mail:");
        lbl_email.setFont(font_label);
        c = position(c, 0, 6);
        this.add(lbl_email, c);
        txt_email = new JTextField(15);
        c = position(c, 1, 6);
        this.add(txt_email, c);

        // Geburtsdatum
        lbl_gebdatum = new JLabel("Geburtsdatum:");
        lbl_gebdatum.setFont(font_label);
        c = position(c, 0, 7);
        this.add(lbl_gebdatum, c);
        txt_gebdatum = new JTextField(15);
        c = position(c, 1, 7);
        this.add(txt_gebdatum, c);

        // Adresse
        lbl_adresse = new JLabel("Adresse:");
        lbl_adresse.setFont(font_label);
        c = position(c, 0, 8);
        this.add(lbl_adresse, c);
        txt_adresse = new JTextField(15);
        c = position(c, 1, 8);
        this.add(txt_adresse, c);

        // AGB Checkbox
        chk_agb = new JCheckBox("Ich akzeptiere die AGB");
        c = position(c, 0, 9);
        c.gridwidth = 2;
        this.add(chk_agb, c);

        // Buttons
        btn_absenden = new JButton("Registrieren");
        btn_absenden.setFont(font_button);
        c = position(c, 0, 10);
        c.gridwidth = 1;
        this.add(btn_absenden, c);

        btn_zuruecksetzen = new JButton("Zurücksetzen");
        btn_zuruecksetzen.setFont(font_button);
        c = position(c, 1, 10);
        this.add(btn_zuruecksetzen, c);

        btn_abbrechen = new JButton("Abbrechen");
        btn_abbrechen.setFont(font_button);
        c = position(c, 0, 11);
        this.add(btn_abbrechen, c);

        btn_absenden.setBackground(BUTTONFARBE);
        btn_absenden.setForeground(Color.WHITE);
        btn_absenden.setFocusPainted(false);

        btn_zuruecksetzen.setBackground(BUTTONFARBE);
        btn_zuruecksetzen.setForeground(Color.WHITE);
        btn_zuruecksetzen.setFocusPainted(false);

        btn_abbrechen.setBackground(new Color(220, 20, 60)); // Crimson für Abbrechen
        btn_abbrechen.setForeground(Color.WHITE);
        btn_abbrechen.setFocusPainted(false);

        btn_absenden = new JButton("Registrieren");
        btn_absenden.setFont(font_button);
        btn_absenden.setBackground(BUTTONFARBE);
        btn_absenden.setForeground(Color.WHITE);
        btn_absenden.setFocusPainted(false);
        c = position(c, 0, 10);
        c.gridwidth = 1;
        this.add(btn_absenden, c);

        // Listener
        btn_absenden.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                JOptionPane.showMessageDialog(null, "Registrierung erfolgreich!");
            }
        });

        btn_zuruecksetzen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt_vname.setText("");
                txt_nname.setText("");
                txt_benutzer.setText("");
                txt_passwort.setText("");
                txt_passwort2.setText("");
                txt_email.setText("");
                txt_gebdatum.setText("");
                txt_adresse.setText("");
                chk_agb.setSelected(false);
            }
        });

        btn_abbrechen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // schließt das Fenster
            }
        });
    }

    private GridBagConstraints position(GridBagConstraints c, int x, int y) {
        c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 10, 5, 10);
        return c;
    }

    public static void main(String[] args) {
        new Registrierungsformular();
    }

}