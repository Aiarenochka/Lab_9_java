package main.service;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import main.io.View;
import main.model.Book;

import java.util.*;
import java.util.stream.Collectors;

public class BookService {
    private final View view = new View();

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

    public List<Book> searchingNameGenreAuthor(List<Book> books, String inputData){
        if (inputData == null || inputData.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String searchTerm = inputData.trim().toLowerCase();
        boolean isNumeric = searchTerm.matches("-?\\d+(\\.\\d+)?");

        return books.stream()
                .filter(book -> book != null && (
                        book.getTitle().toLowerCase().contains(searchTerm) ||
                                book.getAuthor().toLowerCase().contains(searchTerm) ||
                                book.getGenre().toLowerCase().contains(searchTerm) ||
                                book.getPublisher().toLowerCase().contains(searchTerm) ||
                                (isNumeric && (
                                        book.getId() == Integer.parseInt(searchTerm) ||
                                                book.getYear() == Integer.parseInt(searchTerm) ||
                                                book.getPages() == Integer.parseInt(searchTerm)
                                ))
                )).collect(Collectors.toList());
    }

    public Book findBookById(List<Book> books, int id) {
        return books.stream()
                .filter(book -> book != null && book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addBook(List<Book> books) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Додати книгу");
        dialog.setHeaderText("Введіть дані книги");

        ButtonType okButtonType = new ButtonType("Додати", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField publisherField = new TextField();
        TextField pagesField = new TextField();
        TextField yearField = new TextField();
        TextField genreField = new TextField();


        grid.add(new Label("ID:"), 0, 0); grid.add(idField, 1, 0);
        grid.add(new Label("Назва:"), 0, 1); grid.add(titleField, 1, 1);
        grid.add(new Label("Автор:"), 0, 2); grid.add(authorField, 1, 2);
        grid.add(new Label("Видавництво:"), 0, 3); grid.add(publisherField, 1, 3);
        grid.add(new Label("Сторінок:"), 0, 4); grid.add(pagesField, 1, 4);
        grid.add(new Label("Рік:"), 0, 5); grid.add(yearField, 1, 5);
        grid.add(new Label("Жанр:"), 0, 6); grid.add(genreField, 1, 6);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(idField::requestFocus);

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    int pages = Integer.parseInt(pagesField.getText());
                    int year = Integer.parseInt(yearField.getText());
                    return new Book(id, titleField.getText(), authorField.getText(),
                            publisherField.getText(), pages, year, genreField.getText());
                } catch (Exception e) {
                    view.showError("Некоректні дані. ID, сторінки, рік — мають бути числами.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(books::add);
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

    public String chosenFile(String title) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Введіть ім’я файлу та виберіть тип");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField fileName = new TextField();
        ComboBox<String> fileType = new ComboBox<>();
        fileType.getItems().addAll(".txt", ".json", ".bin");

        grid.add(new Label("Ім’я:"), 0, 0); grid.add(fileName, 1, 0);
        grid.add(new Label("Тип:"), 0, 1); grid.add(fileType, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == okButtonType) return fileName.getText() + fileType.getValue();
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
