package com.example.mpesaapi.dto;

import lombok.Data;

@Data
public class RegisterURLDto {
    String shortCode;
    String responseType;
    String confirmationURL;
    String validationURL;

}
