package main.repository;

import main.model.Book;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryBinaryImpl implements BookRepository {
    @Override
    public void saveToFile(List<Book> books, File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
            oos.writeObject(books);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to binary file", e);
        }
    }

    @Override
    public void saveToFile(List<Book> books, String fileName) {
        saveToFile(books, new File(fileName));
    }

    @Override
    public List<Book> loadFromFile(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
            return (ArrayList<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error reading from binary file", e);
        }
    }

    @Override
    public List<Book> loadFromFile(String fileName) {
        return loadFromFile(new File(fileName));
    }
}