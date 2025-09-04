package com.example.mpesaapi.controller;

import com.example.mpesaapi.service.MpesaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/mpesa")
public class MpesaController {

    private final MpesaService mpesaService;

    public MpesaController(MpesaService mpesaService) {
        this.mpesaService = mpesaService;
    }

    @PostMapping("/stk-push")
    public ResponseEntity<?> initiateSTKPush(@RequestBody STKPushRequest request) {
        try {
            String response = mpesaService.initiateSTKPush(
                    mpesaService.getBusinessShortcode(),
                    mpesaService.getPasskey(),
                    request.getTransactionType(),
                    request.getAmount(),
                    request.getPhoneNumber(),
                    request.getAccountReference(),
                    request.getTransactionDesc(),
                    mpesaService.getCallbackUrl(),
                    mpesaService.getTimeoutUrl());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error initiating STK Push: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<?> handleCallback(@RequestBody String callbackData) {
        try {
            // Process the callback from Safaricom
            System.out.println("Received M-Pesa callback: " + callbackData);

            // Parse and process the callback data
            // You should implement proper validation and processing logic here

            // Return success response to M-Pesa
            JSONObject response = new JSONObject();
            response.put("ResultCode", "0");
            response.put("ResultDesc", "Callback processed successfully");

            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            System.err.println("Error processing callback: " + e.getMessage());

            // Return error response to M-Pesa
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("ResultCode", "1");
            errorResponse.put("ResultDesc", "Error processing callback");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse.toString());
        }
    }

    @PostMapping("/timeout")
    public ResponseEntity<?> handleTimeout(@RequestBody String timeoutData) {
        try {
            // Handle timeout callback
            System.out.println("Timeout callback received: " + timeoutData);

            JSONObject response = new JSONObject();
            response.put("ResultCode", "0");
            response.put("ResultDesc", "Timeout callback processed");

            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            System.err.println("Error processing timeout: " + e.getMessage());

            JSONObject errorResponse = new JSONObject();
            errorResponse.put("ResultCode", "1");
            errorResponse.put("ResultDesc", "Error processing timeout");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse.toString());
        }
    }

    @GetMapping("/transaction-status/{checkoutRequestId}")
    public ResponseEntity<?> checkTransactionStatus(@PathVariable String checkoutRequestId) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String password = generatePassword(mpesaService.getBusinessShortcode(), mpesaService.getPasskey(), timestamp);

            String response = mpesaService.STKPushTransactionStatus(
                    mpesaService.getBusinessShortcode(),
                    password,
                    timestamp,
                    checkoutRequestId);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking transaction status: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/debug-config")
    public ResponseEntity<?> debugConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("APP_KEY", mpesaService.getAppKey());
        config.put("APP_SECRET", mpesaService.getAppSecret() != null ? "***SET***" : "NULL");
        config.put("BUSINESS_SHORT_CODE", mpesaService.getBusinessShortcode());
        config.put("PASSKEY", mpesaService.getPasskey() != null ? "***SET***" : "NULL");
        config.put("CALLBACK_URL", mpesaService.getCallbackUrl());
        config.put("TIMEOUT_URL", mpesaService.getTimeoutUrl());

        return ResponseEntity.ok(config);
    }

    private String generatePassword(String businessShortCode, String passkey, String timestamp) {
        String rawPassword = businessShortCode + passkey + timestamp;
        return Base64.getEncoder().encodeToString(rawPassword.getBytes());
    }

    // DTO classes
    public static class STKPushRequest {
        private String transactionType;
        private String amount;
        private String phoneNumber;
        private String accountReference;
        private String transactionDesc;

        // Getters and setters
        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAccountReference() {
            return accountReference;
        }

        public void setAccountReference(String accountReference) {
            this.accountReference = accountReference;
        }

        public String getTransactionDesc() {
            return transactionDesc;
        }

        public void setTransactionDesc(String transactionDesc) {
            this.transactionDesc = transactionDesc;
        }
    }
}