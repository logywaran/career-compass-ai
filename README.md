# Career Compass – AI Job Match & Career Assistant

Career Compass is a hybrid **AI-assisted + deterministic rule-engine** web application that helps students and early professionals evaluate how well their resume matches a job description and understand what to improve to become job-ready.

Unlike purely AI-driven tools that produce unstable or inflated results, this system deliberately separates responsibilities:

• AI is used only for **semantic understanding, summaries, tips, and coaching**  
• A **Java rule engine performs all scoring and skill matching**

This architecture ensures every result is:

✅ Explainable  
✅ Reproducible  
✅ Stable across runs  
✅ Defensible in interviews and evaluations

---

## 🎯 Problem Statement

Most students apply blindly to jobs without clear insight into:

• Whether they actually meet the skill requirements  
• Why they are rejected or shortlisted  
• Which concrete skills they must learn next  
• How to tailor resumes toward specific roles  

Most existing tools provide vague AI feedback or simple keyword matching with no reliable scoring logic.

Career Compass solves this by combining:

• AI-powered resume/JD skill interpretation  
• Deterministic job-fit scoring  
• Transparent skill gap detection  
• Personalized coaching guidance

---

## ⚙️ Setup & Execution

Follow these steps to run Career Compass locally in under 5 minutes.


### ✅ Prerequisites

Make sure you have the following installed:

• Java JDK 17 or later  
• Git  

(Maven installation is NOT required – Maven Wrapper is included.)

---

### ✅ Step 1 – Clone the repository

git clone https://github.com/logywaran/career-compass-ai.git  
cd career-compass-ai

---

### ✅ Step 2 – Configure AI API (Groq)

This project uses **Groq Cloud LLM** for all AI-powered functionality.

Open:

src/main/resources/application.properties

Add your Groq API key:

groq.api.key=YOUR_GROQ_API_KEY

Save the file.

---

### ✅ Step 3 – Run the application

Use the Maven wrapper bundled with the project.

Windows:
mvnw spring-boot:run

Mac / Linux:
./mvnw spring-boot:run

---

### ✅ Step 4 – Open in Browser

After the server starts successfully, open:

http://localhost:8080

---

### ✅ Step 5 – Use the Application

1. Upload your resume PDF or paste resume text
2. Paste a job description
3. Click **Analyze**
4. View the results:
   • Match Score
   • Verified Matched Skills
   • Skill Gaps
   • AI Career Guidance

---

✅ Setup complete — no database or infrastructure setup required.

---

## 🧠 System Architecture – Hybrid Evaluation Pipeline

Resume + Job Description  
  ↓  
Groq AI → Semantic skill interpretation  
  • Extract JD required skills  
  • Identify strong skills  
  • Identify weak / learning-phase skills  
  • Infer role focus and relevance  

  ↓  
Java Rule Engine → Deterministic Scoring  
  • Strong vs weak weighting  
  • Core language presence checks  
  • Framework and tool requirement caps  
  • Domain relevance baseline  
  • Anti-inflation score ceilings  

  ↓  
Final Output  

• Match Score (0–100)  
• Fit Level (Strong / Medium / Weak)  
• Matched Skills  
• Missing Skills  
• AI-generated summaries, strengths, improvement areas, and recommendations

---

### Why Hybrid?

Pure AI scoring is unstable and biased by phrasing or resume verbosity.  
Career Compass ensures:

✅ Stable scoring across reruns  
✅ Transparent, rule-based decisions  
✅ No hallucinated skill claims  
✅ Interview-defensible architecture

---

## 🔧 Tech Stack

### Backend
• Java 17  
• Spring Boot  
• Groq API (LLM)  
• Apache PDFBox (resume text extraction)

### Frontend
• HTML  
• CSS  
• Vanilla JavaScript

### AI Design
• Groq handles **semantic interpretation only**  
• Controlled prompts prevent judgment drift  
• Deterministic Java engine owns all scoring

---

## 📚 Knowledge RAG-Lite Coaching System

Instead of complex vector databases, the project implements a lightweight, controlled coaching layer:

• Curated `KnowledgeSnippet` objects authored by humans  
• Each snippet contains topic keywords + practical career advice  
• Snippet retrieval uses keyword overlap, role focus, and detected skill gaps  
• Groq is instructed to **ground responses using the retrieved snippet context**

This approach:

✅ Avoids canned AI replies  
✅ Keeps advice relevant and structured  
✅ Prevents hallucinated guidance  
✅ Maintains professional coaching tone

---

## 🌐 Application Workflow

### 1️⃣ Resume Input

• Paste resume text or upload PDF  
• PDF text extracted with Apache PDFBox  
• Data remains session-scoped only

---

### 2️⃣ Job Description Analysis

• User pastes JD  
• `/api/analyze` runs the hybrid pipeline  
• Results returned as structured JSON

---

### 3️⃣ Results Dashboard

• Animated percentage match score  
• Fit-level indicators  
• Matched vs missing skill lists  
• Strengths & improvement sections  
• Personalized coaching tip

---

### 4️⃣ AI Career Coach

• Context-aware chat grounded in resume + JD  
• Driven by RAG-Lite knowledge retrieval  
• Supports help for:

Resume rewriting  
Learning roadmaps  
Interview preparation  
Skill explanations

---

## 🔌 Backend API Endpoints

### POST /api/upload-resume
Uploads PDF resume and extracts text using PDFBox.

---

### POST /api/analyze
Input:
{
  "jobDescription": "",
  "resumeText": ""
}

Output:
{
  "score": 0–100,
  "matchLevel": "Strong | Medium | Weak",
  "jdSkills": [],
  "resumeSkills": [],
  "matchedSkills": [],
  "missingSkills": [],
  "summary": "",
  "strengths": [],
  "areasForImprovement": [],
  "recommendations": [],
  "tip": ""
}

---

### POST /api/ask
Used by the AI coaching chatbot with grounded context.

Input:
{
  "question": "",
  "resumeText": "",
  "jobDescription": ""
}

---

## 💻 Frontend Pages

• home.html – Landing page  
• resume.html – Resume upload & paste  
• job.html – Job description entry  
• results.html – Matching dashboard  
• chatbot.html – AI coaching interface  

---

## ✅ Key Features

• AI-powered semantic skill interpretation  
• **Rule-based explainable scoring (Java)**  
• Resume evidence validation (anti hallucination)  
• Skill gap detection and career recommendations  
• PDF resume ingestion  
• RAG-lite coaching chatbot  
• Fully static frontend (no frameworks)  
• No databases or infrastructure dependencies

---

## 🧩 Challenges Faced

### 1️⃣ AI Score Inconsistency

Pure AI scoring produced unpredictable results across runs.

✅ Solved by shifting all scoring logic into a deterministic Java engine and restricting AI to interpretation only.

---

### 2️⃣ Hallucinated Skill Claims

AI models attributed skills to candidates that never appeared in resumes.

✅ Solved using **resume evidence verification filters** that remove any unverified AI skill claims before they touch scoring or output.

---

### 3️⃣ Over-Engineering Risk

Vector databases and embeddings were considered but rejected as unnecessary complexity for one-document JD + resume matching.

✅ Solved with an effective and simpler RAG-Lite keyword retrieval system.

---

## ⚠ Known Limitations

• No server-side session persistence (stateless by design)  
• No vector embedding databases (outside scope)  
• Markdown artifacts occasionally appear in AI text output (cosmetic only)  
• No analytics dashboard

---

## 🧩 Educational Outcome

This project demonstrates:

• Hybrid AI + deterministic decision systems  
• Ethical LLM integration and governance  
• Transparent scoring architectures  
• Controlled prompting methods  
• RAG-Lite retrieval design  
• Building complete backend + frontend systems

---

## 🚀 Project Status

✅ Engineering Complete  
✅ Fully Functional  
✅ Portfolio Ready  

The project is now optimized for demonstrations and technical interviews rather than feature expansion.

---

## 👨‍💻 Author

Logeshwaran J  
Final-Year Engineering Student | Java Backend Developer  
GitHub: https://github.com/logywaran










---------------------------------------------------------------------------------------


