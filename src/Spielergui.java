import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SpielerGUI extends JFrame {

    // Attribute
    private JTable tabelle;
    private DefaultTableModel tabelleModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField filterField;

    // Konstruktor
    public SpielerGUI() {
        setTitle("Spielerliste");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Farben
        Color hintergrundFarbe = new Color(0xF7F6F4);
        Color buttonColor = new Color(15, 105, 140);
        Color buttonHoverColor = new Color(10, 85, 115);

        // Oberes Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        topPanel.setBackground(hintergrundFarbe);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(hintergrundFarbe);
        textPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 30, 30));

        JLabel titleLabel = new JLabel("KickTN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(buttonColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Alle bisher erfassten Spieler");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        ImageIcon logoIcon = new ImageIcon("bilder/Logo.jpg");
        Image logoImage = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoLabel.setHorizontalAlignment(JLabel.RIGHT);
        logoLabel.setVerticalAlignment(JLabel.TOP);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        topPanel.add(textPanel, BorderLayout.WEST);
        topPanel.add(logoLabel, BorderLayout.EAST);

        JPanel filterExportPanel = new JPanel();
        filterExportPanel.setLayout(new BoxLayout(filterExportPanel, BoxLayout.Y_AXIS));
        filterExportPanel.setBackground(hintergrundFarbe);
        filterExportPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        JPanel suchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        suchPanel.setBackground(hintergrundFarbe);

        JLabel filterLabel = new JLabel("Spieler suchen:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterLabel.setForeground(Color.DARK_GRAY);
        suchPanel.add(filterLabel);

        filterField = new JTextField(20);
        filterField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterField.setToolTipText("Nach Spielern filtern...");
        filterField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(buttonColor, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        filterField.setPreferredSize(new Dimension(220, 32));
        suchPanel.add(filterField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(hintergrundFarbe);

        JButton exportButton = new JButton("Als CSV exportieren");
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportButton.setBackground(buttonColor);
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        exportButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exportButton.setUI(new RoundedButtonUI(buttonColor, buttonHoverColor));
        buttonPanel.add(exportButton);

        // Button "Spieler hinzufügen" erstellen
        JButton addButton = new JButton("Spieler hinzufügen");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(buttonColor);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.setUI(new RoundedButtonUI(buttonColor, buttonHoverColor));

        // Button zum Panel hinzufügen
        buttonPanel.add(addButton);

        addButton.addActionListener(e -> spielerHinzufuegen());

        filterExportPanel.add(suchPanel);
        filterExportPanel.add(buttonPanel);

        exportButton.setUI(new RoundedButtonUI(buttonColor, buttonHoverColor));
        filterExportPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        filterExportPanel.add(exportButton);

        topPanel.add(filterExportPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Tabelle
        tabelleModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0 -> Integer.class;
                    case 4 -> LocalDate.class;
                    case 5 -> ImageIcon.class;
                    default -> String.class;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelle = new JTable(tabelleModel);
        tabelle.setRowHeight(180);
        tabelle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelle.setIntercellSpacing(new Dimension(0, 0));
        tabelle.setShowGrid(false);
        tabelle.setBackground(new Color(240, 250, 255));
        tabelle.setRowSelectionAllowed(false);
        tabelle.setColumnSelectionAllowed(false);
        tabelle.setCellSelectionEnabled(false);

        tabelle.getTableHeader().setReorderingAllowed(false);
        sorter = new TableRowSorter<>(tabelleModel);
        tabelle.setRowSorter(sorter);

        // Spalten für die Tabelle festlegen
        tabelleModel.setColumnIdentifiers(new String[] {
                "ID", "Vorname", "Nachname", "Position", "Geburtsdatum", "Foto", "Aktiv", "Verein"
        });

        int[] breiten = { 40, 100, 120, 100, 140, 128, 60, 100 };
        for (int i = 0; i < breiten.length; i++) {
            tabelle.getColumnModel().getColumn(i).setPreferredWidth(breiten[i]);
        }

        JTableHeader header = tabelle.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            private final Insets padding = new Insets(5, 10, 5, 10);
            private final Color headerBackground = buttonColor;
            private final Color headerForeground = Color.WHITE;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(
                        padding.top, padding.left, padding.bottom, padding.right));
                label.setBackground(headerBackground);
                label.setForeground(headerForeground);

                Icon icon = UIManager.getIcon("Table.ascendingSortIcon");
                RowSorter<?> sorter = table.getRowSorter();

                if (sorter != null) {
                    java.util.List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();
                    if (!sortKeys.isEmpty()) {
                        RowSorter.SortKey sortKey = sortKeys.get(0);
                        if (table.convertColumnIndexToModel(column) == sortKey.getColumn()) {
                            icon = switch (sortKey.getSortOrder()) {
                                case ASCENDING -> UIManager.getIcon("Table.ascendingSortIcon");
                                case DESCENDING -> UIManager.getIcon("Table.descendingSortIcon");
                                default -> icon;
                            };
                        }
                    }
                }

                label.setIcon(icon);
                label.setHorizontalTextPosition(SwingConstants.LEFT);
                return label;
            }
        });

        // Styling
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            private final Color evenColor = new Color(200, 225, 230);
            private final Color oddColor = new Color(240, 250, 255);
            private final Color lineColor = buttonColor;
            private final Insets padding = new Insets(5, 10, 5, 10);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                label.setBackground(
                        isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? evenColor : oddColor));
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, lineColor),
                        BorderFactory.createEmptyBorder(padding.top, padding.left, padding.bottom, padding.right)));
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        };
        tabelle.setDefaultRenderer(Object.class, cellRenderer);
        tabelle.setDefaultRenderer(Integer.class, cellRenderer);

        tabelle.setDefaultRenderer(LocalDate.class, new DefaultTableCellRenderer() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMAN);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                if (value instanceof LocalDate datum) {
                    value = datum.format(formatter);
                }
                return cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        tabelle.setDefaultRenderer(ImageIcon.class, new DefaultTableCellRenderer() {
            private final Color evenColor = new Color(200, 225, 230);
            private final Color oddColor = new Color(240, 250, 255);
            private final Color lineColor = buttonColor;
            private final Insets padding = new Insets(20, 10, 20, 10);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true);
                label.setBackground(
                        isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? evenColor : oddColor));

                if (value instanceof ImageIcon icon) {
                    label.setIcon(icon);
                }

                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, lineColor),
                        BorderFactory.createEmptyBorder(padding.top, padding.left, padding.bottom, padding.right)));
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelle);
        add(scrollPane, BorderLayout.CENTER);

        spielerLaden();

        filterField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            private void filter() {
                String text = filterField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                            String vorname = entry.getStringValue(1).toLowerCase();
                            String nachname = entry.getStringValue(2).toLowerCase();
                            String filterText = text.toLowerCase();

                            return vorname.contains(filterText) || nachname.contains(filterText);
                        }
                    });
                }
            }

        });

        exportButton.addActionListener(e -> exportiereAlsCSV());

        setVisible(true);
    }

    static class RoundedButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        private final Color normalColor;
        private final Color hoverColor;
        private boolean hover = false;

        public RoundedButtonUI(Color normalColor, Color hoverColor) {
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.setOpaque(false);
            c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    c.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    c.repaint();
                }
            });
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(hover ? hoverColor : normalColor);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30);

            FontMetrics fm = g2.getFontMetrics();
            Rectangle r = new Rectangle(0, 0, c.getWidth(), c.getHeight());
            String text = b.getText();

            g2.setColor(b.getForeground());
            int textX = (r.width - fm.stringWidth(text)) / 2;
            int textY = (r.height + fm.getAscent() - fm.getDescent()) / 2;
            g2.setFont(b.getFont());
            g2.drawString(text, textX, textY);

            g2.dispose();
        }
    }

    private void spielerLaden() {
        String url = "jdbc:mysql://localhost:3307/kicktn_db";
        String user = "root";
        String pass = "";

        String query = "SELECT spieler_ktn.SpielerID, spieler_ktn.Vorname, spieler_ktn.Nachname, " +
                "spieler_ktn.Position, spieler_ktn.Geburtsdatum, spieler_ktn.Foto, " +
                "spieler_ktn.Aktiv, verein_ktn.Vereinsname " +
                "FROM spieler_ktn " +
                "INNER JOIN verein_ktn ON spieler_ktn.VereinID = verein_ktn.VereinID";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String fotoName = rs.getString("Foto");
                ImageIcon scaledIcon = getScaledImageIcon("bilder/" + fotoName, 128, 160);

                boolean aktiv = rs.getBoolean("Aktiv");

                LocalDate geburtstag = rs.getDate("Geburtsdatum").toLocalDate();

                Object[] row = {
                        rs.getInt("SpielerID"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname"),
                        rs.getString("Position"),
                        geburtstag,
                        scaledIcon,
                        aktiv ? "Aktiv" : "Nicht aktiv",
                        rs.getString("Vereinsname")
                };
                tabelleModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Spieler:\n" + e.getMessage());
        }
    }

    private ImageIcon getScaledImageIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image originalImage = icon.getImage();

        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();

        return new ImageIcon(scaledImage);
    }

    private void exportiereAlsCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("CSV-Datei speichern");

        String defaultFileName = "Spieler.csv";
        chooser.setSelectedFile(new File(defaultFileName));

        int userSelection = chooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile))) {
                for (int i = 0; i < tabelleModel.getColumnCount(); i++) {
                    bw.append(tabelleModel.getColumnName(i));
                    if (i < tabelleModel.getColumnCount() - 1)
                        bw.append(",");
                }
                bw.append("\n");
                for (int row = 0; row < tabelleModel.getRowCount(); row++) {
                    for (int col = 0; col < tabelleModel.getColumnCount(); col++) {
                        Object value = tabelleModel.getValueAt(row, col);
                        if (value instanceof ImageIcon) {
                            bw.append("");
                        } else {
                            bw.append(value.toString().replace(",", " "));
                        }
                        if (col < tabelleModel.getColumnCount() - 1)
                            bw.append(",");
                    }
                    bw.append("\n");
                }
                JOptionPane.showMessageDialog(this, "Erfolgreich exportiert!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Fehler beim Exportieren:\n" + e.getMessage());
            }
        }
    }

    private void spielerHinzufuegen() {
        JTextField vornameField = createRoundedTextField();
        JTextField nachnameField = createRoundedTextField();
        JComboBox<String> positionCombo = new JComboBox<>(new String[] {
                "Torwart", "Verteidiger", "Mittelfeld", "Stürmer", "Ersatz", "Andere"
        });
        JTextField geburtsdatumField = createRoundedTextField("TT.MM.JJJJ");
        JComboBox<String> aktivCombo = new JComboBox<>(new String[] { "Aktiv", "Nicht aktiv" });
        JTextField vereinField = createRoundedTextField();
        JButton fotoButton = new JButton("Foto auswählen");
        JLabel fotoLabel = new JLabel("Kein Bild gewählt");
        final String[] fotoDateiname = { "" };

        fotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("bilder/");
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                fotoDateiname[0] = selectedFile.getName();
                fotoLabel.setText(selectedFile.getName());
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        Color accent = new Color(80, 120, 200);

        addLabeledField(panel, "Vorname:", vornameField, labelFont, accent);
        addLabeledField(panel, "Nachname:", nachnameField, labelFont, accent);
        addLabeledField(panel, "Position:", positionCombo, labelFont, accent);
        addLabeledField(panel, "Geburtsdatum (TT.MM.JJJJ):", geburtsdatumField, labelFont, accent);
        addLabeledField(panel, "Aktiv:", aktivCombo, labelFont, accent);
        addLabeledField(panel, "Verein:", vereinField, labelFont, accent);
        addLabeledField(panel, "Foto-Datei:", fotoButton, fotoLabel, labelFont, accent);

        int result = JOptionPane.showConfirmDialog(this, panel, "Neuen Spieler hinzufügen",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String vorname = vornameField.getText().trim();
                String nachname = nachnameField.getText().trim();
                String position = (String) positionCombo.getSelectedItem();
                String gebDatumStr = geburtsdatumField.getText().trim();
                String aktivStatus = (String) aktivCombo.getSelectedItem();
                String verein = vereinField.getText().trim();
                String fotoName = fotoDateiname[0].isEmpty() ? "standard.jpg" : fotoDateiname[0];

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
                LocalDate geburtsdatum = LocalDate.parse(gebDatumStr, formatter);

                ImageIcon fotoIcon = getScaledImageIcon("bilder/" + fotoName, 128, 160);

                // In Datenbank speichern
                String url = "jdbc:mysql://localhost:3307/kicktn_db";
                String user = "root";
                String pass = "";

                int vereinId = getVereinIdByName(verein);

                String insertSQL = "INSERT INTO spieler_ktn " +
                        "(Vorname, Nachname, Position, Geburtsdatum, Foto, Aktiv, VereinID) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = DriverManager.getConnection(url, user, pass);
                        PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                    pstmt.setString(1, vorname);
                    pstmt.setString(2, nachname);
                    pstmt.setString(3, position);
                    pstmt.setDate(4, java.sql.Date.valueOf(geburtsdatum));
                    pstmt.setString(5, fotoName);
                    pstmt.setBoolean(6, aktivStatus.equalsIgnoreCase("Aktiv"));
                    pstmt.setInt(7, vereinId);

                    pstmt.executeUpdate();

                    // Neue Spieler-ID aus der DB holen
                    int neueId = -1;
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            neueId = keys.getInt(1);
                        }
                    }

                    // Spieler auch im GUI anzeigen
                    Object[] neueZeile = {
                            neueId, vorname, nachname, position, geburtsdatum, fotoIcon, aktivStatus, verein
                    };
                    tabelleModel.addRow(neueZeile);

                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Eingabe oder beim Speichern: " + ex.getMessage(),
                        "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JTextField createRoundedTextField() {
        return createRoundedTextField("");
    }

    private JTextField createRoundedTextField(String placeholder) {
        JTextField field = new JTextField(15);
        field.setText(placeholder);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setBackground(new Color(255, 255, 255));
        field.setOpaque(true);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return field;
    }

    private void addLabeledField(JPanel panel, String labelText, JComponent field, Font font, Color color) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(field);
        panel.add(Box.createVerticalStrut(8));
    }

    private void addLabeledField(JPanel panel, String labelText, JButton button, JLabel label, Font font, Color color) {
        JLabel titleLabel = new JLabel(labelText);
        titleLabel.setFont(font);
        titleLabel.setForeground(color);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        filePanel.setBackground(panel.getBackground());
        filePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filePanel.add(button);
        filePanel.add(Box.createHorizontalStrut(10));
        filePanel.add(label);

        panel.add(titleLabel);
        panel.add(filePanel);
        panel.add(Box.createVerticalStrut(8));
    }

    private int getVereinIdByName(String vereinsname) throws SQLException {
        String url = "jdbc:mysql://localhost:3307/kicktn_db";
        String user = "root";
        String pass = "";

        String sql = "SELECT VereinID FROM verein_ktn WHERE Vereinsname = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vereinsname);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("VereinID");
                } else {
                    throw new SQLException("Verein '" + vereinsname + "' nicht gefunden.");
                }
            }
        }
    }

}