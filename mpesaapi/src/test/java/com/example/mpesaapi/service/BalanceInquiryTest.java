package com.example.mpesaapi.service;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.BalanceInquiryDto;

import okhttp3.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BalanceInquiryTest {

    private BalanceInquiry balanceInquiry;
    private MpesaService mpesaService;
    private MpesaConfigProperties config;

    @BeforeEach
    void setup() {
        mpesaService = mock(MpesaService.class);
        config = new MpesaConfigProperties();
        config.setEnv("sandbox");

        balanceInquiry = new BalanceInquiry(mpesaService, config);
    }

    @Test
    void testRequestBalanceInquiryReturnsExpectedValue() throws IOException {
        BalanceInquiryDto dto = new BalanceInquiryDto();
        dto.setInitiator("Test");
        dto.setSecurityCredential("secure");
        dto.setCommandID("Balance");
        dto.setPartyA("600000");
        dto.setIdentifierType("4");
        dto.setRemarks("Test balance");
        dto.setQueueTimeOutURL("http://timeout");
        dto.setResultURL("http://result");

        when(mpesaService.authenticate()).thenReturn("mock-token");

        OkHttpClient mockClient = mock(OkHttpClient.class);
        ResponseBody mockBody = mock(ResponseBody.class);
        Call mockCall = mock(Call.class);
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(mockBody)
                .build();

        when(mockBody.string()).thenReturn("Balance Response");
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        String result = balanceInquiry.requestBalanceInquiry(dto);
        assertEquals("Balance Response", result);
    }
}
