package main.service;

import main.model.Book;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BookService {

    public List<Book> findBooksByAuthor(List<Book> books, String author) {
        return books.stream()
                .filter(book -> book != null && book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByPublisher(List<Book> books, String publisher) {
        return books.stream()
                .filter(book -> book != null && book.getPublisher().equalsIgnoreCase(publisher))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByMinPages(List<Book> books, int minPages) {
        return books.stream()
                .filter(book -> book != null && book.getPages() >= minPages)
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByGenreSorted(List<Book> books, String genre) {
        return books.stream()
                .filter(book -> book != null && book.getGenre().equalsIgnoreCase(genre))
                .sorted(Comparator.comparingInt(Book::getYear)
                        .thenComparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public Book findBookByTitle(List<Book> books, String title) {
        return books.stream()
                .filter(book -> book != null && book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    public Book findBookById(List<Book> books, int id) {
        return books.stream()
                .filter(book -> book != null && book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addBook(List<Book> books, Book newBook) {
        if (newBook != null) {
            books.add(newBook);
        }
    }

    public boolean removeBookById(List<Book> books, int id) {
        return books.removeIf(b -> b != null && b.getId() == id);
    }

    public Map<String, List<Book>> mapGenreToBooksSortedByAuthor(List<Book> books) {
        return books.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Book::getGenre,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Book::getAuthor))
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public Map<String, Integer> countBooksPerPublisher(List<Book> books) {
        return books.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Book::getPublisher,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));
    }
}
