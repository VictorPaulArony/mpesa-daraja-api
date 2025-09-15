package com.example.mpesaapi.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MpesaConfigPropertiesTest {

    @Test
    void testGettersAndSetters() {
        MpesaConfigProperties config = new MpesaConfigProperties();

        config.setAppKey("testAppKey");
        config.setAppSecret("testAppSecret");
        config.setBusinessShortcode("600000");
        config.setPasskey("testPasskey");
        config.setCallbackUrl("https://example.com/callback");
        config.setTimeoutUrl("https://example.com/timeout");
        config.setEnv("sandbox");

        assertEquals("testAppKey", config.getAppKey());
        assertEquals("testAppSecret", config.getAppSecret());
        assertEquals("600000", config.getBusinessShortcode());
        assertEquals("testPasskey", config.getPasskey());
        assertEquals("https://example.com/callback", config.getCallbackUrl());
        assertEquals("https://example.com/timeout", config.getTimeoutUrl());
        assertEquals("sandbox", config.getEnv());
    }
}

