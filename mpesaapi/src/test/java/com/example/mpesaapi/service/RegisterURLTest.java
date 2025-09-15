package com.example.mpesaapi.service;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.RegisterURLDto;

import okhttp3.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterURLTest {

    private RegisterURL service;
    private MpesaService mpesaService;
    private MpesaConfigProperties config;

    @BeforeEach
    void setUp() {
        mpesaService = mock(MpesaService.class);
        config = new MpesaConfigProperties();
        config.setEnv("sandbox");

        service = new RegisterURL(mpesaService, config);
    }

    @Test
    void testRegisterURLSuccess() throws IOException {
        RegisterURLDto dto = new RegisterURLDto();
        dto.setShortCode("600000");
        dto.setResponseType("Completed");
        dto.setConfirmationURL("https://example.com/confirm");
        dto.setValidationURL("https://example.com/validate");

        when(mpesaService.authenticate()).thenReturn("mock-token");

        ResponseBody mockBody = mock(ResponseBody.class);
        when(mockBody.string()).thenReturn("URL Registered");

        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://sandbox.safaricom.co.ke").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(mockBody)
                .build();

        OkHttpClient client = mock(OkHttpClient.class);
        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(client.newCall(any())).thenReturn(mockCall);

        String result = service.registerURL(dto);
        assertEquals("URL Registered", result);
        verify(mpesaService).authenticate();
    }
}

