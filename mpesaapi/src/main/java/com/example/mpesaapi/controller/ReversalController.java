package com.example.mpesaapi.controller;

import com.example.mpesaapi.dto.ReversalDto;
import com.example.mpesaapi.service.Reversal;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mpesa")
@RequiredArgsConstructor
public class ReversalController {

    private final Reversal reversalService;

    @PostMapping("/reversal")
    public ResponseEntity<String> requestReversal(@RequestBody ReversalDto dto) {
        try {
            String response = reversalService.requestReversal(dto);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to process reversal: " + e.getMessage());
        }
    }
}
