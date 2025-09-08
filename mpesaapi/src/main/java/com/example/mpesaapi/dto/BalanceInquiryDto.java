package com.example.mpesaapi.dto;

import lombok.Data;

@Data
public class BalanceInquiryDto {
    String initiator;
    String commandID;
    String securityCredential;
    String partyA;
    String identifierType;
    String remarks;
    String queueTimeOutURL;
    String resultURL;
}
