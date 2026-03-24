import { useState, useEffect } from 'react'
import { studentService, topicStrengthService } from '../services/api'
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line } from 'recharts'

export default function Dashboard() {
  const [profile, setProfile] = useState(null)
  const [topicStrengths, setTopicStrengths] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchDashboardData()
  }, [])

  const fetchDashboardData = async () => {
    setLoading(true)
    try {
      const profileRes = await studentService.getProfile()
      setProfile(profileRes.data)

      const topicRes = await topicStrengthService.getMyTopicStrengths()
      setTopicStrengths(topicRes.data || [])
    } catch (error) {
      console.error('Failed to fetch dashboard data:', error)
    } finally {
      setLoading(false)
    }
  }

  const calculateOverallReadiness = () => {
    if (topicStrengths.length === 0) return 0
    const average = topicStrengths.reduce((sum, t) => sum + (t.strength || 0), 0) / topicStrengths.length
    return Math.round(average)
  }

  const getTopicsByStrength = () => {
    return [...topicStrengths]
      .sort((a, b) => (b.strength || 0) - (a.strength || 0))
      .slice(0, 3)
  }

  const getWeakTopics = () => {
    return [...topicStrengths]
      .sort((a, b) => (a.strength || 0) - (b.strength || 0))
      .slice(0, 3)
  }

  const getReadinessColor = (score) => {
    if (score >= 70) return 'text-green-600'
    if (score >= 50) return 'text-blue-600'
    if (score >= 30) return 'text-yellow-600'
    return 'text-red-600'
  }

  const getReadinessBg = (score) => {
    if (score >= 70) return 'bg-green-50'
    if (score >= 50) return 'bg-blue-50'
    if (score >= 30) return 'bg-yellow-50'
    return 'bg-red-50'
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center py-12">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    )
  }

  const overallReadiness = calculateOverallReadiness()
  const topTopics = getTopicsByStrength()
  const weakTopics = getWeakTopics()

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-gradient-to-r from-indigo-600 to-pink-500 text-white rounded-lg p-8">
        <h1 className="text-3xl font-bold mb-2">Welcome back, {profile?.firstName}! 🚀</h1>
        <p className="text-lg opacity-90">Every expert was once a beginner. ✨ Start with the basics and build your way up.</p>
      </div>

      {/* Key Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className={`card ${getReadinessBg(overallReadiness)}`}>
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-600 text-sm font-medium">Overall Readiness</p>
              <p className={`text-4xl font-bold mt-2 ${getReadinessColor(overallReadiness)}`}>
                {overallReadiness}%
              </p>
            </div>
            <div className="text-4xl">📈</div>
          </div>
        </div>

        <div className="card bg-green-50">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-600 text-sm font-medium">Topics Tracked</p>
              <p className="text-4xl font-bold text-green-600 mt-2">{topicStrengths.length}</p>
            </div>
            <div className="text-4xl">🎯</div>
          </div>
        </div>

        <div className="card bg-blue-50">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-600 text-sm font-medium">Problems Solved</p>
              <p className="text-4xl font-bold text-blue-600 mt-2">{profile?.problemsSolved || 0}</p>
            </div>
            <div className="text-4xl">✅</div>
          </div>
        </div>

        <div className="card bg-purple-50">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-600 text-sm font-medium">Skill Level</p>
              <p className="text-3xl font-bold text-purple-600 mt-2 capitalize">
                {profile?.skillLevel || 'Beginner'}
              </p>
            </div>
            <div className="text-4xl">💪</div>
          </div>
        </div>
      </div>

      {/* Topics Performance */}
      {topicStrengths.length > 0 && (
        <>
          {/* Strongest Topics */}
          <div className="card">
            <h3 className="text-xl font-bold mb-4">💪 Your Strongest Topics</h3>
            <div className="space-y-3">
              {topTopics.map((topic) => (
                <div key={topic.id} className="flex items-center justify-between p-4 bg-green-50 rounded-lg border-l-4 border-green-500">
                  <div className="flex-1">
                    <p className="font-semibold text-gray-800">{topic.topicName}</p>
                    <p className="text-sm text-gray-600">{topic.problemsSolved} problems solved</p>
                  </div>
                  <div className="text-right">
                    <p className="text-3xl font-bold text-green-600">{(topic.strength || 0).toFixed(1)}%</p>
                    <p className="text-xs text-gray-500 mt-1">{topic.difficulty}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Areas to Improve */}
          <div className="card">
            <h3 className="text-xl font-bold mb-4">🎯 Focus Areas (Need Practice)</h3>
            <div className="space-y-3">
              {weakTopics.map((topic) => (
                <div key={topic.id} className="flex items-center justify-between p-4 bg-orange-50 rounded-lg border-l-4 border-orange-500">
                  <div className="flex-1">
                    <p className="font-semibold text-gray-800">{topic.topicName}</p>
                    <p className="text-sm text-gray-600">{topic.problemsSolved} problems solved</p>
                  </div>
                  <div className="text-right">
                    <p className="text-3xl font-bold text-orange-600">{(topic.strength || 0).toFixed(1)}%</p>
                    <p className="text-xs text-gray-500 mt-1">{topic.difficulty}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* All Topics Bar Chart */}
          <div className="card">
            <h3 className="text-xl font-bold mb-6">📊 All Topics Strength</h3>
            <ResponsiveContainer width="100%" height={350}>
              <BarChart
                data={[...topicStrengths].sort((a, b) => (b.strength || 0) - (a.strength || 0))}
                margin={{ top: 20, right: 30, left: 0, bottom: 80 }}
              >
                <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
                <XAxis
                  dataKey="topicName"
                  angle={-45}
                  textAnchor="end"
                  height={120}
                  interval={0}
                  tick={{ fontSize: 12 }}
                />
                <YAxis 
                  domain={[0, 100]} 
                  label={{ value: 'Skill Score (%)', angle: -90, position: 'insideLeft' }} 
                />
                <Tooltip 
                  formatter={(value) => `${value.toFixed(1)}%`}
                  labelStyle={{ color: '#000' }}
                />
                <Bar dataKey="strength" fill="#6366f1" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Smart Recommendations */}
          <div className="card border-2 border-indigo-200 bg-gradient-to-br from-indigo-50 to-blue-50">
            <h3 className="text-xl font-bold mb-4">💡 Personalized Guidance</h3>
            <div className="space-y-4">
              <div className="p-4 bg-white rounded-lg border-l-4 border-indigo-500">
                <p className="font-semibold text-gray-800 mb-1">📍 Current Status</p>
                <p className="text-gray-700">Your overall readiness is <strong>{overallReadiness}%</strong></p>
                {overallReadiness >= 70 && (
                  <p className="text-sm text-green-700 mt-2">✨ Excellent! You're ready for technical interviews!</p>
                )}
                {overallReadiness >= 50 && overallReadiness < 70 && (
                  <p className="text-sm text-blue-700 mt-2">📈 Good progress! Keep practicing to reach 70%+</p>
                )}
                {overallReadiness < 50 && (
                  <p className="text-sm text-orange-700 mt-2">⚡ Great time to focus on fundamentals!</p>
                )}
              </div>

              <div className="p-4 bg-white rounded-lg border-l-4 border-purple-500">
                <p className="font-semibold text-gray-800 mb-2">🎯 Recommended Focus</p>
                <ul className="text-gray-700 space-y-1">
                  {weakTopics.slice(0, 2).map((topic) => (
                    <li key={topic.id} className="text-sm">
                      • <strong>{topic.topicName}</strong> ({(topic.strength || 0).toFixed(1)}%) - Practice more problems
                    </li>
                  ))}
                </ul>
              </div>

              <div className="p-4 bg-white rounded-lg border-l-4 border-green-500">
                <p className="font-semibold text-gray-800 mb-1">✅ Next Action</p>
                <p className="text-sm text-gray-700">
                  Visit <strong>Topic Strength</strong> page to see detailed breakdown and start practicing weak areas!
                </p>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  )
}
