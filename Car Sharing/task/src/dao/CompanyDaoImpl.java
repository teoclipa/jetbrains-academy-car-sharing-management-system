package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    private final Connection conn;

    public CompanyDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createCompany(String name) {
        String sql = "INSERT INTO COMPANY (NAME) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("The company was created!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String[]> listCompanies() {
        List<String[]> companies = new ArrayList<>();
        String sql = "SELECT ID, NAME FROM COMPANY ORDER BY ID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                companies.add(new String[]{String.valueOf(id), name});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return companies;
    }
}
