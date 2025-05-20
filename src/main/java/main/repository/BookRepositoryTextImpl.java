package main.repository;

import main.model.Book;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class BookRepositoryTextImpl implements BookRepository {
    @Override
    public void saveToFile(List<Book> books, File file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            for (Book book : books) {
                writer.write(String.format("%d|%s|%s|%s|%d|%d|%s%n",
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getPages(),
                        book.getYear(),
                        book.getGenre()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to text file", e);
        }
    }

    @Override
    public void saveToFile(List<Book> books, String fileName) {
        saveToFile(books, new File(fileName));
    }

    @Override
    public List<Book> loadFromFile(File file){
        ArrayList<Book> books = new ArrayList<>();
        try(BufferedReader in = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while((line = in.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7) {
                    books.add(new Book(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            parts[3],
                            Integer.parseInt(parts[4]),
                            Integer.parseInt(parts[5]),
                            parts[6]
                    ));
                }
            }
        }catch (IOException e){
            System.out.println("File not found");
        }
        return books;
    }


    @Override
    public List<Book> loadFromFile(String fileName) {
        return loadFromFile(new File(fileName));
    }
}