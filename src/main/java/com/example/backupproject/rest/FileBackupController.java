package com.example.backupproject.rest;

import com.example.backupproject.model.FileBackup;
import com.example.backupproject.model.ResponseFile;
import com.example.backupproject.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController("/auth")
public class FileBackupController {
    private FileService fileService;
    public FileBackupController(FileService fileService) {
        this.fileService = fileService;
    }
    @PostMapping("/upload")
    public ResponseFile uploadFile(@RequestParam("file")MultipartFile file) throws Exception {
        FileBackup fileBackup = null;
        fileBackup = fileService.saveFile(file);
        String downloadURL = "";
        downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
                .path(fileBackup.getId()).toUriString();
        String loginedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponseFile response = new ResponseFile(fileBackup.getId(), loginedEmail, fileBackup.getFileName(),
                downloadURL, file.getContentType(), file.getSize());
        fileService.saveResponseFile(response);
        return response;
    }
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        FileBackup fileBackup = null;
        fileBackup = fileService.getFile(fileId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileBackup.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        fileBackup.getFileName() + "\"")
                .body(new ByteArrayResource(fileBackup.getData()));
    }
    @GetMapping("/files")
    public List<ResponseFile> filesOfUser() {
        String logined = SecurityContextHolder.getContext().getAuthentication().getName();
        return fileService.getAllOfUser(logined);
    }
    @GetMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam("file")MultipartFile file) {
        fileService.deleteFile(file.getOriginalFilename());
        return ResponseEntity.ok().body(file.getOriginalFilename() + " was deleted");
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateFile(@RequestParam("file")MultipartFile file) throws IOException {
        fileService.updateFile(file);
        return ResponseEntity.ok().body(file.getOriginalFilename() + " was updated");
    }
}