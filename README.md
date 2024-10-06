# Neighborhood Library

Workbook 2 - Workshop Assignment

## Submission Description

### Application Startup

- API request is sent to [Open Library](https://openlibrary.org/) (a free resource for finding information about published books)

- The current library inventory is then set to a random collection of titles returned from Open Library

### Home Page

- Main menu, navigates options to display books based on availability
- Serves as the primary application loop, all functions end back here

### Display Available Books

- Prints display of the currently available inventory
- Provides options to check out a desired book or return to the main menu

#### Check-Out (Action)

- This sub-menu handles user input when submitting a request to check a book out
- Takes the users' name and updates the status in the current inventory
- Error handling directs user to attempt a new input, navigate to Home Page, or display list of available titles again

### Display Checked Out Books

- Prints display of currently unavailable inventory
- Provides options to check a book back in or return to the main menu

#### Check-In (Action)

- This sub-menu handles user input when submitting a request to return a book
- Updates the current inventory based on user input
- Error handling directs user to attempt a new input, navigate to Home Page, or display list of checked out titles again

