package com.example.mpesaapi.controller;

import java.io.IOException;

import com.example.mpesaapi.dto.BalanceInquiryDto;
import com.example.mpesaapi.service.BalanceInquiry;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mpesa")
public class BalanceInquiryController {

    private final BalanceInquiry balanceInquiryService;

    public BalanceInquiryController(BalanceInquiry balanceInquiryService) {
        this.balanceInquiryService = balanceInquiryService;
    }

    @PostMapping("/balance-inquiry")
    public ResponseEntity<String> inquireBalance(@RequestBody BalanceInquiryDto dto) {
        try {
            String response = balanceInquiryService.requestBalanceInquiry(dto);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to perform balance inquiry: " + e.getMessage());
        }
    }
}

