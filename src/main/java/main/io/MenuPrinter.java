package main.io;

public class MenuPrinter {
    public void menuOptions() {
        System.out.print("""
        --- Book Menu ---
        1. List books by author
        2. List books by publisher
        3. List books with min pages
        4. List books by genre (sorted by year/title)
        5. Find book by title
        6. Add a new book
        7. Remove a book by ID
        8. Show all books
        9. Find book by ID
        10. Show books by genre sorted by author
        11. Show number of books per publisher
        12. Save to text file
        13. Save to binary file
        14. Save to JSON file
        15. Load from text file
        16. Load from binary file
        17. Load from JSON file
        0. Exit
        Choose an option: >>\s""");
    }
}