package com.example.webapp3.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String text;
    @Column(columnDefinition = "DATE")
    private LocalDate date = LocalDate.now();
    private String username;

    public Reviews(String text) {
        this.text = text;
    }
}
