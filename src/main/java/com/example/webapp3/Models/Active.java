package com.example.webapp3.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Active {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;

    public Active(StringBuilder text) {
        this.text = String.valueOf(text);
    }

    @Override
    public String toString() {
        return "Active{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
