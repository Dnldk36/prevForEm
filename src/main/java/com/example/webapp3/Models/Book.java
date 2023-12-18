package com.example.webapp3.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Document(indexName = "es_book_index")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookName;
    private String tag;
    @Column(columnDefinition = "TEXT default 'No description yet'")
    private String description;
    private int price;
    @Column(columnDefinition = "TEXT")
    private String Img;
    @Column(columnDefinition = "TEXT")
    private String fileName;
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private Date date = new Date();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    @Column(columnDefinition = "varchar(255) default 'No reviews yet'")
    private List<Reviews> reviews = new ArrayList<>();

    public Object originalBookFileName() {
        if (fileName != null) {
            int index = fileName.indexOf(".");
            if (index != -1) {
                String originalName = fileName.substring(index + 1);
                if (!originalName.isEmpty()) {
                    return fileName;
                }
            }
        }
        return "NoFileName";
    }

    public Book(String bookName, String tag, String description, int price) {
        this.bookName = bookName;
        this.tag = tag;
        this.description = description;
        this.price = price;
    }

}
