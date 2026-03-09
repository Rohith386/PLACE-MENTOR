import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const api = axios.create({
  baseURL: API_BASE_URL,
})

// Add Clerk ID to every request
api.interceptors.request.use(async (config) => {
  try {
    // Get Clerk user ID from window.Clerk if available
    if (window.Clerk) {
      const userId = window.Clerk.user?.id
      if (userId) {
        config.headers['X-Clerk-ID'] = userId
        return config
      }
    }
    
    // Fallback to localStorage if Clerk is not available
    const clerkUserId = localStorage.getItem('clerkUserId')
    if (clerkUserId) {
      config.headers['X-Clerk-ID'] = clerkUserId
    } else {
      console.warn('Clerk ID not available')
    }
  } catch (error) {
    console.error('Error adding Clerk ID:', error)
  }
  return config
})

export const studentService = {
  getProfile: () => api.get('/students/profile'),
  createProfile: (data) => api.post('/students/profile', data),
  updateProfile: (data) => api.put('/students/profile', data),
  getProgress: () => api.get('/students/progress'),
  updateProgress: (data) => api.post('/students/progress', data),
}

export const roadmapService = {
  getRoadmap: () => api.get('/roadmap'),
  generateRoadmap: (preferences) => api.post('/roadmap/generate', preferences),
  updateRoadmapProgress: (topicId, status) => api.put(`/roadmap/${topicId}`, { status }),
}

export const companyService = {
  getAllCompanies: () => api.get('/companies'),
  getCompanyDetails: (companyId) => api.get(`/companies/${companyId}`),
  addStudentCompany: (companyId) => api.post(`/students/companies/${companyId}`),
  removeStudentCompany: (companyId) => api.delete(`/students/companies/${companyId}`),
  getProbability: (companyId) => api.get(`/companies/${companyId}/probability`),
  getInterviewQuestions: (companyId) => api.get(`/companies/${companyId}/questions`),
}

export const mockInterviewService = {
  startInterview: (type) => api.post('/mock-interview/start', { type }),
  submitAnswer: (interviewId, questionId, answer) =>
    api.post(`/mock-interview/${interviewId}/submit`, { questionId, answer }),
  getInterviewFeedback: (interviewId) => api.get(`/mock-interview/${interviewId}/feedback`),
  getInterviewHistory: () => api.get('/mock-interview/history'),
}

export const aiService = {
  getRecommendations: () => api.get('/ai/recommendations'),
  getMotivationalMessage: () => api.get('/ai/motivation'),
  getDailyGoals: () => api.get('/ai/daily-goals'),
  planNextSteps: () => api.post('/ai/plan-next-steps'),
}

export const leetcodeService = {
  getStats: () => api.get('/leetcode/stats'),
  getStatsByUsername: (username) => api.get(`/leetcode/stats/${username}`),
  updateUsername: (username) => api.post('/leetcode/username', { username }),
}

export default api
