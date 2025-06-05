import javax.swing.*;
import java.awt.*;

public class StartGUI extends JFrame {

    public StartGUI() {
        setTitle("Willkommen zur Anwendung");
        setSize(900, 600);
        setLocationRelativeTo(null);  // Fenster zentrieren
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Hintergrundfarbe
        getContentPane().setBackground(new Color(245, 245, 250));  // sehr helles Grau-Blau

        // Überschrift
        JLabel title = new JLabel("Willkommen!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // Panel für Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setLayout(new GridLayout(1, 3, 50, 0)); // 3 Buttons nebeneinander, 50px Abstand

        // Buttons mit schöner Farbe & Schrift
        JButton btnSpieler = createStyledButton("Spieler anzeigen");
        JButton btnLogin = createStyledButton("Login");
        JButton btnRegister = createStyledButton("Registrierung");

        buttonPanel.add(btnSpieler);
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));
        add(buttonPanel, BorderLayout.SOUTH);

        // Panel für Bilder in der Mitte
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(245, 245, 250));
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20)); // Abstand zwischen Bildern

        // Beispielbilder - ersetze die Pfade durch deine eigenen Bilder
        ImageIcon img1 = new ImageIcon("./KickTN/bilder/bild1.jpg");
        ImageIcon img2 = new ImageIcon("./KickTN/bilder/bild2.jpg");
        ImageIcon img3 = new ImageIcon("./KickTN/bilder/bild3.jpg");



        

        // Optional skaliere Bilder, wenn zu groß
        img1 = scaleImageIcon(img1, 150, 150);
        img2 = scaleImageIcon(img2, 150, 150);
        img3 = scaleImageIcon(img3, 150, 150);

        imagePanel.add(new JLabel(img1));
        imagePanel.add(new JLabel(img2));
        imagePanel.add(new JLabel(img3));

        add(imagePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        button.setBackground(new Color(70, 130, 180));  // Stahlblau
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        return button;
    }

    private ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartGUI::new);
    }
}