package main.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.factory.BookFactory;
import main.io.View;
import main.model.Book;
import main.repository.BookRepository;
import main.repository.BookRepositoryBinaryImpl;
import main.repository.BookRepositoryJSONImpl;
import main.repository.BookRepositoryTextImpl;
import main.service.BookService;

import java.util.*;
import java.util.stream.Collectors;

public class BookController {
    private final List<Book> books = new ArrayList<>();

    private final BookService service = new BookService();
    private final BookRepository txtRepo = new BookRepositoryTextImpl();
    private final BookRepository binRepo = new BookRepositoryBinaryImpl();
    private final BookRepository jsonRepo = new BookRepositoryJSONImpl();
    private final View view = new View();

    @FXML private TextField inputField;
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
        books.addAll(BookFactory.createBooks());

        showAll();
    }

    public void showAll() {
        bookTable.setItems(FXCollections.observableList(books));
    }

    public void close() {
        Platform.exit();
    }

    public void about() {
        view.showInfo("About", """
                Предмет: Технології об'єктно орієнтованого програмування
                Лабораторна робота №9
                Тема: Робота з JavaFX
                Варіант: 5 — Книги
                Виконала: Ковальчук А.В.
                Группа: 2141
                Викладач: Беркунський Є.Ю.
                """);
    }


    public void deleteBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            view.showError("Не вибрано книгу для видалення.");
            return;
        }
        books.removeIf(b -> b.getId() == selected.getId());
        showAll();
    }

    public void addBook(){
        service.addBook(books);
        showAll();
    }


    public void saveAs() {
        String name = service.chosenFile("Зберегти у файл");
        if (name == null){
            view.showError("Не введенно назву файла, або не обрано формат!");
            return;
        }
        if(name.matches(".*\\.txt$")){
            txtRepo.saveToFile(books, name);
        }else if(name.matches(".*\\.bin$")){
            binRepo.saveToFile(books, name);
        }else if(name.matches(".*\\.json$")){
            jsonRepo.saveToFile(books, name);
        }
        view.showInfo("Збереження", "Збережено у файл: " + name);
    }


    public void readFrom() {
        String name = service.chosenFile("Зчитати з файлу");
        if (name == null){
            view.showError("Не введенно назву файла, або не обрано формат!");
            return;
        }
        if(name.matches(".*\\.txt$")){
            books.clear();
            books.addAll(txtRepo.loadFromFile(name));
        }else if(name.matches(".*\\.bin$")){
            books.clear();
            books.addAll(binRepo.loadFromFile(name));
        }else if(name.matches(".*\\.json$")){
            books.clear();
            books.addAll(jsonRepo.loadFromFile(name));
        }
        view.showInfo("Зчитування", "Файл прочитано: " + name);
    }


    public void searching(){
        updateTable(service.searchingNameGenreAuthor(books, inputField.getText()));
    }


    public void runOperation() {
        String op = operationsChoiceBox.getValue();
        if (op == null) return;

        switch (op.charAt(0)) {
            case '1' -> updateTable(service.findBooksByAuthor(books, view.prompt("Введіть автора:")));
            case '2' -> updateTable(service.findBooksByPublisher(books, view.prompt("Введіть видавництво:")));
            case '3' -> {
                try {
                    int min = Integer.parseInt(view.prompt("Введіть мінімальну кількість сторінок:"));
                    updateTable(service.findBooksByMinPages(books, min));
                } catch (Exception e) {
                    view.showError("Помилка: введіть число.");
                }
            }
            case '4' -> updateTable(service.findBooksByGenreSorted(books, view.prompt("Введіть жанр:")));
            case '5' -> {
                try {
                    int id = Integer.parseInt(view.prompt("Введіть ID книги:"));
                    Book book = service.findBookById(books, id);
                    view.showInfo("Результат", (book != null) ? book.toString() : "Книгу не знайдено.");
                } catch (Exception e) {
                    view.showError("Помилка: введіть ID числом.");
                }
            }
            case '6' -> {
                Map<String, List<Book>> map = service.mapGenreToBooksSortedByAuthor(books);
                String result = map.entrySet().stream()
                        .map(e -> e.getKey() + ":\n" + e.getValue().stream().map(b -> "  " + b.getAuthor() + " - " + b.getTitle()).collect(Collectors.joining("\n")))
                        .collect(Collectors.joining("\n\n"));
                view.showInfo("Жанри → Книги", result);
            }
            case '7' -> {
                Map<String, Integer> map = service.countBooksPerPublisher(books);
                String result = map.entrySet().stream()
                        .map(e -> e.getKey() + ": " + e.getValue() + " книг")
                        .collect(Collectors.joining("\n"));
                view.showInfo("Видавництва → Кількість книг", result);
            }
        }
    }

    public void updateTable(List<Book> list) {
        bookTable.setItems(FXCollections.observableList(list));
    }
}
