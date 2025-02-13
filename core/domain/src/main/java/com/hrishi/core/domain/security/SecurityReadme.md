# 🔐 Encryption in SpendLess Security Module

This document explains how **encryption works** in our app, why we use certain **security techniques
**.

---
📌 **In our app, we use** `AES-256` **to encrypt sensitive user data.**
---

## **2️⃣ What is AES?**

AES (**Advanced Encryption Standard**) is one of the strongest and most widely used** encryption
methods.

### **Why Do We Use AES?**

✅ **Highly Secure** – Used by banks, governments, and top companies.  
✅ **Fast & Efficient** – Works well even on mobile devices.  
✅ **Supports Different Key Sizes** – We use **AES-256**, the strongest version.

---

## **3️⃣ What is AES-256?**

AES can have different **key sizes**:

- 🔹 **AES-128** – Secure but not the strongest.
- 🔹 **AES-192** – More secure.
- 🔹 **AES-256** – **The strongest encryption we use** in this app.

A **256-bit key** means there are **2²⁵⁶ possible keys** – that's **so many combinations** that even
the world's fastest computers would take **billions of years** to crack it! 🚀

📌 **AES-256 is used in our app to protect sensitive data like user PINs.**

---

## **4️⃣ What is CBC Mode?**

AES supports different **modes**, and we use **CBC (Cipher Block Chaining) mode**.

### **How CBC Works**

- Data is split into **blocks** (small chunks).
- Each block is **linked (chained)** to the previous one using an **IV (Initialization Vector)**.
- This ensures that even if the same data is encrypted multiple times, the output is **always
  different**.

📌 **Why CBC?**
✔ **Prevents patterns from repeating** in encrypted data.  
✔ **Adds an extra layer of security** using a **unique IV**.
---

## **5️⃣ What is an Initialization Vector (IV)?**

Think of an **IV (Initialization Vector)** as a **random starting point** to make encryption more
secure.

📌 **Without an IV:** Encrypting `"HELLO"` twice would give **the same encrypted result both times**
❌  
📌 **With an IV:** `"HELLO"` is encrypted differently **each time**, even if we use the same key ✅

### **How Do We Use IV in Our App?**

✔ **We generate a new IV for each encryption.**  
✔ **We store the IV alongside the encrypted data** (so it can be used for decryption).  
✔ **IV is never reused** to prevent security issues.

### **Decryption Process:**

1. Retrieve the **encrypted data** and **IV** from storage.
2. Use the **IV** to correctly decrypt the data using the same AES key.

---

## **6️⃣ What is Android Keystore?**

Android **Keystore** is a **secure storage area** inside the phone where **encryption keys** are
stored safely.

### **Why Do We Use Keystore?**

✅ **Protects the AES Key** – The encryption key **never leaves the device**.  
✅ **Prevents Key Theft** – Even if someone hacks the app, they **can’t steal the key**.  
✅ **Uses Hardware Security** – If available, stores the key in a **secure hardware chip**.

📌 **In our app, Keystore ensures that our encryption keys are safe and never exposed.**

---

## **7️⃣ Why Do We Use Base64?**

When we encrypt data, the output is **random bytes**, which **can’t be stored easily**.  
Base64 **converts those bytes into a safe text format** that we can store.

### **Example:**

- **Raw Encrypted Data:** `💣🤯🎲📦⚡` (Not safe for storage)
- **Base64 Encoded:** `"YWJjZGVmZ2hpamtsbW5vcA=="` (Safe for storage)

📌 **Base64 ensures that our encrypted data and IV can be stored in a database or shared securely.**
