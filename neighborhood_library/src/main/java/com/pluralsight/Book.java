package com.pluralsight;

public class Book {
    private int id;
    private String isbn;
    private String title;
    private boolean isCheckedOut; // Use primitive type
    private String checkedOutTo;

    // Full Constructor
    public Book(int id, String title, String isbn, boolean isCheckedOut, String checkedOutTo) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.isCheckedOut = isCheckedOut;
        this.checkedOutTo = checkedOutTo;
    }

    // Default Constructor
    public Book() {
        this.id = 0;
        this.isbn = "";
        this.title = "";
        this.isCheckedOut = false;
        this.checkedOutTo = "";
    }

    public void CheckOut(String name) {
        this.isCheckedOut = true;
        this.checkedOutTo = name.trim().toUpperCase();
    }

    public void CheckIn() {
        this.isCheckedOut = false;
        this.checkedOutTo = "";
    }

    // Override toString For Book Output
    @Override
    public String toString() {
        return this.title + " | ISBN: " + this.isbn + " | ID: " + this.id;
    }
}
