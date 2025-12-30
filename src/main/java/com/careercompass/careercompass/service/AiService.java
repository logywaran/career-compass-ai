package com.careercompass.careercompass.service;

import com.careercompass.careercompass.dto.AiSkillProfile;
import com.careercompass.careercompass.dto.AnalysisRequest;
import com.careercompass.careercompass.dto.AnalysisResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct; // Spring Boot 3 uses jakarta
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);
    private static final int MAX_TEXT_LENGTH = 50000; // ~50KB limit to prevent memory leaks/timeouts

    // Groq API key – configure in application.properties as:
    // groq.api.key=YOUR_GROQ_KEY
    @Value("${groq.api.key:}") // safe even if empty, validated in @PostConstruct
    private String groqApiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String GROQ_MODEL = "llama-3.3-70b-versatile";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CareerKnowledgeBase careerKnowledgeBase;

    public AiService(CareerKnowledgeBase careerKnowledgeBase) {
        this.careerKnowledgeBase = careerKnowledgeBase;
        this.restTemplate = createConfiguredRestTemplate();
    }

    // Validate API Key at startup
    @PostConstruct
    public void validateApiKey() {
        // Skip validation in test profile
        String activeProfile = System.getProperty("spring.profiles.active", "");
        if (activeProfile.contains("test")) {
            log.info("Test profile detected - skipping API key validation");
            return;
        }

        if (groqApiKey == null || groqApiKey.isBlank()) {
            String errorMsg = """

                    ╔════════════════════════════════════════════════════════════════╗
                    ║  ERROR: Groq API Key Not Configured                           ║
                    ╠════════════════════════════════════════════════════════════════╣
                    ║  The GROQ_API_KEY environment variable is not set.            ║
                    ║                                                                ║
                    ║  To fix this:                                                  ║
                    ║  1. Get your free API key from: https://console.groq.com/keys ║
                    ║  2. Set the environment variable:                              ║
                    ║                                                                ║
                    ║     Windows:   set GROQ_API_KEY=your_key_here                 ║
                    ║     Mac/Linux: export GROQ_API_KEY=your_key_here              ║
                    ║     Docker:    docker run -e GROQ_API_KEY=your_key_here ...   ║
                    ║                                                                ║
                    ║  3. Restart the application                                    ║
                    ╚════════════════════════════════════════════════════════════════╝
                    """;
            log.error(errorMsg);
            throw new IllegalStateException("Groq API key is not configured. Set GROQ_API_KEY environment variable.");
        }

        log.info("Groq API key validated successfully");
    }

    private RestTemplate createConfiguredRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // 10 seconds to connect
        factory.setReadTimeout(60000); // 60 seconds to read (LLMs can be slow)
        return new RestTemplate(factory);
    }

    private static final String CAREER_CHAT_PROMPT_TEMPLATE = """
            You are Career Compass, a professional and practical career assistant.
            Your role is to give clear, honest, and useful career guidance without exaggeration.

            You are given the following inputs:

            USER QUESTION:
            \"\"\"%s\"\"\"

            JOB DESCRIPTION (may be empty):
            \"\"\"%s\"\"\"

            RESUME TEXT (may be empty):
            \"\"\"%s\"\"\"

            VERIFIED CONTEXT (derived from rule-based analysis and retrieved guidance snippets):
            \"\"\"%s\"\"\"


            ---------------- CORE RESPONSIBILITIES ----------------
            - Answer the user’s question in a helpful, career-focused way.
            - Use the resume, job description, and verified context to make advice specific and grounded.
            - Speak like a thoughtful career coach, not a template or checklist generator.


            ---------------- SKILL TRUTH RULES (VERY IMPORTANT) ----------------
            - You may state that the user HAS a skill only if:
              - the skill appears in the RESUME TEXT, OR
              - the skill appears under verified matched or strong skills in the context.
            - Skills that appear only as missing or required must be treated as gaps or learning goals.
            - Do NOT invent tools, technologies, experience, certifications, or roles.
            - When uncertain, phrase suggestions as “you could consider learning” or “it may help to explore”.


            ---------------- USE OF RETRIEVED SNIPPETS ----------------
            - Retrieved snippets are provided as background guidance, not as answers to copy.
            - Use snippets to understand the idea or direction, then explain it in your own words.
            - You may combine snippet ideas with your general career knowledge.
            - Do NOT repeat or paraphrase snippet text line-by-line.
            - Prefer natural explanations, like a human career coach.
            - If snippets are missing or weak, rely on your general knowledge instead.



            ---------------- RESPONSE STYLE ----------------
            - Write in a natural, professional, conversational tone.
            - Do NOT force a fixed structure or bullet count.
            - Use short paragraphs for explanations.
            - Use bullet points only when they genuinely improve clarity.
            - Avoid repeating the same idea in multiple ways.
            - Avoid generic filler advice.


            ---------------- OPTIONAL ENHANCEMENTS ----------------
            - When helpful, suggest concrete next steps the user can realistically take.
            - You may recommend learning resources (articles, tutorials, courses, or videos),
              but only when they directly support the advice given.
            - If suggesting resources, prefer well-known platforms (e.g., official docs, W3Schools,
              Coursera, YouTube) without fabricating specific URLs.

            ----------------RESOURCE GUIDANCE RULE ------------------
            - You MAY suggest learning resources ONLY when the user asks how to learn, improve, practice, or take next steps.
            - Limit suggestions to at most 1–2 well-known resources.
            - Mention resources as examples, not as the only or best option.
            - Do NOT claim the resource is the latest, official, or up to date.
            - Do NOT browse or fetch links.
            - Prefer widely recognized platforms such as documentation sites, YouTube channels, or learning platforms.
            - If unsure, keep advice conceptual instead of naming resources.



            ---------------- OUTPUT RULES ----------------
            - Plain text only.
            - No markdown symbols or formatting.
            - Emojis are optional and should be minimal (maximum 1–2 if used).
            - Keep the answer readable and suitable for a chat interface.

            IMPORTANT:
            - If resume or job description is empty, give general guidance and mention that
              more details would improve accuracy.
            - Do NOT say you lack access to user data; all relevant information is already provided above.
            """;

    private static final String SKILL_PARSE_PROMPT_TEMPLATE = """
            You are an AI assistant that extracts structured skill information
            from a job description and a candidate's resume or CV.

            Job description:
            \"\"\"%s\"\"\"

            Resume text:
            \"\"\"%s\"\"\"

            Your task is to output a single JSON object with this exact structure (example values only):

            {
              "jdRequiredSkills": ["java", "spring boot", "rest api"],
              "strongSkills": ["java", "spring boot", "mysql"],
              "weakSkills": ["docker"],
              "roleFocus": "backend",
              "generallyRelated": true
            }

            The example uses software skills, but in your answer you must use only the skills,
            tools, qualifications, and keywords that are actually present in THIS job description
            and THIS resume.

            Definitions:
            - jdRequiredSkills = core skills, tools, or qualifications that the JOB clearly expects.
            - strongSkills = skills the candidate has real, hands-on experience with
              (projects, internships, work history, or strong resume bullets).
            - weakSkills = skills the candidate mentions only in a learning or beginner context
              (courses, certifications, "basic knowledge", "currently learning", etc.).
            - roleFocus = your best guess of the main role type of the JOB (not the person).
              Use one of: "backend", "frontend", "fullstack", "data", "testing",
              "non-tech", "other".
            - generallyRelated = true if the candidate seems roughly in the same domain
              as the job (e.g., software vs data vs business).

            CRITICAL CLASSIFICATION RULES (follow strictly):

            strongSkills = Skills with PROVEN hands-on experience:
            - Appears in PROJECT descriptions with concrete details
              Example: "Built REST API using Spring Boot with JWT authentication"
            - Appears in WORK EXPERIENCE with actual responsibilities
              Example: "Developed React components for production application"
            - Shows REAL usage with outcomes/achievements
              Example: "Deployed MySQL database handling 10K+ records"

            weakSkills = Skills in learning/beginner context:
            - ANY skill in COURSES, CERTIFICATIONS, INTERESTS, or TRAINING sections
              Example: "Introduction to Python - Coursera (2024)"
            - Skills with learning indicators:
              * "currently learning X", "exploring X", "studying X"
              * "basic X", "beginner X", "introduction to X"
              * "X (course)", "X certification", "familiar with X"
            - Skills mentioned as future goals or interests
              Example: "Interested in machine learning"

            If a skill appears ONLY in learning contexts and NOT in projects/work, it MUST be weakSkills.
            If a skill appears in BOTH learning contexts AND projects, classify as strongSkills only if the project shows real implementation.

            DO NOT classify as strongSkills just because the word appears in the resume.
            VERIFY the skill is demonstrated through actual project work or employment.

            Very important:
            - Output MUST be valid JSON.
            - Do NOT wrap it in markdown.
            - Do NOT add any explanation before or after.
            - Return exactly one JSON object.
            """;

    private static final String INSIGHTS_PROMPT_TEMPLATE = """
            You are an AI career coach helping a candidate understand their fit for a job.

            You will be given ONLY VERIFIED SKILL DATA produced by a deterministic Java rule engine.

            The lists below are the SINGLE SOURCE OF TRUTH:

            - resumeSkills → skills the user ACTUALLY has
            - matchedSkills → verified overlap between resume and JD
            - missingSkills → skills REQUIRED for the job but NOT present in resume
            - jdSkills → skills required by the job

            You MUST follow these rules:

            CRITICAL SKILL RULES:
            - You may ONLY claim the user HAS a skill if it exists in resumeSkills or matchedSkills.
            - Skills in missingSkills must ALWAYS be described as gaps or learning goals.
            - NEVER describe a missing skill as a strength.
            - DO NOT infer skills based on the job role or resume wording.
            - DO NOT introduce any new skills not present in the provided skill lists.

            Content rules:
            - summary: 1–2 sentences describing overall fit
            - strengths: 2–4 sentences ONLY about skills in resumeSkills or matchedSkills
            - gaps: 2–4 sentences ONLY about skills in missingSkills or general improvement (resume clarity, projects, etc.)
            - nextSteps: 2–4 actionable next steps based ONLY on missingSkills or resume enhancement

            Output EXACT JSON shape:

            {
              "summary": "string",
              "strengths": ["string", "string"],
              "gaps": ["string", "string"],
              "nextSteps": ["string", "string"]
            }

            Additional rules:

            - Use second-person language (“you”).
            - Do NOT mention numeric score or match level.
            - Do NOT mention being an AI.
            - Do NOT use markdown symbols or bullet points.
            - Return ONLY the JSON object and nothing else.
            - NO preamble, NO conversation, NO commentary.
            - Return STRICT JSON.
            """;

    // -------------------------
    // Generic helper: call Groq Chat API
    // -------------------------

    @SuppressWarnings("unchecked")
    private String callGroqChat(String prompt) {

        if (groqApiKey == null || groqApiKey.isBlank()) {
            log.error("Groq API key is missing. Please set groq.api.key in application.properties.");
            return null;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("model", GROQ_MODEL);
        body.put("stream", false);

        List<Map<String, String>> messages = List.of(
                Map.of("role", "user", "content", prompt));
        body.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey.trim());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> apiResponse = restTemplate.exchange(GROQ_URL, HttpMethod.POST, entity, Map.class);

            if (!apiResponse.getStatusCode().is2xxSuccessful() || apiResponse.getBody() == null) {
                log.error("Groq API call failed: {}", apiResponse.getStatusCode());
                return null;
            }

            Object choicesObj = apiResponse.getBody().get("choices");
            if (!(choicesObj instanceof List<?> choices) || choices.isEmpty()) {
                log.warn("Groq API call: no choices in response");
                return null;
            }

            Object firstChoiceObj = choices.get(0);
            if (!(firstChoiceObj instanceof Map<?, ?> firstChoice)) {
                log.warn("Groq API call: unexpected choice structure");
                return null;
            }

            Object messageObj = firstChoice.get("message");
            if (!(messageObj instanceof Map<?, ?> message)) {
                log.warn("Groq API call: no message field in choice");
                return null;
            }

            Object contentObj = message.get("content");
            if (contentObj == null) {
                log.warn("Groq API call: no content in message");
                return null;
            }

            return contentObj.toString();

        } catch (Exception e) {
            log.error("Error calling Groq Chat API: {}", e.getMessage());
            return null;
        }
    }

    // -------------------------
    // RAG SUPPORT METHODS
    // -------------------------

    private List<KnowledgeSnippet> retrieveRelevantSnippets(
            String question,
            List<String> missingSkills,
            String roleFocus) {
        String q = question == null ? "" : question.toLowerCase();
        Set<String> skills = missingSkills == null
                ? Set.of()
                : missingSkills.stream()
                        .filter(Objects::nonNull)
                        .map(s -> s.toLowerCase().trim())
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toSet());
        String role = roleFocus == null ? "" : roleFocus.toLowerCase();

        return careerKnowledgeBase.getAllSnippets().stream()
                .map(snippet -> {
                    int score = 0;
                    for (String kw : snippet.getKeywords()) {
                        String k = kw.toLowerCase();
                        if (q.contains(k))
                            score += 3;
                        if (role.contains(k))
                            score += 2;
                        for (String s : skills) {
                            if (s.contains(k) || k.contains(s)) {
                                score += 4;
                            }
                        }
                    }
                    return new AbstractMap.SimpleEntry<>(snippet, score);
                })
                .filter(entry -> entry.getValue() > 0)
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(3)
                .map(AbstractMap.SimpleEntry::getKey)
                .collect(Collectors.toList());
    }

    private String buildRagContext(String question,
            String resumeText,
            String jobDescription) {

        AiSkillProfile profile = analyzeSkillsWithAi(jobDescription, resumeText);

        if (profile == null || profile.getJdRequiredSkills() == null) {
            return "No structured skill profile available. Fall back to generic career logic.";
        }

        List<String> jdSkills = profile.getJdRequiredSkills();
        List<String> strong = profile.getStrongSkills() != null ? profile.getStrongSkills() : List.of();
        List<String> weak = profile.getWeakSkills() != null ? profile.getWeakSkills() : List.of();

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        // FIX: Case-insensitive matching for RAG Context
        Set<String> strongLower = strong.stream().map(String::toLowerCase).collect(Collectors.toSet());
        Set<String> weakLower = weak.stream().map(String::toLowerCase).collect(Collectors.toSet());

        for (String jdSkill : jdSkills) {
            String skillLower = jdSkill.toLowerCase();
            if (strongLower.contains(skillLower) || weakLower.contains(skillLower)) {
                matched.add(jdSkill);
            } else {
                missing.add(jdSkill);
            }
        }

        String roleFocus = profile.getRoleFocus();
        boolean related = profile.isGenerallyRelated();

        // Retrieve knowledge snippets based on missing skills + role
        List<KnowledgeSnippet> snippets = retrieveRelevantSnippets(question, missing, roleFocus);

        StringBuilder sb = new StringBuilder();

        sb.append("Candidate-job skill context:\n");
        sb.append("JD required skills: ").append(jdSkills).append("\n");
        sb.append("Strong skills: ").append(strong).append("\n");
        sb.append("Weak skills: ").append(weak).append("\n");
        sb.append("Matched skills (by AI profile): ").append(matched).append("\n");
        sb.append("Missing skills (by AI profile): ").append(missing).append("\n");
        sb.append("Role focus: ").append(roleFocus == null ? "unknown" : roleFocus).append("\n");
        sb.append("Generally related: ").append(related).append("\n");

        if (!snippets.isEmpty()) {
            sb.append("\nRelevant career guidance (you MUST use these ideas in your answer):\n");
            for (KnowledgeSnippet s : snippets) {
                sb.append("- [").append(s.getTopic()).append("]\n");
                sb.append(s.getAdviceText()).append("\n");
            }
        } else {
            sb.append("\nNo specific snippets matched; use general career logic.\n");
        }

        return sb.toString();
    }

    // -------------------------
    // TIP GENERATION (Groq)
    // -------------------------

    public String generateImprovedTip(AnalysisRequest request, AnalysisResponse response) {

        log.info("generateImprovedTip: using Groq");

        String prompt = buildPrompt(request, response);
        String tip = callGroqChat(prompt);

        if (tip != null && !tip.isBlank()) {
            return tip.trim();
        }

        // Fallback: if anything goes wrong, use the existing tip
        return response.getTip();
    }

    // Tip prompt uses ONLY rule-engine output, no raw JD/resume
    private String buildPrompt(AnalysisRequest request, AnalysisResponse response) {
        String matchLevel = response.getMatchLevel();
        double score = response.getScore();

        StringBuilder sb = new StringBuilder();

        sb.append("You are a helpful career advisor for students and freshers.\n");
        sb.append(
                "You will see ONLY a structured analysis result that was computed by a deterministic Java rule engine.\n");
        sb.append(
                "Your job is to give ONE short, specific, practical tip (2–3 sentences) that helps the user improve their resume, skills, or job search for this particular job.\n\n");

        sb.append("Verified analysis result:\n");
        sb.append("JD Skills (jdSkills): ").append(response.getJdSkills()).append("\n");
        sb.append("Resume Skills (resumeSkills): ").append(response.getResumeSkills()).append("\n");
        sb.append("Matched Skills (matchedSkills): ").append(response.getMatchedSkills()).append("\n");
        sb.append("Missing Skills (missingSkills): ").append(response.getMissingSkills()).append("\n");
        sb.append("Score (0–100): ").append(score).append("\n");
        sb.append("Match Level: ").append(matchLevel).append("\n\n");

        sb.append("Important skill constraints:\n");
        sb.append("- Treat the above skills and score as ground truth.\n");
        sb.append("- You may ONLY say the user already has a skill if it appears in resumeSkills or matchedSkills.\n");
        sb.append(
                "- If a skill appears only in missingSkills, talk about it as a learning target or gap, not something they already know.\n");
        sb.append("- Do NOT invent or assume any other technologies or skills that are not listed.\n\n");

        sb.append(
                "Now generate exactly ONE short tip, in 2–3 sentences, speaking directly to the student using 'you'.\n\n");

        sb.append("Adapt the tip based on the match level:\n");
        sb.append(
                "- If match level is 'Strong Match': focus on polishing the resume, highlighting strengths, and interview preparation.\n");
        sb.append(
                "- If match level is 'Medium Match': focus on 1–2 key skill gaps to close and how to show learning efforts.\n");
        sb.append(
                "- If match level is 'Weak Match': focus on a simple learning roadmap and which core skills to start with.\n\n");

        sb.append("Do NOT repeat the numeric score or the match level.\n");
        sb.append("Do NOT use bullet points.\n");
        sb.append("Just output the tip as a small, friendly paragraph.\n");

        return sb.toString();
    }

    // -------------------------
    // INSIGHTS GENERATION (Groq)
    // -------------------------

    // Build a strictly fact-based prompt for insights: only rule-engine output, no
    // raw JD/resume text
    private String buildInsightsPrompt(AnalysisRequest request, AnalysisResponse response) {

        StringBuilder sb = new StringBuilder();
        sb.append(INSIGHTS_PROMPT_TEMPLATE).append("\n\n");

        sb.append("Verified analysis result from the Java rule engine:\n");
        sb.append("score: ").append(response.getScore()).append("\n");
        sb.append("matchLevel: ").append(response.getMatchLevel()).append("\n");
        sb.append("jdSkills: ").append(response.getJdSkills()).append("\n");
        sb.append("resumeSkills: ").append(response.getResumeSkills()).append("\n");
        sb.append("matchedSkills: ").append(response.getMatchedSkills()).append("\n");
        sb.append("missingSkills: ").append(response.getMissingSkills()).append("\n");

        sb.append("\nRemember: you MUST follow the CRITICAL SKILL RULES above.\n");

        return sb.toString();
    }

    public void enrichWithInsights(AnalysisRequest request, AnalysisResponse response) {

        log.info(">> AiService.enrichWithInsights CALLED (Groq)");

        String prompt = buildInsightsPrompt(request, response);
        String rawText = callGroqChat(prompt);

        if (rawText == null || rawText.isBlank()) {
            log.warn("Groq insights: empty response, using fallback if needed.");
        } else {
            try {
                log.debug("Groq insights raw response (truncated): {}",
                        rawText.length() > 200 ? rawText.substring(0, 200) + "..." : rawText);

                String jsonText = cleanJsonResponse(rawText);

                if (jsonText == null) {
                    log.error("Groq insights: no valid JSON object found in response.");
                } else {
                    Map<String, Object> parsed = objectMapper.readValue(jsonText, Map.class);

                    // summary
                    Object summaryObj = parsed.get("summary");
                    if (summaryObj != null && !summaryObj.toString().isBlank()) {
                        response.setSummary(summaryObj.toString().trim());
                    }

                    // strengths
                    List<String> strengths = toSentenceList(parsed.get("strengths"));
                    if (!strengths.isEmpty()) {
                        response.setStrengths(strengths);
                    }

                    // gaps (maps to areasForImprovement)
                    List<String> areas = toSentenceList(parsed.get("gaps"));
                    if (areas.isEmpty()) {
                        // fallback to old field name if AI used it anyway
                        areas = toSentenceList(parsed.get("areasForImprovement"));
                    }
                    if (!areas.isEmpty()) {
                        response.setAreasForImprovement(areas);
                    }

                    // nextSteps (maps to recommendations)
                    List<String> recs = toSentenceList(parsed.get("nextSteps"));
                    if (recs.isEmpty()) {
                        // fallback to old field name if AI used it anyway
                        recs = toSentenceList(parsed.get("recommendations"));
                    }
                    if (!recs.isEmpty()) {
                        response.setRecommendations(recs);
                    }

                    // Guardrail to prevent hallucinations
                    sanitizeInsights(response);
                }

            } catch (Exception e) {
                log.error("Groq insights generation failed: {}. Raw response: {}", e.getMessage(), rawText);
            }
        }

        // Optional: deterministic fallback if everything is still empty
        if ((response.getStrengths() == null || response.getStrengths().isEmpty())
                && (response.getAreasForImprovement() == null || response.getAreasForImprovement().isEmpty())
                && (response.getRecommendations() == null || response.getRecommendations().isEmpty())) {

            log.info("Groq insights: all lists empty, using simple fallback.");

            List<String> fallbackStrengths = new ArrayList<>();
            if (response.getMatchedSkills() != null && !response.getMatchedSkills().isEmpty()) {
                fallbackStrengths.add("You already have useful skills such as "
                        + String.join(", ", response.getMatchedSkills()) + ".");
            } else {
                fallbackStrengths.add("You already have a useful base of skills that you can build on for this role.");
            }
            response.setStrengths(fallbackStrengths);

            List<String> fallbackAreas = new ArrayList<>();
            if (response.getMissingSkills() != null && !response.getMissingSkills().isEmpty()) {
                fallbackAreas.add("You can focus on building skills in "
                        + String.join(", ", response.getMissingSkills()) + " to better match this job.");
            } else {
                fallbackAreas.add(
                        "You can still refine your resume layout and highlight your most relevant projects more clearly.");
            }
            response.setAreasForImprovement(fallbackAreas);

            List<String> fallbackRecs = new ArrayList<>();
            fallbackRecs.add(
                    "Update your resume to highlight your matched skills clearly in a projects or skills section.");
            fallbackRecs.add(
                    "Start a small project or course to learn one or two of the missing or less visible skills and add them to your resume.");
            response.setRecommendations(fallbackRecs);
        }
    }

    // -------------------------
    // CAREER CHAT (WITH RAG, Groq)
    // -------------------------

    private String buildSkillAnalysisPrompt(String jobDescription, String resumeText) {
        String safeJd = jobDescription == null ? "" : jobDescription.trim();
        String safeResume = resumeText == null ? "" : resumeText.trim();
        return String.format(SKILL_PARSE_PROMPT_TEMPLATE, safeJd, safeResume);
    }

    private String buildCareerChatPrompt(String question,
            String resumeText,
            String jobDescription,
            String ragContext) {

        String safeQuestion = question == null ? "" : question.trim();
        String safeResume = resumeText == null ? "" : resumeText.trim();
        String safeJd = jobDescription == null ? "" : jobDescription.trim();
        String safeCtx = ragContext == null ? "" : ragContext.trim();

        return String.format(
                CAREER_CHAT_PROMPT_TEMPLATE,
                safeQuestion,
                safeJd,
                safeResume,
                safeCtx);
    }

    public String answerCareerQuestion(String question, String resumeText, String jobDescription) {

        log.info(">> AiService.answerCareerQuestion CALLED (Groq)");

        // SAFEGUARD: Truncate large inputs to prevent token exhaustion or DOS
        if (resumeText != null && resumeText.length() > MAX_TEXT_LENGTH) {
            resumeText = resumeText.substring(0, MAX_TEXT_LENGTH);
            log.warn("Resume text truncated to {} chars for safety", MAX_TEXT_LENGTH);
        }
        if (jobDescription != null && jobDescription.length() > MAX_TEXT_LENGTH) {
            jobDescription = jobDescription.substring(0, MAX_TEXT_LENGTH);
            log.warn("JD text truncated to {} chars for safety", MAX_TEXT_LENGTH);
        }

        String ragContext = buildRagContext(question, resumeText, jobDescription);
        log.debug(">>> RAG context for /api/ask: {}", ragContext);

        String prompt = buildCareerChatPrompt(question, resumeText, jobDescription, ragContext);
        String answer = callGroqChat(prompt);

        if (answer != null && !answer.isBlank()) {
            return answer.trim();
        }

        return fallbackCareerAnswer(question, resumeText, jobDescription);
    }

    // -------------------------
    // SKILL PARSING (Groq)
    // -------------------------

    public AiSkillProfile analyzeSkillsWithAi(String jobDescription, String resumeText) {

        log.info(">> AiService.analyzeSkillsWithAi CALLED (Groq)");

        // SAFEGUARD: Truncate large inputs
        if (resumeText != null && resumeText.length() > MAX_TEXT_LENGTH) {
            resumeText = resumeText.substring(0, MAX_TEXT_LENGTH);
        }
        if (jobDescription != null && jobDescription.length() > MAX_TEXT_LENGTH) {
            jobDescription = jobDescription.substring(0, MAX_TEXT_LENGTH);
        }

        String prompt = buildSkillAnalysisPrompt(jobDescription, resumeText);
        String rawText = callGroqChat(prompt);

        if (rawText == null || rawText.isBlank()) {
            log.warn("Groq skill analysis: empty response");
            return null;
        }

        try {
            String jsonText = cleanJsonResponse(rawText);

            if (jsonText == null) {
                log.error("Groq skill analysis: no valid JSON object found in response.");
                return null;
            }

            Map<String, Object> parsed = objectMapper.readValue(jsonText, Map.class);

            AiSkillProfile profile = new AiSkillProfile();

            profile.setJdRequiredSkills(toStringList(parsed.get("jdRequiredSkills")));
            profile.setStrongSkills(toStringList(parsed.get("strongSkills")));
            profile.setWeakSkills(toStringList(parsed.get("weakSkills")));

            Object roleObj = parsed.get("roleFocus");
            if (roleObj != null) {
                profile.setRoleFocus(roleObj.toString());
            }

            Object relatedObj = parsed.get("generallyRelated");
            if (relatedObj instanceof Boolean b) {
                profile.setGenerallyRelated(b);
            } else if (relatedObj != null) {
                profile.setGenerallyRelated(Boolean.parseBoolean(relatedObj.toString()));
            }

            log.info(">> AiService (Groq): parsed AI profile successfully.");
            return profile;

        } catch (Exception e) {
            log.error("Groq skill analysis failed: {}. Raw response: {}", e.getMessage(), rawText);
        }

        // fallback tell caller to use keyword extractor
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<String> toStringList(Object value) {

        if (value instanceof List<?> list) {

            List<String> result = new ArrayList<>();

            for (Object item : list) {
                if (item != null) {
                    result.add(
                            item.toString()
                                    .toLowerCase()
                                    .trim()
                    // No truncation here but output is small usually
                    );
                }
            }
            return result;
        }

        return Collections.emptyList();
    }

    // for insights sentences (keep original casing, no lowercasing)
    @SuppressWarnings("unchecked")
    private List<String> toSentenceList(Object value) {
        if (value instanceof List<?> list) {
            List<String> result = new ArrayList<>();
            for (Object item : list) {
                if (item != null) {
                    String s = item.toString().trim();
                    if (!s.isEmpty()) {
                        result.add(s);
                    }
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    // ----------------------------------------------------
    // Robust JSON cleaning
    // ----------------------------------------------------
    private String cleanJsonResponse(String raw) {
        if (raw == null || raw.isBlank())
            return null;

        String cleaned = raw.trim();

        // 1. Remove Markdown code blocks if present
        if (cleaned.contains("```json")) {
            int start = cleaned.indexOf("```json") + 7;
            int end = cleaned.lastIndexOf("```");
            if (end > start) {
                cleaned = cleaned.substring(start, end).trim();
            }
        } else if (cleaned.contains("```")) {
            int start = cleaned.indexOf("```") + 3;
            int end = cleaned.lastIndexOf("```");
            if (end > start) {
                cleaned = cleaned.substring(start, end).trim();
            }
        }

        // 2. Extract the first { to the last } to remove surrounding commentary
        int firstBrace = cleaned.indexOf('{');
        int lastBrace = cleaned.lastIndexOf('}');

        if (firstBrace != -1 && lastBrace != -1 && lastBrace > firstBrace) {
            return cleaned.substring(firstBrace, lastBrace + 1);
        }

        return null; // or could return cleaned if we want to be risky
    }

    // ----------------------------------------------------
    // NEW: Guardrail to remove hallucinated skill claims
    // ----------------------------------------------------
    private void sanitizeInsights(AnalysisResponse response) {

        // Build forbidden skill set = missingSkills (lowercased)
        Set<String> forbidden = new HashSet<>();
        if (response.getMissingSkills() != null) {
            for (String s : response.getMissingSkills()) {
                if (s != null && !s.isBlank()) {
                    forbidden.add(s.toLowerCase());
                }
            }
        }

        // 1) Clean strengths: remove any sentence that mentions a forbidden skill
        if (response.getStrengths() != null && !response.getStrengths().isEmpty()) {
            List<String> cleanedStrengths = new ArrayList<>();
            for (String sentence : response.getStrengths()) {
                if (sentence == null || sentence.isBlank())
                    continue;
                String lower = sentence.toLowerCase();

                boolean mentionsForbidden = false;
                for (String fs : forbidden) {
                    // FIX: Use word boundaries for sanitization to avoid substrings (go vs good)
                    // fs = "go"
                    // pattern = \bgo\b
                    try {
                        if (Pattern.compile("\\b" + Pattern.quote(fs) + "\\b").matcher(lower).find()) {
                            mentionsForbidden = true;
                            break;
                        }
                    } catch (Exception e) {
                        // Fallback
                        if (lower.contains(fs)) {
                            mentionsForbidden = true;
                            break;
                        }
                    }
                }

                // Keep only sentences that do NOT talk about missing skills
                if (!mentionsForbidden) {
                    cleanedStrengths.add(sentence);
                }
            }
            response.setStrengths(cleanedStrengths);
        }

        // 2) Clean summary: if it mentions any forbidden skill, replace with a generic,
        // safe summary
        String summary = response.getSummary();
        if (summary != null && !summary.isBlank() && !forbidden.isEmpty()) {
            String lower = summary.toLowerCase();
            boolean badSummary = false;
            for (String fs : forbidden) {
                // FIX: Use word boundaries
                try {
                    if (Pattern.compile("\\b" + Pattern.quote(fs) + "\\b").matcher(lower).find()) {
                        badSummary = true;
                        break;
                    }
                } catch (Exception e) {
                    if (lower.contains(fs)) {
                        badSummary = true;
                        break;
                    }
                }
            }

            if (badSummary) {
                String level = response.getMatchLevel() == null ? "" : response.getMatchLevel().toLowerCase();

                if (level.contains("strong")) {
                    response.setSummary(
                            "You are a strong overall fit for this job based on your current skills, with a few areas you can still refine to stand out even more.");
                } else if (level.contains("medium")) {
                    response.setSummary(
                            "You are a partial fit for this job: some of your skills match well, but there are also important gaps you should work on next.");
                } else {
                    response.setSummary(
                            "You are still early for this job: you have some useful skills, but you will need to build several key skills to become a stronger match.");
                }
            }
        }
    }

    private String fallbackCareerAnswer(String question, String resume, String jd) {
        return "I'm sorry, I couldn't reach the AI service right now. Please try again later. In the meantime, focus on highlighting your matching skills in your resume.";
    }
}
