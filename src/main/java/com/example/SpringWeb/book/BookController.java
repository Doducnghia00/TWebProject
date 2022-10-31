package com.example.SpringWeb.book;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

@Controller
public class BookController {

    @GetMapping("/")
    public String Home(){
        return "redirect:allBook";
    }

    @GetMapping("/allBook")
    public String getAllBook(Model model) throws IOException {
        Connection con;
        String dbClass = "com.mysql.cj.jdbc.Driver";
        String urlString = "jdbc:mysql://localhost:3306/test";
        String rootString = "root";
        String passString = "123456";

        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(urlString, rootString, passString);

            String query="SELECT * FROM test.book;";
            try(PreparedStatement pr = con.prepareStatement(query)){
                ResultSet rs = pr.executeQuery();
                ArrayList<Book> listBook= new ArrayList<>();
                while (rs.next()){
                    int bookCode = rs.getInt("bookCode");

                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String category = rs.getString("category");
                    boolean approved = rs.getInt("approved") != 0;

                    listBook.add(new Book(bookCode, title,author,category,approved));
                }
               // System.out.println(listBook.get(1).toString());
                model.addAttribute("books",listBook);
                pr.close();
                return "Books";

            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }




        return "Error";
    }

    @GetMapping("/book/{id}")
    public String getBook(Model model, @PathVariable String id){

        Connection con;
        String dbClass = "com.mysql.cj.jdbc.Driver";
        String urlString = "jdbc:mysql://localhost:3306/test";
        String rootString = "root";
        String passString = "123456";

        if(!id.equals("add")){
            try {
                Class.forName(dbClass);
                con = DriverManager.getConnection(urlString, rootString, passString);

                String query="SELECT * FROM test.book WHERE bookCode = ?";
                try(PreparedStatement pr = con.prepareStatement(query)){
                    pr.setInt(1,Integer.valueOf(id));
                    ResultSet rs = pr.executeQuery();
                    ArrayList<Book> listBook= new ArrayList<>();
                    Book book = new Book();
                    while (rs.next()){
                        int bookCode = rs.getInt("bookCode");

                        String title = rs.getString("title");
                        String author = rs.getString("author");
                        String category = rs.getString("category");
                        boolean approved = rs.getInt("approved") != 0;

                        book.setBookCode(bookCode);
                        book.setApproved(approved);
                        book.setAuthor(author);
                        book.setCategory(category);
                        book.setTitle(title);
                    }
                    System.out.println(book.toString());
                    model.addAttribute("book",book);
                    pr.close();
                    return "book-detail";

                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }else{
//            Book book = new Book(-1,"","","",false);
            Book book = new Book();
            model.addAttribute("book",book);
            return "book-detail";
        }

        return "Error";
    }

    @GetMapping("/addBook")
    public String showForm(Model model){
        Book book = new Book();
        model.addAttribute("book", book);
        return "redirect:/book/add";
    }

    @PostMapping("/book/{bookCode}")
    public String addBook(Model model, Book book, @PathVariable String bookCode ){

        //Validation
        int error = 0;
        if(book.bookCode < 1){
            model.addAttribute("message","Book Code field should not be empty and book code must bigger than 0");
            error++;
        }
        if (book.title == null ||  book.title.equals("")) {
            model.addAttribute("message2", "Title field should not be empty");
            error++;
        }
        if( book.author == null || book.author.length() < 1){
            model.addAttribute("message3", "Author field should not be empty");
            error++;
        }
        if (book.category == null || book.category.equals("")) {
            model.addAttribute("message4", "Category field should not be empty");
            error++;
        }
        if(error > 0) return "book-detail";

        Connection con;
        String dbClass = "com.mysql.cj.jdbc.Driver";
        String urlString = "jdbc:mysql://localhost:3306/test";
        String rootString = "root";
        String passString = "123456";
        PreparedStatement pr = null;

        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(urlString, rootString, passString);

            // Search book by bookCode


            int key = 0; String query = "";
            String search="SELECT * FROM test.book WHERE bookCode = ?";
            pr = con.prepareStatement(search);
            pr.setInt(1, book.getBookCode());
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                key = rs.getInt("bookCode");
            }

            System.out.println("Code 1 : " + key);
            if(key > 0){ //Update book



                System.out.println("Book Found With Book Code = " + key);
                query="UPDATE test.book SET title=?,author=?,category=?,approved=? WHERE bookCode=" + key;
                pr = con.prepareStatement(query);
                //                    pr.setInt(5, book.getBookCode());
                pr.setString(1, book.getTitle());

                pr.setString(2, book.getAuthor());

                pr.setString(3, book.getCategory());

                pr.setInt(4, (book.isApproved() ) ? 1 : 0);
                pr.executeUpdate();
                pr.close();
                return "redirect:/allBook";


            }else{ // Add new book
                query="INSERT INTO test.book (bookCode, title, author, category, approved) VALUES (?,?,?,?,?);";
                pr = con.prepareStatement(query);
                pr.setInt(1, book.getBookCode());
                pr.setString(2, book.getTitle());

                pr.setString(3, book.getAuthor());

                pr.setString(4, book.getCategory());

                pr.setInt(5, (book.isApproved()) ? 1 : 0);
                pr.executeUpdate();
                pr.close();
                return "redirect:/allBook";

            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return "book-detail";
    }


    @GetMapping ("/delete/{bookCode}")
    public String deleteBook(Book book, @PathVariable String bookCode){
        Connection con;
        String dbClass = "com.mysql.cj.jdbc.Driver";
        String urlString = "jdbc:mysql://localhost:3306/test";
        String rootString = "root";
        String passString = "123456";
        PreparedStatement pr = null;

        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(urlString, rootString, passString);

            String query="DELETE FROM test.book WHERE bookCode = + ?";
            pr = con.prepareStatement(query);
            pr.setInt(1, book.getBookCode());
            int rs = pr.executeUpdate();
            return "redirect:/allBook";

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "Error";
    }


    @GetMapping("/test")
    public String test(Model model){

        Book book = new Book();
        model.addAttribute("book",book);
        model.addAttribute("message","Hello");
        return "book-detail";
    }
}
