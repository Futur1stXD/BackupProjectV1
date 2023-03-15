package com.example.backupproject.service.impl;

import com.example.backupproject.model.FileBackup;
import com.example.backupproject.model.ResponseFile;
import com.example.backupproject.repository.FileBackupRepository;
import com.example.backupproject.repository.ResponseFileRepository;
import com.example.backupproject.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private FileBackupRepository fileBackupRepository;
    private ResponseFileRepository responseFileRepository;
    public FileServiceImpl(FileBackupRepository fileBackupRepository, ResponseFileRepository responseFileRepository) {
        this.fileBackupRepository = fileBackupRepository;
        this.responseFileRepository = responseFileRepository;
    }
    @Override
    public FileBackup saveFile(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence: " + fileName);
            }
            String loginedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            FileBackup fileBackup = new FileBackup(loginedEmail, fileName, file.getContentType(), file.getBytes());
            log.info("IN saveFile - file: {} successfully saved", fileName);
            return fileBackupRepository.save(fileBackup);
        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }

    @Override
    public FileBackup getFile(String fileId) throws Exception {
        return fileBackupRepository.findById(fileId)
                .orElseThrow(() -> new Exception("File not found with id: " + fileId));
    }

    @Override
    public List<ResponseFile> getAllOfUser(String email) {
        return responseFileRepository.findAllByEmail(email);
    }

    @Override
    public ResponseFile saveResponseFile(ResponseFile response) {
        log.info("IN saveResponseFile - file: {} successfully saved", response.getFileName());
        return responseFileRepository.save(response);
    }

    @Override
    public void deleteFile(String fileName) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponseFile responseFile = responseFileRepository.findByEmailAndFileName(email, fileName);
        FileBackup fileBackup = fileBackupRepository.findByEmailAndFileName(email, fileName);
        responseFileRepository.delete(responseFile);
        fileBackupRepository.delete(fileBackup);
        log.info("IN deleteFile - file: {} was successfully deleted", fileName);
    }

    @Override
    public void updateFile(MultipartFile file) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String fileName = file.getOriginalFilename();
        ResponseFile responseFile = responseFileRepository.findByEmailAndFileName(email, fileName);
        FileBackup fileBackup = fileBackupRepository.findByEmailAndFileName(email, fileName);
        responseFile.setFileSize(file.getSize());
        fileBackup.setFileType(file.getContentType());
        try {
            fileBackup.setData(file.getBytes());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        fileBackupRepository.save(fileBackup);
        responseFileRepository.save(responseFile);
    }
}