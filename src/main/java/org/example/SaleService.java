package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleService {
    public static void processNewSale(Sale sale) {
        String query = "INSERT INTO sales (bookid, customerid, dateofsale, quantitysold, totalprice) VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, sale.getBookId());
            preparedStatement.setInt(2, sale.getCustomerId());
            preparedStatement.setDate(3, sale.getDateOfSale());
            preparedStatement.setInt(4, sale.getQuantitySold());
            preparedStatement.setDouble(5, sale.getTotalPrice());
            preparedStatement.execute();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Double> calculateTotalRevenueByGenre() {
        Map<String, Double> map = new HashMap<>();
        String str = "SELECT books.genre, SUM(sales.totalprice) AS TotalRevenue " +
                "FROM sales " +
                "JOIN books ON sales.bookid = books.bookid " +
                "GROUP BY books.genre";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(str);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            while (resultSet.next())
            {
                String genre = resultSet.getString("genre");
                double totalRevenue = resultSet.getDouble("TotalRevenue");
                map.put(genre, totalRevenue);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return map;
    }


    public static List<BooksSoldReport> generateBooksSoldReport()
    {
        String str =
                "SELECT sales.dateofsale, books.title, customers.name " +
                        "FROM sales " +
                        "JOIN books ON sales.bookid = books.bookid " +
                        "JOIN customers ON sales.customerid = customers.customerid";


        List<BooksSoldReport> booksSoldReport = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(str);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                String customerName = resultSet.getString("name");
                java.sql.Date dateOfSale = resultSet.getDate("dateofsale");

                BooksSoldReport reportEntry = new BooksSoldReport(bookTitle, customerName, dateOfSale);
                booksSoldReport.add(reportEntry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booksSoldReport;
    }


    public static void printTotalRevenueByGenre(Map<String, Double> revenueByGenre) {
        System.out.println("Total Revenue by Genre:");
        System.out.println("------------------------");
        for (Map.Entry<String, Double> entry : revenueByGenre.entrySet()) {
            String genre = entry.getKey();
            double totalRevenue = entry.getValue();
            System.out.printf("%-20s : $%.2f%n", genre, totalRevenue);
        }
    }

}
