package dao;

import java.util.List;

public interface CompanyDao {
    void createCompany(String name);
    List<String[]> listCompanies();
}
