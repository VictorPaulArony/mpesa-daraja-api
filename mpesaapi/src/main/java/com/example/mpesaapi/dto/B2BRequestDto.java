package com.example.mpesaapi.dto;

import lombok.Data;

@Data
public class B2BRequestDto {
    String initiatorName;
    String accountReference;
    String securityCredential;
    String commandID;
    String senderIdentifierType;
    String receiverIdentifierType;
    float amount;
    String partyA;
    String partyB;
    String remarks;
    String queueTimeOutURL;
    String resultURL;
    String occassion;

}
