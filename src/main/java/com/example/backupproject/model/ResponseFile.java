package com.example.backupproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "response_file")
public class ResponseFile {
    @Id
    private String id;
    @Column(name = "email")
    private String email;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "download_url")
    private String downloadURL;
    @Column(name = "file_type")
    private String fileType;
    @Column(name = "file_size")
    private long fileSize;
}