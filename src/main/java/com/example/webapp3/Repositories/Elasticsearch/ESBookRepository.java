package com.example.webapp3.Repositories.Elasticsearch;

import com.example.webapp3.Models.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface
ESBookRepository extends ElasticsearchRepository<Book, Long> {
    List<Book> findByTag(String tag);

}
