import { useState, useEffect } from 'react'
import { useStudent, useProgress } from '../hooks/useStudent'
import { aiService } from '../services/api'
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line } from 'recharts'
import { FiTrendingUp, FiAward, FiTarget, FiAlertCircle } from 'react-icons/fi'

export default function Dashboard() {
  const { student, loading: studentLoading } = useStudent()
  const { progress, loading: progressLoading } = useProgress()
  const [recommendations, setRecommendations] = useState([])
  const [motivation, setMotivation] = useState('')

  useEffect(() => {
    const fetchAIInsights = async () => {
      try {
        const [recsRes, motRes] = await Promise.all([
          aiService.getRecommendations(),
          aiService.getMotivationalMessage()
        ])
        setRecommendations(recsRes.data)
        setMotivation(motRes.data.message)
      } catch (error) {
        console.error('Failed to fetch AI insights:', error)
      }
    }
    fetchAIInsights()
  }, [])

  if (studentLoading || progressLoading) {
    return <div className="text-center py-12">Loading your dashboard...</div>
  }

  const readinessScore = progress?.readinessScore || 0
  const chartData = [
    { name: 'DSA', value: progress?.dsaProgress || 0 },
    { name: 'CS Fundamentals', value: progress?.csFundamentalsProgress || 0 },
    { name: 'Projects', value: progress?.projectsProgress || 0 },
    { name: 'Aptitude', value: progress?.aptitudeProgress || 0 },
    { name: 'Soft Skills', value: progress?.softSkillsProgress || 0 },
  ]

  return (
    <div className="space-y-6">
      {/* Header with Motivation */}
      <div className="bg-gradient-to-r from-indigo-600 to-pink-500 text-white rounded-lg p-8">
        <h1 className="text-3xl font-bold mb-2">Welcome back, {student?.firstName}! 🚀</h1>
        <p className="text-lg opacity-90">{motivation}</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Overall Readiness</p>
              <p className="text-3xl font-bold text-indigo-600">{readinessScore}%</p>
            </div>
            <FiTrendingUp className="text-indigo-600" size={40} />
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Topics Completed</p>
              <p className="text-3xl font-bold text-green-600">{progress?.topicsCompleted || 0}</p>
            </div>
            <FiAward className="text-green-600" size={40} />
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Problems Solved</p>
              <p className="text-3xl font-bold text-blue-600">{progress?.problemsSolved || 0}</p>
            </div>
            <FiTarget className="text-blue-600" size={40} />
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Mock Interviews</p>
              <p className="text-3xl font-bold text-purple-600">{progress?.mockInterviews || 0}</p>
            </div>
            <FiAlertCircle className="text-purple-600" size={40} />
          </div>
        </div>
      </div>

      {/* Progress by Category */}
      <div className="card">
        <h2 className="text-xl font-bold mb-6">Preparation Progress by Category</h2>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={chartData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="value" fill="#6366f1" radius={[8, 8, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* AI Recommendations */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 card">
          <h2 className="text-xl font-bold mb-4">🤖 AI Recommendations</h2>
          <div className="space-y-3">
            {recommendations.length > 0 ? (
              recommendations.map((rec, idx) => (
                <div key={idx} className="flex items-start gap-3 p-3 bg-blue-50 rounded-lg">
                  <span className="text-blue-600 font-bold">→</span>
                  <div>
                    <p className="font-semibold text-gray-800">{rec.title}</p>
                    <p className="text-sm text-gray-600">{rec.description}</p>
                    <p className="text-xs text-blue-600 mt-1">Priority: {rec.priority}</p>
                  </div>
                </div>
              ))
            ) : (
              <p className="text-gray-500">Loading recommendations...</p>
            )}
          </div>
        </div>

        <div className="card">
          <h2 className="text-xl font-bold mb-4">📊 Weekly Progress</h2>
          <ResponsiveContainer width="100%" height={250}>
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="value" stroke="#6366f1" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  )
}
