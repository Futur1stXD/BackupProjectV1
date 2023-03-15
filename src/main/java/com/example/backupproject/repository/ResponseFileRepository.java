package com.example.backupproject.repository;

import com.example.backupproject.model.ResponseFile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ResponseFileRepository extends JpaRepository<ResponseFile, String> {
    List<ResponseFile> findAllByEmail(String email);
    ResponseFile findByEmailAndFileName(String email, String fileName);
}