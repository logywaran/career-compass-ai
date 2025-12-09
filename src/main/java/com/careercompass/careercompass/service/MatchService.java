package com.careercompass.careercompass.service;

import com.careercompass.careercompass.dto.AiSkillProfile;
import com.careercompass.careercompass.dto.AnalysisRequest;
import com.careercompass.careercompass.dto.AnalysisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private AiService aiService;

    // Skills we try to detect in raw text (JD + resume)
    // Includes tech + non-tech + business + sales + analyst keywords
    private static final List<String> SKILLS = Arrays.asList(
            // programming languages
            "java", "python", "javascript", "typescript",
            "c", "c++", "c#", "go", "ruby", "kotlin", "swift", "php",

            // backend frameworks / tech
            "spring", "spring boot", "django", "flask", "node.js", "node js",

            // frontend
            "html", "css", "react", "angular", "vue", "bootstrap",

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
            "problem solving", "analytical thinking", "presentation"
    );

    // Phrases that indicate a weaker skill mention (certificates / basic exposure)
    private static final List<String> WEAK_SKILL_HINTS = Arrays.asList(
            "certification",
            "course",
            "introduction to",
            "intro to",
            "learning",
            "currently learning",
            "basics",
            "basic",
            "beginner",
            "foundation",
            "training"
    );

    // Core “programming language” skills we recognize
    private static final Set<String> LANGUAGE_SKILLS = Set.of(
            "java", "python", "javascript", "typescript",
            "c", "c++", "c#", "go", "ruby", "kotlin", "swift", "php"
    );

    // Skills that usually define a role strongly (used to cap high scores if missing)
    // Includes tech (dev, data) + business / analyst / sales signals
    private static final Set<String> KEY_IMPORTANT_SKILLS = Set.of(
            // dev languages
            "java", "python", "javascript", "typescript", "c#", "c++",

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
            "sales", "marketing", "crm", "business development"
    );

    // General “relevant background” skills so we don’t give hard 0 if there is some base
    // These show that the candidate is at least in a related space (tech or business)
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
            "stakeholder management", "customer relationship"
    );

    // -----------------------------
    // UPDATED: helper to detect weak mentions in a local window
    // -----------------------------
    private boolean isWeakMention(String lowerText, String skill, int skillIndex) {

        int start = Math.max(0, skillIndex - 40);
        int end = Math.min(lowerText.length(), skillIndex + skill.length() + 40);
        String window = lowerText.substring(start, end);

        for (String weakKey : WEAK_SKILL_HINTS) {
            String wk = weakKey.toLowerCase();
            // We only treat it as weak if the weak phrase is close to the skill
            if (window.contains(wk + " " + skill) || window.contains(skill + " " + wk)) {
                return true;
            }
        }
        return false;
    }

    // -----------------------------
    // UPDATED: safer skill extraction
    // - Uses word boundaries for single-word skills (avoids "go" inside "ongoing")
    // - Looks for a strong mention even if the first occurrence is weak
    // - Weak detection is local, not "anywhere in document"
    // -----------------------------
    private List<String> extractSkills(String text) {

        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }

        String lower = text.toLowerCase();
        List<String> detected = new ArrayList<>();

        for (String rawSkill : SKILLS) {
            if (rawSkill == null || rawSkill.isBlank()) continue;

            String skill = rawSkill.toLowerCase();

            boolean foundStrong = false;

            // Multi-word or containing dot skills → simple contains with local weak check
            if (skill.contains(" ") || skill.contains(".")) {

                int index = lower.indexOf(skill);
                while (index != -1) {
                    if (!isWeakMention(lower, skill, index)) {
                        foundStrong = true;
                        break;
                    }
                    index = lower.indexOf(skill, index + skill.length());
                }

            } else {
                // Single word skills → use word boundaries to avoid substring matches
                String regex = "\\b" + Pattern.quote(skill) + "\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(lower);

                while (matcher.find()) {
                    int index = matcher.start();
                    if (!isWeakMention(lower, skill, index)) {
                        foundStrong = true;
                        break;
                    }
                }
            }

            if (foundStrong) {
                detected.add(skill);
            }
        }

        return detected.stream().distinct().collect(Collectors.toList());
    }

    // Normalize a collection of skills to a lowercase, trimmed Set
    private Set<String> normalizeSkills(Collection<String> skills) {
        Set<String> result = new HashSet<>();
        if (skills == null) return result;

        for (String s : skills) {
            if (s == null) continue;
            String cleaned = s.toLowerCase().trim();
            if (!cleaned.isEmpty()) {
                result.add(cleaned);
            }
        }
        return result;
    }

    // Keep only skills that are actually mentioned in the resume text (used to safety-check AI profile)
    private List<String> filterSkillsByResumeEvidence(List<String> skills, String resumeText) {
        if (skills == null || skills.isEmpty() || resumeText == null) {
            return new ArrayList<>();
        }

        String lowerResume = resumeText.toLowerCase();
        List<String> verified = new ArrayList<>();

        for (String skill : skills) {
            if (skill == null) continue;
            String s = skill.toLowerCase().trim();
            if (s.isEmpty()) continue;

            // Simple literal check: does the resume actually contain this word/phrase?
            if (lowerResume.contains(s)) {
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
            score0to10 = 2.0;        // baseline
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

        if (!jdLangs.isEmpty() && !resumeHasJdLang && score0to10 > 6.5) {
            score0to10 = 6.5;
        }

        // 4. Key-skill cap
        boolean hasKeySkill = false;

        for (String s : jdSkills) {
            if (KEY_IMPORTANT_SKILLS.contains(s) && resumeSkills.contains(s)) {
                hasKeySkill = true;
                break;
            }
        }

        if (!hasKeySkill && score0to10 > 6.5) {
            score0to10 = 6.5;
        }

        // 5. Hard max cap
        if (score0to10 > 9.0) {
            score0to10 = 9.0;
        }

        // 6. Convert to percentage
        return Math.round(score0to10 * 10.0);
    }

    // (Currently unused) example of scoring directly from AiSkillProfile
    private double calculateMatchScoreFromProfile(AiSkillProfile profile) {

        Set<String> jd = normalizeSkills(profile.getJdRequiredSkills());
        Set<String> strong = normalizeSkills(profile.getStrongSkills());
        Set<String> weak = normalizeSkills(profile.getWeakSkills());

        if (jd.isEmpty()) {
            return 0.0;
        }

        // Strong + weak matches
        Set<String> matchedStrong = new HashSet<>(jd);
        matchedStrong.retainAll(strong);

        Set<String> matchedWeak = new HashSet<>(jd);
        matchedWeak.retainAll(weak);

        double strongCount = matchedStrong.size();
        double weakCount = matchedWeak.size();

        // Weighted scoring: strong = 1.0, weak = 0.5
        double weightedMatched = strongCount * 1.0 + weakCount * 0.5;
        double maxPossible = jd.size() * 1.0;   // ideal: all JD skills are strong

        double baseScore;
        if (maxPossible == 0) {
            baseScore = 0.0;
        } else {
            baseScore = (weightedMatched / maxPossible) * 100.0;
        }

        // Baseline when nothing matches
        if (weightedMatched == 0.0) {
            if (profile.isGenerallyRelated()) {
                baseScore = 20.0; // related field but no exact JD skill match
            } else {
                baseScore = 0.0;  // totally unrelated
            }
        }

        // Language cap
        Set<String> jdLangs = new HashSet<>();
        for (String s : jd) {
            if (LANGUAGE_SKILLS.contains(s)) {
                jdLangs.add(s);
            }
        }

        boolean jdHasLanguage = !jdLangs.isEmpty();
        boolean resumeHasJdLangStrong = false;
        boolean resumeHasJdLangWeak = false;

        for (String lang : jdLangs) {
            if (strong.contains(lang)) {
                resumeHasJdLangStrong = true;
            }
            if (weak.contains(lang)) {
                resumeHasJdLangWeak = true;
            }
        }

        if (jdHasLanguage) {
            if (!resumeHasJdLangStrong && !resumeHasJdLangWeak) {
                // JD language missing completely
                baseScore = Math.min(baseScore, 55.0);
            } else if (!resumeHasJdLangStrong && resumeHasJdLangWeak) {
                // only weak mention (course/cert etc.)
                baseScore = Math.min(baseScore, 65.0);
            }
        }

        // Key skill cap (frameworks / tools / analyst / sales)
        Set<String> jdKeySkills = new HashSet<>();
        for (String s : jd) {
            if (KEY_IMPORTANT_SKILLS.contains(s)) {
                jdKeySkills.add(s);
            }
        }

        boolean hasAnyKeyInStrong = false;
        for (String key : jdKeySkills) {
            if (strong.contains(key)) {
                hasAnyKeyInStrong = true;
                break;
            }
        }

        if (!jdKeySkills.isEmpty() && !hasAnyKeyInStrong) {
            // JD has a key skill but none are strong in resume
            baseScore = Math.min(baseScore, 65.0);
        }

        // Cap "perfect" scores
        baseScore = Math.min(baseScore, 90.0);

        // Round to nearest integer
        return Math.round(baseScore);
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
     * NOTE: This does NOT change the underlying skill lists, only adjusts the score value slightly.
     */
    private double calibrateScoreWithProfile(
            AiSkillProfile profile,
            List<String> jdSkills,
            List<String> resumeSkills,
            double baseScore
    ) {
        if (profile == null || jdSkills == null || jdSkills.isEmpty()) {
            return baseScore;
        }

        // Normalize JD + strong skills
        Set<String> jd = normalizeSkills(jdSkills);
        Set<String> strong = normalizeSkills(profile.getStrongSkills());
        Set<String> weak = normalizeSkills(profile.getWeakSkills());

        // Count how many JD skills are STRONG matches
        Set<String> matchedStrong = new HashSet<>(jd);
        matchedStrong.retainAll(strong);

        double strongRatio = jd.isEmpty()
                ? 0.0
                : (double) matchedStrong.size() / (double) jd.size();

        double adjusted = baseScore;

        // 1) If candidate is not generally related at all,
        //    cap score so it can't look like a good match.
        if (!profile.isGenerallyRelated()) {
            adjusted = Math.min(adjusted, 35.0);
        }

        // 2) If strong coverage of JD skills is very low,
        //    avoid scores that look like 90%+ "perfect"
        if (strongRatio < 0.3 && adjusted > 75.0) {
            adjusted = 75.0;
        }

        // 3) Keep within 0–100 and round
        if (adjusted < 0.0) adjusted = 0.0;
        if (adjusted > 100.0) adjusted = 100.0;

        return Math.round(adjusted);
    }

    public AnalysisResponse analyze(AnalysisRequest request) {

        String jdText = request.getJobDescription();
        String resumeText = request.getResumeText();

        System.out.println("=== /api/analyze called ===");
        System.out.println("JD length: " + (jdText == null ? 0 : jdText.length()));
        System.out.println("Resume length: " + (resumeText == null ? 0 : resumeText.length()));

        // 1) PURE JAVA: deterministic skill extraction (single source of truth)
        List<String> jdSkills = extractSkills(jdText);
        List<String> resumeSkills = extractSkills(resumeText);

        System.out.println("Keyword jdSkills: " + jdSkills);
        System.out.println("Keyword resumeSkills: " + resumeSkills);

        // 2) OPTIONAL AI PROFILE: used only for calibration / RAG, not as source of skills
        AiSkillProfile aiProfile = aiService.analyzeSkillsWithAi(jdText, resumeText);
        System.out.println("AI profile returned: " + (aiProfile != null));

        if (aiProfile != null) {
            System.out.println("AI jdRequiredSkills (raw): " + aiProfile.getJdRequiredSkills());
            System.out.println("AI strongSkills (raw): " + aiProfile.getStrongSkills());
            System.out.println("AI weakSkills (raw): " + aiProfile.getWeakSkills());

            // Safety-check AI strong/weak skills: keep only ones that actually appear in resume text
            List<String> verifiedStrong = filterSkillsByResumeEvidence(aiProfile.getStrongSkills(), resumeText);
            List<String> verifiedWeak   = filterSkillsByResumeEvidence(aiProfile.getWeakSkills(), resumeText);

            aiProfile.setStrongSkills(verifiedStrong);
            aiProfile.setWeakSkills(verifiedWeak);

            System.out.println("AI strongSkills (verified): " + verifiedStrong);
            System.out.println("AI weakSkills (verified): " + verifiedWeak);
        } else {
            System.out.println("AI profile is null; using only keyword-based logic.");
        }

        // 3) Matched + missing (for display + AI tip/insights) – from deterministic Java skills
        List<String> matched = jdSkills.stream()
                .filter(resumeSkills::contains)
                .collect(Collectors.toList());

        List<String> missing = jdSkills.stream()
                .filter(skill -> !resumeSkills.contains(skill))
                .collect(Collectors.toList());

        // 4) Deterministic scoring using existing logic (Java-only skills)
        Set<String> jdSkillSet = new HashSet<>(jdSkills);
        Set<String> resumeSkillSet = new HashSet<>(resumeSkills);

        double score = calculateMatchScore(jdSkillSet, resumeSkillSet); // 0–100

        // Light calibration using AI profile (if available)
        if (aiProfile != null) {
            score = calibrateScoreWithProfile(aiProfile, jdSkills, resumeSkills, score);
        }

        String matchLevel = determineMatchLevel(score);

        // 5) Simple fallback tip (will be refined by AI)
        String tip;
        if (!missing.isEmpty()) {
            tip = "You are missing: " + missing;
        } else if (score == 0.0) {
            tip = "This job does not match your current skills. Consider roles closer to your background or start learning the core technologies in this JD.";
        } else {
            tip = "Great match! Emphasize your strengths.";
        }

        // 6) Build response object (this is the ground truth used by AiService)
        AnalysisResponse response = new AnalysisResponse();
        response.setScore(score);                 // percentage
        response.setMatchLevel(matchLevel);
        response.setJdSkills(jdSkills);
        response.setResumeSkills(resumeSkills);
        response.setMatchedSkills(matched);
        response.setMissingSkills(missing);
        response.setTip(tip);

        // 7) Let AI refine the tip based on these VERIFIED lists (AiService enforces rules)
        String improvedTip = aiService.generateImprovedTip(request, response);
        response.setTip(improvedTip);

        // 8) Enrich with summary + strengths + gaps + recommendations (also constrained by these lists)
        aiService.enrichWithInsights(request, response);

        return response;
    }

}
