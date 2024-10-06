package com.pluralsight;

public class Book {
    private final int id;
    private final String isbn;
    private final String title;
    private boolean isCheckedOut;
    private String checkedOutTo;

    public Book(int id, String title, String isbn, boolean isCheckedOut, String checkedOutTo) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.isCheckedOut = isCheckedOut;
        this.checkedOutTo = checkedOutTo;
    }

    public boolean isAvailable() {
        return !isCheckedOut;
    }

    public String getISBN() {
        return isbn;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s\n\t[ISBN: %s | ID: %d%s]",
                title, isbn, id,
                isCheckedOut ? " | Checked Out By: " + checkedOutTo : "");
    }

    public void checkIn() {
        this.isCheckedOut = false;
        this.checkedOutTo = "";
    }

    public void checkOut(String name) {
        this.isCheckedOut = true;
        this.checkedOutTo = name;
    }

}
