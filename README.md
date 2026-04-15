# HTML & CSS Learning Platform

A web-based learning platform designed to help users learn and practice **HTML and CSS** through structured lessons and interactive challenges.
The system integrates **AI-powered grading** and **email OTP authentication**.

---

# Installation & Setup

Follow the steps below to run the project locally.

---

## 1. Clone the Project

Open a terminal and run the following command to clone the repository:

```bash
git clone https://github.com/thangbodoiqua/Group_9_HtmlCssLearning.git
cd project-folder
```

Alternatively, you can download the project as a **ZIP file** from the GitHub page and extract it to a folder on your computer.

---

## 2. Database Configuration

The project uses a **MySQL database**.

### Requirements

Ensure that:

* MySQL Server is installed.
* MySQL is running on `localhost:3306`.

### Create Database

Create a database named **htmlcss_db**:

```sql
CREATE DATABASE htmlcss_db;
```

Alternatively, the application may create the database automatically depending on the configuration.

### Configure Database Credentials

Open the following file:

```
src/main/resources/application.properties
```

Update the database configuration with your credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/htmlcss_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### Important

After the application starts, you **must run the SQL script** located in the repository:

```
/database/
```

This script will populate the database with:

* Lesson content
* Practice challenges

Without running this script, the **lesson catalog will appear empty**.

---

## 3. API Key Configuration

The system uses third-party services for:

* AI-powered grading
* Email OTP verification

You need to provide your own API keys.

---

### 3.1 Groq API (AI Evaluation)

1. Visit: https://console.groq.com
2. Sign in or create a free account.
3. Navigate to:

```
API Keys → Create API Key
```

4. Copy the generated API key.

5. Update `application.properties`:

```properties
groq.api.key=YOUR_GROQ_API_KEY
```

---

### 3.2 Google SMTP (Email OTP)

To enable email OTP verification:

#### Step 1: Enable 2-Step Verification

Go to your **Google Account → Security** and enable **2-Step Verification**.

#### Step 2: Generate an App Password

Navigate to:

```
Security → App passwords
```

Select:

```
App: Mail
Device: Your device
```

Click **Generate**.

Google will provide a **16-character password**.

#### Step 3: Update SMTP Configuration

Open `application.properties` and update:

```properties
spring.mail.username=YOUR_GMAIL
spring.mail.password=YOUR_APP_PASSWORD
```

---

## 4. Run the Application

You can start the application using either an **IDE** or **Maven command line**.

---

### Option A — Using an IDE

Open the project in:

* IntelliJ IDEA
* Eclipse
* VS Code

Locate the main class:

```
src/main/java/com/se2/htmlcsslearning/HtmlcsslearningApplication.java
```

Right-click the file and select:

```
Run
```

or

```
Debug
```

---

### Option B — Using Maven

Open a terminal in the project root directory and run:

```bash
mvn spring-boot:run
```

---

## 5. Access the Application

Once the server starts successfully, open your browser and navigate to:

```
http://localhost:8888
```

---

## Notes

* Ensure **MySQL is running** before starting the application.
* Run the SQL script in the `/database` folder to populate lesson data.
* Replace all placeholder API keys before running the system.
