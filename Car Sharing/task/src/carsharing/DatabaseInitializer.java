package carsharing;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void init(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY," +
                    "NAME VARCHAR UNIQUE NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY," +
                    "NAME VARCHAR UNIQUE NOT NULL," +
                    "COMPANY_ID INT NOT NULL," +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))");
            stmt.execute("CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY," +
                    "NAME VARCHAR UNIQUE NOT NULL," +
                    "RENTED_CAR_ID INT," +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
