package carsharing;

import dao.CarDaoImpl;
import dao.CompanyDaoImpl;
import dao.CustomerDaoImpl;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class MenuManager {
    private final Connection conn;
    private final Scanner scanner = new Scanner(System.in);

    public MenuManager(Connection conn) {
        this.conn = conn;
    }

    public void displayMainMenu() {
        while (true) {
            System.out.println("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayManagerMenu();
                    break;
                case "2":
                    displayCustomerLogin();
                    break;
                case "3":
                    createCustomer();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Unknown command!");
            }
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        new CustomerDaoImpl(conn).createCustomer(name);
        System.out.println("The customer was added!");
    }

    private void displayCustomerLogin() {
        List<String[]> customers = new CustomerDaoImpl(conn).listCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        }

        System.out.println("Choose a customer:");
        for (String[] customer : customers) {
            System.out.println(customer[0] + ". " + customer[1]);
        }
        System.out.println("0. Back");
        String choice = scanner.nextLine();
        if ("0".equals(choice)) return;
        try {
            int customerId = Integer.parseInt(choice);
            displayCustomerMenu(customers.get(customerId - 1)[0]); // Assumes customer IDs are sequential and start from 1
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Invalid selection.");
        }
    }

    private void displayCustomerMenu(String customerId) {
        while (true) {
            System.out.println("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    rentCar(customerId);
                    break;
                case "2":
                    returnCar(customerId);
                    break;
                case "3":
                    myRentedCar(customerId);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Unknown command!");
            }
        }
    }

    private void displayManagerMenu() {
        while (true) {
            System.out.println("1. Company list\n2. Create a company\n0. Back");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<String[]> companies = new CompanyDaoImpl(conn).listCompanies();
                    if (companies.isEmpty()) {
                        System.out.println("The company list is empty!");
                    } else {
                        displayCompanyList(companies);
                    }
                    break;
                case "2":
                    System.out.println("Enter the company name:");
                    String name = scanner.nextLine();
                    new CompanyDaoImpl(conn).createCompany(name);
                    System.out.println("The company was created!");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Unknown command!");
            }
        }
    }

    private void displayCompanyList(List<String[]> companies) {
        System.out.println("Choose the company:");
        for (String[] company : companies) {
            System.out.println(company[0] + ". " + company[1]);
        }
        System.out.println("0. Back");

        String choice = scanner.nextLine();
        if ("0".equals(choice)) return;

        try {
            int selectedCompanyId = Integer.parseInt(choice);
            for (String[] company : companies) {
                if (Integer.parseInt(company[0]) == selectedCompanyId) {
                    displayCompanyMenu(selectedCompanyId, company[1]);
                    break;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private void displayCompanyMenu(int companyId, String companyName) {
        while (true) {
            System.out.println("'" + companyName + "' company");
            System.out.println("1. Car list\n2. Create a car\n0. Back");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    new CarDaoImpl(conn).listCars(companyId);
                    break;
                case "2":
                    System.out.println("Enter the car name:");
                    String carName = scanner.nextLine();
                    new CarDaoImpl(conn).createCar(carName, companyId);
                    System.out.println("The car was added!");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Unknown command!");
            }
        }
    }

    private void rentCar(String customerIdStr) {
        int customerId = Integer.parseInt(customerIdStr);
        String[] rentedCarInfo = new CustomerDaoImpl(conn).getRentedCarInfo(customerId);
        if (rentedCarInfo != null) {
            System.out.println("You've already rented a car!");
            return;
        }

        List<String[]> companies = new CompanyDaoImpl(conn).listCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        System.out.println("Choose a company:");
        for (String[] company : companies) {
            System.out.println(company[0] + ". " + company[1]);
        }
        System.out.println("0. Back");
        String companyChoice = scanner.nextLine();
        if ("0".equals(companyChoice)) return;
        int companyId = Integer.parseInt(companyChoice);

        List<String[]> availableCars = new CarDaoImpl(conn).listAvailableCars(companyId);
        if (availableCars.isEmpty()) {
            System.out.println("No available cars in the '" + companies.get(companyId - 1)[1] + "' company.");
            return;
        }

        System.out.println("Choose a car:");
        for (String[] car : availableCars) {
            System.out.println(car[0] + ". " + car[1]);
        }
        System.out.println("0. Back");
        String carChoice = scanner.nextLine();
        if ("0".equals(carChoice)) return;
        int carId = Integer.parseInt(carChoice);

        new CustomerDaoImpl(conn).rentCar(customerId, Integer.parseInt(availableCars.get(carId - 1)[0]));
        System.out.println("You rented '" + availableCars.get(carId - 1)[1] + "'");
    }

    private void returnCar(String customerIdStr) {
        int customerId = Integer.parseInt(customerIdStr);
        String[] rentedCarInfo = new CustomerDaoImpl(conn).getRentedCarInfo(customerId);
        if (rentedCarInfo == null) {
            System.out.println("You didn't rent a car!");
            return;
        }

        new CustomerDaoImpl(conn).returnCar(customerId);
        System.out.println("You've returned a rented car!");
    }

    private void myRentedCar(String customerIdStr) {
        int customerId = Integer.parseInt(customerIdStr);
        String[] rentedCarInfo = new CustomerDaoImpl(conn).getRentedCarInfo(customerId);
        if (rentedCarInfo == null) {
            System.out.println("You didn't rent a car!");
            return;
        }

        System.out.println("Your rented car:");
        System.out.println(rentedCarInfo[0]);
        System.out.println("Company:");
        System.out.println(rentedCarInfo[1]);
    }

}
