# Software Guide and Installation Instructions

Welcome to the HTML & CSS Learning Platform project! This document provides detailed instructions on how to set up the environment, install, run the project on a local machine, and utilize the basic features of the system.

---

## 1. System Prerequisites

Before starting, please ensure that your computer has the following software installed:
- **Java Development Kit (JDK):** Version 17 (or newer).
- **MySQL Server:** Running on localhost, default port 3306.
- **Git:** For cloning the repository.
- **Web Browser:** Chrome, Firefox, Edge, Safari, etc.

*Note: The project includes the Maven Wrapper (`mvnw`), so you do not need to install Maven separately.*

---

## 2. Installation Guide

### Step 1: Clone the Project
Open your Terminal or Command Prompt and run the following command to download the project to your local machine:
```bash
git clone <GITHUB_REPO_URL>
cd htmlcsslearning
```

### Step 2: Database Configuration
The project uses a MySQL database. In the `src/main/resources/application.properties` file, the default configuration is:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/html_css_learning_prototype?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
```
- **Important:** The application will automatically create the tables, but it may require the database to exist first depending on your MySQL setup. You must either:
  1. Create a database named `html_css_learning_prototype` in your MySQL server manually before running the application.
  2. Or, change the database name in the `application.properties` file to an existing database in your system.
- Update the `spring.datasource.username` and `spring.datasource.password` to match your local MySQL credentials.

### Step 3: API Key Configuration
The system uses third-party APIs for automated email sending and AI-powered evaluation. The keys currently provided in the `application.properties` file might be expired or restricted. To ensure the application works perfectly, you need to configure your own keys:

- **Groq API (for AI Evaluation):**
  1. Go to the GroqCloud Console (https://console.groq.com/).
  2. Sign in or create an account.
  3. Navigate to the "API Keys" section and click "Create API Key".
  4. Copy the generated key and replace the value of `spring.ai.openai.api-key` in the `application.properties` file.
  *Note: The project uses the `llama-3.1-8b-instant` model.*

- **Google SMTP Mail (for OTP Emails):**
  1. Go to the Google Account settings of the Gmail you want to use for sending emails.
  2. Navigate to "Security" and enable "2-Step Verification".
  3. Search for "App passwords" in the security settings.
  4. Select "Mail" and the device you are using, then click "Generate".
  5. Copy the 16-character generated app password.
  6. In `application.properties`, update `spring.mail.username` with your email address and `spring.mail.password` with the generated app password.

---

## 3. Running the Application

### Running via Terminal/Command Prompt
At the root directory of the project, execute the following command:

- **On Windows:**
  ```cmd
  mvnw spring-boot:run
  ```
- **On macOS / Linux:**
  ```bash
  ./mvnw spring-boot:run
  ```

### Running via IDE (IntelliJ IDEA, Eclipse, VS Code)
1. Open your IDE and select the project directory. Wait for the IDE to resolve the dependencies from the `pom.xml` file.
2. Locate the `HtmlcsslearningApplication.java` file in the `src/main/java/com/se2/htmlcsslearning/` directory.
3. Run the main method in this file.

When the console displays: `Started HtmlcsslearningApplication in X seconds...`, the server has started successfully.

---

## 4. Accessing the Application

After successfully starting the application on port 8888, you can access the web application through your browser at the following URL:

**Homepage:** http://localhost:8888

---

## 5. Overview of Features

Once you access the system, you can explore the following features:

1. **Authentication System (Auth):**
   - Registration, login, and password recovery via email OTP.
2. **HTML/CSS Courses & Lessons (Learning & Try Example):**
   - Study theory and try executing code directly on the web in real-time (Live Preview).
3. **Interactive Compiler:**
   - A workspace for practicing coding logic and structures.
4. **Challenge System:**
   - **CSS Debugging:** Given an incorrect layout, find and fix the CSS errors.
   - **Pixel-Perfect:** Recreate the user interface based on a provided image.
   - *These challenges are automatically evaluated and scored using AI technology.*
5. **Compare CSS Tool:**
   - Understand the differences between CSS properties through visual comparisons and demonstrations.

---

## 6. Troubleshooting

- **Port 8888 is already in use:**
  Change the `server.port=8888` variable in the `application.properties` file to a different port, such as `server.port=8080`.
- **Database Connection Issues (Access denied or Unknown database):**
  Verify that your MySQL Service is running. Ensure that the database was created and that the username and password in `application.properties` match your local setup.
- **Errors when running the Mvnw command:**
  Ensure that Java 17 or higher is installed correctly. Verify this by running `java -version` in your terminal.

