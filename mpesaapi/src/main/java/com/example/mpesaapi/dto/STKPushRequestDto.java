package com.example.mpesaapi.dto;

import lombok.Data;

@Data
public class STKPushRequestDto {
    // DTO classes
    private String transactionType;
    private String amount;
    private String phoneNumber;
    private String accountReference;
    private String transactionDesc;

}
