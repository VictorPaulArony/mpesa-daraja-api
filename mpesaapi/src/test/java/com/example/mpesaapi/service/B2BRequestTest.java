package com.example.mpesaapi.service;

import java.io.IOException;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.B2BRequestDto;

import okhttp3.ResponseBody;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class B2BRequestTest {

    private B2BRequest service;
    private MpesaService mpesaService;
    private MpesaConfigProperties config;

    @BeforeEach
    void setUp() {
        mpesaService = mock(MpesaService.class);
        config = new MpesaConfigProperties();
        config.setEnv("sandbox");
        service = new B2BRequest(mpesaService, config);
    }

    @Test
    void testSendB2BRequestReturnsExpectedResponse() throws IOException {
        B2BRequestDto dto = new B2BRequestDto();
        dto.setInitiatorName("Test");
        dto.setSecurityCredential("cred");
        dto.setCommandID("Command");
        dto.setSenderIdentifierType("4");
        dto.setReceiverIdentifierType("4");
        dto.setAmount(1000f);
        dto.setPartyA("600000");
        dto.setPartyB("600001");
        dto.setRemarks("Test");
        dto.setAccountReference("Ref");
        dto.setQueueTimeOutURL("http://timeout");
        dto.setResultURL("http://result");

        // Mock token
        when(mpesaService.authenticate()).thenReturn("mock-token");

        // Partial mocking
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

        when(mockBody.string()).thenReturn("Success");
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        // Run the method under test
        String result = service.sendB2BRequest(dto);

        assertEquals("Success", result);
    }
}

