package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "books")
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tittle", nullable = false)
    @Size(min = 1, max = 128, message = "Название книги должно быть от 1 до 128 символов")
    private String tittle;

    @Column(name = "author", nullable = false)
    @Size(min = 1, max = 128, message = "Имя автора должно быть от 1 до 128 символов  ")
    private String author;

    @JsonBackReference
    @ManyToOne(optional = true)
    private User user;
}