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

The project explores the architectural choice between running AI models directly on a smartphone (**On-Device**) versus processing them on a remote server (**On-Server**).

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

Implementation on Flutter is more complex due to the nature of the Dart language, which is not designed for heavy, intensive mathematical calculations.

To achieve optimal performance on Flutter, a **Method Channel (Method Bridge)** system was implemented.

This architecture allows the application to:

1. Capture the request in the Flutter layer.
2. Pass the task to the Native Layer (Kotlin/Swift) via the bridge.
3. Process the model using the device's native OS capabilities.
4. Return the result back to the Flutter UI layer.

---

# 📂 Repository Structure

```text
Android_MVP   - Native implementation using Kotlin and Jetpack Compose

flutter_mvp   - Cross-platform implementation using Dart and Method Channels
```
---

# 📄 License

This project is licensed under the MIT License.