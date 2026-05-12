# Domain-Based Roadmap System

## Overview
This document describes the domain-based personalized roadmap feature that allows students to select a learning path based on their career goals (Full Stack, ML, Cybersecurity, Cloud, DevOps).

## Features

### 1. **Domain Selection**
Students can choose from 5 specialized domains:
- **Full Stack Development**: Master frontend, backend, and deployment
- **Machine Learning**: Build intelligent systems with ML algorithms
- **Cybersecurity**: Secure systems and networks
- **Cloud Engineering**: Design and manage cloud infrastructure
- **DevOps**: Automate and optimize deployments

### 2. **Personalized Roadmap Generation**
Each domain has a structured roadmap with:
- **Multiple Phases**: Progressive learning stages
- **Topics**: Specific skills to master in each phase
- **Resources**: Learning materials and practice problems
- **Priority Levels**: High, Medium, Low priority indicators
- **Time Estimates**: Weeks required per phase

### 3. **Progress Tracking**
- Track completion of individual topics
- Mark topics as "Not Started", "In Progress", or "Completed"
- View overall roadmap completion percentage
- Monitor completed topics count

## Backend Architecture

### Entities

#### `Student.java`
Added field:
```java
private String domain; // fullstack, ml, cyber-security, cloud, devops, etc.
```

### DTOs

#### `DomainRoadmapDTO`
Main DTO for domain-based roadmaps containing:
- Domain name
- Title and description
- Skill level (beginner, intermediate, advanced)
- Estimated duration in weeks
- Phases list
- Completion percentage

#### `RoadmapPhaseDTO`
Represents a learning phase with:
- Phase name and description
- Week duration
- Order (phase number)
- Topics list

#### `RoadmapTopicDTO`
Individual topic with:
- Topic name and description
- Category
- Priority level
- Status (not-started, in-progress, completed)
- Completion percentage
- Resources list
- Practice problems list

### Services

#### `RoadmapService.java`
Core service for roadmap generation with methods:

```java
// Generate roadmap for a domain
public DomainRoadmapDTO generateRoadmapForDomain(Student student, String domain)

// Generate phases for each domain
private List<RoadmapPhaseDTO> generateFullStackPhases(String skillLevel)
private List<RoadmapPhaseDTO> generateMLPhases(String skillLevel)
private List<RoadmapPhaseDTO> generateCybersecurityPhases(String skillLevel)
private List<RoadmapPhaseDTO> generateCloudPhases(String skillLevel)
```

**Roadmap Structure**:

1. **Full Stack Development** (26 weeks)
   - Phase 1: Frontend Fundamentals (4 weeks)
   - Phase 2: Frontend Framework - React (6 weeks)
   - Phase 3: Backend Fundamentals (5 weeks)
   - Phase 4: Database & ORM (4 weeks)
   - Phase 5: Advanced Backend (4 weeks)
   - Phase 6: DevOps & Deployment (3 weeks)

2. **Machine Learning** (30 weeks)
   - Phase 1: Math Foundations (5 weeks)
   - Phase 2: Python for ML (4 weeks)
   - Phase 3: ML Fundamentals (6 weeks)
   - Phase 4: Deep Learning (7 weeks)
   - Phase 5: Advanced Topics (5 weeks)
   - Phase 6: ML in Production (3 weeks)

3. **Cybersecurity** (27 weeks)
   - Phase 1: Networking & Fundamentals (4 weeks)
   - Phase 2: System Security (5 weeks)
   - Phase 3: Cryptography (5 weeks)
   - Phase 4: Web Application Security (5 weeks)
   - Phase 5: Penetration Testing (5 weeks)
   - Phase 6: Incident Response (3 weeks)

4. **Cloud Engineering** (22 weeks)
   - Phase 1: Cloud Fundamentals (3 weeks)
   - Phase 2: Compute Services (4 weeks)
   - Phase 3: Storage & Databases (4 weeks)
   - Phase 4: Networking & Security (4 weeks)
   - Phase 5: Monitoring & DevOps (4 weeks)
   - Phase 6: Advanced Services (3 weeks)

### Controller

#### `RoadmapController.java`
REST API endpoints:

```
GET /roadmap/domain/{domain}
- Get domain-based roadmap for current student
- Headers: X-Clerk-ID

POST /roadmap/set-domain
- Set student's preferred domain
- Params: domain (fullstack, ml, cybersecurity, cloud, devops)
- Headers: X-Clerk-ID

GET /roadmap/current-domain
- Get student's current domain
- Headers: X-Clerk-ID

GET /roadmap/available-domains
- Get list of available domains (public endpoint)

PUT /roadmap/topic/{topicId}/status
- Update topic completion status
- Params: status (not-started, in-progress, completed)
- Headers: X-Clerk-ID

GET /roadmap/progress
- Get roadmap progress for current domain
- Headers: X-Clerk-ID
```

## Frontend Architecture

### Components

#### `DomainSelector.jsx`
- Displays available domains with:
  - Domain icon and title
  - Description
  - Estimated duration
  - Visual cards with gradient backgrounds
  - Selection indicator (checkmark)
- Handles domain selection and API calls
- Shows success/error messages

#### `RoadmapViewer.jsx`
- Displays complete roadmap with:
  - Roadmap header with title and stats
  - Progress bar showing completion percentage
  - Expandable phase cards
  - Topic items with status selector
  - Priority indicators
  - Resources and practice problems links
- Features:
  - Collapsible phases
  - Inline status update dropdown
  - Color-coded status indicators
  - Responsive design

### Updated Pages

#### `Roadmap.jsx`
- Integrates both domain selector and roadmap viewer
- Conditional rendering based on domain selection
- Domain switcher button
- Combines old roadmap with new domain-based system

### API Integration

Updated `api.js` with new methods:
```javascript
roadmapService.getDomainRoadmap(domain)
roadmapService.setStudentDomain(domain)
roadmapService.getCurrentDomain()
roadmapService.getAvailableDomains()
roadmapService.updateTopicStatus(topicId, status)
roadmapService.getRoadmapProgress()
```

## Database Schema

Added to `students` table:
```sql
domain VARCHAR(100) -- Added domain field
```

Index added:
```sql
INDEX (domain)
```

## API Response Examples

### GET /roadmap/domain/fullstack
```json
{
  "domain": "fullstack",
  "title": "Full Stack Web Development Roadmap",
  "description": "Comprehensive roadmap to become a full-stack web developer with expertise in frontend, backend, and DevOps.",
  "skillLevel": "beginner",
  "estimatedDuration": 26,
  "completionPercentage": 25,
  "totalTopics": 24,
  "completedTopics": 6,
  "phases": [
    {
      "phaseName": "Frontend Fundamentals",
      "description": "Master HTML, CSS, and JavaScript basics",
      "weekDuration": 4,
      "order": 1,
      "topics": [
        {
          "id": 1,
          "topicName": "HTML5 & Semantic Markup",
          "category": "Frontend",
          "description": "Learn semantic HTML structure",
          "priority": "high",
          "status": "completed",
          "completionPercentage": 100,
          "resources": ["https://..."],
          "practiceProblems": ["https://..."]
        },
        ...
      ]
    },
    ...
  ]
}
```

### GET /roadmap/available-domains
```json
{
  "domains": ["fullstack", "ml", "cybersecurity", "cloud", "devops"],
  "descriptions": {
    "fullstack": "Full Stack Web Development",
    "ml": "Machine Learning Engineer",
    "cybersecurity": "Cybersecurity Professional",
    "cloud": "Cloud Engineer",
    "devops": "DevOps Engineer"
  }
}
```

### GET /roadmap/progress
```json
{
  "domain": "fullstack",
  "completionPercentage": 25,
  "completedTopics": 6,
  "totalTopics": 24,
  "estimatedDuration": 26
}
```

## User Flow

1. **Initial Setup**
   - Student lands on Roadmap page
   - If no domain selected, `DomainSelector` displays
   - Student selects a domain

2. **Domain Selected**
   - Domain is saved to student profile
   - `RoadmapViewer` displays the personalized roadmap
   - Student can see all phases and topics

3. **Progress Tracking**
   - Student selects dropdown to change topic status
   - Status updates in real-time
   - Progress bar updates
   - Completion percentage recalculates

4. **Domain Change**
   - Student clicks "Change Domain" button
   - `DomainSelector` appears again
   - Selecting new domain refreshes the roadmap

## Styling

### Responsive Design
- Mobile-first approach with media queries
- Cards scale appropriately on smaller screens
- Touch-friendly buttons and selectors
- Grid layouts adapt to screen size

### Color Scheme
- **High Priority**: Red (#ef4444)
- **Medium Priority**: Orange (#f59e0b)
- **Low Priority**: Blue (#3b82f6)
- **Completed**: Green (#10b981)
- **In Progress**: Orange (#f59e0b)
- **Not Started**: Gray (#9ca3af)

### Animations
- Smooth transitions on hover
- Pop-in animation for selection checkmark
- Expandable/collapsible phase cards
- Progress bar fill animation

## Future Enhancements

1. **AI-Powered Recommendations**
   - Suggest topics based on weakness areas
   - Auto-adjust roadmap based on progress speed

2. **Resource Library**
   - Curated learning resources per topic
   - Video tutorials, articles, practice sets
   - Community recommendations

3. **Milestone Badges**
   - Gamification with badges for milestones
   - Leaderboards and achievements

4. **Adaptive Learning**
   - Adjust difficulty based on skill level
   - Skip topics based on existing knowledge

5. **Expert Reviews**
   - Get feedback from mentors
   - Personalized coaching notes

6. **Interview Preparation**
   - Link roadmap topics to mock interviews
   - Company-specific roadmaps

## Configuration

To customize roadmaps or add new domains:

1. Add new domain case in `RoadmapService.java` switch statement
2. Create new phase generation method following the pattern
3. Add domain to frontend domain info mapping
4. Add domain to available domains list in controller

## Testing

### Unit Tests
- Test roadmap generation for each domain
- Validate phase structure
- Check topic count and duration calculations

### Integration Tests
- Test API endpoints
- Test database operations
- Verify domain persistence

### E2E Tests
- Domain selection flow
- Roadmap display and interaction
- Status update workflow
- Domain switching

## Deployment

1. Run database migrations to add `domain` column
2. Deploy backend changes
3. Clear frontend cache
4. Deploy frontend changes
5. Test domain selection flow

## Troubleshooting

### Domain Not Persisting
- Check X-Clerk-ID header is being sent
- Verify student exists in database
- Check for constraint violations

### Roadmap Not Loading
- Verify domain is supported
- Check API endpoint returns data
- Review browser console for errors

### Status Updates Not Working
- Verify topic ID is valid
- Check status value is correct (not-started, in-progress, completed)
- Verify X-Clerk-ID header

## Support

For issues or feature requests, contact the development team or create an issue in the project repository.
