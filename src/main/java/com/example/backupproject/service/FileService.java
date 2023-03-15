package com.example.backupproject.service;

import com.example.backupproject.model.FileBackup;
import com.example.backupproject.model.ResponseFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface FileService {
    FileBackup saveFile(MultipartFile file) throws Exception;
    FileBackup getFile(String fileId) throws Exception;
    List<ResponseFile> getAllOfUser(String email);
    ResponseFile saveResponseFile(ResponseFile response);
    void deleteFile(String fileName);
    void updateFile(MultipartFile file) throws IOException;
}