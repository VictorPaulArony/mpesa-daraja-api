package com.example.mpesaapi.controller;

import com.example.mpesaapi.dto.B2BRequestDto;
import com.example.mpesaapi.service.B2BRequest;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mpesa")
public class B2BController {

    private final B2BRequest b2bRequest;

    public B2BController(B2BRequest b2bRequest) {
        this.b2bRequest = b2bRequest;
    }

    @PostMapping("/b2b")
    public ResponseEntity<String> sendB2BRequest(@RequestBody B2BRequestDto dto) {
        try {
            String response = b2bRequest.sendB2BRequest(dto);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to process B2B request: " + e.getMessage());
        }
    }
}

