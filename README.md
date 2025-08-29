------------

## 🚀 Overview

This project is my solution to the **Bajaj Finserv Health Hiring Challenge (Java)**.
It is a **Spring Boot application** that runs automatically on startup and performs the following tasks:

1. Sends a **POST request** to generate a webhook.
2. Determines the correct SQL question based on my **registration number (`22BCE2394` → Even → Question 2)**.
3. Builds the **SQL solution query**.
4. Submits the query to the provided webhook using **JWT authentication**.
5. Logs all the steps and responses to the console.

-------------

## 🛠️ Tech Stack

* **Java 17**
* **Spring Boot 3**
* **Maven**
* **RestTemplate** for REST API calls
* **Jackson** for JSON serialization

---

## 📂 Project Structure

```
bajaj-finserv-hiring-challenge/
├── src/
│   └── main/
│       ├── java/com/bajajfinserv/hiring/
│       │   └── BajajFinservHiringApplication.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/y2jjjj/bajaj-finserv-hiring-challenge.git
cd bajaj-finserv-hiring-challenge
```

### 2. Build the Project

```bash
mvn clean package
```

The JAR file will be generated at:

```
target/bajaj-finserv-hiring-challenge-1.0.0.jar
```

### 3. Run the Application

Option 1 – Using Maven:

```bash
mvn spring-boot:run
```

Option 2 – Using the JAR:

```bash
java -jar target/bajaj-finserv-hiring-challenge-1.0.0.jar
```

---

## 🔑 What Happens When You Run

1. **Webhook Generation** → App sends a POST request with my name, regNo, and email.
2. **Response Handling** → Receives a `webhook` URL and an `accessToken`.
3. **SQL Selection** → Since my regNo ends with `94` (even), the app picks **Question 2**.
4. **Solution Submission** → Posts the SQL query to the webhook with JWT in the header.
5. **Logs** → All actions and responses are printed to the console.

---

## ✅ Final SQL Query (Question 2)

```sql
SELECT e1.EMP_ID,
       e1.FIRST_NAME,
       e1.LAST_NAME,
       d.DEPARTMENT_NAME,
       COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE e1
JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
LEFT JOIN EMPLOYEE e2 
       ON e1.DEPARTMENT = e2.DEPARTMENT
      AND e2.DOB > e1.DOB
GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
ORDER BY e1.EMP_ID DESC;
```

---

## 📤 Submission Checklist

* [x] Personal details updated in `BajajFinservHiringApplication.java`.
* [x] Correct SQL query added for Question 2 (Even RegNo).
* [x] Application tested locally.
* [x] GitHub repo made public.
* [x] JAR uploaded (in `/target` or GitHub releases).
* [x] Links submitted in the official form.

**GitHub Repo:** `https://github.com/<your-username>/bajaj-finserv-hiring-challenge`
**JAR Download:** Direct link to `target/bajaj-finserv-hiring-challenge-1.0.0.jar`

---

## 📝 Notes

* No REST endpoints are exposed; the flow runs entirely on startup.
* JWT is automatically handled in the Authorization header.
* Console logs help track each step for easy debugging.

---
