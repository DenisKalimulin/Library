package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity(name = "Users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 1, max = 64, message = "Имя должно содержать от 1 до 64 символов")
    private String name;

    @Email
    @NotNull(message = "Поле не должно быть пустым")
    @Column(name = "email", nullable = false)
    private String email;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Book> books;
}
