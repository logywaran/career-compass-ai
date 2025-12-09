package com.careercompass.careercompass.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CareerKnowledgeBase {

    private final List<KnowledgeSnippet> snippets;

    public CareerKnowledgeBase() {
        this.snippets = List.of(
                // ----------------
                // EXISTING SNIPPETS
                // ----------------
                new KnowledgeSnippet(
                        "resume-quantify",
                        "Write stronger resume bullets",
                        "resume",
                        List.of("resume", "achievements", "experience"),
                        """
                        When improving your resume, focus on quantifiable achievements instead of generic duties.
                        Start bullets with strong action verbs that show you did something, not just observed it.
                        Add numbers wherever possible, such as percentages, counts, time saved, or revenue impact.
                        Connect each bullet to a clear impact on the team, project, customer, or business outcome.
                        Even student projects or internships can be framed with scope, tools used, and measurable results.
                        This makes your profile stand out in crowded shortlists where everyone lists similar responsibilities.
                        """
                ),
                new KnowledgeSnippet(
                        "java-backend-roadmap",
                        "Java Backend Developer roadmap",
                        "skills",
                        List.of("java", "spring", "backend", "rest"),
                        """
                        For a Java Backend Developer path, build your foundation in Core Java concepts like OOP, collections, exceptions, generics, and multithreading.
                        Next, learn Spring Boot and practice building REST APIs with proper request handling, validation, and error responses.
                        Add database skills by learning SQL, joins, indexing, and writing queries in MySQL or PostgreSQL.
                        Use Git from day one and get comfortable with branching, pull requests, and basic CI or build pipelines.
                        Finally, take one small project from local to cloud by deploying on AWS or a similar platform, ideally using Docker.
                        This combination of Java, Spring Boot, SQL, and basic DevOps is enough for many entry-level backend roles.
                        """
                ),
                new KnowledgeSnippet(
                        "cloud-gap",
                        "Closing cloud and DevOps gaps",
                        "skills",
                        List.of("aws", "docker", "kubernetes", "cloud", "devops"),
                        """
                        To close cloud and DevOps gaps, start by containerizing one small API or web app using Docker and running it locally.
                        Learn the basics of a major cloud provider such as AWS, focusing on core services like EC2, RDS, and S3 instead of trying to learn everything.
                        Deploy a simple end-to-end project so that you understand how code moves from your laptop to a public URL.
                        Document your steps in a short README and include screenshots or links so recruiters can see your deployment story.
                        Mention this deployment experience clearly on your resume and LinkedIn, highlighting tools like Docker, AWS, and CI pipelines.
                        Over time, slowly add concepts like monitoring, logging, and basic security instead of trying to master full DevOps at once.
                        """
                ),
                new KnowledgeSnippet(
                        "interview-prep",
                        "Interview preparation structure",
                        "interview",
                        List.of("interview", "preparation", "behavioral"),
                        """
                        For interviews, prepare in three structured areas instead of reading random questions.
                        First, revise fundamentals such as DSA, OOP, and the core tools or frameworks that appear in your resume.
                        Second, prepare to explain your projects clearly by covering problem, approach, tools, and your personal contribution.
                        Third, create a small library of STAR stories for common behavioral themes like conflict, leadership, failure, and learning.
                        Practise speaking your answers aloud so they fit in one to two minutes rather than long, unfocused stories.
                        This combination of technical depth and clean storytelling makes you sound confident and intentional to interviewers.
                        """
                ),

                // ----------------
                // SOFTWARE & FULLSTACK CAREERS
                // ----------------
                new KnowledgeSnippet(
                        "fullstack-roadmap",
                        "Fullstack Developer roadmap",
                        "skills",
                        List.of("fullstack", "frontend", "backend", "react", "spring"),
                        """
                        For a fullstack developer path, first make sure your core web foundations in HTML, CSS, and JavaScript are solid.
                        On the frontend side, pick one framework such as React and learn components, state, routing, and API integration.
                        On the backend side, use a stack like Spring Boot or Node.js to build APIs, handle authentication, and connect to databases.
                        Build at least one end to end project where you design the screens, APIs, database tables, and deployment strategy yourself.
                        Keep the scope small but realistic, such as a job board, learning tracker, or simple CRM tool.
                        Document your project in a short case study so that recruiters can see not just the code but also your design thinking.
                        """
                ),

                new KnowledgeSnippet(
                        "testing-career-overview",
                        "Software testing and QA career path",
                        "skills",
                        List.of("testing", "qa", "manual testing", "automation"),
                        """
                        A testing or QA career is a great entry point into software for detail-oriented people who like breaking things safely.
                        Start with manual testing fundamentals such as test case design, exploratory testing, and reporting bugs clearly.
                        Learn basic SQL and API tools like Postman so you can test beyond just the UI layer.
                        Gradually move into automation by learning one scripting language, usually Java or Python, and a framework like Selenium or Cypress.
                        Build a small testing portfolio where you show test cases, bug reports, and a mini automation suite against a demo application.
                        Highlight your mindset of risk analysis, prioritization, and clear communication because these matter a lot in QA hiring.
                        """
                ),

                // ----------------
                // DATA & ANALYTICS CAREERS
                // ----------------
                new KnowledgeSnippet(
                        "data-analyst-roadmap",
                        "Data Analyst roadmap",
                        "skills",
                        List.of("data analyst", "excel", "sql", "power bi", "tableau"),
                        """
                        For a data analyst path, start by mastering spreadsheets using Excel or Google Sheets for cleaning, filtering, and basic analysis.
                        In parallel, learn SQL well enough to join tables, aggregate data, and answer business questions directly from databases.
                        Pick one visualization tool such as Power BI or Tableau and learn how to build dashboards with filters, drill downs, and clear charts.
                        Build two or three small case studies where you take a raw dataset, clean it, analyze it, and present insights as if to a manager.
                        Emphasize business questions and decisions rather than just showing graphs with no story behind them.
                        Put your projects on GitHub or a portfolio site and include them on your resume so that non-tech hiring managers can easily understand your impact.
                        """
                ),

                new KnowledgeSnippet(
                        "data-engineer-basics",
                        "Data Engineer foundations",
                        "skills",
                        List.of("data engineer", "etl", "pipeline", "big data"),
                        """
                        For data engineering, focus first on strong programming fundamentals in Python or Java and solid knowledge of SQL.
                        Learn how batch and streaming data flows work, including concepts like ETL, pipelines, and data warehouses.
                        Practise building simple pipelines that move data from one source to a cleaned table using tools or plain code.
                        When comfortable, explore one cloud data stack such as AWS with services like S3, Glue, and Redshift or their equivalents.
                        Build a small end to end project where data is ingested, transformed, and made available for reporting or analytics.
                        Make sure you can explain your design choices using words like reliability, scalability, and cost, even if your project is small.
                        """
                ),

                // ----------------
                // BUSINESS & NON-TECH CAREERS
                // ----------------
                new KnowledgeSnippet(
                        "business-analyst-overview",
                        "Business Analyst skills and path",
                        "skills",
                        List.of("business analyst", "requirements", "stakeholder", "process"),
                        """
                        As a business analyst, your value comes from understanding problems, clarifying requirements, and translating them into clear documentation.
                        Focus on skills like requirement gathering, process mapping, stakeholder communication, and basic data analysis.
                        Learn to write user stories, acceptance criteria, and simple diagrams that make it easy for tech and non tech teams to align.
                        Tools like Excel, PowerPoint, basic SQL, and one visualization tool are enough at the start.
                        Practise by taking a simple domain such as e commerce or banking and writing mock requirements for a feature end to end.
                        Highlight your communication, clarity, and ability to ask the right questions on your resume and in interviews.
                        """
                ),

                new KnowledgeSnippet(
                        "sales-marketing-entry",
                        "Starting in sales and marketing roles",
                        "skills",
                        List.of("sales", "marketing", "b2b", "b2c", "crm"),
                        """
                        For sales and marketing roles, show that you understand customers, markets, and how to communicate value clearly.
                        Develop comfort with tools like CRM systems, spreadsheets for tracking leads, and presentation tools for pitches.
                        Practise writing short, clear messages and emails that would make a real customer want to respond.
                        If you have no formal experience, create mini campaigns or outreach projects for a local business, college event, or personal project.
                        Quantify simple metrics such as leads contacted, responses received, or conversion rates, even if the numbers are small.
                        Emphasize your resilience, follow up discipline, and ability to learn product knowledge quickly because these are key in early sales roles.
                        """
                ),

                // ----------------
                // ATS & RESUME OPTIMIZATION
                // ----------------
                new KnowledgeSnippet(
                        "ats-basics",
                        "ATS friendly resume basics",
                        "resume",
                        List.of("ats", "applicant tracking", "resume format", "keywords"),
                        """
                        To pass ATS filters, keep your resume layout simple with clear headings like Experience, Education, and Skills.
                        Avoid heavy tables, complex columns, or graphics that can confuse automated parsers.
                        Use standard job relevant keywords from the job description in your skills and experience sections wherever they are genuinely true.
                        Write your tech stack in plain text instead of embedding it inside images or icons.
                        Save and upload your resume as PDF unless the employer specifically asks for a Word document.
                        Remember that ATS gets you past the first filter, but real impact comes from clear, quantified achievements once a human reads it.
                        """
                ),

                new KnowledgeSnippet(
                        "ats-targeted-keywords",
                        "Targeting keywords for ATS and recruiters",
                        "resume",
                        List.of("ats", "keywords", "tailor", "job description"),
                        """
                        Before applying, spend a few minutes scanning the job description for repeated skills, tools, and responsibilities.
                        Make sure these important keywords appear naturally in your resume where they are actually true for your experience.
                        Instead of copying the JD line by line, translate it into your own context using your projects, internships, or coursework.
                        Keep a master resume with everything and then create shorter, targeted versions for different role types like backend, data, or business analyst.
                        Over time you will notice patterns in what different companies prioritize, and you can preemptively highlight those strengths.
                        This small habit of tailoring improves both ATS matching and human recruiter interest without requiring a complete rewrite each time.
                        """
                ),

                // ----------------
                // FRESHERS & NO-EXPERIENCE
                // ----------------
                new KnowledgeSnippet(
                        "fresher-no-experience-strategy",
                        "Strategy for freshers with little or no experience",
                        "fresher",
                        List.of("fresher", "student", "no experience", "entry level"),
                        """
                        As a fresher, your job is to turn academics and small projects into a convincing story of potential.
                        Focus on two or three projects that show real skills, even if they started as college assignments.
                        Rewrite them to highlight problem, tools used, and outcomes instead of just listing technology names.
                        Take short internships, freelancing work, or volunteer roles where you can show responsibility and ownership.
                        In your resume summary, clearly mention the role you are targeting so recruiters do not have to guess.
                        Remember that clarity, consistency, and a few solid projects often beat a long list of random courses with no depth.
                        """
                ),

                new KnowledgeSnippet(
                        "fresher-project-portfolio",
                        "Building a simple portfolio as a fresher",
                        "fresher",
                        List.of("portfolio", "projects", "github", "student"),
                        """
                        A simple portfolio matters more for freshers than a long list of skills typed on the resume.
                        Pick two or three projects that are easy to explain and relevant to the roles you want, such as a job portal clone, dashboard, or basic CRM.
                        Host code on GitHub with a clean README that explains problem, tech stack, and key features in a few lines.
                        If possible, deploy at least one project so that you can share a live link along with the repository.
                        Add one screenshot per project inside the README to make it visually clear even for non technical recruiters.
                        This combination of code, documentation, and live demo makes you look serious and self driven compared to other freshers.
                        """
                ),

                // ----------------
                // CAREER SWITCHING
                // ----------------
                new KnowledgeSnippet(
                        "switch-to-software",
                        "Switching into software development from non-tech",
                        "switch",
                        List.of("career switch", "software", "developer", "coding"),
                        """
                        When switching into software from a non tech background, your goal is to prove consistent effort and real hands on practice.
                        Start with one programming language such as Java or Python and go deep rather than hopping between many.
                        Build small but complete projects that solve simple problems, even if they are internal tools or personal utilities.
                        Use your previous domain knowledge as a strength by creating projects that relate to your old industry when possible.
                        In your resume, be transparent about the switch and clearly show timeline, courses, and projects that support it.
                        Interviewers care more about your current skill level and seriousness than your original degree once you can demonstrate capability.
                        """
                ),

                new KnowledgeSnippet(
                        "switch-to-data",
                        "Switching into data and analytics roles",
                        "switch",
                        List.of("career switch", "data", "analytics", "data analyst"),
                        """
                        To switch into data and analytics, focus on skills that create visible business insights rather than only learning tools.
                        Start with Excel and SQL so you can answer real questions from raw data, such as trends, top performers, and problem areas.
                        Take a dataset related to your previous domain and build a small case study with questions, analysis, and charts.
                        Then learn one BI tool like Power BI or Tableau and recreate your analysis as an interactive dashboard.
                        On your resume, create a separate section for data projects and highlight impact, not just the dataset source.
                        This shows hiring managers that you can think like an analyst even if your previous job title was different.
                        """
                ),

                // ----------------
                // INTERVIEW SPECIFIC SNIPPETS
                // ----------------
                new KnowledgeSnippet(
                        "junior-dev-interview-answers",
                        "Answering junior developer interview questions",
                        "interview",
                        List.of("junior developer", "entry level", "interview answer"),
                        """
                        For junior developer interviews, focus on clear, simple explanations instead of trying to sound overly advanced.
                        When asked about a project, explain the problem, your approach, and what you personally implemented.
                        If you do not know an answer, say what you do understand and how you would quickly find the missing information.
                        Prepare a few examples of bugs you faced and how you debugged them, because this shows practical thinking.
                        Keep your answers to one or two minutes so that the interviewer has space to ask follow up questions.
                        This makes you come across as thoughtful, honest, and easy to work with rather than just memorizing definitions.
                        """
                ),

                new KnowledgeSnippet(
                        "data-analyst-interview",
                        "Interview preparation for data analyst roles",
                        "interview",
                        List.of("data analyst interview", "case study", "sql questions"),
                        """
                        For data analyst interviews, expect a mix of SQL questions, scenario based analysis, and communication checks.
                        Practise writing SQL queries on paper or a simple editor without autocomplete so that you can think clearly under pressure.
                        Work through small case studies where you are given a business problem and a dataset and asked to find trends or recommendations.
                        Practise explaining charts as if you are talking to a non technical manager, focusing on insights and decisions, not tool features.
                        Prepare two or three examples where you handled messy data, conflicting requirements, or ambiguous questions.
                        These stories help interviewers trust that you can handle real world data situations, not just clean textbook examples.
                        """
                ),

                // ----------------
                // JOB SEARCH & LINKEDIN
                // ----------------
                new KnowledgeSnippet(
                        "job-search-strategy",
                        "Practical job search strategy",
                        "job-search",
                        List.of("job search", "applications", "networking", "referrals"),
                        """
                        A smart job search is about focused consistency rather than sending hundreds of random applications in one day.
                        Shortlist a few role types and industries that fit your skills and interests instead of applying to everything.
                        For each target role, maintain a simple tracker of companies, applications, status, and follow up dates.
                        Spend part of your time messaging people on LinkedIn, attending events, or joining communities instead of only filling forms.
                        Customize your resume and first message slightly for each opportunity so that it does not feel copy pasted.
                        Over a few weeks, this steady and organized approach usually gives better results than one time application bursts.
                        """
                ),

                new KnowledgeSnippet(
                        "linkedin-optimization",
                        "Optimizing LinkedIn profile for job search",
                        "job-search",
                        List.of("linkedin", "profile", "headline", "about section"),
                        """
                        Your LinkedIn profile should quickly tell a stranger what role you are aiming for and what skills you bring.
                        Write a clear headline that mentions your target role and core skills instead of only your current degree or college.
                        In the About section, write a short paragraph that covers your background, main tools, and what kind of problems you like solving.
                        Add project links, GitHub repos, or case studies in the Featured section so that recruiters can see proof of work in one click.
                        Keep your experience section consistent with your resume and avoid big gaps in dates without a simple explanation.
                        Regularly post or share small learnings, project updates, or reflections so that your profile looks active and serious to hiring managers.
                        """
                )
        );
    }

    public List<KnowledgeSnippet> getAllSnippets() {
        return snippets;
    }
}
