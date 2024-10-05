package com.pluralsight;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NeighborhoodLibraryApplication {

    static Book[] inventory = new Book[20];

    public static void main(String[] args) {
        InitializeInventory();

        //============================================================================================================== Main Application Loop
        while (true) {
            switch (HomeScreen()) {
                case 1 -> DisplayAvailableBooks();
                case 2 -> DisplayCheckedOut();
                case 3 -> {
                    return;
                }
                default -> {
                }
            }
        }
    }

    //================================================================================================================== Fill List With Randomized Books
    private static void InitializeInventory() {
        List<Book> bookList = FetchBooks();

        Random random = new Random();
        for (int i = 0; i < inventory.length; i++) {
            Book book = bookList.get(random.nextInt(bookList.size()));
            inventory[i] = book;
        }
    }

        //============================================================================================================== Menu Display Methods
    private static int HomeScreen() {
        Scanner userInput = new Scanner(System.in);
        int option;

        System.out.println("\t\t\tHome Page\n\nMessage Of The Day:\nHello, Welcome To Our Community Library!\n\nMenu Options:\n1 - Show Available Books\n2 - Show Checked Out Books\n3 - Exit");
        option = userInput.nextInt();

        return option;
    }

    private static void DisplayAvailableBooks() {
        for (Book b : inventory) {  // Temporary for testing randomized inventory
            if (b != null) {
                System.out.println(b);
            }
        }
    }

    private static void DisplayCheckedOut() {

    }

    //================================================================================================================== Sub-menu Action Methods
    private static void CheckBookIn(){

    }

    private static void CheckBookOut(){

    }

    //================================================================================================================== Randomized Inventory Fetching
    private static List<Book> FetchBooks() {
        String apiUrl = "https://openlibrary.org/search.json?q=programming&limit=100";
        List<Book> bookList = new ArrayList<>();

        try {  //  Send GET, Read Response
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //  Convert Response To JSON Object For Easier Parsing
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.getInt("numFound") > 0) {
                JSONArray docs = jsonResponse.getJSONArray("docs");
                for (int i = 0; i < docs.length(); i++) {
                    JSONObject doc = docs.getJSONObject(i);
                    String title = doc.getString("title");
                    JSONArray isbnArray = doc.optJSONArray("isbn");
                    String isbn = (isbnArray != null && isbnArray.length() > 0) ? isbnArray.getString(0) : "No ISBN available";

                    //  Generate New Book From The Web Data, Add To Return List
                    Book book = new Book(i, title, isbn, false, "");
                    bookList.add(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }


}
