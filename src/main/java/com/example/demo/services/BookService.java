package com.example.demo.services;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.models.Book;
import com.example.demo.models.User;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book createBook(String title, String author, Long userId) {
        Book book = new Book();
        book.setTittle(title);
        book.setAuthor(author);

        if(userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            book.setUser(user);
        }
        return bookRepository.save(book);
    }



}
