import { useState, useEffect } from 'react'
import { topicStrengthService } from '../services/api'

export default function TopicStrength() {
  const [topicStrengths, setTopicStrengths] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [sortBy, setSortBy] = useState('strength') // 'strength', 'name', 'solved'

  useEffect(() => {
    fetchTopicStrengths()
  }, [])

  const fetchTopicStrengths = async () => {
    setLoading(true)
    setError('')
    try {
      const response = await topicStrengthService.getMyTopicStrengths()
      console.log('Topic strengths response:', response.data)
      setTopicStrengths(response.data)
    } catch (err) {
      console.error('Error fetching topic strengths:', err)
      setError('Failed to load topic strengths. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  const getSortedTopics = () => {
    const sorted = [...topicStrengths]
    if (sortBy === 'strength') {
      return sorted.sort((a, b) => (b.strength || 0) - (a.strength || 0))
    } else if (sortBy === 'solved') {
      return sorted.sort((a, b) => (b.problemsSolved || 0) - (a.problemsSolved || 0))
    } else if (sortBy === 'name') {
      return sorted.sort((a, b) => a.topicName.localeCompare(b.topicName))
    }
    return sorted
  }

  const getStrengthColor = (strength) => {
    if (strength >= 70) return 'bg-green-100'
    if (strength >= 40) return 'bg-yellow-100'
    return 'bg-red-100'
  }

  const getStrengthBadge = (strength) => {
    if (strength >= 70) return 'text-green-700'
    if (strength >= 40) return 'text-yellow-700'
    return 'text-red-700'
  }

  const getProgressBarColor = (strength) => {
    if (strength >= 70) return 'bg-green-500'
    if (strength >= 40) return 'bg-yellow-500'
    return 'bg-red-500'
  }

  const totalStrength = topicStrengths.length > 0
    ? Math.round(topicStrengths.reduce((sum, t) => sum + (t.strength || 0), 0) / topicStrengths.length)
    : 0

  const sortedTopics = getSortedTopics()

  if (loading) {
    return (
      <div className="flex justify-center items-center py-12">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-gradient-to-r from-indigo-600 to-pink-500 text-white rounded-lg p-6">
        <h1 className="text-3xl font-bold mb-2">Topic Strength Analysis</h1>
        <p className="text-indigo-100">Based on your LeetCode problems solved</p>
      </div>

      {/* Overall Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="card">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-600 text-sm">Overall Strength</p>
              <p className="text-4xl font-bold text-indigo-600 mt-2">{totalStrength}%</p>
            </div>
            <div className="text-4xl">📊</div>
          </div>
        </div>

        <div className="card">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-600 text-sm">Topics Tracked</p>
              <p className="text-4xl font-bold text-pink-600 mt-2">{topicStrengths.length}</p>
            </div>
            <div className="text-4xl">🎯</div>
          </div>
        </div>

        <div className="card">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-gray-600 text-sm">Total Problems</p>
              <p className="text-4xl font-bold text-purple-600 mt-2">
                {topicStrengths.reduce((sum, t) => sum + (t.problemsSolved || 0), 0)}
              </p>
            </div>
            <div className="text-4xl">✅</div>
          </div>
        </div>
      </div>

      {/* Sort Options */}
      <div className="flex gap-2">
        <button
          onClick={() => setSortBy('strength')}
          className={`px-4 py-2 rounded-lg font-medium transition ${
            sortBy === 'strength'
              ? 'bg-indigo-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Sort by Strength
        </button>
        <button
          onClick={() => setSortBy('solved')}
          className={`px-4 py-2 rounded-lg font-medium transition ${
            sortBy === 'solved'
              ? 'bg-indigo-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Sort by Problems
        </button>
        <button
          onClick={() => setSortBy('name')}
          className={`px-4 py-2 rounded-lg font-medium transition ${
            sortBy === 'name'
              ? 'bg-indigo-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Sort by Name
        </button>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 rounded-lg p-4 text-red-700">
          {error}
        </div>
      )}

      {/* Topic Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {sortedTopics.map((topic) => (
          <div key={topic.id} className={`card ${getStrengthColor(topic.strength || 0)}`}>
            <div className="flex justify-between items-start mb-4">
              <div>
                <h3 className="font-bold text-lg">{topic.topicName}</h3>
                <p className="text-sm text-gray-600">{topic.category}</p>
              </div>
              <span className={`text-2xl font-bold ${getStrengthBadge(topic.strength || 0)}`}>
                {(topic.strength || 0).toFixed(1)}%
              </span>
            </div>

            {/* Progress Bar */}
            <div className="mb-4">
              <div className="w-full bg-gray-300 rounded-full h-3 overflow-hidden">
                <div
                  className={`h-full ${getProgressBarColor(topic.strength || 0)} transition-all duration-300`}
                  style={{ width: `${Math.min(topic.strength || 0, 100)}%` }}
                ></div>
              </div>
            </div>

            {/* Stats */}
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div>
                <p className="text-gray-600">Solved</p>
                <p className="font-semibold text-lg">
                  {topic.problemsSolved}/{topic.totalProblems}
                </p>
              </div>
              <div>
                <p className="text-gray-600">Level</p>
                <p className="font-semibold">{topic.difficulty || 'N/A'}</p>
              </div>
            </div>

            {/* Last Updated */}
            <p className="text-xs text-gray-500 mt-4">
              Updated: {topic.lastUpdated}
            </p>
          </div>
        ))}
      </div>

      {topicStrengths.length === 0 && !loading && (
        <div className="card text-center py-12">
          <p className="text-gray-600 mb-4">No topic strengths calculated yet</p>
          <button
            onClick={fetchTopicStrengths}
            className="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 font-medium"
          >
            Calculate Now
          </button>
        </div>
      )}
    </div>
  )
}
