package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;


public class Main {
    private static String dbUrl = "jdbc:h2:./src/carsharing/db/carsharing"; // Default database name

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-databaseFileName".equals(args[i]) && i + 1 < args.length) {
                dbUrl = "jdbc:h2:./src/carsharing/db/" + args[i + 1];
                break;
            }
        }

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(true);
            DatabaseInitializer.init(conn);
            new MenuManager(conn).displayMainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


