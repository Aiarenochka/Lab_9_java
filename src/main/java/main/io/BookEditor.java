package main.io;

import main.model.Book;

import java.util.Scanner;

public class BookEditor {
    private final Scanner scanner = new Scanner(System.in);

    public Book createBookFromInput(View view) {
        view.prompt("Enter ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        view.prompt("Enter title: ");
        String title = scanner.nextLine();

        view.prompt("Enter author: ");
        String author = scanner.nextLine();

        view.prompt("Enter publisher: ");
        String publisher = scanner.nextLine();

        view.prompt("Enter number of pages: ");
        int pages = scanner.nextInt();
        scanner.nextLine();

        view.prompt("Enter year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        view.prompt("Enter genre: ");
        String genre = scanner.nextLine();

        return new Book(id, title, author, publisher, pages, year, genre);
    }

    public int getBookIdToRemove(View view) {
        view.prompt("Enter book ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

}
