import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class BirdGuiFrame extends JFrame {

    private BirdDatabase database;

    JTable table;
    JScrollPane scrollPane;
    JButton deleteButton;
    JButton addSightingButton;
    JButton changeDisplayNameButton;


    public BirdGuiFrame(BirdDatabase database) {
        this.database = database;

        // Configure this window
        setLayout(new FlowLayout());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Login controls
        LoginPanel loginPanel = new LoginPanel(this::attemptLogin);
        add(loginPanel);

        setVisible(true);
    }

    private void addTable(String[] columnNames, Object[][] data) {
        table = new JTable(data, columnNames);
        // table.setMinimumSize(new Dimension(1200, 800));
        // table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null);  // Make non-editable
        add(scrollPane);
        setVisible(true);
    }

    private void attemptLogin(String username, String password) {
        System.out.println("Trying to login with " + username + " and " + password);
        System.out.println("Hash of password " + password + " is: " + Passwords.sha256Hash(password));

        // Example of getting data and printing it to a table
        // This should probably only happen if login was successful!
        // String[] columnNames = {"Genus name", "Species epithet", "Family"};
        // Object[][] data = database.getSpeciesTableContents();
        // addTable(columnNames, data);

        // connect to database and check that the username and password are correct using func_valid_credentials
        // if the credentials are correct, show the bird database
        // if the credentials are incorrect, show an error message
        if (database.isValidLogin(username, Passwords.sha256Hash(password))) {
            String[] columnNames = {"id", "Common Name", "Scientific Name", "Family", "Date Spotted", "Latitude", "Longitude"};
            Object[][] data = database.getUsersSightings(username);
            deleteButton = new JButton("Delete row");
            deleteButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    remove(table);
                    remove(scrollPane);
                    int id = (int) table.getValueAt(row, 0);
                    database.deleteSighting(id);
                    Object[][] newData = database.getUsersSightings(username);

                    addTable(columnNames, newData);
                    revalidate();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            addSightingButton = new JButton("Add entry");
            addSightingButton.addActionListener(e -> {
                addSighting(username);
            });

            changeDisplayNameButton = new JButton("Change display name");
            changeDisplayNameButton.addActionListener(e -> {
                JTextField displayNameField = new JTextField();
                Object[] message = {
                    "Display Name:", displayNameField
                };
                int option = JOptionPane.showConfirmDialog(null, message, "Enter new display name", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String displayName = displayNameField.getText();
                    database.changeDisplayName(username, displayName);
                }
            });

            add(deleteButton);
            add(addSightingButton);
            add(changeDisplayNameButton);
            addTable(columnNames, data);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid login", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Invalid login");
        }
    }

    private void addSighting(String username) {
        JTextField genusField = new JTextField();
        JTextField speciesField = new JTextField();
        JTextField latitudeField = new JTextField();
        JTextField longitudeField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField notesField = new JTextField();

        Object[] message = {
            "Genus:", genusField,
            "Species:", speciesField,
            "Latitude:", latitudeField,
            "Longitude:", longitudeField,
            "Date YYYY/MM/DD:", dateField,
            "Notes:", notesField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Enter sighting details", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String genus = genusField.getText();
            String species = speciesField.getText();
            double latitude;
            try {
                latitude = Double.parseDouble(latitudeField.getText());
            } catch (NumberFormatException e) {
                latitude = 0;
            }
            double longitude;
            try {
                longitude = Double.parseDouble(longitudeField.getText());
            } catch (NumberFormatException e) {
                longitude = 0;
            }
            String date = dateField.getText();
            String notes = notesField.getText();

            database.addSighting(genus, species, latitude, longitude, date, notes, username);
            remove(table);
            remove(scrollPane);
            String[] columnNames = {"id", "Common Name", "Scientific Name", "Family", "Date Spotted", "Latitude", "Longitude"};
            Object[][] data = database.getUsersSightings(username);
            addTable(columnNames, data);
            revalidate();
            repaint();
        }
    }
}
