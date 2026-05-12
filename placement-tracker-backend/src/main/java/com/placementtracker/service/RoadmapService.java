package com.placementtracker.service;

import com.placementtracker.dto.*;
import com.placementtracker.entity.RoadmapTopic;
import com.placementtracker.entity.Student;
import com.placementtracker.repository.RoadmapTopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoadmapService {
    
    private final RoadmapTopicRepository roadmapTopicRepository;
    
    public RoadmapService(RoadmapTopicRepository roadmapTopicRepository) {
        this.roadmapTopicRepository = roadmapTopicRepository;
    }
    
    /**
     * Generate domain-based roadmap for a student
     */
    public DomainRoadmapDTO generateRoadmapForDomain(Student student, String domain) {
        log.info("Generating roadmap for student {} with domain: {}", student.getId(), domain);
        
        DomainRoadmapDTO roadmap = new DomainRoadmapDTO();
        roadmap.setDomain(domain);
        roadmap.setSkillLevel(student.getSkillLevel() != null ? student.getSkillLevel() : "beginner");
        
        // Get phases based on domain
        List<RoadmapPhaseDTO> phases = generatePhasesForDomain(domain, roadmap.getSkillLevel());
        roadmap.setPhases(phases);
        
        // Calculate totals
        int totalTopics = phases.stream()
            .mapToInt(p -> p.getTopics().size())
            .sum();
        int totalDuration = phases.stream()
            .mapToInt(RoadmapPhaseDTO::getWeekDuration)
            .sum();
        
        roadmap.setTotalTopics(totalTopics);
        roadmap.setEstimatedDuration(totalDuration);
        roadmap.setTitle(getDomainTitle(domain));
        roadmap.setDescription(getDomainDescription(domain));
        
        // Calculate completion percentage
        List<RoadmapTopic> existingTopics = roadmapTopicRepository.findByStudent(student);
        int completedTopics = (int) existingTopics.stream()
            .filter(t -> "completed".equalsIgnoreCase(t.getStatus()))
            .count();
        roadmap.setCompletedTopics(completedTopics);
        roadmap.setCompletionPercentage(totalTopics > 0 ? (completedTopics * 100 / totalTopics) : 0);
        
        return roadmap;
    }
    
    /**
     * Generate phases for Full Stack Development
     */
    private List<RoadmapPhaseDTO> generateFullStackPhases(String skillLevel) {
        List<RoadmapPhaseDTO> phases = new ArrayList<>();
        
        // Phase 1: Frontend Fundamentals
        phases.add(createPhase(
            "Frontend Fundamentals",
            "Master HTML, CSS, and JavaScript basics",
            4,
            1,
            Arrays.asList(
                createTopic(null, "HTML5 & Semantic Markup", "Frontend", "Learn semantic HTML structure", "high"),
                createTopic(null, "CSS3 & Responsive Design", "Frontend", "Master CSS layouts and responsiveness", "high"),
                createTopic(null, "JavaScript Basics", "Frontend", "Variables, functions, and DOM manipulation", "high"),
                createTopic(null, "ES6+ Features", "Frontend", "Arrow functions, destructuring, async/await", "high")
            )
        ));
        
        // Phase 2: Frontend Framework
        phases.add(createPhase(
            "Frontend Framework (React)",
            "Build modern UIs with React",
            6,
            2,
            Arrays.asList(
                createTopic(null, "React Fundamentals", "Frontend", "Components, JSX, Props, State", "high"),
                createTopic(null, "React Hooks", "Frontend", "useState, useEffect, custom hooks", "high"),
                createTopic(null, "State Management", "Frontend", "Context API, Redux", "medium"),
                createTopic(null, "Routing", "Frontend", "React Router, navigation", "medium"),
                createTopic(null, "API Integration", "Frontend", "Fetch, Axios, data fetching", "high"),
                createTopic(null, "Testing React", "Frontend", "Jest, React Testing Library", "medium")
            )
        ));
        
        // Phase 3: Backend Fundamentals
        phases.add(createPhase(
            "Backend Fundamentals",
            "Learn server-side development with Node.js/Java",
            5,
            3,
            Arrays.asList(
                createTopic(null, "HTTP & REST APIs", "Backend", "HTTP methods, status codes, RESTful design", "high"),
                createTopic(null, "Node.js Basics", "Backend", "Event loop, modules, npm packages", "high"),
                createTopic(null, "Express.js", "Backend", "Routing, middleware, controllers", "high"),
                createTopic(null, "Authentication", "Backend", "JWT, sessions, OAuth", "high"),
                createTopic(null, "Error Handling", "Backend", "Middleware, error codes, logging", "medium")
            )
        ));
        
        // Phase 4: Database & ORM
        phases.add(createPhase(
            "Database & ORM",
            "Work with databases and ORMs",
            4,
            4,
            Arrays.asList(
                createTopic(null, "SQL Basics", "Database", "CREATE, SELECT, JOIN, aggregations", "high"),
                createTopic(null, "MongoDB", "Database", "Collections, documents, queries", "high"),
                createTopic(null, "ORM/ODM", "Database", "Sequelize, TypeORM, Mongoose", "medium"),
                createTopic(null, "Database Design", "Database", "Normalization, relationships, indexing", "high")
            )
        ));
        
        // Phase 5: Advanced Backend
        phases.add(createPhase(
            "Advanced Backend",
            "Master advanced backend concepts",
            4,
            5,
            Arrays.asList(
                createTopic(null, "Caching", "Backend", "Redis, caching strategies", "medium"),
                createTopic(null, "Message Queues", "Backend", "RabbitMQ, Kafka, pub/sub", "medium"),
                createTopic(null, "Microservices", "Backend", "Service architecture, communication", "medium"),
                createTopic(null, "Security", "Backend", "CORS, HTTPS, input validation, SQL injection", "high")
            )
        ));
        
        // Phase 6: DevOps & Deployment
        phases.add(createPhase(
            "DevOps & Deployment",
            "Deploy applications to production",
            3,
            6,
            Arrays.asList(
                createTopic(null, "Docker", "DevOps", "Containerization, Docker basics", "high"),
                createTopic(null, "Git & Version Control", "DevOps", "Branching, merging, collaboration", "high"),
                createTopic(null, "CI/CD", "DevOps", "GitHub Actions, deployment pipelines", "medium"),
                createTopic(null, "Cloud Platforms", "DevOps", "Heroku, AWS, DigitalOcean, Vercel", "high")
            )
        ));
        
        return phases;
    }
    
    /**
     * Generate phases for Machine Learning
     */
    private List<RoadmapPhaseDTO> generateMLPhases(String skillLevel) {
        List<RoadmapPhaseDTO> phases = new ArrayList<>();
        
        // Phase 1: Math Foundations
        phases.add(createPhase(
            "Math Foundations",
            "Master linear algebra, calculus, and statistics",
            5,
            1,
            Arrays.asList(
                createTopic(null, "Linear Algebra", "Math", "Matrices, vectors, eigenvalues", "high"),
                createTopic(null, "Calculus", "Math", "Derivatives, gradients, chain rule", "high"),
                createTopic(null, "Probability & Statistics", "Math", "Distributions, Bayes theorem, hypothesis testing", "high"),
                createTopic(null, "Optimization", "Math", "Gradient descent, convex optimization", "high")
            )
        ));
        
        // Phase 2: Python Programming
        phases.add(createPhase(
            "Python for ML",
            "Learn Python and essential ML libraries",
            4,
            2,
            Arrays.asList(
                createTopic(null, "Python Basics", "Programming", "Variables, functions, OOP", "high"),
                createTopic(null, "NumPy", "Libraries", "Arrays, matrix operations", "high"),
                createTopic(null, "Pandas", "Libraries", "Data manipulation and analysis", "high"),
                createTopic(null, "Matplotlib & Seaborn", "Libraries", "Data visualization", "medium")
            )
        ));
        
        // Phase 3: Machine Learning Basics
        phases.add(createPhase(
            "ML Fundamentals",
            "Learn core ML algorithms",
            6,
            3,
            Arrays.asList(
                createTopic(null, "Supervised Learning", "ML", "Regression, classification", "high"),
                createTopic(null, "Unsupervised Learning", "ML", "Clustering, dimensionality reduction", "high"),
                createTopic(null, "Decision Trees & Ensemble", "ML", "Random forests, gradient boosting", "high"),
                createTopic(null, "SVM & KNN", "ML", "Support vector machines, K-nearest neighbors", "medium"),
                createTopic(null, "Model Evaluation", "ML", "Accuracy, precision, recall, F1-score", "high")
            )
        ));
        
        // Phase 4: Deep Learning
        phases.add(createPhase(
            "Deep Learning",
            "Master neural networks and deep learning",
            7,
            4,
            Arrays.asList(
                createTopic(null, "Neural Network Basics", "Deep Learning", "Perceptrons, activation functions", "high"),
                createTopic(null, "CNNs", "Deep Learning", "Convolutional networks for images", "high"),
                createTopic(null, "RNNs & LSTM", "Deep Learning", "Recurrent networks for sequences", "high"),
                createTopic(null, "Transformers", "Deep Learning", "Attention mechanisms, BERT, GPT", "high"),
                createTopic(null, "TensorFlow & PyTorch", "Frameworks", "Deep learning frameworks", "high")
            )
        ));
        
        // Phase 5: Advanced Topics
        phases.add(createPhase(
            "Advanced ML Topics",
            "Explore cutting-edge ML techniques",
            5,
            5,
            Arrays.asList(
                createTopic(null, "NLP", "Advanced", "Text processing, embeddings, language models", "high"),
                createTopic(null, "Computer Vision", "Advanced", "Image classification, object detection", "high"),
                createTopic(null, "Reinforcement Learning", "Advanced", "Q-learning, policy gradients", "medium"),
                createTopic(null, "Transfer Learning", "Advanced", "Fine-tuning pre-trained models", "medium")
            )
        ));
        
        // Phase 6: Production & Deployment
        phases.add(createPhase(
            "ML in Production",
            "Deploy ML models to production",
            3,
            6,
            Arrays.asList(
                createTopic(null, "Model Serialization", "Deployment", "Saving and loading models", "high"),
                createTopic(null, "API Deployment", "Deployment", "Flask, FastAPI, REST APIs", "high"),
                createTopic(null, "MLOps", "Deployment", "Model versioning, monitoring, retraining", "medium"),
                createTopic(null, "Cloud ML", "Deployment", "AWS SageMaker, Google Cloud ML", "medium")
            )
        ));
        
        return phases;
    }
    
    /**
     * Generate phases for Cybersecurity
     */
    private List<RoadmapPhaseDTO> generateCybersecurityPhases(String skillLevel) {
        List<RoadmapPhaseDTO> phases = new ArrayList<>();
        
        // Phase 1: Networking & Fundamentals
        phases.add(createPhase(
            "Networking & Fundamentals",
            "Understand network protocols and basics",
            4,
            1,
            Arrays.asList(
                createTopic(null, "OSI Model", "Networking", "7-layer model and protocols", "high"),
                createTopic(null, "TCP/IP", "Networking", "Internet protocols", "high"),
                createTopic(null, "DNS & DHCP", "Networking", "Name resolution and IP allocation", "medium"),
                createTopic(null, "Firewall & Routing", "Networking", "Network security basics", "high")
            )
        ));
        
        // Phase 2: System Security
        phases.add(createPhase(
            "System Security",
            "Secure operating systems and servers",
            5,
            2,
            Arrays.asList(
                createTopic(null, "Linux Security", "Systems", "User management, permissions, hardening", "high"),
                createTopic(null, "Windows Security", "Systems", "Active Directory, UAC, security policies", "high"),
                createTopic(null, "SSH & VPN", "Systems", "Secure remote access", "high"),
                createTopic(null, "Privilege Escalation", "Systems", "Vulnerability and prevention", "medium")
            )
        ));
        
        // Phase 3: Cryptography
        phases.add(createPhase(
            "Cryptography",
            "Master encryption and data protection",
            5,
            3,
            Arrays.asList(
                createTopic(null, "Symmetric Encryption", "Crypto", "AES, DES, encryption algorithms", "high"),
                createTopic(null, "Asymmetric Encryption", "Crypto", "RSA, ECC, public key cryptography", "high"),
                createTopic(null, "Hashing", "Crypto", "MD5, SHA, password hashing", "high"),
                createTopic(null, "Digital Signatures", "Crypto", "Authentication and non-repudiation", "medium")
            )
        ));
        
        // Phase 4: Web Application Security
        phases.add(createPhase(
            "Web Application Security",
            "Secure web applications",
            5,
            4,
            Arrays.asList(
                createTopic(null, "OWASP Top 10", "Web Security", "Common web vulnerabilities", "high"),
                createTopic(null, "SQL Injection", "Web Security", "Detection and prevention", "high"),
                createTopic(null, "XSS & CSRF", "Web Security", "Cross-site attacks and mitigation", "high"),
                createTopic(null, "Authentication & Authorization", "Web Security", "Secure access control", "high"),
                createTopic(null, "HTTPS & SSL/TLS", "Web Security", "Secure communication", "high")
            )
        ));
        
        // Phase 5: Penetration Testing
        phases.add(createPhase(
            "Penetration Testing",
            "Learn ethical hacking and vulnerability assessment",
            5,
            5,
            Arrays.asList(
                createTopic(null, "Reconnaissance", "Pen Testing", "Information gathering", "high"),
                createTopic(null, "Scanning & Enumeration", "Pen Testing", "Nmap, vulnerability scanning", "high"),
                createTopic(null, "Exploitation", "Pen Testing", "Metasploit, exploitation techniques", "high"),
                createTopic(null, "Post-Exploitation", "Pen Testing", "Maintaining access, data exfiltration", "medium")
            )
        ));
        
        // Phase 6: Incident Response & Monitoring
        phases.add(createPhase(
            "Incident Response & Monitoring",
            "Detect and respond to security incidents",
            3,
            6,
            Arrays.asList(
                createTopic(null, "SIEM & Monitoring", "Monitoring", "Security information and event management", "high"),
                createTopic(null, "Log Analysis", "Monitoring", "Analyzing security logs", "high"),
                createTopic(null, "Incident Response", "Response", "Detection and response procedures", "high"),
                createTopic(null, "Forensics", "Forensics", "Digital forensics and investigation", "medium")
            )
        ));
        
        return phases;
    }
    
    /**
     * Generate phases for Cloud Engineering
     */
    private List<RoadmapPhaseDTO> generateCloudPhases(String skillLevel) {
        List<RoadmapPhaseDTO> phases = new ArrayList<>();
        
        // Phase 1: Cloud Fundamentals
        phases.add(createPhase(
            "Cloud Fundamentals",
            "Understand cloud computing concepts",
            3,
            1,
            Arrays.asList(
                createTopic(null, "Cloud Models", "Fundamentals", "IaaS, PaaS, SaaS", "high"),
                createTopic(null, "Regions & Availability Zones", "Fundamentals", "Infrastructure distribution", "high"),
                createTopic(null, "Cost Optimization", "Fundamentals", "Cloud pricing and ROI", "medium")
            )
        ));
        
        // Phase 2: Compute Services
        phases.add(createPhase(
            "Compute Services",
            "Master compute resources",
            4,
            2,
            Arrays.asList(
                createTopic(null, "EC2/Virtual Machines", "Compute", "Instance types, scaling", "high"),
                createTopic(null, "Containerization", "Compute", "Docker, container orchestration", "high"),
                createTopic(null, "Kubernetes", "Compute", "Container orchestration platform", "high"),
                createTopic(null, "Serverless", "Compute", "Lambda, Functions as a Service", "high")
            )
        ));
        
        // Phase 3: Storage & Databases
        phases.add(createPhase(
            "Storage & Databases",
            "Work with cloud storage and databases",
            4,
            3,
            Arrays.asList(
                createTopic(null, "Object Storage", "Storage", "S3, cloud object storage", "high"),
                createTopic(null, "Databases", "Storage", "RDS, NoSQL, DynamoDB", "high"),
                createTopic(null, "Data Warehousing", "Storage", "Redshift, BigQuery", "medium"),
                createTopic(null, "Backup & Recovery", "Storage", "Disaster recovery, backups", "high")
            )
        ));
        
        // Phase 4: Networking & Security
        phases.add(createPhase(
            "Networking & Security",
            "Secure cloud infrastructure",
            4,
            4,
            Arrays.asList(
                createTopic(null, "VPC & Networking", "Networking", "Virtual networks, subnets", "high"),
                createTopic(null, "IAM", "Security", "Identity and access management", "high"),
                createTopic(null, "Network Security", "Security", "Security groups, NACLs", "high"),
                createTopic(null, "DDoS Protection", "Security", "WAF, DDoS mitigation", "medium")
            )
        ));
        
        // Phase 5: Monitoring & DevOps
        phases.add(createPhase(
            "Monitoring & DevOps",
            "Monitor and automate cloud infrastructure",
            4,
            5,
            Arrays.asList(
                createTopic(null, "CloudWatch & Monitoring", "Monitoring", "Logging and monitoring", "high"),
                createTopic(null, "Infrastructure as Code", "DevOps", "Terraform, CloudFormation", "high"),
                createTopic(null, "CI/CD", "DevOps", "Continuous integration and deployment", "high"),
                createTopic(null, "Auto-scaling", "DevOps", "Scaling policies and automation", "high")
            )
        ));
        
        // Phase 6: Advanced Cloud Services
        phases.add(createPhase(
            "Advanced Cloud Services",
            "Explore specialized cloud services",
            3,
            6,
            Arrays.asList(
                createTopic(null, "AI/ML Services", "Advanced", "Cloud-based AI/ML services", "medium"),
                createTopic(null, "Analytics", "Advanced", "Data analytics and processing", "medium"),
                createTopic(null, "API Management", "Advanced", "API gateways and management", "medium")
            )
        ));
        
        return phases;
    }
    
    // Helper methods
    
    private RoadmapPhaseDTO createPhase(String name, String description, Integer duration, Integer order, List<RoadmapTopicDTO> topics) {
        return new RoadmapPhaseDTO(name, description, duration, order, topics);
    }
    
    private RoadmapTopicDTO createTopic(Long id, String name, String category, String description, String priority) {
        RoadmapTopicDTO topic = new RoadmapTopicDTO();
        topic.setId(id);
        topic.setTopicName(name);
        topic.setCategory(category);
        topic.setDescription(description);
        topic.setPriority(priority);
        topic.setStatus("not-started");
        topic.setCompletionPercentage(0);
        topic.setResources(new ArrayList<>());
        topic.setPracticeProblems(new ArrayList<>());
        return topic;
    }
    
    private List<RoadmapPhaseDTO> generatePhasesForDomain(String domain, String skillLevel) {
        return switch (domain.toLowerCase()) {
            case "fullstack", "full-stack" -> generateFullStackPhases(skillLevel);
            case "ml", "machine-learning" -> generateMLPhases(skillLevel);
            case "cybersecurity", "cyber-security" -> generateCybersecurityPhases(skillLevel);
            case "cloud", "cloud-engineering" -> generateCloudPhases(skillLevel);
            default -> new ArrayList<>();
        };
    }
    
    private String getDomainTitle(String domain) {
        return switch (domain.toLowerCase()) {
            case "fullstack", "full-stack" -> "Full Stack Web Development Roadmap";
            case "ml", "machine-learning" -> "Machine Learning Engineer Roadmap";
            case "cybersecurity", "cyber-security" -> "Cybersecurity Professional Roadmap";
            case "cloud", "cloud-engineering" -> "Cloud Engineering Roadmap";
            case "devops" -> "DevOps Engineer Roadmap";
            default -> domain + " Development Roadmap";
        };
    }
    
    private String getDomainDescription(String domain) {
        return switch (domain.toLowerCase()) {
            case "fullstack", "full-stack" -> "Comprehensive roadmap to become a full-stack web developer with expertise in frontend, backend, and DevOps.";
            case "ml", "machine-learning" -> "Complete journey to master machine learning from mathematics to production deployment.";
            case "cybersecurity", "cyber-security" -> "Professional cybersecurity roadmap covering networking, systems, cryptography, and penetration testing.";
            case "cloud", "cloud-engineering" -> "Master cloud platforms and infrastructure to become a cloud engineer.";
            case "devops" -> "Learn DevOps practices and tools for continuous integration, deployment, and infrastructure management.";
            default -> "Specialized roadmap for " + domain + " development.";
        };
    }
}
