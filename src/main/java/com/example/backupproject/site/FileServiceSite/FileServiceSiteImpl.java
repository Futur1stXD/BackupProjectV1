package com.example.backupproject.site.FileServiceSite;

import com.example.backupproject.model.FileBackup;
import com.example.backupproject.model.ResponseFile;
import com.example.backupproject.repository.FileBackupRepository;
import com.example.backupproject.repository.ResponseFileRepository;
import com.example.backupproject.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@Service
@Slf4j
public class FileServiceSiteImpl implements FileServiceSite {
    private FileBackupRepository fileBackupRepository;
    private JwtTokenProvider jwtTokenProvider;
    private ResponseFileRepository responseFileRepository;

    public FileServiceSiteImpl(FileBackupRepository fileBackupRepository, JwtTokenProvider jwtTokenProvider, ResponseFileRepository responseFileRepository) {
        this.fileBackupRepository = fileBackupRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.responseFileRepository = responseFileRepository;
    }

    @Override
    public FileBackup saveFile(MultipartFile file, String token) throws Exception {
        try {
            String fileName = file.getOriginalFilename();
            String loginedEmail = jwtTokenProvider.getUsername(token);
            FileBackup check = fileBackupRepository.findByEmailAndFileName(loginedEmail, fileName);
            if (check != null) {
                updateFile(file, loginedEmail);
                throw new FileAlreadyExistsException("File was updated");
            }
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence: " + fileName);
            }
            FileBackup fileBackup = new FileBackup(loginedEmail, fileName, file.getContentType(), file.getBytes(), file.getOriginalFilename());
            log.info("IN saveFile - file: {} successfully saved", fileName);
            return fileBackupRepository.save(fileBackup);
        } catch (Exception e) {
            throw new Exception("Could not save File: " + file.getOriginalFilename());
        }
    }

    @Override
    public ResponseFile saveResponseFile(ResponseFile response) {
        log.info("IN saveResponseFile - file: {} successfully saved", response.getFileName());
        return responseFileRepository.save(response);
    }

    @Override
    public void deleteFile(String filename, String token) {
        String email = jwtTokenProvider.getUsername(token);
        ResponseFile responseFile = responseFileRepository.findByEmailAndFileName(email, filename);
        FileBackup fileBackup = fileBackupRepository.findByEmailAndFileName(email, filename);
        responseFileRepository.delete(responseFile);
        fileBackupRepository.delete(fileBackup);
        log.info("IN deleteFile - file: {} was successfully deleted", filename);
    }
    @Override
    public void updateFile(MultipartFile file, String loginedEmail) throws IOException {
        String fileName = file.getOriginalFilename();
        ResponseFile responseFile = responseFileRepository.findByEmailAndFileName(loginedEmail, fileName);
        FileBackup fileBackup = fileBackupRepository.findByEmailAndFileName(loginedEmail, fileName);
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
