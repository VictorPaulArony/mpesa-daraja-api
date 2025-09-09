package com.example.mpesaapi.controller;

import com.example.mpesaapi.dto.C2BSimulationDto;
import com.example.mpesaapi.service.C2BSimulation;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mpesa")
@RequiredArgsConstructor
public class C2BSimulationController {

    private final C2BSimulation c2bSimulation;

    @PostMapping("/c2b/simulate")
    public ResponseEntity<String> simulateC2B(@RequestBody C2BSimulationDto dto) {
        try {
            String response = c2bSimulation.sendC2BSimulation(dto);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to simulate C2B transaction: " + e.getMessage());
        }
    }
}

