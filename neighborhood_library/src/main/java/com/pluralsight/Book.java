package com.pluralsight;

public class Book {
    private final int id;
    private final String isbn;
    private final String title;
    private boolean isCheckedOut;
    private String checkedOutTo;

    //================================================================================================================== Constructor
    public Book(int id, String title, String isbn, boolean isCheckedOut, String checkedOutTo) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.isCheckedOut = isCheckedOut;
        this.checkedOutTo = checkedOutTo;
    }

    //================================================================================================================== Getter Methods
    public boolean IsAvailable(){
        return !isCheckedOut;
    }
    public String GetISBN(){return isbn;}

    //================================================================================================================== Override toString For Book Output
    @Override
    public String toString() {
        if(this.isCheckedOut) return this.title + "\n\t[ISBN:\t" + this.isbn + "\t|\tID:\t" + this.id+"\t|\tChecked Out By:\t"+this.checkedOutTo+"]\n";
        return this.title + "\n\t[ISBN:\t" + this.isbn + "\t|\tID:\t" + this.id+"]\n";
    }

    //================================================================================================================== Setter Methods
    public void CheckIn() {
        this.isCheckedOut = false;
        this.checkedOutTo = "";
    }

    public void CheckOut(String name) {
        this.isCheckedOut = true;
        this.checkedOutTo = name.trim();
    }
}
