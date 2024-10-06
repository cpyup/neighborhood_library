package com.pluralsight;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NeighborhoodLibraryApplication {

    // Inventory to hold available books
    static Book[] inventory = new Book[20];
    static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) {
        initializeInventory(); // Initialize the book inventory

        // Main Application Loop
        while (true) {
            switch (homeScreen()) {
                case "1" -> displayAvailableBooks(); // Display available books
                case "2" -> displayCheckedOut(); // Display checked-out books
                case "3" -> {
                    userInput.close(); // Close the scanner when exiting
                    return; // Exit the application
                }
                default -> {
                    // Handle invalid input
                }
            }
        }
    }

    // Fill inventory with randomized books from an external source
    private static void initializeInventory() {
        List<Book> bookList = fetchBooks(); // Fetch books from the API
        Set<String> uniqueBooks = new HashSet<>(); // To ensure unique ISBNs
        Random random = new Random();

        int count = 0;
        while (count < inventory.length) { // Control loop for filling inventory
            Book book = bookList.get(random.nextInt(bookList.size()));

            // Check if this book has already been added based on ISBN
            if (uniqueBooks.add(book.getISBN())) {
                inventory[count] = book; // Add unique book to inventory
                count++;
            }
        }
    }

    // Display the home screen and return the user's menu choice
    private static String homeScreen() {
        System.out.println("\n\t\t\tHome Page\n\nMessage Of The Day:\n\tHello, Welcome To Our Community Library!\n\nMenu Options:\n1 - Show Available Books\n2 - Show Checked Out Books\n3 - Exit");
        return userInput.nextLine().trim(); // Return user's choice
    }

    // Display available books and handle user checkout actions
    private static void displayAvailableBooks() {
        System.out.println("\nEnter Your Name To Proceed:\n");
        String name = userInput.nextLine().trim(); // Get user's name

        // Display available books
        for (Book b : inventory) {
            if (b.isAvailable()) {
                System.out.println(b);
            }
        }

        handleBookAction(name, true); // Checkout action
    }

    // Display checked-out books and handle user check-in actions
    private static void displayCheckedOut() {
        for (Book b : inventory) {
            if (!b.isAvailable()) {
                System.out.println(b); // Show checked-out books
            }
        }

        handleBookAction(null, false); // Check-in action
    }

    // Handle book check-in and check-out actions
    private static void handleBookAction(String name, boolean isCheckout) {
        while (true) {
            System.out.println(isCheckout ? "\nOptions:\nC - Checkout Book\nX - Return To Home Page" : "\nOptions:\n\tC - Check Book In\n\tX - Return To Home Page");
            String selection = userInput.nextLine().trim();

            switch (selection.toLowerCase()) {
                case "c" -> {
                    processBook(name, isCheckout); // Handle the book process
                    return; // Return to the main menu
                }
                case "x" -> {
                    return; // Return to the main menu
                }
                default -> System.out.println("\nError: Invalid Selection"); // Handle invalid selection
            }
        }
    }

    // Handle the process of checking a book in or out
    private static void processBook(String name, boolean isCheckout) {
        String action = isCheckout ? "Checkout" : "Return";
        System.out.println("\nEnter The ID Of The Desired Book:\n");
        String selection = userInput.nextLine().trim(); // Get book ID

        for (Book b : inventory) {
            boolean isAvailable = b.isAvailable();
            boolean isMatch = Integer.toString(b.getId()).equals(selection);
            if ((isCheckout && isAvailable && isMatch) || (!isCheckout && !isAvailable && isMatch)) {
                if (isCheckout) {
                    b.checkOut(name); // Mark the book as checked out
                } else {
                    b.checkIn(); // Mark the book as checked in
                }
                System.out.println("\n" + b + "\nSuccessful " + action + "!\nPress Enter To Return To Home");
                userInput.nextLine(); // Wait for user input
                return; // Return to the previous menu
            }
        }

        // Handle invalid ID input
        System.out.println("\nError: ID Not Found\n\tC - Enter New ID\n\tX - Return Home\n\tEnter - Display " + (isCheckout ? "Available" : "Checked Out") + " Books");
        selection = userInput.nextLine().trim();

        if (selection.equalsIgnoreCase("x")) return; // Exit to home
        if (selection.equalsIgnoreCase("c")) {
            processBook(name, isCheckout); // Re-enter process
        } else {
            if (isCheckout) {
                displayAvailableBooks(); // Redisplay available books
            } else {
                displayCheckedOut(); // Redisplay checked-out books
            }
        }
    }

    // Fetch books from an external API and create a list of Book objects
    private static List<Book> fetchBooks() {
        String apiUrl = "https://openlibrary.org/search.json?q=programming+java&limit=50";
        List<Book> bookList = new ArrayList<>();
        Random random = new Random();

        try {  // Send GET request and read the response
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine); // Build the response string
            }
            in.close();

            // Convert response to JSON object for easier parsing
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

                    // Determine if the book is checked out (25% chance)
                    boolean isCheckedOut = random.nextInt(100) < 25;
                    String checkedOutTo = isCheckedOut ? author : "";

                    // Create a new Book object from the web data and add it to the list
                    Book book = new Book(i, title, isbn, isCheckedOut, checkedOutTo);
                    bookList.add(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for any exceptions
        }

        return bookList; // Return the list of fetched books
    }
}
