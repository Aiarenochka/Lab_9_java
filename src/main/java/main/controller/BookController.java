package main.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;
import main.model.Book;
import main.service.BookService;

import java.util.*;
import java.util.stream.Collectors;

public class BookController {
    private final List<Book> books = new ArrayList<>();
    private final BookService service = new BookService();

    @FXML private ChoiceBox<String> operationsChoiceBox;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idCol, pagesCol, yearCol;
    @FXML private TableColumn<Book, String> titleCol, authorCol, publisherCol, genreCol;

    @FXML public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        pagesCol.setCellValueFactory(new PropertyValueFactory<>("pages"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Початкові книги
        books.addAll(List.of(
                new Book(1, "The Catcher in the Rye", "J.D. Salinger", "Little, Brown and Company", 277, 1951, "Fiction"),
                new Book(2, "1984", "George Orwell", "Secker & Warburg", 328, 1949, "Dystopia"),
                new Book(3, "To Kill a Mockingbird", "Harper Lee", "J.B. Lippincott & Co.", 281, 1960, "Drama"),
                new Book(4, "Brave New World", "Aldous Huxley", "Chatto & Windus", 311, 1932, "Dystopia"),
                new Book(5, "Fahrenheit 451", "Ray Bradbury", "Ballantine Books", 249, 1953, "Sci-Fi"),
                new Book(6, "Animal Farm", "George Orwell", "Secker & Warburg", 112, 1945, "Dystopia"),
                new Book(7, "Go Set a Watchman", "Harper Lee", "HarperCollins", 278, 2015, "Drama"),
                new Book(8, "The Martian Chronicles", "Ray Bradbury", "Doubleday", 222, 1950, "Sci-Fi"),
                new Book(9, "Nine Stories", "J.D. Salinger", "Little, Brown and Company", 198, 1951, "Fiction"),
                new Book(10, "Do Androids Dream of Electric Sheep?", "Philip K. Dick", "Doubleday", 210, 1968, "Sci-Fi"),
                new Book(11, "The Handmaid's Tale", "Margaret Atwood", "McClelland and Stewart", 311, 1985, "Dystopia"),
                new Book(12, "The Left Hand of Darkness", "Ursula K. Le Guin", "Ace Books", 304, 1969, "Sci-Fi"),
                new Book(13, "Slaughterhouse-Five", "Kurt Vonnegut", "Delacorte", 275, 1969, "Fiction"),
                new Book(14, "Lord of the Flies", "William Golding", "Faber and Faber", 224, 1954, "Drama"),
                new Book(15, "Neuromancer", "William Gibson", "Ace Books", 271, 1984, "Sci-Fi")
        ));

        showAll();
    }

    public void showAll() {
        bookTable.setItems(FXCollections.observableList(books));
    }

    public void close() {
        Platform.exit();
    }

    public void about() {
        showInfo("About", """
                Лабораторна робота №9
                Тема: Робота з JavaFX
                Варіант: 5 — Книги
                """);
    }

    public void deleteBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Не вибрано книгу для видалення.");
            return;
        }
        books.removeIf(b -> b.getId() == selected.getId());
        showAll();
    }

    public void addBook() {
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
                    showError("Некоректні дані. ID, сторінки, рік — мають бути числами.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(book -> {
            books.add(book);
            showAll();
        });
    }

    public void saveAs() {
        String name = chosenFile("Зберегти у файл");
        if (name == null) return;

        // Ти можеш реалізувати тут роботу з JSON, TXT, BIN
        showInfo("Збереження", "Збережено у файл: " + name);
    }

    public void readFrom() {
        String name = chosenFile("Зчитати з файлу");
        if (name == null) return;

        // Ти можеш реалізувати тут логіку зчитування
        showInfo("Зчитування", "Файл прочитано: " + name);
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

    public void runOperation() {
        String op = operationsChoiceBox.getValue();
        if (op == null) return;

        switch (op.charAt(0)) {
            case '1' -> updateTable(service.findBooksByAuthor(books, prompt("Введіть автора:")));
            case '2' -> updateTable(service.findBooksByPublisher(books, prompt("Введіть видавництво:")));
            case '3' -> {
                try {
                    int min = Integer.parseInt(prompt("Введіть мінімальну кількість сторінок:"));
                    updateTable(service.findBooksByMinPages(books, min));
                } catch (Exception e) {
                    showError("Помилка: введіть число.");
                }
            }
            case '4' -> updateTable(service.findBooksByGenreSorted(books, prompt("Введіть жанр:")));
            case '5' -> {
                try {
                    int id = Integer.parseInt(prompt("Введіть ID книги:"));
                    Book book = service.findBookById(books, id);
                    showInfo("Результат", (book != null) ? book.toString() : "Книгу не знайдено.");
                } catch (Exception e) {
                    showError("Помилка: введіть ID числом.");
                }
            }
            case '6' -> {
                Map<String, List<Book>> map = service.mapGenreToBooksSortedByAuthor(books);
                String result = map.entrySet().stream()
                        .map(e -> e.getKey() + ":\n" + e.getValue().stream().map(b -> "  " + b.getAuthor() + " - " + b.getTitle()).collect(Collectors.joining("\n")))
                        .collect(Collectors.joining("\n\n"));
                showInfo("Жанри → Книги", result);
            }
            case '7' -> {
                Map<String, Integer> map = service.countBooksPerPublisher(books);
                String result = map.entrySet().stream()
                        .map(e -> e.getKey() + ": " + e.getValue() + " книг")
                        .collect(Collectors.joining("\n"));
                showInfo("Видавництва → Кількість книг", result);
            }
        }
    }

    public void updateTable(List<Book> list) {
        bookTable.setItems(FXCollections.observableList(list));
    }

    private String prompt(String msg) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ввід");
        dialog.setHeaderText(msg);
        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
