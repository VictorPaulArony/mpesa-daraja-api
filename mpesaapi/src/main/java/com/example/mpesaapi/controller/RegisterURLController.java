package com.example.mpesaapi.controller;

import com.example.mpesaapi.dto.RegisterURLDto;
import com.example.mpesaapi.service.RegisterURL;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/mpesa")
@RequiredArgsConstructor
public class RegisterURLController {

    private final RegisterURL registerURLService;

    @PostMapping("/c2b/register-url")
    public ResponseEntity<String> registerURL(@RequestBody RegisterURLDto dto) {
        try {
            String response = registerURLService.registerURL(dto);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to register URL: " + e.getMessage());
        }
    }
}

