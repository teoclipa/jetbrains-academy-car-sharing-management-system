package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    private final Connection conn;

    public CustomerDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createCustomer(String name) {
        String sql = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String[]> listCustomers() {
        List<String[]> customers = new ArrayList<>();
        String sql = "SELECT ID, NAME FROM CUSTOMER ORDER BY ID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new String[]{String.valueOf(rs.getInt("ID")), rs.getString("NAME")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public String[] getRentedCarInfo(int customerId) {
        String sql = "SELECT CAR.NAME, COMPANY.NAME FROM CUSTOMER " +
                "JOIN CAR ON CUSTOMER.RENTED_CAR_ID = CAR.ID " +
                "JOIN COMPANY ON CAR.COMPANY_ID = COMPANY.ID " +
                "WHERE CUSTOMER.ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new String[]{rs.getString(1), rs.getString(2)};
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No car rented or error
    }

    @Override
    public void rentCar(int customerId, int carId) {
        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            pstmt.setInt(2, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnCar(int customerId) {
        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
