package com.example.mpesaapi.service;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.C2BSimulationDto;
import okhttp3.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class C2BSimulationTest {

    private C2BSimulation service;
    private MpesaService mpesaService;
    private MpesaConfigProperties config;

    @BeforeEach
    void setUp() {
        mpesaService = mock(MpesaService.class);
        config = new MpesaConfigProperties();
        config.setEnv("sandbox");
        service = new C2BSimulation(mpesaService, config);
    }

    @Test
    void testSendC2BSimulation() throws IOException {
        C2BSimulationDto dto = new C2BSimulationDto();
        dto.setShortCode("600000");
        dto.setCommandID("CustomerPayBillOnline");
        dto.setAmount("100");
        dto.setMSISDN("254712345678");
        dto.setBillRefNumber("INV123");

        when(mpesaService.authenticate()).thenReturn("mock-token");

        // Mock response body
        ResponseBody mockBody = mock(ResponseBody.class);
        when(mockBody.string()).thenReturn("Success");

        // Mock HTTP response
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://sandbox.safaricom.co.ke").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(mockBody)
                .build();

        // Mock HTTP client behavior
        OkHttpClient client = mock(OkHttpClient.class);
        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(client.newCall(any())).thenReturn(mockCall);

        // Actually call the method (Note: OkHttpClient is not injected, so we test token + JSON logic here only)
        String response = service.sendC2BSimulation(dto);

        assertEquals("Success", response);
        verify(mpesaService, times(1)).authenticate();
    }
}

