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

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mpesaapi.dto.STKPushRequestDto;
import com.example.mpesaapi.service.SendSTKPush;

@RestController
@RequestMapping("/api/mpesa")
public class MpesaController {

    private final MpesaService mpesaService;
    private final SendSTKPush sendSTKPush;
    private static final String RESULTCODE = "ResultCode";
    private static final String RESULTDESC = "ResultDesc";

    public MpesaController(MpesaService mpesaService, SendSTKPush sendSTKPush) {
        this.mpesaService = mpesaService;
        this.sendSTKPush = sendSTKPush;
    }

    @PostMapping("/stk-push")
    public ResponseEntity<String> initiateSTKPush(@RequestBody STKPushRequestDto request) {
        try {
            String response = sendSTKPush.initiateSTKPush(
                    mpesaService.getBusinessShortcode(),
                    mpesaService.getPasskey(),
                    mpesaService.getCallbackUrl(),
                    mpesaService.getTimeoutUrl(),
                    request);

            return ResponseEntity.ok(response);

        } catch (JSONException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error initiating STK Push: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody String callbackData) {
        try {
            // Parse and process the callback data
            // You should implement proper validation and processing logic here

            // Return success response to M-Pesa
            JSONObject response = new JSONObject();
            response.put(RESULTCODE, "0");
            response.put(RESULTDESC, "Callback processed successfully");

            return ResponseEntity.ok(response.toString());

        } catch (JSONException e) {
            System.err.println("Error processing callback: " + e.getMessage());

            // Return error response to M-Pesa
            JSONObject errorResponse = new JSONObject();
            errorResponse.put(RESULTCODE, "1");
            errorResponse.put(RESULTDESC, "Error processing callback");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse.toString());
        }
    }

    @PostMapping("/timeout")
    public ResponseEntity<String> handleTimeout(@RequestBody String timeoutData) {
        try {
            // Handle timeout callback

            JSONObject response = new JSONObject();
            response.put(RESULTCODE, "0");
            response.put(RESULTDESC, "Timeout callback processed");

            return ResponseEntity.ok(response.toString());

        } catch (JSONException e) {

            JSONObject errorResponse = new JSONObject();
            errorResponse.put(RESULTCODE, "1");
            errorResponse.put(RESULTDESC, "Error processing timeout");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse.toString() + e.getMessage());
        }
    }

    @GetMapping("/transaction-status/{checkoutRequestId}")
    public ResponseEntity<String> checkTransactionStatus(@PathVariable String checkoutRequestId) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String password = generatePassword(mpesaService.getBusinessShortcode(), mpesaService.getPasskey(),
                    timestamp);

            String response = sendSTKPush.sTKPushTransactionStatus(
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
    public ResponseEntity<Map<String, String>> debugConfig() {
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

}