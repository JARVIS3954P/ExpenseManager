package com.zidioDev.ExpenseManager.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " with ID " + id + " not found", HttpStatus.NOT_FOUND);
    }
}

