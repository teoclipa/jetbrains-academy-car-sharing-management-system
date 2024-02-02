package dao;

import java.util.List;

public interface CustomerDao {
    void createCustomer(String name);
    List<String[]> listCustomers();
    String[] getRentedCarInfo(int customerId);
    void rentCar(int customerId, int carId);
    void returnCar(int customerId);
}
