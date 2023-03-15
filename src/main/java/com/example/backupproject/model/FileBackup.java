package com.example.backupproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@Table(name = "file_store")
public class FileBackup {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(name = "email")
    private String email;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_type")
    private String fileType;
    @Lob
    @Column(name = "data")
    private byte[] data;
    public FileBackup (String email, String fileName, String fileType, byte[] data) {
        this.email = email;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}