import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Startfenster extends JFrame {

    private BufferedImage img1, img2, img3, logoImg;

    private final Color lightButtonColor = new Color(15, 105, 140);
    private final Color lightHoverColor = new Color(10, 85, 115);
    private final Color lightCardBackground = new Color(230, 245, 255);
    private final Color darkButtonColor = new Color(40, 40, 60);
    private final Color darkHoverColor = new Color(70, 70, 90);
    private final Color darkCardBackground = new Color(55, 55, 70);
    private final Color darkTextColor = new Color(230, 230, 230);

    private boolean darkMode = false;

    private JPanel mainPanel, headerPanel, cardPanel, footerPanel;
    private JLabel title, footerLabel;
    private RoundedToggleButton darkModeToggle;

    public Startfenster() {
        setTitle("Startfenster für das Spielprojekt");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            img1 = ImageIO.read(new File("./bilder/bild1.jpg"));
            img2 = ImageIO.read(new File("./bilder/bild2.jpg"));
            img3 = ImageIO.read(new File("./bilder/bild3.jpg"));
            logoImg = ImageIO.read(new File("./bilder/logo.jpg"));
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Bilder: " + e.getMessage());
        }

        initUI();
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel mit Farbverlauf
        headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        darkMode ? darkButtonColor.brighter() : lightButtonColor.brighter(),
                        getWidth(), 0,
                        darkMode ? darkButtonColor.darker() : lightButtonColor.darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 100));

        title = new JLabel("Willkommen zur Spielerwelt", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        applyDropShadow(title);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);

        darkModeToggle = new RoundedToggleButton("Dark Mode", darkButtonColor, darkHoverColor);
        darkModeToggle.setFocusPainted(false);
        darkModeToggle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        darkModeToggle.setBackground(Color.WHITE);
        darkModeToggle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        darkModeToggle.addActionListener(e -> toggleDarkMode());

        JLabel logoLabel = new JLabel();
        if (logoImg != null) {
            Image scaledLogo = logoImg.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
            logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        }

        topRightPanel.add(darkModeToggle);
        topRightPanel.add(logoLabel);

        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(topRightPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Karten-Panel (ohne Hover-Effekt auf Karte)
        cardPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Footer
        footerPanel = new JPanel(new GridBagLayout());
        footerLabel = new JLabel("© 2025 KickTN Projekt – Entwickelt mit Leidenschaft");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footerPanel.add(footerLabel);
        footerPanel.setPreferredSize(new Dimension(0, 40));

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        refreshTheme();
    }

    private void refreshTheme() {
        Color background = darkMode ? new Color(30, 30, 40) : Color.WHITE;
        Color btnColor = darkMode ? darkButtonColor : lightButtonColor;

        mainPanel.setBackground(background);
        cardPanel.removeAll();

        cardPanel.setBackground(background);

        // Footer-Farbe anpassen:
        if (darkMode) {
            footerPanel.setBackground(darkButtonColor);
        } else {
            footerPanel.setBackground(new Color(245, 245, 245)); // helles Grau statt Blau
        }
        footerLabel.setForeground(darkMode ? Color.WHITE : Color.DARK_GRAY);

        mainPanel.setBackground(background);
        cardPanel.removeAll();

        cardPanel.setBackground(background);
        footerPanel.setBackground(btnColor);
        footerLabel.setForeground(Color.WHITE);

        cardPanel.add(createCard("Login",
                "Logge dich mit deinem Account ein und betrete die Spielerwelt.",
                img1, () -> new Login()));

        cardPanel.add(createCard("Registrieren",
                "Erstelle ein neues Konto und werde Teil der Community.",
                img2, () -> new Registrierungsformular()));

        cardPanel.add(createCard("Spielerliste",
                "Sieh dir alle registrierten Spieler und ihre Daten an.",
                img3, () -> new SpielerGUI()));

        darkModeToggle.setText(darkMode ? "Light Mode" : "Dark Mode");
        repaint();
        revalidate();
    }

    private JPanel createCard(String titleText, String desc, BufferedImage image, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(darkMode ? darkCardBackground : lightCardBackground);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darkMode ? darkButtonColor : lightButtonColor, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        applyDropShadow(card);

        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(darkMode ? darkTextColor : lightButtonColor);

        JTextArea beschreibung = new JTextArea(desc);
        beschreibung.setWrapStyleWord(true);
        beschreibung.setLineWrap(true);
        beschreibung.setEditable(false);
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        beschreibung.setForeground(darkMode ? darkTextColor : Color.DARK_GRAY);

        JLabel bildLabel = createImageLabel(image);

        RoundedButton btn = new RoundedButton(titleText,
                darkMode ? darkButtonColor : lightButtonColor,
                darkMode ? darkHoverColor : lightHoverColor);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> action.run());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(beschreibung, BorderLayout.NORTH);
        contentPanel.add(bildLabel, BorderLayout.CENTER);
        contentPanel.add(btn, BorderLayout.SOUTH);

        card.add(title, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createImageLabel(BufferedImage img) {
        if (img == null)
            return new JLabel("Kein Bild verfügbar");
        Image scaled = img.getScaledInstance(240, 160, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }

    private void applyDropShadow(JComponent comp) {
        /*comp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(0, 0, 0, 30)),
                comp.getBorder() != null ? comp.getBorder() : BorderFactory.createEmptyBorder(10, 10, 10, 10)));*/
    }

    private void toggleDarkMode() {
        darkMode = !darkMode;
        refreshTheme();
    }

    // Abgerundeter normaler Button
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
            setPreferredSize(new Dimension(150, 40));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

            // Text zentrieren
            FontMetrics fm = g2.getFontMetrics();
            Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();

            int textX = (getWidth() - stringBounds.width) / 2;
            int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();

            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), textX, textY);

            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // keine Border
        }
    }

    // Abgerundeter Toggle Button mit sauber zentriertem Text
    class RoundedToggleButton extends JToggleButton {
        private final Color normalColor;
        private final Color hoverColor;
        private boolean hovered = false;

        public RoundedToggleButton(String text, Color normalColor, Color hoverColor) {
            super(text);
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setPreferredSize(new Dimension(150, 40));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

            // Text zentrieren
            FontMetrics fm = g2.getFontMetrics();
            Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();

            int textX = (getWidth() - stringBounds.width) / 2;
            int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();

            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), textX, textY);

            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // keine Border
        }
    }
    public static void main(String[] args) {
        new Startfenster().setVisible(true);
    }
}