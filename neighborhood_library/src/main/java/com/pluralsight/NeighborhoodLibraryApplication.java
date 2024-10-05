package com.pluralsight;

import java.util.Scanner;

public class NeighborhoodLibraryApplication {

    Book[] inventory = new Book[20];
    public static void main(String[] args) {
        InitializeInventory();

        //  Main Application Loop, Handles All I/O Methods
        while(1==1){
            switch(HomeScreen()){
                case 1:
                    DisplayAvailableBooks();
                    break;

                case 2:
                    DisplayCheckedOut();
                    break;

                case 3:
                    return;

                default:  //  Invalid Selection Handling
                    break;
            }
        }
    }

    //  Method To Set A Random Inventory Of Books
    private static void InitializeInventory(){
        // Generate Random ISBN
        // Pull Remaining Data
        // Construct Book, Add To Array
        // Delay At Least 1 Second For API Limits

    }

    private static Book RandomBook(String randomISBN){
        int id = 0;
        String title = "";

        Book newBook = new Book();

        return newBook;
    }

    //  Menu Display Methods, Called From Main Loop
    private static int HomeScreen(){
        Scanner userInput = new Scanner(System.in);
        int option;

        System.out.println("\t\t\tHome Page\n\nMessage Of The Day:\nHello, Welcome To Our Community Library!\n\nMenu Options:\n1 - Show Available Books\n2 - Show Checked Out Books\n3 - Exit");
        option = userInput.nextInt();

        return option;
    }

    private static void DisplayAvailableBooks(){

    }

    private static void DisplayCheckedOut(){

    }

    //  Sub-menu Action Methods
    private static void CheckBookIn(){

    }

    private static void CheckBookOut(){

    }


}
