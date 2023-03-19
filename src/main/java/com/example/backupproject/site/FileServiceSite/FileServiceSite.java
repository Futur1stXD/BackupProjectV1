package com.example.backupproject.site.FileServiceSite;

import com.example.backupproject.model.FileBackup;

import com.example.backupproject.model.ResponseFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileServiceSite {
    FileBackup saveFile(MultipartFile file, String token) throws Exception;
    ResponseFile saveResponseFile(ResponseFile response);
    void deleteFile(String filename, String token);
    void updateFile(MultipartFile file, String loginedEmail) throws IOException;
}
