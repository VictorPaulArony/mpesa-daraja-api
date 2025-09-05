package com.example.mpesaapi.controller;

import java.io.IOException;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mpesaapi.dto.B2CRequestDto;
import com.example.mpesaapi.service.B2CRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mpesa")
@RequiredArgsConstructor
public class B2CRequestController {

    private final B2CRequest b2CRequest; // Service to handle B2C payment requests

    @PostMapping("/b2c")
    public ResponseEntity<String> b2cPayment(@RequestBody B2CRequestDto dto) {
        try {
            // Forward DTO data to B2C request service
            String response = b2CRequest.sendB2CRequest(dto);    
            return ResponseEntity.ok(response); // Return success response

        } catch (IOException | JSONException e) {
            // Handle and return error response
            return ResponseEntity
                    .status(500)
                    .body("B2C Request failed: " + e.getMessage());
        }
    }
}
