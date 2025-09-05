package com.example.mpesaapi.dto;

import lombok.Data;

@Data
public class B2CRequestDto {
    private String initiatorName;
    private String securityCredential;
    private String commandID;
    private String amount;
    private String partyA;
    private String partyB;
    private String remarks;
    private String queueTimeOutURL;
    private String resultURL;
    private String occassion;
}
