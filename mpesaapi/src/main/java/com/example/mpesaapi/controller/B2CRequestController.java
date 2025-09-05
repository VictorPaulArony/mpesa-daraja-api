package com.example.mpesaapi.controller;

import com.example.mpesaapi.dto.B2CRequestDto;
import com.example.mpesaapi.service.B2CRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mpesa")
@RequiredArgsConstructor
public class B2CRequestController {

    private final B2CRequest b2CRequest; // Service to handle B2C payment requests

    @PostMapping("/b2c")
    public ResponseEntity<String> b2cPayment(@RequestBody B2CRequestDto dto) {
        try {
            // Forward DTO data to B2C request service
            String response = b2CRequest.B2CRequest(
                    dto.getInitiatorName(),
                    dto.getSecurityCredential(),
                    dto.getCommandID(),
                    dto.getAmount(),
                    dto.getPartyA(),
                    dto.getPartyB(),
                    dto.getRemarks(),
                    dto.getQueueTimeOutURL(),
                    dto.getResultURL(),
                    dto.getOccassion());
            return ResponseEntity.ok(response); // Return success response

        } catch (Exception e) {
            // Handle and return error response
            return ResponseEntity
                    .status(500)
                    .body("B2C Request failed: " + e.getMessage());
        }
    }
}
