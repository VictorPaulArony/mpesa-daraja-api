package com.example.mpesaapi.dto;

import lombok.Data;

@Data
public class C2BSimulationDto {
    String shortCode;
    String commandID;
    String amount;
    String mSISDN;
    String billRefNumber;

}
