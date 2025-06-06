import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartFenster extends JFrame {

    public StartFenster() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 800; // feste Breite
        int height = screenSize.height;

        setTitle("Willkommen bei KickTN");
        setSize(width, height);
        setLocation((screenSize.width - width) / 2, 0); // zentriert horizontal
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(false);

        Color hintergrundFarbe = new Color(0xF7F6F4);
        Color buttonColor = new Color(15, 105, 140);
        Color buttonHoverColor = new Color(10, 85, 115);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(hintergrundFarbe);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));

        // Titel + Logo nebeneinander
        JPanel titelPanel = new JPanel();
        titelPanel.setLayout(new BoxLayout(titelPanel, BoxLayout.X_AXIS));
        titelPanel.setBackground(hintergrundFarbe);

        JLabel titleLabel = new JLabel("KickTN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(buttonColor);
        titelPanel.add(titleLabel);
        titelPanel.add(Box.createHorizontalGlue());

        try {
            ImageIcon logoIcon = new ImageIcon("bilder/Logo.jpg");
            Image logoImage = logoIcon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
            titelPanel.add(logoLabel);
        } catch (Exception e) {
            System.out.println("Logo konnte nicht geladen werden: " + e.getMessage());
        }

        mainPanel.add(titelPanel);

        JLabel subtitleLabel = new JLabel("Willkommen! Bitte anmelden oder registrieren.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
        mainPanel.add(subtitleLabel);

        // Daten für Buttons + Bilder
        String[] buttonTexte = {"Anmelden", "Registrieren", "Spieler anzeigen"};
        String[] bildPfade = {"bilder/bild1.jpg", "bilder/bild2.jpg", "bilder/bild3.jpg"};
        ActionListener[] actions = {
                e -> {
                    dispose();
                    new Login();
                },
                e -> {
                    dispose();
                    new Registrierungsformular();
                },
                e -> {
                    dispose();
                    new SpielerGUI();
                }
        };

        for (int i = 0; i < buttonTexte.length; i++) {
            JPanel zeile = new JPanel();
            zeile.setLayout(new BoxLayout(zeile, BoxLayout.X_AXIS));
            zeile.setBackground(hintergrundFarbe);
            zeile.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Bild links
            try {
                ImageIcon icon = new ImageIcon(bildPfade[i]);
                Image image = icon.getImage().getScaledInstance(400, 227, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                imageLabel.setAlignmentY(Component.TOP_ALIGNMENT);
                zeile.add(imageLabel);
            } catch (Exception e) {
                System.out.println("Bild konnte nicht geladen werden: " + bildPfade[i]);
            }

            zeile.add(Box.createRigidArea(new Dimension(30, 0)));

            // Button rechts
            JButton button = new JButton(buttonTexte[i]);
            button.setFont(new Font("Segoe UI", Font.BOLD, 22));
            button.setForeground(Color.WHITE);
            button.setBackground(buttonColor);
            button.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
            button.setUI(new SpielerGUI.RoundedButtonUI(buttonColor, buttonHoverColor));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.addActionListener(actions[i]);
            button.setAlignmentY(Component.TOP_ALIGNMENT);

            zeile.add(button);

            mainPanel.add(zeile);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartFenster::new);
    }
}