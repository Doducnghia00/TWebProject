package com.example.SpringWeb.book;

import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class Book {


    int bookCode;



    String title;

    String author;

    String category;
    boolean approved;

    public Book() {
    }

    public Book(int bookCode, String title, String author, String category, boolean approved) {
        this.bookCode = bookCode;
        this.title = title;
        this.author = author;
        this.category = category;
        this.approved = approved;
    }

    public int getBookCode() {
        return bookCode;
    }

    public void setBookCode(int bookCode) {
        this.bookCode = bookCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }


    @Override
    public String toString() {
        return "Book{" +
                "bookCode=" + bookCode +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", approved=" + approved +
                '}';
    }
}
