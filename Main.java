package org.example;

import java.sql.*;
import java.sql.Date;
import java.util.*;

class Book
{
    private int bookId;
    private String title;
    private String author;
    private double price;
    private String genre;
    private int quantityInStock;


    public Book(int bookId, String title, String author, double price, String genre, int quantityInStock)
    {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.price = price;
        this.genre = genre;
        this.quantityInStock = quantityInStock;
    }


    public int getBookId()
    {
        return bookId;
    }

    public String getTitle()
    {
        return title;
    }

    public String getAuthor()
    {
        return author;
    }

    public double getPrice()
    {
        return price;
    }

    public String getGenre()
    {
        return genre;
    }

    public int getQuantityInStock()
    {
        return quantityInStock;
    }
}



class Customer
{
    private int customerId;
    private String name;
    private String email;
    private String phone;
    public Customer(int customerId, String name, String email, String phone)
    {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhone()
    {
        return phone;
    }
}





class Sale
{
    private int saleId;
    private int bookId;
    private int customerId;
    private Date dateOfSale;
    private int quantitySold;
    private double totalPrice;


    public Sale(int saleId, int bookId, int customerId, Date dateOfSale, int quantitySold, double totalPrice)
    {
        this.saleId = saleId;
        this.bookId = bookId;
        this.customerId = customerId;
        this.dateOfSale = dateOfSale;
        this.quantitySold = quantitySold;
        this.totalPrice = totalPrice;
    }

    public int getSaleId()
    {
        return saleId;
    }

    public int getBookId()
    {
        return bookId;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public Date getDateOfSale()
    {
        return dateOfSale;
    }

    public int getQuantitySold()
    {
        return quantitySold;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }
}
class BooksSoldReport
{
    private String bookTitle;
    private String customerName;
    private Date dateOfSale;

    public BooksSoldReport(String bookTitle, String customerName, Date dateOfSale) {
        this.bookTitle = bookTitle;
        this.customerName = customerName;
        this.dateOfSale = dateOfSale;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Date getDateOfSale() {
        return dateOfSale;
    }
}
public class Main {
    public static Connection getConnection() throws SQLException {
        String address = "jdbc:postgres:/localhost:5432/bookstoredb";
        String username = "postgres";
        String password = "4652343L";
        Connection connection = DriverManager.getConnection(address, username, password);
        return DriverManager.getConnection(address, username, password);
    }

    public static void updateBookDetails(Book book) {
        String str = "UPDATE Books SET bookid=?, title=?, author=?, price=?, genre=?, quantityinstock=? WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(str)) {
            preparedStatement.setInt(1, book.getBookId());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setDouble(4, book.getPrice());
            preparedStatement.setString(5, book.getGenre());
            preparedStatement.setInt(6, book.getQuantityInStock());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Book> getBooksByGenre(String genre) {
        String str = "SELECT * FROM books WHERE genre=?";
        return getBooksByCriteria(str, genre);
    }

    public static List<Book> getBooksByAuthor(String author) {
        String str = "SELECT * FROM books WHERE author=?";
        return getBooksByCriteria(str, author);
    }

    private static List<Book> getBooksByCriteria(String query, String criteria) {
        List<Book> books = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, criteria);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int bookId = resultSet.getInt("book_id");
                    String title = resultSet.getString("title");
                    String bookAuthor = resultSet.getString("author");
                    double price = resultSet.getDouble("price");
                    String bookGenre = resultSet.getString("genre");
                    int quantityInStock = resultSet.getInt("quantityInStock");
                    Book book = new Book(bookId, title, bookAuthor, price, bookGenre, quantityInStock);
                    books.add(book);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }


    public static void updateCustomerDetails(Customer customer) {
        String str = "UPDATE customers SET first_name=?, email=?, phone=? WHERE customer_id=?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(str)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setInt(5, customer.getCustomerId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Sale> getPurchaseHistory(int customerId) {
        List<Sale> saleHistory = new ArrayList<>();

        String str = "SELECT saleID, dateOfSale, title AS BookTitle, books.author AS BookAuthor, sales.totalPrice " +
                "FROM sales " +
                "JOIN customers ON sales.customerID = customers.customerID " +
                "WHERE sales.customerID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(str)) {

            preparedStatement.setInt(1, customerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int saleId = resultSet.getInt("saleid");
                    int bookId = resultSet.getInt("bookid");
                    int customerid = resultSet.getInt("customerid");
                    Date dateOfSale = resultSet.getDate("dateofsale");
                    int quantitySold = resultSet.getInt("quantitysold");
                    double totalPrice = resultSet.getDouble("totalprice");

                    Sale sale = new Sale(saleId, bookId, customerid, dateOfSale, quantitySold, totalPrice);
                    saleHistory.add(sale);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return saleHistory;
    }


    public static void processNewSale(Sale sale)
    {
        String str = "INSERT INTO Sales (saleid, bookid, customerid, dateofsale, quantitysold, totalprice) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(str))
        {

            preparedStatement.setInt(1, sale.getSaleId());
            preparedStatement.setInt(2, sale.getBookId());
            preparedStatement.setInt(3, sale.getCustomerId());
            preparedStatement.setDate(4, new Date(sale.getDateOfSale().getTime()));
            preparedStatement.setInt(5, sale.getQuantitySold());
            preparedStatement.setDouble(6, sale.getTotalPrice());
            preparedStatement.executeUpdate();

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

}



public static Map<String, Double> calculateTotalRevenueByGenre()
{
    String str = "SELECT books.genre, SUM(sales.totalprice) AS TotalRevenue " +
                        "FROM sales " +
                        "JOIN books ON sales.bookiD = books.bookiD " +
                        "GROUP BY books.genre";
    Map<String, Double> totalRevenueByGenre = new HashMap<>();

    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(str);
         ResultSet resultSet = preparedStatement.executeQuery())
    {
        while (resultSet.next())
        {
            String genre = resultSet.getString("genre");
            double totalRevenue = resultSet.getDouble("TotalRevenue");
            totalRevenueByGenre.put(genre, totalRevenue);
        }

    } catch (SQLException e)
    {
        e.printStackTrace();
    }

    return totalRevenueByGenre;
}


public static List<BooksSoldReport> generateBooksSoldReport()
    {
        String str =
                "SELECT sales.dateofsale, books.title, customers.name " +
                        "FROM sales " +
                        "JOIN books ON sales.bookid = books.bookid " +
                        "JOIN customers ON sales.customerid = customers.customerid";


        List<BooksSoldReport> booksSoldReport = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(str);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("BookTitle");
                String customerName = resultSet.getString("CustomerName");
                java.sql.Date dateOfSale = resultSet.getDate("DateOfSale");

                BooksSoldReport reportEntry = new BooksSoldReport(bookTitle, customerName, dateOfSale);
                booksSoldReport.add(reportEntry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booksSoldReport;
    }

    public static Map<String, Double> generateTotalRevenueByGenreReport() {
        Map<String, Double> totalRevenueByGenre = new HashMap<>();

        String str =
                "SELECT books.genre, SUM(sales.totalprice) as TotalRevenue " +
                        "FROM sales " +
                        "JOIN books ON sales.bookid = books.bookid " +
                        "GROUP BY books.genre";


        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(str);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String genre = resultSet.getString("genre");
                double totalRevenue = resultSet.getDouble("TotalRevenue");
                totalRevenueByGenre.put(genre, totalRevenue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRevenueByGenre;
    }


    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    updateBook1();
                    break;
                case 2:
                    updateCustomer1();
                    break;
                case 3:
                    viewCustomerPurchaseHistory1();
                    break;
                case 4:
                    processNewSale1();
                    break;
                case 5:
                    calculateTotalRevenueByGenre1();
                    break;
                case 6:
                    generateBooksSoldReport1();
                    break;
                case 7:
                    generateTotalRevenueByGenreReport1();
                    break;
                case 0:
                    System.out.println("Exiting the application");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("Bookstore Management System");
        System.out.println("1. Update Book");
        System.out.println("2. Update Customer");
        System.out.println("3. View Customer's Purchase History");
        System.out.println("4. Process New Sale");
        System.out.println("5. Calculate Total Revenue by Genre");
        System.out.println("6. Generate Books Sold Report");
        System.out.println("7. Generate Total Revenue by Genre Report");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }


    private static void calculateTotalRevenueByGenre1() {
        Map<String, Double> totalRevenueByGenre = calculateTotalRevenueByGenre();
        for (Map.Entry<String, Double> entry : totalRevenueByGenre.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    private static void generateBooksSoldReport1() {
        List<BooksSoldReport> booksSoldReport = generateBooksSoldReport();
        for (BooksSoldReport entry : booksSoldReport) {
            System.out.println("Date of Sale: " + entry.getDateOfSale() +
                    " Book Title: " + entry.getBookTitle() +
                    " Customer Name: " + entry.getCustomerName());
        }
    }

    private static void generateTotalRevenueByGenreReport1() {
        Map<String, Double> totalRevenueByGenreReport = generateTotalRevenueByGenreReport();
        for (Map.Entry<String, Double> entry : totalRevenueByGenreReport.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


    private static void updateBook1() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Book ID to update: ");
        int bookIdToUpdate = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new title: ");
        String newTitle = scanner.nextLine();

        System.out.print("Enter new author: ");
        String newAuthor = scanner.nextLine();

        System.out.print("Enter new genre: ");
        String newGenre = scanner.nextLine();

        System.out.print("Enter new price: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter new quantity in stock: ");
        int newQuantityInStock = scanner.nextInt();
        Book book = new Book(bookIdToUpdate, newTitle, newAuthor, newPrice, newGenre, newQuantityInStock);
        updateBookDetails(book);
    }

    private static void updateCustomer1()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Customer ID to update: ");
        int customerIdToUpdate = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();

        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();

        System.out.print("Enter new phone: ");
        String newPhone = scanner.nextLine();

        Customer customer = new Customer(customerIdToUpdate, newName, newEmail, newPhone);
        updateCustomerDetails(customer);
    }


    private static void viewCustomerPurchaseHistory1() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Customer ID to view purchase history: ");
        int customerIdToView = scanner.nextInt();
        scanner.nextLine();
        List<Sale> purchaseHistory = getPurchaseHistory(customerIdToView);
        for (Sale sale : purchaseHistory) {
            System.out.println("Sale ID: " + sale.getSaleId() +
                    " Date: " + sale.getDateOfSale() +
                    " BookId: " + sale.getBookId() +
                    " CustomerId: " + sale.getCustomerId() +
                    " Quantity Sold: " + sale.getQuantitySold() +
                    " Total Price: " + sale.getTotalPrice());
        }
    }

    private static void processNewSale1() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Sale ID for the sale: ");
        int saleId = scanner.nextInt();
        scanner.nextLine();


        System.out.print("Enter Book ID for the sale: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Customer ID for the sale: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter sale date (YYYY-MM-DD): ");
        String saleDateString = scanner.nextLine();
        Date saleDate = java.sql.Date.valueOf(saleDateString);

        System.out.print("Enter quantity sold: ");
        int quantitySold = scanner.nextInt();

        System.out.print("Enter total price: ");
        int totalPrice = scanner.nextInt();

        Sale sale = new Sale(saleId, bookId, customerId, saleDate, quantitySold, totalPrice);
        processNewSale(sale);

    }
}