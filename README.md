# Mobile AI Integration: On-Device vs On-Server MVP Comparison

This repository contains two MVP applications developed for the International Scientific and Practical Conference  
**"Student Research: Challenges and Development Trends"**.

---

# 🏫 Conference Information

- **Event:** International Scientific and Practical Conference  
  *"Student Research: Challenges and Development Trends"*

- **Organizers:**  
  Ministry of Education of the Republic of Kazakhstan,  
  Department of Education of Aktobe Region,  
  Aktobe Higher Humanitarian College,  
  National Centre for Professional Development **"Orleu"**

- **Section:**  
  Science, Technology, and Digital Innovations

- **Date:**  
  May 22, 2026

---

# 📱 Project Overview

The project explores the architectural choice between running AI models directly on a smartphone (On-Device) versus processing them on a remote server (On-Server)

For this research, image classification was chosen as the primary use case to demonstrate the differences in performance and accuracy

### 🤖 Applied Models
On-Device: Powered by the ML Kit Image Labeling API from Google for local, real-time inference

On-Server: Powered by the Hugging Face google/vit-base-patch16-224 model (Vision Transformer), accessed via REST API for high-precision processing

### ⚙️ Cross-Platform Methodology

It is important to note that while the development environments differ, the methodology, architectural logic, and implementation steps remain virtually identical across both platforms—Android (Jetpack Compose) and Flutter

This consistency ensures that the results of the study—such as the trade-offs between latency (94ms vs 2000ms+) and accuracy—are directly comparable, regardless of whether the app is native or cross-platform

This approach can be easily adapted for other AI tasks beyond image classification, such as text recognition or object detection

---

# 📊 Comparison of Approaches

Based on research and experimental data, the following comparison demonstrates when each approach should be used.

| Criterion     | On-Device (ML Kit)      | On-Server (REST API)     |
|---------------|-------------------------|--------------------------|
| Response Time | 200–500 ms (Fast)       | 2000–9000 ms (Slow)      |
| Accuracy      | Average (~61%)          | High (up to 98%)         |
| Autonomy      | Full Offline Mode       | Requires Internet        |
| Privacy       | High (Local processing) | Requires secure channels |

---

# 🔍 Key Takeaways

- **On-Device** inference is best suited for simple tasks such as text scanning or face recognition, where user privacy and instantaneous response are the highest priorities.

- **On-Server** inference is necessary for complex Large Language Models (LLMs) or heavy data analytics that require high precision and significant computational power.

---

# 🛠 Implementation Differences

## Native Android (Jetpack Compose)

Implementation on Android is generally more straightforward because tools like **ML Kit** and **CameraX** are specifically tailored for the platform.

Managing camera permissions and hardware access is more intuitive within the native Android ecosystem.

Native Android remains the most effective choice for deep, high-performance ML integration.

---

## Flutter (Cross-platform)

Implementation on Flutter involves a more sophisticated architecture to ensure high performance and UI responsiveness. To optimize the interaction between cross-platform code and native system resources, a Method Channel (Method Bridge) system was implemented

This architecture allows the application to:

1. Capture the input data (such as image frames) within the Flutter UI layer
2. Delegate the intensive computation task to the Native Layer (Kotlin/Swift) via the Method Channel bridge to avoid blocking the main thread
3. Process the AI model using the device's native OS capabilities and hardware acceleration (CPU/GPU) for maximum efficiency
4. Synchronize and return the result back to the Flutter layer for an immediate and smooth update of the user interface

---

# 📂 Repository Structure

```text
Android_MVP   - Native implementation using Kotlin and Jetpack Compose

flutter_mvp   - Cross-platform implementation using Dart and Method Channels
```
---

# 📄 License

This project is licensed under the MIT License.