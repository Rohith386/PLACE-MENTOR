# ⚡ Quick Reference Guide

## 🏃 Getting Started in 5 Minutes

### Step 1: Open Terminal 1 - Frontend
```bash
cd placement-tracker-frontend
npm install
npm run dev
```
✅ Frontend ready at: `http://localhost:3000`

### Step 2: Open Terminal 2 - Backend
```bash
cd placement-tracker-backend
mvn clean install
mvn spring-boot:run
```
✅ Backend ready at: `http://localhost:8080/api`

### Step 3: Setup Database
```bash
mysql -u root -p
CREATE DATABASE placement_tracker;
USE placement_tracker;
SOURCE placement-tracker-backend/src/main/resources/schema.sql;
```
✅ Database ready with sample data

### Step 4: Sign In
- Go to http://localhost:3000
- Sign up with Clerk
- Create your profile
- Select dream companies
- Start preparing! 🚀

---

## 🎯 Key File Locations

### Frontend Files
| File | Purpose |
|------|---------|
| `src/App.jsx` | Main app component & routing |
| `src/pages/Dashboard.jsx` | Main dashboard |
| `src/services/api.js` | API client setup |
| `src/store/studentStore.js` | Global state |
| `.env.local` | Clerk key config |

### Backend Files
| File | Purpose |
|------|---------|
| `PlacementTrackerApplication.java` | App entry point |
| `config/SecurityConfig.java` | Security settings |
| `ai/AgenticAIGuideService.java` | AI recommendations |
| `controller/StudentController.java` | Student endpoints |
| `application.yml` | Database config |

---

## 🔌 API Quick Reference

### Get Student Profile
```bash
curl -X GET http://localhost:8080/api/students/profile \
  -H "X-Clerk-ID: user_123"
```

### Get AI Recommendations
```bash
curl -X GET http://localhost:8080/api/ai/recommendations \
  -H "X-Clerk-ID: user_123"
```

### Get All Companies
```bash
curl -X GET http://localhost:8080/api/companies
```

### Get Motivational Message
```bash
curl -X GET http://localhost:8080/api/ai/motivation \
  -H "X-Clerk-ID: user_123"
```

---

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Find and kill process on port 3000/8080
lsof -ti:3000 | xargs kill -9
lsof -ti:8080 | xargs kill -9
```

### MySQL Connection Failed
```bash
# Check MySQL is running
mysql -u root -p

# If not installed, install MySQL 8.0+
# Windows: Download from mysql.com
# Mac: brew install mysql
# Linux: apt-get install mysql-server
```

### Clerk Key Not Working
- Verify `.env.local` has correct key
- Key: `pk_test_bmVlZGVkLWRvZy0wLmNsZXJrLmFjY291bnRzLmRldiQ`
- Restart frontend after changing

### API Not Responding
- Check backend is running on 8080
- Check CORS is enabled in SecurityConfig
- Look for errors in backend logs

---

## 📊 Sample Data

### Pre-loaded Companies
1. Google (45% base probability)
2. Amazon (55%)
3. Microsoft (50%)
4. Meta (48%)
5. Apple (40%)
6. Tesla (35%)
7. Netflix (42%)
8. LinkedIn (50%)

### Sample Topics (DSA)
- Arrays
- Strings
- Linked Lists
- Trees
- Graphs
- Dynamic Programming
- Sorting
- Hashing

---

## 🤖 AI Features Demo

### Test Recommendations
1. Create student profile
2. Select 2-3 dream companies
3. Check AI recommendations endpoint
4. See personalized suggestions

### Test Probabilities
1. View dashboard (initial: 50%)
2. Complete some topics
3. Check companies again
4. See probability increase

### Test Motivation
1. Call motivation endpoint
2. Get contextual message based on progress
3. Each call returns different message

---

## 📱 Component Hierarchy

```
App
├── Layout
│   ├── Sidebar (Navigation)
│   └── MainContent
│       ├── Dashboard
│       ├── Roadmap
│       ├── DreamCompanies
│       ├── MockInterview
│       └── Profile
└── Auth (Clerk)
```

---

## 🗄️ Database Quick Stats

- **5 Tables** created automatically
- **8 Companies** pre-loaded
- **12+ DSA Topics** pre-loaded
- **0 Users** initially (grows with signups)

---

## 🔑 Environment Variables

### Frontend Required
```
VITE_CLERK_PUBLISHABLE_KEY=pk_test_...
VITE_API_URL=http://localhost:8080/api
```

### Backend Required
```
MYSQL_URL=jdbc:mysql://localhost:3306/placement_tracker
MYSQL_USER=root
MYSQL_PASSWORD=your_password
```

---

## 📈 Monitoring

### Frontend Logs
- Check browser console (F12)
- Network tab for API calls
- React DevTools extension

### Backend Logs
- Spring Boot logs in terminal
- Check application.yml logging level
- Database queries visible if `show-sql: true`

---

## 🚨 Important Notes

⚠️ **Before Going to Production:**
- [ ] Change database credentials
- [ ] Update Clerk to production keys
- [ ] Enable HTTPS
- [ ] Set CORS to actual domain
- [ ] Review security configs
- [ ] Add rate limiting
- [ ] Set up logging/monitoring
- [ ] Create backup strategy

---

## 🆘 Emergency Commands

```bash
# Reset everything
rm -rf node_modules package-lock.json
npm install

# Clear database
mysql -u root -p
DROP DATABASE placement_tracker;
CREATE DATABASE placement_tracker;

# Kill stuck processes
pkill -f "npm run dev"
pkill -f "java -jar"

# Check ports
netstat -tuln | grep 3000
netstat -tuln | grep 8080
```

---

## 📚 Documentation

| Document | Content |
|----------|---------|
| README.md | Project overview |
| SETUP_GUIDE.md | Installation guide |
| API_DOCUMENTATION.md | API endpoints |
| DATABASE_SCHEMA.md | DB structure |
| PROJECT_SUMMARY.md | Complete details |
| This file | Quick reference |

---

## 💬 What to Try First

1. **Sign up** → See profile page
2. **Select companies** → See probability chart
3. **Check dashboard** → See AI recommendations
4. **View roadmap** → Mark topics as done
5. **Test API** → Call endpoints with curl
6. **Check database** → Query MySQL tables

---

## 🎓 Learning Path

1. Read `README.md`
2. Follow `SETUP_GUIDE.md`
3. Explore `API_DOCUMENTATION.md`
4. Review `DATABASE_SCHEMA.md`
5. Check source code in `src/`
6. Run locally and test
7. Deploy to production

---

**Last Updated:** January 31, 2024
**Version:** 1.0.0
**Status:** ✅ Ready to Use

Happy Learning! 🚀
