# M-Pesa STK Push API Integration - Spring Boot

This is a Spring Boot application that integrates with Safaricom's **M-Pesa STK Push API**. It allows for initiating payments, handling callbacks and timeouts, and checking transaction status.

---

## Features

* Authenticate with Safaricom M-Pesa API (OAuth)
* STK Push payment initiation
* Callback handling for successful payments
* Timeout handling
* Transaction status query
* Configurable for both **sandbox** and **production**
* Secure management of credentials via `application.properties`
* Test with **CURL**
* Local tunnel via **ngrok** for development
* Minimal unit test setup support

---

## Prerequisites

* Java 21+
* Maven or Gradle
* ngrok (for local tunneling)
* Safaricom developer account with:

  * App Key & Secret
  * Shortcode
  * Passkey
  * Whitelisted callback/timeout URLs

---

## Project Structure

```
├── config/
│   └── MpesaConfigProperties.java
├── controller/
│   └── MpesaController.java
├── service/
│   └── MpesaService.java
└── application.properties
```

---

## Configuration

### `application.properties`

```properties
mpesa.appKey=your_app_key
mpesa.appSecret=your_app_secret
mpesa.businessShortcode=174379
mpesa.passkey=your_passkey
mpesa.callbackUrl=https://your-ngrok-url/api/mpesa/callback
mpesa.timeoutUrl=https://your-ngrok-url/api/mpesa/timeout
mpesa.env=sandbox  # or production
```

> For **callback** and **timeout** URLs, you need to expose your local server via **ngrok**.

---

## Running the Application

### 1. Clone & Build the App

```bash
git clone https://github.com/VictorPaulArony/mpesa-daraja-api.git
cd mpesa-daraja-api
./mvnw spring-boot:run
```

### 2. Expose via ngrok

```bash
ngrok http 8080
```

Use the provided `https://xxxx.ngrok.io` as the prefix in your `application.properties`.

## Related Documentation

- [NGROK Setup Guide](./NGROK_SETUP.md) – How to expose your local app for Safaricom callbacks.

---

## API Endpoints

### 1. **Initiate STK Push**

```http
POST /api/mpesa/stk-push
Content-Type: application/json
```

#### Request Body:

```json
{
  "transactionType": "CustomerPayBillOnline",
  "amount": "10",
  "phoneNumber": "2547XXXXXXXX",
  "accountReference": "Order123",
  "transactionDesc": "Test payment"
}
```

#### CURL Example:

```bash
curl -X POST http://localhost:8080/api/mpesa/stk-push \
  -H "Content-Type: application/json" \
  -d '{
    "transactionType": "CustomerPayBillOnline",
    "amount": "10",
    "phoneNumber": "254768744700",
    "accountReference": "TestAccount",
    "transactionDesc": "Payment for order"
  }'
```

---

### 2. **Callback Handler**

```http
POST /api/mpesa/callback
Content-Type: application/json
```

> Safaricom will call this endpoint with the transaction result.

---

### 3. **Timeout Handler**

```http
POST /api/mpesa/timeout
Content-Type: application/json
```

> Safaricom will call this if the transaction request times out.

---

### 4. **Transaction Status**

```http
GET /api/mpesa/transaction-status/{checkoutRequestId}
```

#### CURL Example:

```bash
curl http://localhost:8080/api/mpesa/transaction-status/ws_CO_123456789
```

---

### 5. **Debug Configuration**

```http
GET /api/mpesa/debug-config
```

Use this endpoint to verify the app is loading config values correctly.

---

## Unit Testing Overview

You can write tests using **JUnit 5** and **Mockito**. For example:

### Run Test

```bash
mvn clean test
```

---

## Manual Testing with Postman/CURL

Use [Postman](https://www.postman.com/) or CURL to test your endpoints locally. Ensure the ngrok URL is active and updated in your application config for real callbacks.

---

## License

This project is licensed under the Apache License.

---

## FAQ

**Q: My STK Push is not working!**

* Check if the phone number is in correct format (`2547XXXXXXXX`)
* Ensure you're using **sandbox** credentials and the Safaricom test phone number
* Confirm ngrok is running and URLs are correctly set

**Q: Can I test with a real number?**

* Only in **production** environment with approved credentials

**Q: Why use ngrok with M-Pesa?**

* Safaricom needs publicly accessible URLs for callback and timeout. ngrok allows you to expose your localhost server.


---

## cURL Command to Test B2C Endpoint

```bash
curl -X POST http://localhost:8080/api/mpesa/b2c \
  -H "Content-Type: application/json" \
  -d '{
    "initiatorName": "testapi",
    "securityCredential": "secureCredentialHere",
    "commandID": "BusinessPayment",
    "amount": "1",
    "partyA": "600998",
    "partyB": "254712345678",
    "remarks": "Test B2C payment",
    "queueTimeOutURL": "https://yourdomain.com/timeout",
    "resultURL": "https://yourdomain.com/result",
    "occassion": "Salary Payment"
}'
```
### example
```bash
curl -X POST https://bc5e517a5a99.ngrok-free.app/api/mpesa/b2c \
  -H "Content-Type: application/json" \
  -d '{
    "initiatorName": "testapi",
    "securityCredential": "secureCredentialHere",
    "commandID": "BusinessPayment",
    "amount": "1",
    "partyA": "600998",
    "partyB": "254768744700",
    "remarks": "Test B2C payment",
    "queueTimeOutURL": "https://bc5e517a5a99.ngrok-free.app",
    "resultURL": "https://bc5e517a5a99.ngrok-free.app",
    "occassion": "Salary Payment"
}'
```