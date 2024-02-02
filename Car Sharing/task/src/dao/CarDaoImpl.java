package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class CarDaoImpl implements CarDao {
    private final Connection conn;

    public CarDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createCar(String name, int companyId) {
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, companyId);
            pstmt.executeUpdate();
            System.out.println("The car was added!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void listCars(int companyId) {
        String sql = "SELECT ID, NAME FROM CAR WHERE COMPANY_ID = ? ORDER BY ID";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("The car list is empty!");
                return;
            }

            System.out.println("Car list:");
            int index = 1; // Start indexing from 1 for the output
            do {
                String name = rs.getString("NAME");
                System.out.println(index + ". " + name);
                index++; // Increment index for each car
            } while (rs.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String[]> listAvailableCars(int companyId) {
        List<String[]> cars = new ArrayList<>();
        String sql = "SELECT ID, NAME FROM CAR WHERE COMPANY_ID = ? AND ID NOT IN " +
                "(SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT NULL) ORDER BY ID";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cars.add(new String[]{String.valueOf(rs.getInt("ID")), rs.getString("NAME")});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }
}