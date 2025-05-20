package main.model;

import java.io.Serializable;

public class Book implements Serializable, Comparable<Book> {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int pages;
    private int year;
    private String genre;

    public Book(int id, String title, String author, String publisher, int pages, int year, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pages = pages;
        this.year = year;
        this.genre = genre;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getPublisher() {
        return publisher;
    }
    public int getPages() {
        return pages;
    }
    public int getYear() {
        return year;
    }
    public String getGenre() {return genre; }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setGenre(String genre) {this.genre = genre;}

    @Override
    public String toString() {
        return "Book {\n" +
                "  id=" + id + ",\n" +
                "  title='" + title + "',\n" +
                "  author='" + author + "',\n" +
                "  publisher='" + publisher + "',\n" +
                "  pages=" + pages + ",\n" +
                "  year=" + year + ",\n" +
                "  genre='" + genre + "'\n" +
                '}';
    }


    @Override
    public int compareTo(Book other) {
        return Integer.compare(this.year, other.year);
    }
}


