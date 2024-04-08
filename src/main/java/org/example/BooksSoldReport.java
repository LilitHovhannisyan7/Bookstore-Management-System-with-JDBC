package org.example;

import java.sql.Date;

public class BooksSoldReport {
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
