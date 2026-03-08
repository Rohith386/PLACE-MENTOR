import { create } from 'zustand'

export const useStudentStore = create((set) => ({
  student: null,
  progress: {},
  companies: [],
  roadmap: null,
  aiRecommendations: [],

  setStudent: (student) => set({ student }),
  setProgress: (progress) => set({ progress }),
  setCompanies: (companies) => set({ companies }),
  setRoadmap: (roadmap) => set({ roadmap }),
  setAIRecommendations: (recommendations) => set({ aiRecommendations: recommendations }),

  updateProgress: (updates) => set((state) => ({
    progress: { ...state.progress, ...updates }
  })),

  addCompany: (company) => set((state) => ({
    companies: [...state.companies, company]
  })),
}))
