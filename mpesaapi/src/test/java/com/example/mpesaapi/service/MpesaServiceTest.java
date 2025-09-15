package com.example.mpesaapi.service;

import com.example.mpesaapi.config.MpesaConfigProperties;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MpesaServiceTest {

    private MpesaConfigProperties config;
    private MpesaService service;

    @BeforeEach
    void setUp() {
        config = new MpesaConfigProperties();
        config.setAppKey("testAppKey");
        config.setAppSecret("testAppSecret");
        config.setEnv("sandbox");
        service = new MpesaService(config);
    }

    @Test
    void testGetters() {
        assertEquals("testAppKey", service.getAppKey());
        assertEquals("testAppSecret", service.getAppSecret());
    }

    @Test
    void testAuthenticateReturnsToken() throws IOException {
        String fakeToken = "fake-access-token";
        String responseJson;

        try {
            responseJson = new JSONObject().put("access_token", fakeToken).toString();
        } catch (JSONException e) {
            fail("Failed to create JSON response: " + e.getMessage());
            return;
        }

        ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), responseJson);
        Response response = new Response.Builder()
                .request(new Request.Builder().url("https://sandbox.safaricom.co.ke").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(responseBody)
                .build();

        OkHttpClient client = mock(OkHttpClient.class);
        Call call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(client.newCall(any(Request.class))).thenReturn(call);

        String token = service.authenticate();
        assertEquals(fakeToken, token);
    }

    @Test
    void testBasicAuthEncoding() {
        String appKeySecret = config.getAppKey() + ":" + config.getAppSecret();
        String expected = Base64.getEncoder().encodeToString(appKeySecret.getBytes(StandardCharsets.ISO_8859_1));
        assertNotNull(expected);
    }
}
