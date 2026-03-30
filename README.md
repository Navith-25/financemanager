# 💸 Hybrid Personal Finance Manager

<div align="center">
  <img src="https://img.shields.io/badge/Status-Completed-success?style=for-the-badge" alt="Status">
  <img src="https://img.shields.io/badge/Backend-Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Database-Oracle_%7C_SQLite-blue?style=for-the-badge" alt="Databases">
</div>

<br>

An innovative, offline-first personal finance management application engineered to bridge the gap between reliable local storage and powerful cloud analytics. It seamlessly synchronizes data between a local **SQLite** database and a central **Oracle** database.

## 🚀 Key Features

* **Offline-First Architecture:** Log transactions, set budgets, and track savings goals completely offline using a lightweight local SQLite database. No internet? No problem.
* **Custom Synchronization Engine:** Features a robust backend `SyncService` that intelligently detects unsynced local records and securely pushes them to the central Oracle cloud database when a connection is available.
* **Advanced Financial Reporting:** Leverages the power of the central Oracle database to generate complex, data-driven reports including:
  * *Budget Adherence Reports*
  * *Category-wise Expense Breakdowns*
  * *Monthly Expense Trends*
  * *Forecasted Savings Analysis*
* **Dual-Database Integration:** Seamlessly configures and manages two distinct data sources (Oracle & SQLite) within a single unified Spring Boot application.

## 🛠️ Tech Stack

### Backend
* **Java 17** & **Spring Boot 3** (RESTful APIs, Services, and Configuration)
* **Maven** (Dependency Management)

### Databases
* **SQLite** (Local, embedded database for offline functionality)
* **Oracle Database** (Centralized cloud database for aggregation and advanced reporting)
* **Spring Data JPA / Hibernate** (ORM and Database interactions)

### Frontend
* **HTML5**, **CSS3**, and **Vanilla JavaScript** (Clean, responsive user interface communicating via REST APIs)

## 💡 How It Works

1. **Local Operations:** When the user interacts with the app, data (like adding an expense) is instantly saved to the local `sqlite` database.
2. **Sync Process:** The application checks for a connection to the Oracle database. If connected, the `SyncService` securely uploads all pending transactions, budgets, and savings goals.
3. **Analytics:** For complex reports, the application queries the robust Oracle database to deliver accurate insights and forecasts.

---
*Architected and Developed by [Navith Fernando](https://github.com/Navith-25)*
