package com.careercompass.careercompass.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CareerKnowledgeBase {

    private final List<KnowledgeSnippet> snippets;

    public CareerKnowledgeBase() {
        this.snippets = List.of(

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
                ),

                // ----------------
                // EXTRA RESUME / ATS / PROJECTS
                // ----------------
                new KnowledgeSnippet(
                        "resume-summary-fresher",
                        "Writing a clear resume summary as a fresher",
                        "resume",
                        List.of("resume summary", "objective", "fresher", "about"),
                        """
                                As a fresher, your resume summary should say three things quickly: your current degree, your target role, and your main skills or tools.
                                Avoid vague lines like "To work in a challenging environment"; instead, be specific about the kind of work you want to do.
                                Mention 2–3 core skills or tools that match the roles you are applying for, such as Java, SQL, or data analysis.
                                Keep it to 3–4 short lines and avoid buzzwords that do not add meaning.
                                The goal of the summary is to help a recruiter understand in 5 seconds who you are and what you are aiming for.
                                """
                ),
                new KnowledgeSnippet(
                        "resume-project-section",
                        "Optimizing the projects section on your resume",
                        "resume",
                        List.of("projects", "project section", "portfolio", "academic project"),
                        """
                                Your projects section should not read like a raw list of technologies; it should show what problem you solved and how.
                                For each project, mention one line of context, the tech stack, and 2–3 bullets focused on your personal contribution and impact.
                                Include at least one link such as a GitHub repository, deployed demo, or short case study document.
                                Put the most relevant projects for your target role at the top, even if they are college or personal projects.
                                This section is often more important than work experience for freshers, so give it real attention instead of treating it as filler.
                                """
                ),
                new KnowledgeSnippet(
                        "resume-skills-structure",
                        "Structuring the skills section for clarity",
                        "resume",
                        List.of("skills section", "tech stack", "tool list"),
                        """
                                A good skills section groups related tools instead of mixing everything into one long confusing line.
                                Use small categories such as Programming, Web, Databases, Cloud or Tools and list only technologies you can actually explain.
                                Avoid writing the same tool in multiple categories or adding buzzwords that you have only heard once.
                                Keep the section readable so that a recruiter or interviewer can quickly map your skills to their job description.
                                Remember that you can reinforce these skills later in your projects and experience bullets where you used them in context.
                                """
                ),
                new KnowledgeSnippet(
                        "resume-fresher-layout",
                        "Resume layout for freshers with little experience",
                        "resume",
                        List.of("fresher resume", "no experience", "college student"),
                        """
                                For freshers, the most effective resume order is usually Summary, Skills, Projects, Education, then Activities or Achievements.
                                Do not worry if you have no formal experience; instead, make your projects, coursework, and hackathons look like serious work.
                                Use a one-page layout with clean headings, equal margins, and consistent font sizes instead of creative but unreadable designs.
                                Remove long paragraphs and convert them into bullet points that show tools used and outcomes clearly.
                                A simple, clean resume that highlights 2–3 strong projects often beats a fancy template with no depth for entry-level roles.
                                """
                ),

                // ----------------
                // SOFTWARE / BACKEND PRACTICE
                // ----------------
                new KnowledgeSnippet(
                        "backend-rest-best-practices",
                        "REST API best practices for junior backend developers",
                        "skills",
                        List.of("rest api", "backend", "spring boot best practices"),
                        """
                                As a junior backend developer, focus on making your APIs predictable, consistent, and easy to understand.
                                Use clear naming for endpoints and HTTP methods, such as GET for fetching, POST for creating, PUT or PATCH for updating, and DELETE for removal.
                                Always return proper status codes and simple, meaningful error messages so that frontend developers know what went wrong.
                                Start with basic validation, logging, and simple security instead of trying to implement every advanced pattern you read about.
                                Over time, practise building small APIs end to end with documentation so that you can explain your design decisions confidently in interviews.
                                """
                ),
                new KnowledgeSnippet(
                        "backend-common-mistakes",
                        "Common mistakes in junior backend interviews",
                        "interview",
                        List.of("backend interview", "mistakes", "spring boot interview"),
                        """
                                Many juniors fail backend interviews not because they do not know anything, but because they skip fundamentals while chasing frameworks.
                                Weak understanding of HTTP, status codes, and database relationships often hurts more than not knowing an advanced annotation.
                                Another common mistake is being unable to explain a project architecture clearly at a high level before diving into code-level details.
                                Candidates also tend to guess answers instead of honestly stating what they know and how they would find out the rest.
                                Focusing on clarity, basic web concepts, and a few well-understood projects will usually take you further than memorizing rare annotations.
                                """
                ),
                new KnowledgeSnippet(
                        "git-workflow-basics",
                        "Basic Git and GitHub workflow for beginners",
                        "skills",
                        List.of("git", "github", "version control", "branches"),
                        """
                                At the start, learn a simple Git workflow instead of trying to master every command.
                                Practise creating a repository, making small commits with meaningful messages, and pushing code to GitHub.
                                Learn how to create and switch branches, merge them, and resolve simple conflicts without panicking.
                                When working with others, use pull requests with short descriptions of what you changed rather than pushing directly to main.
                                Consistent Git usage not only protects your work but also makes your portfolio look professional to potential employers.
                                """
                ),
                new KnowledgeSnippet(
                        "git-commit-messages",
                        "Writing useful commit messages",
                        "skills",
                        List.of("git", "commit message", "version history"),
                        """
                                A good commit message tells your future self and teammates what changed and why in one or two short lines.
                                Avoid messages like "update" or "final code"; instead, write things like "Add validation for empty job description" or "Fix null pointer in skill matching".
                                Group related changes into one commit rather than mixing many unrelated modifications into a single giant one.
                                Over time, a clean commit history becomes a narrative of your project that can even help you explain your work in interviews.
                                Treat commit messages as part of your communication skill, not just a Git requirement to get past the terminal.
                                """
                ),
                new KnowledgeSnippet(
                        "debugging-mindset",
                        "Building a systematic debugging mindset",
                        "skills",
                        List.of("debugging", "bug fixing", "problem solving"),
                        """
                                Debugging is not about random trial and error; it is about forming small hypotheses and testing them quickly.
                                Start by carefully reading error messages and logs instead of ignoring them and restarting the application repeatedly.
                                Reproduce the bug consistently with the smallest possible input so that you can see whether your changes actually help.
                                Add temporary logs or use a debugger to inspect variable values instead of guessing what the code might be doing.
                                Over time, a calm and structured approach to debugging will make you much more valuable than simply knowing more frameworks.
                                """
                ),
                new KnowledgeSnippet(
                        "code-review-basics",
                        "How to give and receive code review feedback",
                        "skills",
                        List.of("code review", "teamwork", "collaboration"),
                        """
                                In code reviews, your goal is to improve the code and share knowledge, not to win arguments or show off.
                                When reviewing others, focus on clarity, correctness, and potential long-term issues rather than personal preferences.
                                Ask questions like "What do you think about handling this edge case?" instead of giving blunt orders.
                                When your own code is reviewed, treat feedback as a learning opportunity rather than personal criticism.
                                Over time, respectful and focused code reviews help teams move faster while keeping quality under control.
                                """
                ),

                // ----------------
                // DSA / CS FUNDAMENTALS
                // ----------------
                new KnowledgeSnippet(
                        "dsa-prep-plan",
                        "Practical DSA preparation plan for interviews",
                        "skills",
                        List.of("dsa", "data structures", "algorithms", "coding interview"),
                        """
                                For most entry-level roles, you do not need to master every advanced algorithm; you need strong basics done well.
                                Focus on arrays, strings, hashing, recursion, linked lists, stacks, queues, and simple trees before trying hard dynamic programming.
                                Use a mix of topic-wise practice and actual timed mock tests on platforms like LeetCode, HackerRank, or similar.
                                Try to fully solve fewer problems with deep understanding instead of speed-solving dozens with no retention.
                                Track patterns you see repeatedly so that in interviews you can quickly recognize which technique to use rather than starting from zero each time.
                                """
                ),
                new KnowledgeSnippet(
                        "dsa-depth-vs-role",
                        "How much DSA depth is needed for different roles",
                        "skills",
                        List.of("dsa depth", "service company", "product company"),
                        """
                                The depth of DSA you need depends heavily on the kind of companies and roles you are targeting.
                                Many service and mid-sized companies care more about clean basic problem solving and project work than about advanced graph theory.
                                Large product-based companies and very competitive roles may require stronger DSA skills, especially in time and space optimization.
                                Instead of copying someone else's full roadmap, calibrate your DSA effort based on the companies and roles that realistically fit your current stage.
                                Combining moderate DSA strength with strong projects is often a better overall strategy than going extreme in only one direction.
                                """
                ),

                // ----------------
                // DATA & ANALYTICS EXTRAS
                // ----------------
                new KnowledgeSnippet(
                        "sql-interview-patterns",
                        "Common SQL interview question patterns",
                        "skills",
                        List.of("sql interview", "joins", "group by", "analytics"),
                        """
                                Many SQL interviews repeat a few common ideas such as filtering rows, joining tables, grouping data, and finding top or bottom values.
                                Practise writing queries that use inner joins, left joins, group by, having, and order by with realistic sample tables.
                                Work on questions that require thinking in steps, such as finding the second highest value or counting distinct users per day.
                                Focus on writing readable queries with clear aliases instead of trying to compress everything into one unreadable line.
                                Being able to explain why your query is correct is as important as getting the answer itself in an interview.
                                """
                ),
                new KnowledgeSnippet(
                        "data-storytelling",
                        "Data storytelling with dashboards",
                        "skills",
                        List.of("power bi", "tableau", "dashboard", "data storytelling"),
                        """
                                A dashboard is not just a collage of charts; it should help a decision-maker answer specific questions quickly.
                                Start by identifying three to five key questions your dashboard should answer, such as "Which product line is declining?" or "Which region is growing fastest?".
                                Choose chart types that match the questions instead of using fancy visuals that look attractive but confuse the message.
                                Provide filters and simple interactions, but avoid clutter that forces the viewer to think about controls instead of insights.
                                Always be prepared to explain one or two key decisions or actions that your dashboard could influence in a real business scenario.
                                """
                ),
                new KnowledgeSnippet(
                        "data-portfolio-ideas",
                        "Simple portfolio ideas for data roles",
                        "skills",
                        List.of("data portfolio", "case study", "sql project"),
                        """
                                You do not need access to secret company datasets to build a solid data portfolio.
                                Start with public datasets such as sales, ecommerce, or HR data and design your own business questions around them.
                                Build small case studies where you show the problem, the queries or transformations you used, and the final insights or recommendations.
                                Combine SQL notebooks, Excel files, and BI dashboards into a single GitHub repository or portfolio page with clear documentation.
                                Recruiters and managers are often more impressed by a few well-documented case studies than a long list of disconnected practice queries.
                                """
                ),

                // ----------------
                // FRESHERS / INTERNSHIPS / PROJECTS
                // ----------------
                new KnowledgeSnippet(
                        "fresher-get-internships",
                        "Finding internships with no prior experience",
                        "fresher",
                        List.of("internship", "no experience", "college", "summer internship"),
                        """
                                When you have no experience, the goal is to make your skills and effort visible in places where opportunities might appear.
                                Start by shortlisting smaller companies, startups, and local businesses rather than only applying to famous brands.
                                Reach out with short, personalized emails or LinkedIn messages that highlight your projects and the specific ways you can help.
                                Offer to work on small, time-bound tasks or trial periods where appropriate to reduce the risk for the company.
                                Even a short, unpaid or low-paid internship that gives real responsibilities is more valuable than months of only online courses.
                                """
                ),
                new KnowledgeSnippet(
                        "final-year-project-selection",
                        "Choosing a final-year project that helps your career",
                        "fresher",
                        List.of("final year project", "academic project", "mini project"),
                        """
                                A good final-year project should sit somewhere between what you enjoy and what your target roles expect.
                                Pick a domain such as backend, data, or fullstack and ensure your project uses tools and patterns that appear in real job descriptions.
                                Avoid ideas that are so broad that you cannot finish them or explain them clearly within the semester timeline.
                                Focus on building a project where you can show design decisions, trade-offs, and clear responsibilities rather than just a long feature list.
                                A well-chosen and well-executed project can serve as a major talking point in resumes, interviews, and LinkedIn posts.
                                """
                ),
                new KnowledgeSnippet(
                        "college-to-first-job-mindset",
                        "Mindset shift from college to first job",
                        "fresher",
                        List.of("first job", "college to corporate", "professionalism"),
                        """
                                In college, marks and passing exams are often the main focus, but in your first job the focus shifts to reliability and impact.
                                Showing up on time, communicating delays early, and being consistent with small tasks matters more than being brilliant once in a while.
                                Instead of waiting for detailed instructions, practise breaking down tasks into smaller steps and clarifying requirements proactively.
                                Learn to document your work in simple terms so that teammates and future you can understand what was done and why.
                                This mindset shift from "completing assignments" to "owning outcomes" is one of the biggest changes in the transition to work life.
                                """
                ),

                // ----------------
                // CAREER SWITCH SPECIFICS
                // ----------------
                new KnowledgeSnippet(
                        "switch-mech-to-it",
                        "Switching from mechanical or civil to IT roles",
                        "switch",
                        List.of("mechanical to it", "civil to it", "career switch", "non cs"),
                        """
                                When switching from a core branch like mechanical or civil into IT, you have to show that your interest and effort are consistent, not random.
                                Start with one programming language and a clear target role such as backend developer, data analyst, or QA.
                                Build projects that either solve real problems from your original domain or show general software skills that companies value.
                                Update your resume to frame your earlier degree as a strength that gives you domain understanding rather than something unrelated.
                                Many companies are open to such switches if you can show serious self-learning, projects, and the ability to explain your journey clearly.
                                """
                ),
                new KnowledgeSnippet(
                        "switch-support-to-dev",
                        "Moving from support roles into core development",
                        "switch",
                        List.of("support to developer", "production support", "career switch"),
                        """
                                If you are in a support role and want to move into development, your daily work might already give you useful context about systems and users.
                                Start by learning the main tech stack used by your current product so that your future projects are aligned with real-world systems.
                                Volunteer to automate small manual tasks or improve scripts rather than waiting only for official dev openings.
                                Build side projects that mirror parts of your company's architecture so you can explain both your support experience and coding skills in one story.
                                When applying out, highlight problem-solving, systems understanding, and your concrete code contributions rather than only saying you want to switch.
                                """
                ),
                new KnowledgeSnippet(
                        "freelancing-as-bridge",
                        "Using freelancing to bridge into full-time roles",
                        "switch",
                        List.of("freelancing", "upwork", "fiverr", "side projects"),
                        """
                                Freelancing can act as a bridge between self-learning and full-time work if used strategically.
                                Start with very small, low-risk projects that match your current skill level, even if the payment is modest.
                                Focus on delivering on time, communicating clearly, and collecting testimonials or portfolio pieces rather than only maximizing short-term earnings.
                                Present freelancing projects on your resume like any other experience, with clear problem, tools, and impact.
                                This shows recruiters that you can handle real clients, requirements, and deadlines even before your first official job.
                                """
                ),

                // ----------------
                // INTERVIEW EXTRAS
                // ----------------
                new KnowledgeSnippet(
                        "behavioral-story-bank",
                        "Building a behavioral interview story bank",
                        "interview",
                        List.of("behavioral interview", "star method", "hr questions"),
                        """
                                Instead of improvising in every behavioral interview, prepare a small bank of stories that you can reuse in different questions.
                                Pick real experiences that show themes like conflict resolution, learning from failure, leadership, teamwork, and ownership.
                                Structure each story using a simple format like STAR: situation, task, action, and result, and keep it under two minutes.
                                Practise saying these stories aloud so that they sound natural but still focused on your contribution and the outcome.
                                With a story bank ready, you will feel calmer and can adapt the same story to multiple questions without sounding repetitive.
                                """
                ),
                new KnowledgeSnippet(
                        "hr-round-basics",
                        "Do's and don'ts for HR interview rounds",
                        "interview",
                        List.of("hr round", "salary", "relocation", "attitude"),
                        """
                                HR rounds are usually checking for attitude, communication, and basic alignment rather than deep technical skills.
                                Answer honestly about relocation, notice periods, and constraints instead of giving answers that sound good but are not true.
                                Be clear but respectful when talking about salary expectations, and show that you understand you are entering at a fresher level.
                                Avoid speaking negatively about previous colleges, internships, companies, or colleagues, even if you had bad experiences.
                                A calm, open attitude with realistic expectations often matters more to HR than having perfect technical knowledge.
                                """
                ),
                new KnowledgeSnippet(
                        "take-home-assignment-strategy",
                        "Strategy for take-home coding or data assignments",
                        "interview",
                        List.of("take home assignment", "case study", "coding assignment"),
                        """
                                For take-home assignments, your goal is not only to solve the problem but also to show how you think and structure work.
                                Read the requirements twice and list them out before you start coding, so that you do not miss hidden expectations.
                                Aim for a clean, small solution with clear structure, comments, and a short README instead of trying to build an overcomplicated system.
                                If you cannot finish everything, be honest and mention what is done and what you would do next if you had more time.
                                Reviewers often care more about code quality, clarity, and communication than about squeezing in every extra feature.
                                """
                ),

                // ----------------
                // JOB SEARCH & MINDSET
                // ----------------
                new KnowledgeSnippet(
                        "cold-messages-linkedin",
                        "Writing effective cold messages on LinkedIn",
                        "job-search",
                        List.of("cold message", "linkedin", "referral", "networking"),
                        """
                                A good cold message is short, specific, and respectful of the other person's time.
                                Mention who you are, what role you are targeting, and one or two reasons you are reaching out to that person in particular.
                                Attach or link your resume or portfolio only when appropriate, and avoid pushing for immediate referrals in the first line.
                                Instead of copying one generic template to everyone, slightly customize each message to show genuine interest in their work or company.
                                Even if many people do not respond, a few meaningful conversations can create opportunities that random applications would never reach.
                                """
                ),
                new KnowledgeSnippet(
                        "handling-rejections",
                        "Handling job rejections and following up",
                        "job-search",
                        List.of("rejection", "follow up", "mindset", "resilience"),
                        """
                                Job rejections are normal, especially early in your career, but they can still feel discouraging if you take them personally.
                                Whenever possible, treat a rejection as data and ask politely if there is any feedback you can use to improve.
                                Review your resume, projects, and interview answers to see if there are common patterns in where things break down.
                                Instead of applying in random bursts, maintain a simple tracker so that you can see consistent effort and gradual improvement over time.
                                Remember that you only need a few 'yes' responses to change your situation, even if most applications say 'no'.
                                """
                ),
                new KnowledgeSnippet(
                        "job-search-30-60-90",
                        "Designing a 30-60-90 day job search plan",
                        "job-search",
                        List.of("job search plan", "30 60 90", "planning"),
                        """
                                A 30-60-90 day plan can make your job search feel more controlled and less chaotic.
                                In the first 30 days, focus on fixing your resume, LinkedIn, and portfolio while applying to a smaller set of well-chosen roles.
                                In the next 30 days, increase your application volume slightly and add networking activities such as LinkedIn messages or events.
                                In the final 30 days, refine based on what worked, double down on roles where you are getting some response, and plug clear skill gaps.
                                This structured approach helps you avoid burnout and gives you clear checkpoints to adjust your strategy instead of guessing in the dark.
                                """
                ),

                // ----------------
                // SOFT SKILLS & SELF-MANAGEMENT
                // ----------------
                new KnowledgeSnippet(
                        "time-management-learning",
                        "Time management for learning while studying or working",
                        "fresher",
                        List.of("time management", "consistency", "schedule"),
                        """
                                When you are balancing college, courses, or a job, the key is consistency rather than long, rare study sessions.
                                Block small, specific time slots in your week for learning and treat them like non-negotiable appointments.
                                Prioritize one or two learning goals at a time instead of trying to study five different topics in parallel with no depth.
                                Use simple tools like to-do lists or calendars instead of overcomplicating your planning system.
                                Over months, even one focused hour a day can create a huge difference compared to people who only study when motivation is high.
                                """
                ),
                new KnowledgeSnippet(
                        "imposter-syndrome-junior",
                        "Dealing with imposter syndrome as a beginner",
                        "fresher",
                        List.of("imposter syndrome", "confidence", "self doubt"),
                        """
                                Feeling like you are not good enough or do not belong is extremely common when you start in tech or switch careers.
                                Remember that most people only show their highlight reel on social media or LinkedIn, not their struggles or confusion.
                                Focus on tracking your small wins and improvements instead of constantly comparing yourself to others with years of experience.
                                When you feel stuck, talk to peers, mentors, or communities instead of trying to silently solve everything alone.
                                Over time, consistent practice and small successes will slowly replace self-doubt with grounded confidence.
                                """
                ),
                new KnowledgeSnippet(
                        "learning-roadmap-design",
                        "Designing your own learning roadmap for a new skill",
                        "skills",
                        List.of("learning roadmap", "self learning", "plan"),
                        """
                                When learning something new, start by defining a simple end goal, such as building a basic REST API, dashboard, or small app.
                                Break that goal into layers: fundamentals, tools, and one or two small projects that use those concepts together.
                                Avoid collecting too many courses; pick one or two high-quality resources and commit to finishing them.
                                Push yourself to build something slightly beyond your comfort zone so that you encounter and solve real problems.
                                Review and adjust your roadmap every few weeks based on what is working instead of feeling guilty about not following a rigid plan.
                                """
                ),


                new KnowledgeSnippet(
                        "backend-hiring-2024",
                        "Backend developer hiring expectations (2024+)",
                        "industry-update",
                        List.of("backend", "spring boot", "java", "docker"),
                        """
                        From 2024 onwards, many backend roles expect candidates to show basic deployment
                        and environment awareness, not just API development.
                        Even for freshers, recruiters value seeing one backend project run using Docker
                        or deployed to a cloud platform.
                        This shows understanding of how backend code behaves outside local machines.
                        """
                ),

                new KnowledgeSnippet(
                        "docker-baseline-2024",
                        "Docker as a baseline backend skill",
                        "industry-update",
                        List.of("docker", "backend", "deployment"),
                        """
                        Docker is increasingly treated as a baseline skill rather than an advanced DevOps tool.
                        Recruiters often expect candidates to at least containerize one project and explain
                        how Dockerfiles and images work at a high level.
                        Full Kubernetes expertise is not expected at entry level.
                        """
                ),

                new KnowledgeSnippet(
                        "cloud-exposure-vs-theory",
                        "Cloud exposure matters more than theory",
                        "industry-update",
                        List.of("aws", "cloud", "deployment"),
                        """
                        In recent hiring trends, hands-on cloud exposure matters more than theoretical knowledge.
                        Deploying one small project using services like EC2 or managed databases
                        is often valued more than listing multiple cloud services without experience.
                        """
                ),

                new KnowledgeSnippet(
                        "deployment-over-certificates",
                        "Deployment experience over certificates",
                        "industry-update",
                        List.of("fresher", "certification", "deployment"),
                        """
                        Many recruiters now prioritize real deployment experience over online certificates.
                        A single deployed project with clear documentation often carries more weight
                        than multiple course completion certificates.
                        """
                ),

                new KnowledgeSnippet(
                        "github-readme-2024",
                        "GitHub README importance for recruiters",
                        "industry-update",
                        List.of("github", "readme", "portfolio"),
                        """
                        Recruiters increasingly rely on GitHub READMEs to quickly assess a candidate’s project.
                        Clear setup steps, screenshots, and a short explanation of design decisions
                        significantly improve first impressions.
                        """
                ),

                new KnowledgeSnippet(
                        "resume-project-explain-2024",
                        "Explaining projects clearly on resumes",
                        "industry-update",
                        List.of("resume", "projects", "backend"),
                        """
                        Modern resumes are expected to briefly explain what problem a project solves,
                        not just list technologies.
                        Recruiters prefer 2–3 strong projects with clear explanations
                        over many shallow project listings.
                        """
                ),

                new KnowledgeSnippet(
                        "ats-keywords-2024",
                        "ATS keyword behavior updates",
                        "industry-update",
                        List.of("ats", "resume", "keywords"),
                        """
                        ATS systems increasingly combine keyword matching with contextual scoring.
                        This means keyword stuffing is less effective than placing skills naturally
                        within experience and project descriptions.
                        """
                ),

                new KnowledgeSnippet(
                        "ai-tools-awareness",
                        "AI tool awareness in tech hiring",
                        "industry-update",
                        List.of("ai", "chatgpt", "developer tools"),
                        """
                        Many teams now expect developers to be aware of AI tools like code assistants
                        without expecting deep AI expertise.
                        Knowing how to use AI responsibly for productivity is seen as a plus.
                        """
                ),

                new KnowledgeSnippet(
                        "system-design-fresher-2024",
                        "System design expectations for freshers",
                        "industry-update",
                        List.of("system design", "backend", "fresher"),
                        """
                        Freshers are not expected to design large systems,
                        but they are expected to explain basic flows such as request handling,
                        database interactions, and error scenarios.
                        """
                ),

                new KnowledgeSnippet(
                        "internship-weight-2024",
                        "Internship experience weight increase",
                        "industry-update",
                        List.of("internship", "experience", "fresher"),
                        """
                        Internships and real-world exposure have gained more importance in shortlisting.
                        Even short internships help recruiters assess workplace readiness better
                        than academic projects alone.
                        """
                ),

                new KnowledgeSnippet(
                        "documentation-skill-2024",
                        "Documentation as a core engineering skill",
                        "industry-update",
                        List.of("documentation", "backend", "engineering"),
                        """
                        Clear documentation is increasingly treated as a core engineering skill.
                        Recruiters value candidates who can explain APIs, setup steps,
                        and assumptions clearly in writing.
                        """
                ),

                new KnowledgeSnippet(
                        "sql-still-mandatory",
                        "SQL remains mandatory for backend roles",
                        "industry-update",
                        List.of("sql", "database", "backend"),
                        """
                        Despite new tools and frameworks, SQL remains a mandatory skill
                        for most backend roles.
                        Interviewers still test joins, indexes, and query reasoning.
                        """
                ),

                new KnowledgeSnippet(
                        "overengineering-penalty",
                        "Overengineering hurts junior candidates",
                        "industry-update",
                        List.of("overengineering", "backend", "architecture"),
                        """
                        Overengineering small projects is often viewed negatively for junior candidates.
                        Simple, well-explained solutions are preferred over complex architectures
                        that the candidate cannot clearly justify.
                        """
                ),

                new KnowledgeSnippet(
                        "testing-awareness-2024",
                        "Testing awareness expectations",
                        "industry-update",
                        List.of("testing", "unit tests", "backend"),
                        """
                        While deep testing expertise is not expected from freshers,
                        awareness of unit tests and basic testing principles
                        is increasingly expected in interviews.
                        """
                ),

                new KnowledgeSnippet(
                        "communication-over-buzzwords",
                        "Communication valued over buzzwords",
                        "industry-update",
                        List.of("communication", "interview", "resume"),
                        """
                        Clear communication and honest explanations are valued more
                        than resumes filled with buzzwords.
                        Interviewers prefer candidates who explain clearly
                        what they know and what they are still learning.
                        """
                ),

                new KnowledgeSnippet(
                        "portfolio-over-marks",
                        "Portfolio valued over academic marks",
                        "industry-update",
                        List.of("portfolio", "projects", "fresher"),
                        """
                        Academic marks alone are no longer strong differentiators.
                        A solid project portfolio with clear explanations
                        often has more impact during shortlisting.
                        """
                ),

                new KnowledgeSnippet(
                        "api-security-basics",
                        "Basic API security expectations",
                        "industry-update",
                        List.of("api security", "backend", "authentication"),
                        """
                        Basic understanding of API security concepts like authentication,
                        authorization, and input validation is increasingly expected.
                        Advanced security expertise is not required at entry level.
                        """
                ),

                new KnowledgeSnippet(
                        "learning-speed-2024",
                        "Learning speed matters in hiring",
                        "industry-update",
                        List.of("learning", "adaptability", "career"),
                        """
                        Hiring teams increasingly value learning speed and adaptability.
                        Showing how you learned a new tool or concept quickly
                        can be more impactful than claiming mastery.
                        """
                ),

                new KnowledgeSnippet(
                        "devops-lite",
                        "DevOps-lite expectations for developers",
                        "industry-update",
                        List.of("devops", "docker", "ci cd"),
                        """
                        Developers are now expected to have DevOps-lite awareness.
                        This includes understanding pipelines, environments,
                        and deployment flow without deep infrastructure expertise.
                        """
                ),

                new KnowledgeSnippet(
                        "career-clarity-2024",
                        "Career clarity helps shortlisting",
                        "industry-update",
                        List.of("career clarity", "resume", "job search"),
                        """
                        Candidates with a clear target role and consistent profile
                        are easier to shortlist than those applying everywhere.
                        Clear career direction reduces recruiter uncertainty.
                        """
                )

        );
    }

    public List<KnowledgeSnippet> getAllSnippets() {
        return snippets;
    }
}
