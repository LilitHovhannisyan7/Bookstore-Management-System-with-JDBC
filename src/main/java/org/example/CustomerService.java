package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class CustomerService {
    public static void updateCustomerDetails(int id, Customer customer) {
        String query = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE customerId = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhone());

            preparedStatement.setInt(4, id);
            preparedStatement.execute();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Sale> getSaleHistory(int customerId) {
        List<Sale> list = new ArrayList<>();
        String query = "SELECT sales.saleid AS saleID, sales.bookid AS BookID, sales.customerid AS CustomerId, sales.dateofsale, sales.quantitysold, sales.totalprice " +
                "FROM sales JOIN customers ON sales.customerId = customers.customerId " +
                "WHERE customers.customerId = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    int saleId = resultSet.getInt("saleid");
                    int bookId = resultSet.getInt("bookid");
//                    int customerId = resultSet.getInt("customerId");
                    Date dateOfSale = resultSet.getDate("dateofsale");
                    int quantitySold = resultSet.getInt("quantitysold");
                    double totalPrice = resultSet.getDouble("totalprice");
                    Sale sale = new Sale(saleId, bookId, customerId, dateOfSale, quantitySold, totalPrice);
                    list.add(sale);
                }
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
