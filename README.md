# 📊 Price Tracker

An automated price monitoring system with email alerts.

## 🚀 Features

- ✅ Product registration from Mercado Livre
- ✅ Automatic price history via API
- ✅ Price alerts with email notifications
- ✅ Automatic verification with Scheduler
- ✅ JWT Authentication
- ✅ Dark mode web interface

## 🧱 Tech Stack

- Java 21
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Thymeleaf + Bootstrap 5
- Mercado Livre API

## ⚙️ Getting Started

### Prerequisites
- Java 21
- PostgreSQL

### 1. Clone the repository
```bash
git clone https://github.com/Kaua-Araujo-Soares/price-tracker.git
cd price-tracker
```

### 2. Create the database
```sql
CREATE DATABASE price_tracker;
```

### 3. Set environment variables
```
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key_here
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

### 4. Run the application
```bash
./mvnw spring-boot:run
```

### 5. Access
```
http://localhost:8080/login
```

## 📁 Project Structure

```
src/main/java/com/kaua/price_tracker
├── controller      # REST and Web controllers
├── service         # Business logic
├── repository      # Database access
├── model           # JPA entities
├── dto             # Data transfer objects
├── config          # Security and filters
├── exception       # Error handling
├── provider        # External API integrations
└── scheduler       # Scheduled jobs
```

## 🔐 Environment Variables

| Variable | Description |
|---|---|
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `JWT_SECRET` | JWT secret key (min. 32 chars) |
| `MAIL_USERNAME` | Email for sending alerts |
| `MAIL_PASSWORD` | Gmail app password |

## 📝 License

MIT
