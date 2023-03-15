package com.example.backupproject.repository;

import com.example.backupproject.model.FileBackup;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FileBackupRepository extends JpaRepository<FileBackup, String> {
    FileBackup findByEmailAndFileName(String email, String fileName);
}