package com.example.webapp3.Repositories;

import com.example.webapp3.Models.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}