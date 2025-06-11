import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Startfenster extends JFrame {

    private BufferedImage img1, img2, img3, logoImg;

    private final Color buttonColor = new Color(15, 105, 140);
    private final Color hoverColor = new Color(10, 85, 115);
    private final Color cardBackground = new Color(230, 245, 255);

    public Startfenster() {
        setTitle("Startfenster für das Spielprojekt");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            img1 = ImageIO.read(new File("./bilder/bild1.jpg"));
            img2 = ImageIO.read(new File("./bilder/bild2.jpg"));
            img3 = ImageIO.read(new File("./bilder/bild3.jpg"));
            logoImg = ImageIO.read(new File("./bilder/logo.jpg")); // <- dein Logo hier
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Bilder: " + e.getMessage());
        }

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header oben mit Logo rechts
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Willkommen zur Spielerwelt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(buttonColor);

        JLabel logoLabel = new JLabel();
        if (logoImg != null) {
            Image scaledLogo = logoImg.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        }

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(logoLabel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Kartenbereich
        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        cardPanel.setBackground(Color.WHITE);

        cardPanel.add(createCard(
                "Login",
                "Logge dich mit deinem Account ein und betrete die Spielerwelt.",
                img1,
                () -> zeigeLogin()
        ));
        cardPanel.add(createCard(
                "Registrieren",
                "Erstelle ein neues Konto und werde Teil der Community.",
                img2,
                () -> zeigeRegistrieren()
        ));
        cardPanel.add(createCard(
                "Spielerliste",
                "Sieh dir alle registrierten Spieler und ihre Daten an.",
                img3,
                () -> zeigeSpieler()
        ));

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(buttonColor);
        footerPanel.setPreferredSize(new Dimension(0, 40));
        footerPanel.setLayout(new GridBagLayout());

        JLabel footerLabel = new JLabel("© 2025 KickTN Projekt – Entwickelt mit Leidenschaft ⚽");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footerPanel.add(footerLabel);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createCard(String titleText, String desc, BufferedImage image, Runnable action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(cardBackground);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(buttonColor, 3, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(buttonColor);

        JTextArea beschreibung = new JTextArea(desc);
        beschreibung.setWrapStyleWord(true);
        beschreibung.setLineWrap(true);
        beschreibung.setEditable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        beschreibung.setForeground(Color.DARK_GRAY);

        JLabel bildLabel = createImageLabel(image);

        RoundedButton btn = new RoundedButton(titleText, buttonColor, hoverColor);
        btn.addActionListener(e -> action.run());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(beschreibung, BorderLayout.NORTH);
        contentPanel.add(bildLabel, BorderLayout.CENTER);
        contentPanel.add(btn, BorderLayout.SOUTH);

        card.add(title, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createImageLabel(BufferedImage img) {
        if (img == null) return new JLabel("Kein Bild verfügbar");
        Image scaled = img.getScaledInstance(240, 160, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }

    private void zeigeLogin() {
        JOptionPane.showMessageDialog(this, "🚪 Login-Fenster geöffnet");
    }

    private void zeigeRegistrieren() {
        JOptionPane.showMessageDialog(this, "📝 Registrierungs-Fenster geöffnet");
    }

    private void zeigeSpieler() {
        JOptionPane.showMessageDialog(this, "📋 Spielerliste angezeigt");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Startfenster().setVisible(true));
    }

    // Runde, stylische Buttons
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
}