package com.example.demo.services;

import com.example.demo.DTO.BookDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.models.Book;
import com.example.demo.models.User;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public UserService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public UserDTO findById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.map(this::toUserDTO).orElse(null);
    }

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        User savedUser = userRepository.save(user);
        return toUserDTO(savedUser);
    }

    public boolean deleteUser(Long id) {
        Optional<User> foundUser = userRepository.findById(id);

        if(foundUser.isPresent()) {
            userRepository.delete(foundUser.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUser(Long id, String name, String email) {
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()) {
            User user = foundUser.get();
            user.setName(name);
            user.setEmail(email);
            User savedUser = userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean addBookToUser(Long userId, Long bookId) {
        User user = getUserById(userId);
        Book book = getBookById(bookId);

        book.setUser(user);
        bookRepository.save(book);

        return true;
    }

    public boolean removeBookFromUser(Long userId, Long bookId) {
        User user = getUserById(userId);
        Book book = getBookById(bookId);

        if (!book.getUser().equals(user)) {
            throw new IllegalStateException("Book does not belong to the user");
        }

        book.setUser(null);
        bookRepository.save(book);

        return true;
    }



    private UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        List<BookDTO> bookDTOs = user.getBooks().stream()
                .map(book -> new BookDTO(book.getId(), book.getTittle(), book.getAuthor()))
                .collect(Collectors.toList());

        return new UserDTO(user.getId(), user.getName(), user.getEmail(), bookDTOs);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));
    }

}