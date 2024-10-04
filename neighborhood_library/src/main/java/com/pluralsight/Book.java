package com.pluralsight;

public class Book {
    private int id;
    private String isbn;
    private String title;
    private Boolean isCheckedOut;
    private String checkedOutTo;

    public Book(int id, String isbn, String title) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.isCheckedOut = false;
        this.checkedOutTo = "";
    }

    private void checkOut(String name){
        this.isCheckedOut = true;
        this.checkedOutTo = name.trim().toUpperCase();
    }

    private void checkIn(){
        this.isCheckedOut = false;
        this.checkedOutTo = "";
    }
}
