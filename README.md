# M-Pesa Daraja API Integration

A comprehensive Spring Boot application for integrating with Safaricom's M-Pesa Daraja API. This application provides RESTful endpoints for various M-Pesa services including STK Push, B2C, B2B, C2B, and transaction status queries.

## Features

- **STK Push** - Initiate Lipa Na M-Pesa Online payments
- **B2C Payments** - Business to Customer transactions
- **B2B Payments** - Business to Business transactions  
- **C2B Simulation** - Customer to Business transactions
- **Transaction Status** - Check payment status
- **Balance Inquiry** - Account balance checks
- **Reversal API** - Transaction reversals
- **URL Registration** - Register validation and confirmation URLs
- **Webhook Support** - Handle M-Pesa callbacks

## Quick Start

### Prerequisites

- Java 21 or higher
- Maven 3.8+
- M-Pesa Daraja API credentials
- Ngrok (for testing callbacks)


**Update your M-Pesa credentials** in `src/main/resources/application.properties`:
   ```properties
   mpesa.app-key=your_app_key_here
   mpesa.app-secret=your_app_secret_here
   mpesa.business-shortcode=174379
   mpesa.passkey=your_passkey_here
   mpesa.callback-url=https://your-ngrok-url.ngrok.io/api/mpesa/callback
   mpesa.timeout-url=https://your-ngrok-url.ngrok.io/api/mpesa/timeout
   mpesa.env=sandbox
   ```

4. **Build and run the application**
   ```bash
   mvn clean package
   java -jar target/mpesaapi-0.0.1-SNAPSHOT.jar
   ```

   Or run with Maven:
   ```bash
   mvn spring-boot:run
   ```

### Using Ngrok for Testing

1. **Install ngrok**
   ```bash
   # Ubuntu/Debian
   curl -s https://ngrok-agent.s3.amazonaws.com/ngrok.asc | sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null
   echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | sudo tee /etc/apt/sources.list.d/ngrok.list
   sudo apt update && sudo apt install ngrok

   # Authenticate
   ngrok config add-authtoken your_ngrok_auth_token
   ```

2. **Start ngrok tunnel**
   ```bash
   ngrok http 8080
   ```

3. **Update callback URLs** with your ngrok URL in `application.properties`



The application provides comprehensive error handling:

- **400 Bad Request** - Invalid input parameters
- **401 Unauthorized** - Authentication failures
- **500 Internal Server Error** - Server-side issues
- **Custom error messages** with detailed explanations

## Logging

The application uses SLF4J with Logback for logging. Check the logs for:

- API request/response details
- Authentication events
- Error debugging information
- Callback processing


## Deployment

### Docker Deployment

1. **Build Docker image:**
   ```bash
   docker build -t mpesa-api .
   ```

2. **Run container:**
   ```bash
   docker run -p 8080:8080 \
     -e MPESA_APP_KEY=your_key \
     -e MPESA_APP_SECRET=your_secret \
     mpesa-api
   ```

### Traditional Deployment

1. **Build JAR:**
   ```bash
   mvn clean package
   ```

2. **Deploy:**
   ```bash
   java -jar target/mpesaapi-0.0.1-SNAPSHOT.jar \
     --mpesa.app-key=your_key \
     --mpesa.app-secret=your_secret
   ```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


**Note**: This application is for integration with Safaricom's M-Pesa Daraja API. Ensure you have proper authorization and comply with all terms of service when using this integration.