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
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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

        //Styling
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

        // Geburtsdatum "schöner" angeben
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

        // Vordefinierten Dateinamen setzen
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
}