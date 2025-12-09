package com.careercompass.careercompass.service;

import java.util.List;

public class KnowledgeSnippet {

    private final String id;
    private final String topic;        // e.g. "Java Backend Roadmap"
    private final String category;     // e.g. "skills", "resume", "interview"
    private final List<String> keywords;
    private final String adviceText;

    public KnowledgeSnippet(String id,
                            String topic,
                            String category,
                            List<String> keywords,
                            String adviceText) {
        this.id = id;
        this.topic = topic;
        this.category = category;
        this.keywords = keywords;
        this.adviceText = adviceText;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getAdviceText() {
        return adviceText;
    }
}
