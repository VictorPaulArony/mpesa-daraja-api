# ngrok Setup Guide for M-Pesa API

This document helps you set up and use **ngrok** to expose your local Spring Boot application to the internet. This is essential for receiving **M-Pesa STK Push callbacks** and **timeout notifications** from Safaricom.

---

## What is ngrok?

[ngrok](https://ngrok.com/) is a tool that creates a secure tunnel to your local machine from a public URL. This is useful when testing webhooks or callbacks like those from the **M-Pesa API**.

---

## Why You Need ngrok

Safaricom requires publicly accessible URLs for:

* Callback URL
* Timeout URL

Localhost (`http://localhost:8080`) is **not** accessible from outside your machine. ngrok solves this by providing a publicly accessible URL.

---

## Prerequisites

* Java and your Spring Boot app installed 
* ngrok installed 
* Safaricom Developer App credentials (App Key, App Secret, Shortcode, Passkey) 

---

## Step-by-Step ngrok Setup

### Step 1: Install ngrok

#### For Windows:

Download and install ngrok from:
[https://ngrok.com/download](https://ngrok.com/download)

Extract the binary and move it to a folder in your system `PATH`.

#### For macOS:

```bash
brew install ngrok/ngrok/ngrok
```

#### For Linux:

```bash
sudo snap install ngrok
```

---

### Install ngrok via Apt with the following command:

```bash
curl -sSL https://ngrok-agent.s3.amazonaws.com/ngrok.asc \
  | sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null \
  && echo "deb https://ngrok-agent.s3.amazonaws.com bookworm main" \
  | sudo tee /etc/apt/sources.list.d/ngrok.list \
  && sudo apt update \
  && sudo apt install ngrok
```

### Step 2: Authenticate ngrok (Optional but Recommended)

Create an account at [ngrok.com](https://dashboard.ngrok.com/), then run:

```bash
ngrok config add-authtoken YOUR_AUTHTOKEN
```

This will allow you to use **reserved domains** and **longer sessions**.

---

### Step 3: Start Spring Boot App

In your project directory, run:

```bash
./mvnw spring-boot:run
```

This starts your application on port `8080` by default.

---

### Step 4: Start ngrok Tunnel

In another terminal window, run:

```bash
ngrok http 8080
```

You should see output like:

```
Forwarding    https://abcd-1234.ngrok.io -> http://localhost:8080
```

**Copy the HTTPS URL** (e.g., `https://abcd-1234.ngrok.io`)

---

### Step 5: Update Application Config

Update your `application.properties`:

```properties
mpesa.callbackUrl=https://abcd-1234.ngrok.io/api/mpesa/callback
mpesa.timeoutUrl=https://abcd-1234.ngrok.io/api/mpesa/timeout
```

Restart your Spring Boot application if necessary.

---

## Step 6: Test with Safaricom

1. Use the **STK Push** endpoint.
2. Safaricom will send a callback to your **ngrok-exposed URL**.
3. Check your Spring Boot logs for:

```
Received M-Pesa callback: {...}
```

---

## Tip: Reserve a ngrok Subdomain (Pro Feature)

To avoid constantly changing URLs:

```bash
ngrok http --domain=yourname.ngrok.io 8080
```

Then use `https://yourname.ngrok.io` in your application config.

---

## Logs & Debugging

ngrok provides a **web UI** for inspecting request/response data:

* Visit: [http://127.0.0.1:4040](http://127.0.0.1:4040)
* You'll see all incoming requests from Safaricom.

---

## Notes

* Always use the **HTTPS** version of the ngrok URL.
* Every time you restart `ngrok`, the domain will change (unless reserved).
* Update your `application.properties` if the URL changes.
* Ensure ngrok is always running while testing.

---

## Useful Commands

| Command                          | Description                          |
| -------------------------------- | ------------------------------------ |
| `ngrok http 8080`                | Start tunnel to localhost:8080       |
| `ngrok config add-authtoken ...` | Set authentication token             |
| `ngrok help`                     | Display all options                  |
| `ngrok tunnel config.yaml`       | Start using config (advanced)        |
| `http://localhost:4040`          | Inspect requests made to your tunnel |

---

## Troubleshooting

| Issue                          | Solution                            |
| ------------------------------ | ----------------------------------- |
| Callback not received          | Check if ngrok is running           |
| URL expired or changed         | Restart ngrok and update properties |
| 403 or 404 errors on callback  | Check URL and endpoint correctness  |
| App not responding to callback | Check logs or run app in debug mode |

---

## Additional Resources

* [ngrok Documentation](https://ngrok.com/docs)
* [Safaricom Developer Portal](https://developer.safaricom.co.ke/)

---

## That's it!

You're now ready to:

* Expose your local Spring Boot app with ngrok
* Receive real-time callbacks from Safaricom
* Test the entire M-Pesa payment lifecycle locally!

---
