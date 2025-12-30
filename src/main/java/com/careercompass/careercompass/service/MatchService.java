package com.careercompass.careercompass.service;

import com.careercompass.careercompass.dto.AiSkillProfile;
import com.careercompass.careercompass.dto.AnalysisRequest;
import com.careercompass.careercompass.dto.AnalysisResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);

    @Autowired
    private AiService aiService;

    // Constants for Scoring Logic
    private static final double BASELINE_SCORE = 2.0;
    private static final double LANGUAGE_CAP_THRESHOLD = 6.5;
    private static final double UNRELATED_SCORE_CAP = 35.0;
    private static final double LOW_STRONG_RATIO = 0.3;
    private static final double LOW_STRONG_SCORE_CAP = 75.0;

    // Skills we try to detect in raw text (JD + resume)
    // Includes tech + non-tech + business + sales + analyst keywords
    private static final List<String> SKILLS = Arrays.asList(
            // programming languages
            "java", "python", "javascript", "typescript",
            "c", "c programming", "c++", "c#", "go", "golang", "ruby", "kotlin", "swift", "php",

            // backend frameworks / tech
            "spring", "spring boot", "django", "flask", "node.js", "node js",

            // frontend
            "html", "css", "react", "angular", "vue", "bootstrap",
            "redux", "next.js", "jest", "unit testing",

            // databases / data tech
            "sql", "mysql", "postgresql", "oracle", "mongodb",

            // cloud / devops / tools
            "git", "github", "docker", "aws", "azure", "gcp",

            // data / analytics
            "excel", "power bi", "tableau", "data analysis", "data analyst",
            "business analyst", "business analysis", "requirements gathering",
            "requirement gathering", "stakeholder management",

            // office / business tools
            "ms office", "microsoft office", "word", "powerpoint",

            // sales / marketing / crm
            "sales", "marketing", "crm", "customer relationship",
            "business development", "b2b", "b2c",

            // fundamentals / soft-ish tech
            "oops", "object oriented programming",
            "data structures", "algorithms", "dsa",

            // generic soft skills (for relevance detection)
            "communication", "teamwork", "leadership",
            "problem solving", "analytical thinking", "presentation");

    // Feature flag to enable/disable new weak skill detection
    private static final boolean USE_ENHANCED_WEAK_DETECTION = true;

    // Phrases that indicate a weaker skill mention (certificates / basic exposure)
    private static final List<String> WEAK_SKILL_HINTS = Arrays.asList(
            // Courses & Certifications
            "certification",
            "certified",
            "certificate",
            "course",
            "training",

            // Learning phrases
            "introduction to",
            "intro to",
            "learning",
            "currently learning",
            "exploring",
            "currently exploring",
            "studying",

            // Beginner indicators
            "basics",
            "basic",
            "beginner",
            "foundation",
            "fundamentals",
            "familiar with",
            "exposure to",
            "working knowledge",

            // Interest/Future goals
            "interested in",
            "interest in");

    // Core “programming language” skills we recognize
    private static final Set<String> LANGUAGE_SKILLS = Set.of(
            "java", "python", "javascript", "typescript",
            "c", "c++", "c#", "go", "golang", "ruby", "kotlin", "swift", "php");

    // Skills that usually define a role strongly (used to cap high scores if
    // missing)
    // Includes tech (dev, data) + business / analyst / sales signals
    private static final Set<String> KEY_IMPORTANT_SKILLS = Set.of(
            // dev languages
            "java", "python", "javascript", "typescript", "c#", "c++", "go", "golang",

            // back-end frameworks
            "spring", "spring boot", "django", "flask", "node.js", "node js",

            // front-end frameworks
            "react", "angular", "vue",

            // data / db
            "sql", "mysql", "postgresql", "oracle", "mongodb",

            // analytics / business tools
            "excel", "power bi", "tableau",

            // analyst / business roles
            "data analyst", "business analyst", "business analysis",

            // sales / marketing
            "sales", "marketing", "crm", "business development");

    // General “relevant background” skills so we don’t give hard 0 if there is some
    // base
    // These show that the candidate is at least in a related space (tech or
    // business)
    private static final Set<String> GENERAL_RELEVANT_SKILLS = Set.of(
            // fundamentals
            "oops", "object oriented programming",
            "data structures", "algorithms", "dsa",

            // generic dev tools
            "git", "github", "html", "css", "javascript",

            // general business / analyst skills
            "excel", "ms office", "microsoft office",
            "communication", "teamwork", "leadership",
            "problem solving", "analytical thinking", "presentation",
            "requirements gathering", "requirement gathering",
            "stakeholder management", "customer relationship");

    // Cache for compiled patterns to avoid recompiling in loops (Performance Fix)
    private static final Map<String, Pattern> PATTERN_CACHE = new HashMap<>();

    // Canonical Skill Map to handle synonyms (Node.js == Node JS)
    private static final Map<String, String> CANONICAL_SKILLS = new HashMap<>();

    // Static block to pre-compile patterns & load synonyms
    static {
        // 1. Initialize Canonical Map (Synonyms)
        // Self-mappings (Identity)
        CANONICAL_SKILLS.put("go", "go");
        CANONICAL_SKILLS.put("node.js", "node.js");

        // Synonyms -> Canonical
        CANONICAL_SKILLS.put("golang", "go");
        CANONICAL_SKILLS.put("node js", "node.js");
        CANONICAL_SKILLS.put("microsoft office", "ms office");
        CANONICAL_SKILLS.put("ms office", "ms office");
        CANONICAL_SKILLS.put("react.js", "react");
        CANONICAL_SKILLS.put("react", "react");
        CANONICAL_SKILLS.put("requirement gathering", "requirements gathering");
        CANONICAL_SKILLS.put("requirements gathering", "requirements gathering");
        // Fix: Map Role -> Skill (Standardization)
        CANONICAL_SKILLS.put("business analyst", "business analysis");
        CANONICAL_SKILLS.put("business analysis", "business analysis");
        CANONICAL_SKILLS.put("data analyst", "data analysis");
        CANONICAL_SKILLS.put("data analysis", "data analysis");
        CANONICAL_SKILLS.put("c programming", "c");
        CANONICAL_SKILLS.put("c", "c");

        // 2. Compile Patterns
        for (String skill : SKILLS) {
            String lowerSkill = skill.toLowerCase();
            String regex;

            // Boundary includes forward slash now [\\s\\.,;\\)\\(//]

            if (lowerSkill.matches(".*[\\+\\#]$")) {
                // Ends in symbol (C++, C#) - strict boundaries
                regex = "(?<=^|[\\s\\.,;\\)\\(//])" + Pattern.quote(lowerSkill) + "(?=[\\s\\.,;\\)\\(//]|$|\\z)";
            } else if (lowerSkill.equals("c")) {
                // Special case for "C" (no ++ or # after)
                regex = "\\bc(?![\\+\\#])\\b";
            } else if (lowerSkill.contains(" ") || lowerSkill.contains(".")) {
                // Multi-word / dot
                // Ensure boundaries handle slash
                regex = "(?<=^|[\\s\\(//])" + Pattern.quote(lowerSkill) + "(?=[\\s\\.,;\\)//]|$|\\z)";
            } else {
                // Standard single word
                regex = "\\b" + Pattern.quote(lowerSkill) + "\\b";
            }
            PATTERN_CACHE.put(lowerSkill, Pattern.compile(regex));
        }
    }

    // -----------------------------
    // UPDATED: helper to detect weak mentions in a local window
    // -----------------------------
    private boolean isWeakMention(String lowerText, String skill, int skillIndex) {

        if (!USE_ENHANCED_WEAK_DETECTION) {
            // Old Logic (Fallback)
            int matchEnd = Math.min(lowerText.length(), skillIndex + skill.length());
            int start = Math.max(0, skillIndex - 40);
            int end = Math.min(lowerText.length(), matchEnd + 40);

            String window = lowerText.substring(start, end);

            for (String weakKey : WEAK_SKILL_HINTS) {
                String wk = weakKey.toLowerCase();
                if (window.contains(wk + " " + skill) || window.contains(skill + " " + wk)) {
                    return true;
                }
            }
            return false;
        }

        // New Logic (Enhanced)
        // FIX: Increase window size to 100
        int matchEnd = Math.min(lowerText.length(), skillIndex + skill.length());
        int start = Math.max(0, skillIndex - 100);
        int end = Math.min(lowerText.length(), matchEnd + 100);

        String window = lowerText.substring(start, end);

        for (String weakKey : WEAK_SKILL_HINTS) {
            String wk = weakKey.toLowerCase();

            // Pattern 1: "currently learning python" or "python basics"
            if (window.contains(wk + " " + skill) || window.contains(skill + " " + wk)) {
                return true;
            }

            // Pattern 2: "learning: python" or "introduction to: typescript"
            if (window.contains(wk + ": " + skill) || window.contains(wk + ":" + skill)) {
                return true;
            }

            // Pattern 3: "python (course)" or "typescript (certification)"
            if (window.contains(skill + " (" + wk) || window.contains(skill + "(" + wk)) {
                return true;
            }

            // Pattern 4: "python - course" or "excel - linkedin learning"
            if (window.contains(skill + " - ") && window.contains(wk)) {
                return true;
            }

            // Pattern 5: "python, typescript (currently learning)"
            if (window.contains("(" + wk) && window.contains(skill)) {
                return true;
            }

            // Pattern 6: More flexible check - weak keyword appears anywhere within 5 words
            // of
            // skill
            // FIX: Ensure no sentence boundary (., ;, \n) exists between them
            String[] words = window.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                // Find the skill word
                if (words[i].toLowerCase().contains(skill)) {
                    // Check 5 words before and 5 words after
                    int checkStart = Math.max(0, i - 5);
                    int checkEnd = Math.min(words.length, i + 6);

                    for (int j = checkStart; j < checkEnd; j++) {
                        String potentialWeak = words[j].toLowerCase();
                        if (potentialWeak.contains(wk)) {
                            // Found weak keyword nearby.
                            // CRITICAL CHECK: Is there a sentence boundary between them?
                            if (hasSentenceBoundary(window, skill, wk)) {
                                continue;
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean hasSentenceBoundary(String window, String skill, String weakKey) {
        // Find indices of skill and weakKey in the window
        int pSkill = window.toLowerCase().indexOf(skill);
        int pWeak = window.toLowerCase().indexOf(weakKey);

        if (pSkill == -1 || pWeak == -1)
            return false; // Should not happen

        int start = Math.min(pSkill, pWeak);
        int end = Math.max(pSkill, pWeak);

        String between = window.substring(start, end);
        // Check for boundary chars: . ; \n
        return between.contains(".") || between.contains(";") || between.contains("\n") || between.contains("\r");
    }

    /**
     * Detects if a skill appears in a resume section that indicates learning/weak
     * context.
     * Checks for section headers like INTERESTS, COURSES, CERTIFICATIONS within 500
     * chars before the skill.
     */
    private boolean isInLearningSection(String lowerText, int skillIndex) {
        if (!USE_ENHANCED_WEAK_DETECTION)
            return false;

        try {
            // Get larger context to detect section headers
            int start = Math.max(0, skillIndex - 500);
            int end = Math.min(lowerText.length(), skillIndex + 50);
            String sectionContext = lowerText.substring(start, end);

            // Section indicators that suggest learning/weak skills
            List<String> learningSectionKeywords = Arrays.asList(
                    "courses",
                    "course",
                    "certifications",
                    "certification",
                    "currently learning",
                    "interests",
                    "hobbies",
                    "learning goals",
                    "training",
                    "coursework",
                    "online courses",
                    "mooc");

            // Check if any learning section keyword appears before the skill in the context
            for (String keyword : learningSectionKeywords) {
                int keyIndex = sectionContext.indexOf(keyword);
                if (keyIndex != -1) {
                    // FIX: Ensure the header is actually BEFORE the skill
                    // sectionContext starts at 'start'. Skill is at 'skillIndex'.
                    // So relative skill index in sectionContext is (skillIndex - start).
                    int relativeSkillIndex = skillIndex - start;

                    if (keyIndex > relativeSkillIndex) {
                        continue; // Header appears AFTER the skill -> ignore
                    }

                    // Verify it's actually a section header (usually in caps or followed by
                    // newline/colon)
                    String upperContext = sectionContext.toUpperCase();
                    String keyUpper = keyword.toUpperCase();

                    // Pattern: Header followed by colon or newline?
                    // We check if the keyword exists effectively as a header
                    if (upperContext.contains(keyUpper + ":") ||
                            upperContext.contains(keyUpper + "\n") ||
                            upperContext.contains(keyUpper + "\r") ||
                            upperContext.contains(keyUpper + " ")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Fail safe
            // e.printStackTrace(); // Optional logging
        }

        return false;
    }

    // -----------------------------
    // UPDATED: Optimized & Bug-Fixed Skill Extraction
    // - Uses PATTERN_CACHE
    // - Fixes C/C++ boundaries
    // - Fixes Synonyms (Canonicalization)
    // - Fixes Subsumed skills (Double Counting with Safety)
    // - Resolves Ambiguity ("Go")
    // -----------------------------
    private List<String> extractSkills(String text) {

        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }

        String lower = text.toLowerCase();
        Set<String> detectedSet = new HashSet<>();

        for (String rawSkill : SKILLS) {
            String skill = rawSkill.toLowerCase();
            Pattern pattern = PATTERN_CACHE.get(skill);

            if (pattern == null)
                continue;

            Matcher matcher = pattern.matcher(lower);

            while (matcher.find()) {
                int index = matcher.start();

                boolean isWeak = isWeakMention(lower, skill, index) || isInLearningSection(lower, index);

                if (!isWeak) {
                    // FIX: Map to Canonical Form if exists
                    String canonical = CANONICAL_SKILLS.getOrDefault(skill, skill);
                    detectedSet.add(canonical);
                    break;
                }
            }
        }

        // ---------------------------------------------------------
        // LOGIC FIX: Deduplication with Cached Regex (Performance check)
        // Remove "shorter" skills if they are subsumed by "longer" skills AND appear as
        // a word.
        // ex: "Spring" in "Spring Boot" -> Remove "Spring".
        // ex: "Java" in "Javascript" -> Keep "Java" (not a whole word).
        // ---------------------------------------------------------
        List<String> finalSkills = new ArrayList<>(detectedSet);

        finalSkills.removeIf(shorter -> {

            // Reuse cached pattern for the shorter skill (Correct "C" regex logic
            // included!)
            Pattern shorterPattern = PATTERN_CACHE.get(shorter);
            if (shorterPattern == null) {
                // Should not happen for skills in detectedSet, unless canonical mapping changed
                // it to something without a pattern.
                // But canonical skills like "go" are in PATTERN_CACHE.
                // Fallback: don't remove if we can't verify
                return false;
            }

            for (String longer : detectedSet) {
                if (longer.equals(shorter))
                    continue;

                if (longer.contains(shorter)) {
                    // Optimized: Use Cached Pattern (O(1) lookup)
                    // This uses the correct regex logic (e.g. C vs C++)
                    if (shorterPattern.matcher(longer).find()) {
                        return true; // It IS a substring word (Spring in Spring Boot) -> Remove
                    }
                }
            }
            return false;
        });

        return finalSkills;
    }

    // Normalize a collection of skills to a lowercase, trimmed Set
    private Set<String> normalizeSkills(Collection<String> skills) {
        Set<String> result = new HashSet<>();
        if (skills == null)
            return result;

        for (String s : skills) {
            if (s == null)
                continue;
            String cleaned = s.toLowerCase().trim();
            if (!cleaned.isEmpty()) {
                result.add(cleaned);
            }
        }
        return result;
    }

    // Keep only skills that are actually mentioned in the resume text (used to
    // safety-check AI profile)
    private List<String> filterSkillsByResumeEvidence(List<String> skills, String resumeText) {
        if (skills == null || skills.isEmpty() || resumeText == null) {
            return new ArrayList<>();
        }

        String lowerResume = resumeText.toLowerCase();
        List<String> verified = new ArrayList<>();

        for (String skill : skills) {
            if (skill == null)
                continue;
            String s = skill.toLowerCase().trim();
            if (s.isEmpty())
                continue;

            // CRITICAL FIX: Use Regex instead of .contains()
            // contains("go") matches "going", contains("c") matches "success".
            boolean found = false;

            // 1. Try our highly-tuned cached patterns first (Handles C++, C#, Go, Node.js
            // correct boundaries)
            Pattern cached = PATTERN_CACHE.get(s);
            if (cached != null) {
                if (cached.matcher(lowerResume).find()) {
                    found = true;
                }
            } else {
                // 2. Fallback for new skills AI found that aren't in our list
                // Use standard word boundaries \bSKILL\b
                try {
                    Pattern fallback = Pattern.compile("\\b" + Pattern.quote(s) + "\\b");
                    if (fallback.matcher(lowerResume).find()) {
                        found = true;
                    }
                } catch (Exception e) {
                    if (lowerResume.contains(s)) {
                        found = true;
                    }
                }
            }

            if (found) {
                verified.add(s);
            }
        }

        // Remove duplicates
        return verified.stream().distinct().collect(Collectors.toList());
    }

    // Core deterministic scoring based only on Java-extracted JD + resume skills
    private double calculateMatchScore(Set<String> jdSkillsRaw, Set<String> resumeSkillsRaw) {

        Set<String> jdSkills = normalizeSkills(jdSkillsRaw);
        Set<String> resumeSkills = normalizeSkills(resumeSkillsRaw);

        if (jdSkills.isEmpty()) {
            return 0.0;
        }

        // 1. Direct overlap
        Set<String> matched = new HashSet<>(jdSkills);
        matched.retainAll(resumeSkills);

        double matchedCount = matched.size();
        double totalJdSkills = jdSkills.size();

        double score0to10 = (matchedCount / totalJdSkills) * 10.0;

        // 2. Background baseline
        boolean hasRelevantBackground = false;

        for (String s : resumeSkills) {
            if (LANGUAGE_SKILLS.contains(s) || GENERAL_RELEVANT_SKILLS.contains(s)) {
                hasRelevantBackground = true;
                break;
            }
        }

        if (matchedCount == 0 && hasRelevantBackground) {
            score0to10 = BASELINE_SCORE; // baseline
        }

        if (matchedCount == 0 && !hasRelevantBackground) {
            score0to10 = 0.0;
        }

        // 3. Main language cap
        Set<String> jdLangs = new HashSet<>();

        for (String s : jdSkills) {
            if (LANGUAGE_SKILLS.contains(s)) {
                jdLangs.add(s);
            }
        }

        boolean resumeHasJdLang = false;

        for (String s : resumeSkills) {
            if (jdLangs.contains(s)) {
                resumeHasJdLang = true;
                break;
            }
        }

        if (!jdLangs.isEmpty() && !resumeHasJdLang && score0to10 > LANGUAGE_CAP_THRESHOLD) {
            score0to10 = LANGUAGE_CAP_THRESHOLD;
        }

        // 4. Key-skill cap
        boolean hasKeySkill = false;

        for (String s : jdSkills) {
            if (KEY_IMPORTANT_SKILLS.contains(s) && resumeSkills.contains(s)) {
                hasKeySkill = true;
                break;
            }
        }

        if (!hasKeySkill && score0to10 > LANGUAGE_CAP_THRESHOLD) {
            score0to10 = LANGUAGE_CAP_THRESHOLD;
        }

        // 5. Hard max cap
        if (score0to10 > 9.0) {
            score0to10 = 9.0;
        }

        // 6. Convert to percentage
        return Math.round(score0to10 * 10.0);
    }

    /**
     * Checks if a skill appears ONLY in learning contexts (courses, interests,
     * certifications)
     * and NOT in actual project or work experience sections.
     */
    private boolean appearsOnlyInLearningContext(String skill, String resumeText) {
        if (resumeText == null || skill == null)
            return false;

        String lower = resumeText.toLowerCase();
        String skillLower = skill.toLowerCase();

        // Phrases that indicate learning context
        List<String> learningPhrases = Arrays.asList(
                "currently learning " + skillLower,
                "currently exploring " + skillLower,
                "learning " + skillLower,
                "exploring " + skillLower,
                "studying " + skillLower,
                "introduction to " + skillLower,
                "intro to " + skillLower,
                "basic " + skillLower,
                "basics of " + skillLower,
                skillLower + " course",
                skillLower + " certification",
                skillLower + " (course)",
                skillLower + " - course",
                "familiar with " + skillLower,
                "exposure to " + skillLower);

        // Check if any learning phrase matches
        for (String phrase : learningPhrases) {
            if (lower.contains(phrase)) {
                return true;
            }
        }

        // Check if skill appears in INTERESTS, COURSES, or CERTIFICATIONS sections
        // Pattern: Find section header, then check if skill appears within next 1000
        // chars
        String[] learningSections = { "interests", "courses", "certifications", "training", "coursework" };

        for (String section : learningSections) {
            Pattern sectionPattern = Pattern.compile(
                    "\\b" + section + "\\b[\\s\\S]{0,1000}\\b" + Pattern.quote(skillLower) + "\\b",
                    Pattern.CASE_INSENSITIVE);

            if (sectionPattern.matcher(resumeText).find()) {
                // Skill found in learning section - now check if it ALSO appears in
                // projects/experience
                boolean appearsInProjects = checkIfInProjectsOrExperience(skillLower, resumeText);
                if (!appearsInProjects) {
                    return true; // Only in learning section, not in actual work
                }
            }
        }

        return false;
    }

    /**
     * Helper: Check if skill appears in PROJECTS or EXPERIENCE sections (indicates
     * real usage)
     */
    private boolean checkIfInProjectsOrExperience(String skillLower, String resumeText) {
        String[] experienceSections = { "projects", "project", "experience", "work experience",
                "internship", "employment", "professional experience" };

        for (String section : experienceSections) {
            Pattern sectionPattern = Pattern.compile(
                    "\\b" + section + "\\b[\\s\\S]{0,2000}\\b" + Pattern.quote(skillLower) + "\\b",
                    Pattern.CASE_INSENSITIVE);

            if (sectionPattern.matcher(resumeText).find()) {
                return true; // Skill appears in actual work/project context
            }
        }

        return false;
    }

    // Map numeric score (percentage) → label
    private String determineMatchLevel(double score) {
        if (score >= 75) {
            return "Strong Match";
        } else if (score >= 40) {
            return "Medium Match";
        } else {
            return "Weak Match";
        }
    }

    /**
     * Light calibration on top of deterministic score using AI profile.
     * - Avoid very high scores when strong JD skill coverage is low.
     * - Cap score when profile is not generally related.
     * NOTE: This does NOT change the underlying skill lists, only adjusts the score
     * value slightly.
     */
    private double calibrateScoreWithProfile(
            AiSkillProfile profile,
            List<String> jdSkills,
            List<String> resumeSkills,
            double baseScore) {
        if (profile == null || jdSkills == null || jdSkills.isEmpty()) {
            return baseScore;
        }

        // Normalize JD + strong skills
        Set<String> jd = normalizeSkills(jdSkills);
        Set<String> strong = normalizeSkills(profile.getStrongSkills());

        // Count how many JD skills are STRONG matches
        Set<String> matchedStrong = new HashSet<>(jd);
        matchedStrong.retainAll(strong);

        double strongRatio = jd.isEmpty()
                ? 0.0
                : (double) matchedStrong.size() / (double) jd.size();

        double adjusted = baseScore;

        // 1) If candidate is not generally related at all,
        // cap score so it can't look like a good match.
        if (!profile.isGenerallyRelated()) {
            adjusted = Math.min(adjusted, UNRELATED_SCORE_CAP);
        }

        // 2) If strong coverage of JD skills is very low,
        // avoid scores that look like 90%+ "perfect"
        if (strongRatio < LOW_STRONG_RATIO && adjusted > LOW_STRONG_SCORE_CAP) {
            adjusted = LOW_STRONG_SCORE_CAP;
        }

        // 3) Keep within 0–100 and round
        if (adjusted < 0.0)
            adjusted = 0.0;
        if (adjusted > 100.0)
            adjusted = 100.0;

        return Math.round(adjusted);
    }

    public AnalysisResponse analyze(AnalysisRequest request) {

        // FIX: Critical Null Check
        if (request == null) {
            throw new IllegalArgumentException("AnalysisRequest cannot be null");
        }

        String jdText = request.getJobDescription();
        String resumeText = request.getResumeText();

        log.info("=== /api/analyze called ===");
        log.debug("JD length: {}", (jdText == null ? 0 : jdText.length()));
        log.debug("Resume length: {}", (resumeText == null ? 0 : resumeText.length()));

        // 1) PURE JAVA: deterministic skill extraction (single source of truth)
        List<String> jdSkills = extractSkills(jdText);
        List<String> resumeSkills = extractSkills(resumeText);

        log.info("Keyword jdSkills: {}", jdSkills);
        log.info("Keyword resumeSkills: {}", resumeSkills);

        // 2) OPTIONAL AI PROFILE: used only for calibration / RAG, not as source of
        // skills
        AiSkillProfile aiProfile = aiService.analyzeSkillsWithAi(jdText, resumeText);
        log.info("AI profile returned: {}", (aiProfile != null));

        if (aiProfile != null) {
            log.debug("AI jdRequiredSkills (raw): {}", aiProfile.getJdRequiredSkills());
            log.debug("AI strongSkills (raw): {}", aiProfile.getStrongSkills());
            log.debug("AI weakSkills (raw): {}", aiProfile.getWeakSkills());

            // Safety-check AI strong/weak skills: keep only ones that actually appear in
            // resume text
            List<String> verifiedStrong = filterSkillsByResumeEvidence(aiProfile.getStrongSkills(), resumeText);
            List<String> verifiedWeak = filterSkillsByResumeEvidence(aiProfile.getWeakSkills(), resumeText);

            // ADDITIONAL SAFETY: Remove skills from strongSkills if they appear in learning
            // contexts
            List<String> finalVerifiedStrong = new ArrayList<>();
            if (USE_ENHANCED_WEAK_DETECTION) {
                for (String skill : verifiedStrong) {
                    if (!appearsOnlyInLearningContext(skill, resumeText)) {
                        finalVerifiedStrong.add(skill);
                    } else {
                        // Move to weak skills if it's actually a learning context
                        log.debug("Moving '{}' from strong to weak (detected in learning context)", skill);
                        if (verifiedWeak != null && !verifiedWeak.contains(skill)) {
                            verifiedWeak.add(skill);
                        }
                    }
                }
                verifiedStrong = finalVerifiedStrong;
            }

            aiProfile.setStrongSkills(verifiedStrong);
            aiProfile.setWeakSkills(verifiedWeak);

            log.info("Verified AI strongSkills: {}", verifiedStrong);
            log.info("Verified AI weakSkills: {}", verifiedWeak);
        } else {
            log.info("AI profile is null; using only keyword-based logic.");
        }

        // 3) Matched + missing (for display + AI tip/insights) – from deterministic
        // Java skills
        Set<String> jdSkillSet = new HashSet<>(jdSkills);
        Set<String> resumeSkillSet = new HashSet<>(resumeSkills);

        double finalScore = calculateMatchScore(jdSkillSet, resumeSkillSet); // 0–100

        // Light calibration using AI profile (if available)
        if (aiProfile != null) {
            finalScore = calibrateScoreWithProfile(aiProfile, jdSkills, resumeSkills, finalScore);
        }

        // 5) Build response object
        AnalysisResponse response = new AnalysisResponse();
        response.setScore(finalScore);
        response.setMatchLevel(determineMatchLevel(finalScore));

        response.setJdSkills(jdSkills);
        response.setResumeSkills(resumeSkills);

        // Compute overlap for display
        Set<String> matched = new HashSet<>(normalizeSkills(jdSkills));
        matched.retainAll(normalizeSkills(resumeSkills));
        response.setMatchedSkills(new ArrayList<>(matched));

        List<String> missing = new ArrayList<>(normalizeSkills(jdSkills));
        missing.removeAll(normalizeSkills(resumeSkills));
        response.setMissingSkills(missing);

        // 6) Optional AI tip (short practical advice)
        String improvedTip = aiService.generateImprovedTip(request, response);
        response.setTip(improvedTip);

        // 7) Optional AI insights (rich content for results page)
        aiService.enrichWithInsights(request, response);

        log.info("Analysis Complete. Score: {} ({})", finalScore, response.getMatchLevel());
        return response;
    }
}
