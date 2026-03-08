# Placement Tracker Frontend

AI-powered placement preparation platform built with React and Clerk authentication.

## Features

- 📊 Interactive dashboard with progress tracking
- 🎯 Personalized learning roadmap
- 🏢 Dream company selection with crack probability
- 🎬 AI-powered mock interviews
- 🤖 Intelligent recommendations and motivation
- 👤 Complete profile management

## Setup

### Prerequisites
- Node.js 16+ and npm

### Installation

1. Install dependencies:
```bash
npm install
```

2. Create `.env.local`:
```
VITE_CLERK_PUBLISHABLE_KEY=pk_test_bmVlZGVkLWRvZy0wLmNsZXJrLmFjY291bnRzLmRldiQ
VITE_API_URL=http://localhost:8080/api
```

3. Start development server:
```bash
npm run dev
```

The app will run at http://localhost:3000

## Project Structure

```
src/
├── components/     # Reusable UI components
├── pages/         # Page components
├── services/      # API services
├── store/         # Zustand state management
├── hooks/         # Custom React hooks
└── App.jsx        # Main app component
```

## Technology Stack

- React 18
- Vite
- Tailwind CSS
- Recharts (visualizations)
- Clerk AI (authentication)
- Zustand (state management)
