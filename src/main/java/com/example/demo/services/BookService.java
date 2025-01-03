package com.example.demo.services;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.models.Book;
import com.example.demo.models.User;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Возвращает список всех книг.
     *
     * @return список всех книг.
     */
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * Находит книгу по ее ID.
     *
     * @param id идентификатор книги.
     * @return объект Book, если книга найдена, или null, если книга не найдена.
     */
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * Создает новую книгу с заданными параметрами и привязывает ее к пользователю, если userId не равен null.
     *
     * @param title  название книги.
     * @param author автор книги.
     * @param userId идентификатор пользователя, к которому будет привязана книга.
     * @return созданный объект Book.
     * @throws UserNotFoundException если пользователь с указанным ID не найден.
     */
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

    /**
     * Удаляет книгу по ее ID.
     *
     * @param id идентификатор книги, которую нужно удалить.
     * @return true, если книга была успешно удалена, и false, если книга не найдена.
     */
    public boolean deleteBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()) {
            bookRepository.delete(book.get());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Обновляет информацию о книге.
     *
     * @param id     идентификатор книги, которую нужно обновить.
     * @param title  новое название книги.
     * @param author новый автор книги.
     * @return true, если книга была успешно обновлена, и false, если книга не найдена.
     */
    public boolean updateBook(Long id, String title, String author) {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()) {
            book.get().setTittle(title);
            book.get().setAuthor(author);
            bookRepository.save(book.get());
            return true;
        } else {
            return false;
        }
    }

}
