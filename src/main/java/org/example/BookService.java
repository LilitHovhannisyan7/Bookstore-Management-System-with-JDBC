package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private Book book;

    public static void updateBookDetails(int id, Book book) {
        String query = "UPDATE books SET title = ?, author = ?, price = ?, genre = ?, quantityinstock = ? WHERE bookid = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setDouble(3, book.getPrice());
            preparedStatement.setString(4, book.getGenre());
            preparedStatement.setInt(5, book.getQuantityInStock());
            preparedStatement.setInt(6, id);
            preparedStatement.execute();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Book> listByGenre(String genre) {
        String query = "SELECT * FROM Books WHERE genre = ?";
        return getByCriteria(query, genre);
    }



    public static List<Book> listByAuthor(String author) {
        String query = "SELECT * FROM Books WHERE author = ?";
        return getByCriteria(query, author);
    }


    private static List<Book> getByCriteria(String query, String criteria) {
        List<Book> list = new ArrayList<>();
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, criteria);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    int bookId = resultSet.getInt("bookid");
                    String title = resultSet.getString("title");
                    String bookAuthor = resultSet.getString("author");
                    double price = resultSet.getDouble("price");
                    String bookGenre = resultSet.getString("genre");
                    int quantityInStock = resultSet.getInt("quantityInStock");
                    Book book = new Book(bookId, title, bookAuthor, bookGenre, price, quantityInStock);
                    list.add(book);
                }
            }
            catch(SQLException e) {
                e.printStackTrace();
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
