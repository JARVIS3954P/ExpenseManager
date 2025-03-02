# 🏢 Enterprise Expense Management System

## 📌 Overview  
The **Enterprise Expense Management System** is designed to help organizations efficiently **track, manage, and analyze employee expenses**. It features **role-based authentication**, **expense categorization**, **multi-level approval workflows**, and **real-time analytics**.

---

## 🛠️ Tech Stack  
### **🔹 Backend (Spring Boot)**
- Spring Boot (REST API)
- Spring Security (JWT Authentication)
- Spring Data JPA (Hibernate ORM)
- Flyway (Database Migrations)
- Lombok (Boilerplate Reduction)
- OpenAPI/Swagger (API Documentation)

### **🔹 Database (PostgreSQL/MySQL)**
- Relational Database (PostgreSQL/MySQL)
- Entity Relationship Design
- Stored Procedures & Triggers

### **🔹 Frontend (React or Angular)**
- React.js (with Redux Toolkit) **OR** Angular (with RxJS)
- Material UI / Angular Material
- Axios (API Calls)
- Chart.js / D3.js (Analytics)

---

## 📊 Progress So Far: **Database Schema & Model Classes**
### **📝 Implemented Model Classes**
The database schema follows **relational database principles** and is structured as follows:

| Entity | Relationship |
|--------|-------------|
| **User** | One-to-Many with **Expense** |
| **Role** | Enum (`EMPLOYEE`, `MANAGER`, `ADMIN`) |
| **Expense** | Many-to-One with **User**, One-to-One with **FileAttachment** |
| **ExpenseCategory** | Enum (`TRAVEL`, `FOOD`, `OFFICE_SUPPLIES`, etc.) |
| **Approval Workflow** | One-to-One with **Expense**, Many-to-One with **User** (Approver) |
| **Audit Log** | Many-to-One with **User** |
| **Notification** | Many-to-One with **User** (Recipient) |
| **FileAttachment** | One-to-One with **Expense** (For invoices) |

---

### **📌 Database Schema Diagram**
```mermaid
erDiagram
    USER ||--o{ EXPENSE : owns
    USER ||--o{ APPROVAL_WORKFLOW : approves
    USER ||--o{ AUDIT_LOG : logs
    USER ||--o{ NOTIFICATION : receives
    EXPENSE ||--|| FILE_ATTACHMENT : has
    EXPENSE ||--o| APPROVAL_WORKFLOW : linked
