import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Object that can be used to interact with the database itself (to keep this separate from all the GUI stuff). */
public class BirdDatabase {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public BirdDatabase(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public Object[][] getUsersSightings(String username) {
        List<Object[]> dataList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT sighting.id, sighting.genus_name, sighting.species_epithet, sighting.latitude, sighting.longitude, sighting.date_spotted, common_name, family_name FROM sighting JOIN common_name ON sighting.genus_name = common_name.genus_name AND sighting.species_epithet = common_name.species_epithet JOIN species ON sighting.genus_name = species.genus_name AND sighting.species_epithet = species.species_epithet WHERE sighting.user_email = '" + username + "';");

            while (rs.next()) {
                int id = rs.getInt("id");
                String genus = rs.getString("genus_name");
                String species = rs.getString("species_epithet");
                String family = rs.getString("family_name");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                String date = rs.getDate("date_spotted").toString();
                String commonName = rs.getString("common_name");
                dataList.add(new Object[]{id, commonName, genus + " " + species, family, date, lat, lon});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[dataList.size()][3]);
    }

    public void deleteSighting(int id) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM sighting WHERE id = " + id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidLogin(String user, String pass) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT func_valid_credentials('" + user + "', '" + pass + "');");

            while (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addSighting(String genus, String species, double latitude, double longitude, String date, String notes, String username) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);) {
            System.out.println(latitude + " " + longitude);

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO sighting (genus_name, species_epithet, latitude, longitude, date_spotted, user_email, notes) VALUES ('");
            sb.append(genus);
            sb.append("', '");
            sb.append(species);
            sb.append("', ");
            sb.append(latitude);
            sb.append(", ");
            sb.append(longitude);
            sb.append(", '");
            sb.append(date);
            sb.append("', '");
            sb.append(username);
            sb.append("', '");
            sb.append(notes);
            sb.append("');");

            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sb.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void changeDisplayName(String username, String displayName) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("UPDATE user SET display_name = '" + displayName + "' WHERE email = '" + username + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
