package com.example.mpesaapi.dto;

import lombok.Data;

@Data
public class ReversalDto {
    String initiator;
    String securityCredential;
    String commandID;
    String transactionID;
    String amount;
    String receiverParty;
    String recieverIdentifierType;
    String resultURL;
    String queueTimeOutURL;
    String remarks;
    String ocassion;

}
