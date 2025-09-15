package com.example.mpesaapi.service;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.B2CRequestDto;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class B2CRequestTest {

    private MpesaService mpesaService;
    private MpesaConfigProperties config;
    private B2CRequest b2cRequest;

    @BeforeEach
    void setup() {
        mpesaService = mock(MpesaService.class);
        config = new MpesaConfigProperties();
        config.setEnv("sandbox");

        b2cRequest = new B2CRequest(mpesaService, config);
    }

    @Test
    void testSendB2CRequestReturnsExpectedValue() throws IOException {
        B2CRequestDto dto = new B2CRequestDto();
        dto.setInitiatorName("Init");
        dto.setSecurityCredential("cred");
        dto.setCommandID("Command");
        dto.setAmount("1000");
        dto.setPartyA("600000");
        dto.setPartyB("254712345678");
        dto.setRemarks("Test");
        dto.setQueueTimeOutURL("http://timeout");
        dto.setResultURL("http://result");
        dto.setOccassion("Occasion");

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

        when(mockBody.string()).thenReturn("B2C Response");
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        String result = b2cRequest.sendB2CRequest(dto);
        assertEquals("B2C Response", result);
    }
}

