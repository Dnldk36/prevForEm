
package com.example.webapp3.Service;


import com.example.webapp3.Models.Book;
import com.example.webapp3.Repositories.BookRepository;
import com.example.webapp3.Repositories.Elasticsearch.ESBookRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ESBookRepository esBookRepository;
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public List<Book> search(String searchTag) {
        List<Book> bookES = esBookRepository.findByTag(searchTag);

        SearchRequest searchRequest = new SearchRequest("es_book_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(
                QueryBuilders.multiMatchQuery(searchTag, "tag", "bookName")
                        .fuzziness("AUTO")
        );

        searchRequest.source(searchSourceBuilder);



        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            System.out.println("\nsearchRequest: " + searchResponse + "\n");

            List<Book> books = new ArrayList<>();
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                Book book = new Book();
                book.setId(((Integer) sourceAsMap.get("id")).longValue());
                book.setBookName((String) sourceAsMap.get("bookName"));
                book.setTag((String) sourceAsMap.get("tag"));
                book.setDescription((String) sourceAsMap.get("description"));
                book.setPrice((int) sourceAsMap.get("price"));

                if ((String)sourceAsMap.get("Img") == null) {
                    book.setImg((String) sourceAsMap.get("img"));
                }

                if ((String)sourceAsMap.get("img") == null) {
                    book.setImg((String) sourceAsMap.get("Img"));
                }

                book.setFileName((String) sourceAsMap.get("file_name"));



                String dateString = (String) sourceAsMap.get("date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                Date date = dateFormat.parse(dateString);

                book.setDate(date);
                books. add(book);
            }
            return books;

        } catch (IOException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Elasticsearch Search Results: " + bookES);
        return Collections.emptyList();
    }

    public Optional<Book> searchId(String str) {
        Long id = Long.parseLong(str);
        System.out.println("id: " + id + " str: " + str);
        Optional<Book> bookList = esBookRepository.findById(id);
        return bookList;
    }

    public Book searchFileName(String searchFile) {
        Book bookJPA = bookRepository.findByFileName(searchFile);
        return bookJPA;
    }

    public void saveData(Book book) {
        bookRepository.save(book);
        esBookRepository.save(book);
        System.out.println("bookId: " + book.getId());
    }

    public Iterable<Book> findAllES() {
        return esBookRepository.findAll();
    }
}

