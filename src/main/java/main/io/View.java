package main.io;

import main.model.Book;

import java.util.List;

public class View {
    public void showBooks(List<Book> books) {
        if (books == null) {
            System.out.println("No books found.");
        } else {
            for (Book book : books) {
                if (book != null) {
                    System.out.println(book);
                }
            }
        }
    }

    public void showBookDetails(Book book) {
        System.out.println("Book details:");
        System.out.println("  Genre: " + book.getGenre());
        System.out.println("  Year: " + book.getYear());
    }

    public void showBookNotFound() {
        System.out.println("Book not found in catalog");
    }

    public void prompt(String message) {
        System.out.print(message);
    }

    public void showAllBooks(List<Book> books) {
        System.out.println("All books in catalog:");
        showBooks(books);
    }

}