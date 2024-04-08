public class BirdGui {
    public static void main(String[] args) {
        try {
            String dbUser = args[0];
            String dbPassword = args[1];
            String dbName = dbUser + "_CS3101_P2";
            String dbUrl = "jdbc:mariadb://" + dbUser + ".teaching.cs.st-andrews.ac.uk:3306/" + dbName;

            BirdDatabase database = new BirdDatabase(dbUrl, dbUser, dbPassword);
            BirdGuiFrame frame = new BirdGuiFrame(database);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: java -cp mariadb-java-client-3.3.3.jar:. Test USERNAME PASSWORD");
        } 
    }

}
