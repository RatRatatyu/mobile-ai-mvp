# AI Lens: Hybrid Intelligence Demo 📱🤖

This Android application designed to demonstrate the architectural differences, performance trade-offs, and integration patterns between **On-Device** and **On-Server** Machine Learning models.

---
## 📱 Screenshots
| **On-device (ML Kit)** | **On-server (Hugging Face ViT)** |
|:---:|:---:|
| <img src="screenShots/screenShot_OnDevice.jpg" width="220" alt="ML Kit Inference Result"> | <img src="screenShots/screenShot_OnServerr.jpg" width="220" alt="Hugging Face Inference Result"> |

---

# 🌟 Key Features

## Dual-Mode Inference
Switch between local **ML Kit** processing and remote **Hugging Face API** inference with a single toggle.

## Performance Metrics
Real-time tracking of **Inference Time (ms)** to compare local CPU inference vs cloud latency.

## Reactive UI
Built with **Jetpack Compose** using a modern declarative UI architecture.

## Optimized Networking
Image compression and efficient JPEG byte streaming for fast remote inference requests.

---

# 🛠 Tech Stack

## Core Android

- **Jetpack Compose** — Modern declarative UI toolkit.
- **CameraX** — High-performance camera preview and image capture.
- **Kotlin Coroutines** — Asynchronous processing without blocking the UI thread.
- **Material 3** — Modern Android design components.

## Machine Learning
- **On-Device**:
  Google ML Kit (Image Labeling) for instant, offline results.
- **On-Server**:
  Hugging Face Inference API using the Vision Transformer (ViT) model for high-accuracy classification.

## Networking
- **Retrofit 2**: Type-safe HTTP client for API interactions.
- **OkHttp**: For interceptors and efficient image data streaming.
- **Gson**: JSON serialization/deserialization.

# 📊 Comparison: Local vs Server Inference

| Feature | ML Kit (On-Device) | Hugging Face (On-Server) |
|---|---|---|
| Latency | Extremely Low (~100–200ms) | Higher (~1–2s depending on network) |
| Accuracy | General Categories | High-Precision / State-of-the-art |
| Connectivity | Offline | Requires Internet |
| Battery Usage | CPU/GPU Intensive | Data Transfer Intensive |
| Processing Location | Device Hardware | Remote Cloud GPU |
| Speed Stability | Very Stable | Depends on Network |
| Privacy | Data stays on device | Image sent to server |
| Best Use Case | Fast real-time detection | More accurate classification |


# 🚀 Getting Started

## Prerequisites

- Android Studio Ladybug or newer
- Android device with camera support
- Hugging Face account
- Hugging Face API Token

---

# Installation

## Clone Repository
for github
```bash 
git clone https://github.com/RatRatatyu/mobile-ai-mvp/tree/main/Android_MVP.git
```
for gitLab
```bash
git clone https://gitlab.com/RatRatatyu/mobile-ai-mvp/-/tree/main/Android_MVP.git
```

## 🔑 Create Hugging Face API Token

To use the Inference API (including serverless models), you need to create a Hugging Face access token.

### Step 1 - Create Hugging Face Account

Go to:  
[https://huggingface.co](https://huggingface.co)

Sign up for a new account or sign in with your existing one.

### Step 2 - Access Tokens

1. Click on your profile picture (top right corner).
2. Go to **Settings** - **Access Tokens**.

### Step 3 - Create New Token

Click the **New Token** button.

Set the following permissions:

- **Read access to contents of all public gated repos you can access**
- **Read access to contents of selected repos**
- **Inference - Make calls to the serverless Inference API**

> These permissions are required to access hosted inference models via the Hugging Face API.

### Step 4 - Copy Your Token

Your token will look similar to this:
hf_xxxxxxxxxxxxxxxxxxxxxxxxx


### Step 5 — Add Token to Project

Add your token to the `local.properties` file in the root of your project:

```properties
HF_TOKEN="hf_your_token_here"
```

# 💡 Why This Project?

This project was built as a conference-style engineering demo to explore one important architectural question:

> “When should Machine Learning run locally, and when should it move to the cloud?”

By comparing:

- inference latency
- response consistency
- model accuracy
- network overhead
- on-device vs remote processing


