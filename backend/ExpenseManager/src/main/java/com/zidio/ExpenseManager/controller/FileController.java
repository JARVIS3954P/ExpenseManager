package com.zidio.ExpenseManager.controller;

import com.zidio.ExpenseManager.model.Expense;
import com.zidio.ExpenseManager.repository.ExpenseRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FileController {

    private final Path fileStorageLocation;
    private final ExpenseRepository expenseRepository;

    public FileController(@org.springframework.beans.factory.annotation.Value("${file.upload-dir}") String uploadDir, ExpenseRepository expenseRepository) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/expenses/{expenseId}/attachment")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long expenseId) {
        //  Find the expense by its ID
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);
        if (optionalExpense.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Expense expense = optionalExpense.get();

        // Check if the expense actually has an attachment
        String storedFilename = expense.getAttachmentUrl();
        if (storedFilename == null || storedFilename.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        try {
            //Load the file from the filesystem
            Path filePath = this.fileStorageLocation.resolve(storedFilename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Sanitize the expense title to create a safe filename
            String originalFilename = expense.getTitle().replaceAll("[^a-zA-Z0-9.-]", "_");
            String fileExtension = StringUtils.getFilenameExtension(storedFilename);
            String suggestedFilename = originalFilename + "." + fileExtension;

            // Build the response with the CORRECT Content-Disposition header
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    // The 'attachment' disposition suggests to the browser to download it.
                    // 'inline' suggests to display it if possible (like for PDFs/images). Let's use 'attachment' for consistency.
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + suggestedFilename + "\"")
                    .body(resource);

        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}