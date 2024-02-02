package dao;

import java.util.List;

public interface CarDao {
    void createCar(String name, int companyId);
    void listCars(int companyId);
    List<String[]> listAvailableCars(int companyId);

}
