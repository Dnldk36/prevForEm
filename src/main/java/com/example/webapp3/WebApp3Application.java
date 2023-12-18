package com.example.webapp3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.webapp3.Repositories")
@EnableElasticsearchRepositories(basePackages = "com.example.webapp3.Repositories.Elasticsearch")
public class WebApp3Application {

    public static void main(String[] args) {
        SpringApplication.run(WebApp3Application.class, args);
    }

}
