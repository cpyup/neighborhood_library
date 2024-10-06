package com.pluralsight;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NeighborhoodLibraryApplication {

    static Book[] inventory = new Book[20];
    static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) {
        initializeInventory();

        // Main Application Loop
        while (true) {
            switch (homeScreen()) {
                case 1 -> displayAvailableBooks();
                case 2 -> displayCheckedOut();
                case 3 -> {
                    userInput.close(); // Close the scanner when done
                    return;
                }
                default -> {
                }
            }
        }
    }

    // Fill List With Randomized Books
    private static void initializeInventory() {
        List<Book> bookList = fetchBooks();
        Set<String> uniqueBooks = new HashSet<>(); // For Unique Randomization
        Random random = new Random();

        int count = 0;

        while (count < inventory.length) {  // Using While Loop To Better Control Increment
            Book book = bookList.get(random.nextInt(bookList.size()));

            // Check If This Book Is Already Added Based On ISBN
            if (uniqueBooks.add(book.getISBN())) {
                inventory[count] = book;
                count++;
            }
        }
    }

    // Menu Display Methods
    private static int homeScreen() {
        int option;

        System.out.println("\n\t\t\tHome Page\n\nMessage Of The Day:\n\tHello, Welcome To Our Community Library!\n\nMenu Options:\n1 - Show Available Books\n2 - Show Checked Out Books\n3 - Exit");
        option = userInput.nextInt();
        userInput.nextLine();

        return option;
    }

    private static void displayAvailableBooks() {
        String selection;

        System.out.println("\nEnter Your Name To Proceed:\n");
        String name = userInput.nextLine().trim();

        for (Book b : inventory) {
            if (b.isAvailable()) {
                System.out.println(b);
            }
        }

        while (true) {  // Submenu Loop - Checkout
            System.out.println("\nOptions:\nC - Checkout Book\nX - Return To Home Page");
            selection = userInput.nextLine().trim();

            switch (selection.charAt(0)) {
                case 'c', 'C' -> {
                    checkBookOut(name);
                    return;
                }
                case 'x', 'X' -> {
                    return;
                }
                default -> {
                }
            }
        }
    }

    private static void displayCheckedOut() {
        char selection;

        for (Book b : inventory) {
            if (!b.isAvailable()) {
                System.out.println(b);
            }
        }

        while (true) {  // Submenu Loop - Check in
            System.out.println("\nOptions:\n\tC - Check Book In\n\tX - Return To Home Page");
            selection = userInput.next().charAt(0);
            userInput.nextLine();

            switch (selection) {
                case 'c', 'C' -> {
                    checkBookIn();
                    return;
                }
                case 'x', 'X' -> {
                    return;
                }
                default -> System.out.println("\nError: Invalid Selection");
            }
        }
    }

    // Sub-menu Action Methods
    private static void checkBookIn() {
        String selection;

        System.out.println("\nEnter The ID Of The Book Being Returned:\n");
        selection = userInput.nextLine().trim();

        for (Book b : inventory) {
            // Book Exists And Is Marked As Checked Out, Proceed With Check-in
            if (!b.isAvailable() && Integer.toString(b.getId()).equals(selection)) {
                b.setCheckedIn();
                System.out.println("\n" + b + "\nSuccessfully Returned!\nPress Enter To Return To Home Page");
                userInput.nextLine();
                return;
            }
        }

        // Handle invalid ID
        System.out.println("\nError: ID Not Found\n\tC - Enter New ID\n\tX - Return Home\n\tEnter - Display Checked Out Books");
        selection = userInput.nextLine().trim();

        if (selection.equalsIgnoreCase("x")) return;
        if (selection.equalsIgnoreCase("c")) {
            checkBookIn();
        } else {
            displayCheckedOut();
        }
    }

    private static void checkBookOut(String name) {
        String selection;

        System.out.println("\nEnter The ID Of The Desired Book:\n");
        selection = userInput.nextLine().trim();

        for (Book b : inventory) {
            // Book Exists And Is Marked As Available, Proceed With Checkout
            if (b.isAvailable() && Integer.toString(b.getId()).equals(selection)) {
                b.setCheckedOut(name);
                System.out.println("\n" + b + "\nPress Enter To Return To Home\n");
                userInput.nextLine();
                return;
            }
        }

        // Handle invalid ID
        System.out.println("\nError: ID Not Found\n\tC - Enter New ID\n\tX - Return Home\n\tEnter - Display Available Books");
        selection = userInput.nextLine().trim();

        if (selection.equalsIgnoreCase("x")) return;
        if (selection.equalsIgnoreCase("c")) {
            checkBookOut(name);
        } else {
            displayAvailableBooks();
        }
    }

    // Randomized Inventory Fetching
    private static List<Book> fetchBooks() {
        String apiUrl = "https://openlibrary.org/search.json?q=programming+java&limit=50";
        List<Book> bookList = new ArrayList<>();
        Random random = new Random();

        try {  // Send GET, Read Response
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

            // Convert Response To JSON Object For Easier Parsing
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.getInt("numFound") > 0) {
                JSONArray docs = jsonResponse.getJSONArray("docs");
                for (int i = 0; i < docs.length(); i++) {
                    JSONObject doc = docs.getJSONObject(i);
                    String title = doc.getString("title");

                    // Extract ISBNs
                    JSONArray isbnArray = doc.optJSONArray("isbn");
                    String isbn = (isbnArray != null && isbnArray.length() > 0) ? isbnArray.getString(0) : "No ISBN available";

                    // Extract Author Names
                    JSONArray authorsArray = doc.optJSONArray("author_name");
                    String author = (authorsArray != null && authorsArray.length() > 0) ? authorsArray.getString(0) : "Unknown";

                    // Determine If The Book Is Checked Out (25% chance)
                    boolean isCheckedOut = random.nextInt(100) < 25;
                    String checkedOutTo = isCheckedOut ? author : "";

                    // Generate New Book From The Web Data, Add To Return List
                    Book book = new Book(i, title, isbn, isCheckedOut, checkedOutTo);
                    bookList.add(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }
}
