package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main
{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Bookstore Management System!");

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. List books by genre");
            System.out.println("2. List books by author");
            System.out.println("3. Update book details");
            System.out.println("4. Update customer details");
            System.out.println("5. Get sale history for a customer");
            System.out.println("6. Process a new sale");
            System.out.println("7. Calculate total revenue by genre");
            System.out.println("8. Generate books sold report");
            System.out.println("9. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter genre:");
                    String genre = scanner.nextLine();
                    List<Book> booksByGenre = BookService.listByGenre(genre);
                    displayBooks(booksByGenre);
                    break;
                case 2:
                    System.out.println("Enter author:");
                    String author = scanner.nextLine();
                    List<Book> booksByAuthor = BookService.listByAuthor(author);
                    displayBooks(booksByAuthor);
                    break;
                case 3:

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
                    Book book = new Book(bookIdToUpdate, newTitle, newAuthor, newGenre, newPrice, newQuantityInStock);

                    BookService.updateBookDetails(bookIdToUpdate, book);
                    break;
                case 4:
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
                    CustomerService.updateCustomerDetails(customerIdToUpdate, customer);
                    break;
                case 5:
                    System.out.println("Enter customer ID:");
                    int customerId = scanner.nextInt();
                    scanner.nextLine();
                    List<Sale> saleHistory = new CustomerService().getSaleHistory(customerId);
                    displaySaleHistory(saleHistory);
                    break;
                case 6:
                    System.out.print("Enter Sale ID for the sale: ");
                    int saleId = scanner.nextInt();
                    scanner.nextLine();


                    System.out.print("Enter Book ID for the sale: ");
                    int bookId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter Customer ID for the sale: ");
                    int customerid = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter sale date (YYYY-MM-DD): ");
                    String saleDateString = scanner.nextLine();
                    Date saleDate = java.sql.Date.valueOf(saleDateString);

                    System.out.print("Enter quantity sold: ");
                    int quantitySold = scanner.nextInt();

                    System.out.print("Enter total price: ");
                    int totalPrice = scanner.nextInt();

                    Sale sale = new Sale(saleId, bookId, customerid, saleDate, quantitySold, totalPrice);
                    SaleService.processNewSale(sale);
                    break;
                case 7:
                    Map<String, Double> revenueByGenre = SaleService.calculateTotalRevenueByGenre();
                    SaleService.printTotalRevenueByGenre(revenueByGenre);
                    break;
                case 8:
                    List<BooksSoldReport> booksSoldReport = SaleService.generateBooksSoldReport();
                    displayBooksSoldReport(booksSoldReport);
                    break;
                case 9:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please select again.");
            }
        }
    }

    private static void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("Books:");
            for (Book book : books) {
                System.out.println(book.getTitle() + " by " + book.getAuthor());
            }
        }
    }

    private static void displaySaleHistory(List<Sale> saleHistory) {
        if (saleHistory.isEmpty()) {
            System.out.println("No sale history found.");
        } else {
            System.out.println("Sale History:");
            for (Sale sale : saleHistory) {
                System.out.println("Sale ID: " + sale.getSaleId() + ", Book ID: " + sale.getBookId() +
                        ", Customer ID: " + sale.getCustomerId() + ", Date: " + sale.getDateOfSale() +
                        ", Quantity Sold: " + sale.getQuantitySold() + ", Total Price: $" + sale.getTotalPrice());
            }
        }
    }

    private static void displayBooksSoldReport(List<BooksSoldReport> booksSoldReport) {
        if (booksSoldReport.isEmpty()) {
            System.out.println("No books sold report found.");
        } else {
            System.out.println("Books Sold Report:");
            for (BooksSoldReport report : booksSoldReport) {
                System.out.println("Book Title: " + report.getBookTitle() + ", Customer Name: " + report.getCustomerName() +
                        ", Date of Sale: " + report.getDateOfSale());
            }
        }
    }
}