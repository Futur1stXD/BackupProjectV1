package com.example.backupproject.repository;

import com.example.backupproject.model.FileBackup;
import com.example.backupproject.model.ResponseFile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface FileBackupRepository extends JpaRepository<FileBackup, String> {
    FileBackup findByEmailAndFileName(String email, String fileName);
    List<FileBackup> findAllByEmail(String email);
}