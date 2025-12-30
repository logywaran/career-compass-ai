# Career Compass – AI Job Match & Career Assistant

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.12-brightgreen.svg)](https://spring.io/projects/spring-boot)


Career Compass is a hybrid **AI-assisted + deterministic rule-engine** web application that helps students and early professionals evaluate how well their resume matches a job description and understand what to improve to become job-ready.

Unlike purely AI-driven tools that produce unstable or inflated results, this system deliberately separates responsibilities:

• **AI** is used only for **semantic understanding, summaries, tips, and coaching**  
• A **Java rule engine performs all scoring and skill matching**

This architecture ensures every result is:

✅ **Explainable** – You know exactly why you got that score  
✅ **Reproducible** – Same inputs = same outputs  
✅ **Stable** – No random AI fluctuations  
✅ **Defensible** – Interview-ready architecture

---

## � Live Demo

**🚀 Try it now:** [https://career-compass-ai-itmy.onrender.com](https://career-compass-ai-itmy.onrender.com)

> **Note:** The app may take 30 seconds to wake up on first visit (free tier spins down after inactivity).

---

## �🎯 Problem Statement

Most students apply blindly to jobs without clear insight into:

• Whether they actually meet the skill requirements  
• Why they are rejected or shortlisted  
• Which concrete skills they must learn next  
• How to tailor resumes toward specific roles

Most existing tools provide vague AI feedback or simple keyword matching with no reliable scoring logic.

**Career Compass solves this** by combining:
- AI-powered resume/JD skill interpretation
- Deterministic job-fit scoring
- Transparent skill gap detection
- Personalized coaching guidance

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [System Architecture](#-system-architecture)
- [Prerequisites](#-prerequisites)
- [Setup & Installation](#-setup--installation)
  - [Method 1: Local Setup (Recommended for Development)](#method-1-local-setup-recommended-for-development)
  - [Method 2: Docker Setup (Recommended for Deployment)](#method-2-docker-setup-recommended-for-deployment)
- [Running the Application](#-running-the-application)
- [Testing](#-testing)
- [API Documentation](#-api-documentation)
- [Deployment](#-deployment)
- [Troubleshooting](#-troubleshooting)
- [Project Structure](#-project-structure)
- [Known Limitations](#-known-limitations)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

### Core Features
- ✅ **Hybrid AI + Deterministic Scoring** – Best of both worlds
- ✅ **Resume PDF Upload** – Automatic text extraction with Apache PDFBox
- ✅ **Skill Extraction** – Smart parsing of job descriptions and resumes
- ✅ **Weak Skill Detection** – Distinguishes "currently learning" from "built with"
- ✅ **Resume Evidence Verification** – Filters AI hallucinations
- ✅ **Match Score (0-100)** – Transparent, rule-based calculation
- ✅ **Skill Gap Analysis** – Shows exactly what's missing
- ✅ **AI Career Coach** – RAG-Lite chatbot with grounded responses
- ✅ **Personalized Insights** – Strengths, gaps, and next steps

### Technical Features
- ✅ **Stateless Design** – No database required
- ✅ **Docker Support** – Easy deployment
- ✅ **Environment Variable Configuration** – Secure API key management
- ✅ **Comprehensive Logging** – SLF4J with configurable levels
- ✅ **Test Coverage** – Unit and integration tests
- ✅ **Production-Ready** – Error handling, timeouts, input validation

---

## 🔧 Tech Stack

### Backend
- **Java 17** – Modern LTS version
- **Spring Boot 3.4.12** – Latest stable release
- **Groq API** – Fast LLM inference (llama-3.3-70b-versatile)
- **Apache PDFBox 2.0.30** – PDF text extraction
- **Jackson** – JSON processing
- **SLF4J** – Logging framework

### Frontend
- **HTML5** – Semantic markup
- **CSS3** – Modern styling
- **Vanilla JavaScript** – No framework bloat

### DevOps & Tools
- **Maven** – Build automation
- **Docker** – Containerization
- **JaCoCo** – Test coverage reporting
- **Mockito** – Mocking framework for tests

---

## 🧠 System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    USER INPUT                                │
│            Resume (PDF/Text) + Job Description               │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                  GROQ AI (Semantic Layer)                    │
│  • Extract JD required skills                                │
│  • Identify strong skills (project experience)               │
│  • Identify weak skills (courses, learning)                  │
│  • Infer role focus (backend/frontend/data/etc.)             │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│            JAVA RULE ENGINE (Scoring Layer)                  │
│  • Resume evidence verification (anti-hallucination)         │
│  • Strong vs weak skill weighting                            │
│  • Core language presence checks                             │
│  • Framework/tool requirement caps                           │
│  • Domain relevance baseline                                 │
│  • Anti-inflation score ceilings                             │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                    FINAL OUTPUT                              │
│  • Match Score (0–100)                                       │
│  • Fit Level (Strong/Medium/Weak)                            │
│  • Matched Skills                                            │
│  • Missing Skills                                            │
│  • AI-generated insights (summary, strengths, gaps, steps)   │
└─────────────────────────────────────────────────────────────┘
```

### Why Hybrid?

Pure AI scoring is unstable and biased by phrasing or resume verbosity. Career Compass ensures:

✅ **Stable scoring** across reruns  
✅ **Transparent**, rule-based decisions  
✅ **No hallucinated** skill claims  
✅ **Interview-defensible** architecture

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

### Required
- **Java JDK 17 or later** ([Download](https://www.oracle.com/java/technologies/downloads/#java17))
  ```bash
  # Verify installation
  java -version
  # Should show: java version "17.x.x" or higher
  ```

- **Git** ([Download](https://git-scm.com/downloads))
  ```bash
  # Verify installation
  git --version
  ```

### Optional (for Docker deployment)
- **Docker** ([Download](https://www.docker.com/products/docker-desktop))
  ```bash
  # Verify installation
  docker --version
  docker-compose --version
  ```

### API Key (Required)
- **Groq API Key** (Free tier available)
  1. Visit [https://console.groq.com/keys](https://console.groq.com/keys)
  2. Sign up for a free account
  3. Generate a new API key
  4. Save it securely (you'll need it in setup)

> **Note:** Maven installation is **NOT required** – Maven Wrapper is included in the project.

---

## 🚀 Setup & Installation

### Method 1: Local Setup (Recommended for Development)

#### Step 1: Clone the Repository

```bash
# Clone the repository
git clone https://github.com/logywaran/career-compass-ai.git

# Navigate to project directory
cd career-compass-ai
```

#### Step 2: Configure Environment Variables

**Option A: Using .env file (Recommended)**

```bash
# Copy the example environment file
cp .env.example .env

# Edit .env file and add your Groq API key
# Windows: notepad .env
# Mac/Linux: nano .env
```

Add the following to `.env`:
```properties
GROQ_API_KEY=your_actual_groq_api_key_here
```

**Option B: Set environment variable directly**

**Windows (Command Prompt):**
```cmd
set GROQ_API_KEY=your_actual_groq_api_key_here
```

**Windows (PowerShell):**
```powershell
$env:GROQ_API_KEY="your_actual_groq_api_key_here"
```

**Mac/Linux (Bash/Zsh):**
```bash
export GROQ_API_KEY=your_actual_groq_api_key_here
```

> **Important:** Never commit your `.env` file or API key to Git! The `.gitignore` file is configured to exclude it.

#### Step 3: Build the Project

```bash
# Windows
mvnw.cmd clean install

# Mac/Linux
./mvnw clean install
```

This will:
- Download all dependencies
- Compile the code
- Run tests
- Create the JAR file in `target/` directory

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

#### Step 4: Verify Configuration

```bash
# Check that environment variable is set
# Windows (Command Prompt)
echo %GROQ_API_KEY%

# Windows (PowerShell)
echo $env:GROQ_API_KEY

# Mac/Linux
echo $GROQ_API_KEY
```

You should see your API key (not empty).

---

### Method 2: Docker Setup (Recommended for Deployment)

#### Step 1: Clone the Repository

```bash
git clone https://github.com/logywaran/career-compass-ai.git
cd career-compass
```

#### Step 2: Create .env File

```bash
# Copy example file
cp .env.example .env

# Edit and add your Groq API key
# The .env file should contain:
GROQ_API_KEY=your_actual_groq_api_key_here
```

#### Step 3: Build Docker Image

```bash
# Build the Docker image
docker build -t career-compass:latest .
```

This will:
- Use multi-stage build to compile the application
- Create an optimized production image (~200MB)
- Include health checks

**Expected output:**
```
Successfully built xxxxxxxxx
Successfully tagged career-compass:latest
```

#### Step 4: Verify Docker Image

```bash
# List Docker images
docker images | grep career-compass
```

You should see:
```
career-compass   latest   xxxxxxxxx   X minutes ago   XXX MB
```

---

## 🏃 Running the Application

### Method 1: Run Locally (Development)

```bash
# Windows
mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

**With specific profile:**
```bash
# Run with development profile (more verbose logging)
mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**Expected output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v3.4.12)

2025-12-30 09:30:00.000  INFO --- [main] c.c.c.CareerCompassApplication : Starting CareerCompassApplication
2025-12-30 09:30:01.000  INFO --- [main] c.c.c.service.AiService        : Groq API key validated successfully
2025-12-30 09:30:02.000  INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8080 (http)
2025-12-30 09:30:02.000  INFO --- [main] c.c.c.CareerCompassApplication : Started CareerCompassApplication in X.XXX seconds
```

### Method 2: Run with Docker

**Using docker run:**
```bash
docker run -d \
  --name career-compass \
  -p 8080:8080 \
  -e GROQ_API_KEY=your_actual_groq_api_key_here \
  career-compass:latest
```

**Using docker-compose (Recommended):**
```bash
# Make sure .env file exists with GROQ_API_KEY
docker-compose up -d
```

**Check container status:**
```bash
docker ps | grep career-compass
```

**View logs:**
```bash
# Follow logs
docker logs -f career-compass

# Last 100 lines
docker logs --tail 100 career-compass
```

**Stop the container:**
```bash
# Using docker-compose
docker-compose down

# Using docker directly
docker stop career-compass
docker rm career-compass
```

### Method 3: Run JAR Directly

```bash
# Build first
mvnw clean package -DskipTests

# Run the JAR
java -jar target/career-compass-0.0.1-SNAPSHOT.jar
```

---

## 🌐 Accessing the Application

Once the application is running, open your browser and navigate to:

```
http://localhost:8080
```

You should see the **Career Compass** landing page.

### Application Pages

- **Home** – `http://localhost:8080/home.html` – Landing page
- **Resume Upload** – `http://localhost:8080/resume.html` – Upload/paste resume
- **Job Description** – `http://localhost:8080/job.html` – Enter job description
- **Results** – `http://localhost:8080/results.html` – View match analysis
- **AI Coach** – `http://localhost:8080/chatbot.html` – Career coaching chatbot

### Health Check

```bash
# Check if application is healthy
curl http://localhost:8080/health

# Expected response:
{"status":"UP"}
```

---

## 🧪 Testing

### Run All Tests

```bash
# Windows
mvnw.cmd test

# Mac/Linux
./mvnw test
```

### Run Specific Test Class

```bash
mvnw test -Dtest=MatchServiceTest
```

### Generate Test Coverage Report

```bash
mvnw clean test jacoco:report
```

View the coverage report:
```
open target/site/jacoco/index.html
```

### Test Structure

```
src/test/java/
├── com/careercompass/careercompass/
│   ├── service/
│   │   ├── AiServiceTest.java          # AI service unit tests
│   │   ├── MatchServiceTest.java       # Match service unit tests
│   │   └── MatchServiceIntegrationTest.java  # Integration tests
│   └── controller/
│       └── MatchControllerTest.java    # Controller tests
```

---

## 📡 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints

#### 1. Upload Resume PDF

**POST** `/api/upload-resume`

**Request:**
- Content-Type: `multipart/form-data`
- Body: `file` (PDF file)

**Response:**
```json
{
  "resumeText": "Extracted text from PDF..."
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/upload-resume \
  -F "file=@/path/to/resume.pdf"
```

---

#### 2. Analyze Resume vs Job Description

**POST** `/api/analyze`

**Request:**
```json
{
  "jobDescription": "We are looking for a Java Developer with Spring Boot...",
  "resumeText": "I am a software engineer with 2 years of experience..."
}
```

**Response:**
```json
{
  "score": 75.5,
  "matchLevel": "Strong Match",
  "jdSkills": ["java", "spring boot", "rest api", "mysql"],
  "resumeSkills": ["java", "spring boot", "mysql", "git"],
  "matchedSkills": ["java", "spring boot", "mysql"],
  "missingSkills": ["rest api"],
  "summary": "You have a strong foundation for this role...",
  "strengths": [
    "Your Java and Spring Boot experience aligns well with the requirements.",
    "You have demonstrated MySQL database skills."
  ],
  "areasForImprovement": [
    "You could strengthen your REST API development skills.",
    "Consider adding more project examples to your resume."
  ],
  "recommendations": [
    "Build a REST API project using Spring Boot to showcase your skills.",
    "Add the project to your resume with specific technical details."
  ],
  "tip": "Focus on highlighting your Spring Boot projects more prominently in your resume..."
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "jobDescription": "Java Developer with Spring Boot experience",
    "resumeText": "Software Engineer with Java and Spring Boot skills"
  }'
```

---

#### 3. Ask Career Question (AI Coach)

**POST** `/api/ask`

**Request:**
```json
{
  "question": "How do I learn React?",
  "resumeText": "I am a Java backend developer...",
  "jobDescription": "Frontend Developer role requiring React..."
}
```

**Response:**
```json
{
  "answer": "Given your Java backend background, learning React is a great next step..."
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/ask \
  -H "Content-Type: application/json" \
  -d '{
    "question": "How do I improve my resume?",
    "resumeText": "My resume text...",
    "jobDescription": "Job description..."
  }'
```

---

## 🚢 Deployment

### Deploy to Render (Recommended)

1. **Push to GitHub**
   ```bash
   git add .
   git commit -m "Prepare for deployment"
   git push origin main
   ```

2. **Create Render Account**
   - Visit [https://render.com](https://render.com)
   - Sign up with GitHub

3. **Create New Web Service**
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Configure:
     - **Name:** `career-compass`
     - **Environment:** `Docker`
     - **Instance Type:** Free
     - **Environment Variables:**
       - Key: `GROQ_API_KEY`
       - Value: `your_actual_groq_api_key`

4. **Deploy**
   - Click "Create Web Service"
   - Wait for build to complete (~5-10 minutes)
   - Access your app at: `https://career-compass-xxxx.onrender.com`

### Deploy to Other Platforms

**Heroku:**
```bash
# Install Heroku CLI
# Create Heroku app
heroku create career-compass

# Set environment variable
heroku config:set GROQ_API_KEY=your_key_here

# Deploy
git push heroku main
```

**Railway:**
```bash
# Install Railway CLI
# Initialize
railway init

# Set environment variable
railway variables set GROQ_API_KEY=your_key_here

# Deploy
railway up
```

---

## 🔧 Troubleshooting

### Common Issues

#### 1. "Groq API Key Not Configured" Error

**Problem:** Application fails to start with API key error.

**Solution:**
```bash
# Verify environment variable is set
echo $GROQ_API_KEY  # Mac/Linux
echo %GROQ_API_KEY%  # Windows CMD
echo $env:GROQ_API_KEY  # Windows PowerShell

# If empty, set it:
export GROQ_API_KEY=your_key_here  # Mac/Linux
set GROQ_API_KEY=your_key_here  # Windows CMD
$env:GROQ_API_KEY="your_key_here"  # Windows PowerShell
```

#### 2. Port 8080 Already in Use

**Problem:** `Port 8080 is already in use`

**Solution:**
```bash
# Find process using port 8080
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Mac/Linux
lsof -i :8080
kill -9 <PID>

# Or change port in application.properties
server.port=8081
```

#### 3. Maven Build Fails

**Problem:** `mvnw: command not found` or build errors

**Solution:**
```bash
# Make mvnw executable (Mac/Linux)
chmod +x mvnw

# Use full path (Windows)
.\mvnw.cmd clean install

# Or install Maven globally
# Then use: mvn clean install
```

#### 4. Docker Build Fails

**Problem:** Docker build errors or slow build

**Solution:**
```bash
# Clear Docker cache
docker system prune -a

# Rebuild without cache
docker build --no-cache -t career-compass:latest .

# Check Docker daemon is running
docker ps
```

#### 5. Tests Fail

**Problem:** Tests fail during build

**Solution:**
```bash
# Skip tests during build
mvnw clean package -DskipTests

# Run tests with verbose output
mvnw test -X

# Run specific failing test
mvnw test -Dtest=MatchServiceTest#testExtractSkills
```

#### 6. PDF Upload Fails

**Problem:** "Unable to read PDF file" error

**Solution:**
- Ensure PDF is not password-protected
- Check PDF is not corrupted
- Try a different PDF
- Check file size (max ~10MB recommended)

---

## 📁 Project Structure

```
career-compass/
├── src/
│   ├── main/
│   │   ├── java/com/careercompass/careercompass/
│   │   │   ├── CareerCompassApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── HealthController.java
│   │   │   │   └── MatchController.java
│   │   │   ├── dto/
│   │   │   │   ├── AiSkillProfile.java
│   │   │   │   ├── AnalysisRequest.java
│   │   │   │   ├── AnalysisResponse.java
│   │   │   │   ├── QuestionRequest.java
│   │   │   │   ├── QuestionResponse.java
│   │   │   │   └── ResumeExtractResponse.java
│   │   │   └── service/
│   │   │       ├── AiService.java           # Groq API integration
│   │   │       ├── MatchService.java        # Core matching logic
│   │   │       ├── CareerKnowledgeBase.java # RAG-Lite knowledge
│   │   │       └── KnowledgeSnippet.java
│   │   └── resources/
│   │       ├── application.properties       # Main config
│   │       ├── application-dev.properties   # Dev config
│   │       └── static/
│   │           ├── home.html
│   │           ├── resume.html
│   │           ├── job.html
│   │           ├── results.html
│   │           └── chatbot.html
│   └── test/
│       ├── java/com/careercompass/careercompass/
│       │   ├── service/
│       │   │   ├── AiServiceTest.java
│       │   │   ├── MatchServiceTest.java
│       │   │   └── MatchServiceIntegrationTest.java
│       │   └── controller/
│       │       └── MatchControllerTest.java
│       └── resources/
│           └── application-test.properties
├── .env.example                # Environment variable template
├── .gitignore
├── Dockerfile                  # Docker configuration
├── docker-compose.yml          # Docker Compose setup
├── pom.xml                     # Maven dependencies
├── mvnw                        # Maven wrapper (Unix)
├── mvnw.cmd                    # Maven wrapper (Windows)
└── README.md                   # This file
```

---

## ⚠️ Known Limitations

- **No server-side session persistence** – Stateless by design
- **No vector embedding databases** – Outside project scope
- **Markdown artifacts** – Occasionally appear in AI text output (cosmetic only)
- **No analytics dashboard** – Future enhancement
- **Single-user focus** – Not designed for multi-tenancy

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---


## 👨‍💻 Author

**Logeshwaran J**  
Final-Year Engineering Student | Java Backend Developer

- GitHub: [@logywaran](https://github.com/logywaran)
- LinkedIn: [Logeshwaran J](https://www.linkedin.com/in/logeshwaran-j-165a52258/)
- Email: logeshwaran.jagadesh@gmail.com
- 🎥 **Demo Video:** [Watch on Google Drive](https://drive.google.com/file/d/15WXa36cllPDpQEc4FypKCROChdk2UOV1/view?usp=drive_link)

---

## 🙏 Acknowledgments

- [Groq](https://groq.com/) for providing fast LLM inference
- [Spring Boot](https://spring.io/projects/spring-boot) for the excellent framework
- [Apache PDFBox](https://pdfbox.apache.org/) for PDF processing

---

## 📊 Project Stats

- **Lines of Code:** ~3,500+
- **Test Coverage:** 60%+
- **API Response Time:** <2s average
- **Docker Image Size:** ~200MB

---


