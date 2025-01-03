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

    /**
     * Находит пользователя по его ID и возвращает объект DTO.
     *
     * @param id идентификатор пользователя.
     * @return объект UserDTO, содержащий информацию о пользователе, или null, если пользователь не найден.
     */
    public UserDTO findById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.map(this::toUserDTO).orElse(null);
    }

    /**
     * Возвращает список всех пользователей в виде списка DTO.
     *
     * @return список объектов UserDTO, представляющих всех пользователей.
     */
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Создает нового пользователя с заданными параметрами и возвращает его в виде DTO.
     *
     * @param name  имя пользователя.
     * @param email email пользователя.
     * @return объект UserDTO, содержащий информацию о созданном пользователе.
     */
    public UserDTO createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        User savedUser = userRepository.save(user);
        return toUserDTO(savedUser);
    }

    /**
     * Удаляет пользователя по его ID.
     *
     * @param id идентификатор пользователя, которого нужно удалить.
     * @return true, если пользователь был удален, false, если пользователь не найден.
     */
    public boolean deleteUser(Long id) {
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isPresent()) {
            userRepository.delete(foundUser.get());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Обновляет информацию о пользователе.
     *
     * @param id    идентификатор пользователя, информацию о котором нужно обновить.
     * @param name  новое имя пользователя.
     * @param email новый email пользователя.
     * @return true, если пользователь был обновлен, false, если пользователь не найден.
     */
    public boolean updateUser(Long id, String name, String email) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            user.setName(name);
            user.setEmail(email);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Добавляет книгу пользователю.
     *
     * @param userId идентификатор пользователя, которому будет добавлена книга.
     * @param bookId идентификатор книги, которую нужно добавить пользователю.
     * @return true, если книга была успешно добавлена пользователю.
     */
    public boolean addBookToUser(Long userId, Long bookId) {
        User user = getUserById(userId);
        Book book = getBookById(bookId);

        book.setUser(user);
        bookRepository.save(book);

        return true;
    }

    /**
     * Удаляет книгу у пользователя.
     *
     * @param userId идентификатор пользователя, у которого нужно удалить книгу.
     * @param bookId идентификатор книги, которую нужно удалить у пользователя.
     * @return true, если книга была успешно удалена у пользователя.
     * @throws IllegalStateException если книга не принадлежит пользователю.
     */
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

    /**
     * Преобразует объект User в объект UserDTO.
     *
     * @param user объект User, который нужно преобразовать.
     * @return объект UserDTO, содержащий информацию о пользователе.
     */
    private UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        List<BookDTO> bookDTOs = user.getBooks().stream()
                .map(book -> new BookDTO(book.getId(), book.getTittle(), book.getAuthor()))
                .collect(Collectors.toList());

        return new UserDTO(user.getId(), user.getName(), user.getEmail(), bookDTOs);
    }

    /**
     * Находит пользователя по его ID.
     *
     * @param userId идентификатор пользователя.
     * @return объект User, если пользователь найден.
     * @throws UserNotFoundException если пользователь с данным ID не найден.
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    /**
     * Находит книгу по ее ID.
     *
     * @param bookId идентификатор книги.
     * @return объект Book, если книга найдена.
     * @throws BookNotFoundException если книга с данным ID не найдена.
     */
    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));
    }

}