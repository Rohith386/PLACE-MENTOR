import { useState, useEffect } from 'react'
import { roadmapService } from '../services/api'
import { FiCheckCircle, FiCircle, FiChevronDown } from 'react-icons/fi'
import './Roadmap.css'

export default function Roadmap() {
  const [roadmap, setRoadmap] = useState(null)
  const [expandedCategory, setExpandedCategory] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchRoadmap = async () => {
      try {
        const response = await roadmapService.getRoadmap()
        setRoadmap(response.data)
      } catch (error) {
        console.error('Failed to fetch roadmap:', error)
      } finally {
        setLoading(false)
      }
    }
    fetchRoadmap()
  }, [])

  const handleToggleStatus = async (topicId, currentStatus) => {
    try {
      await roadmapService.updateRoadmapProgress(topicId, 
        currentStatus === 'completed' ? 'in-progress' : 'completed'
      )
      // Refresh roadmap
      const response = await roadmapService.getRoadmap()
      setRoadmap(response.data)
    } catch (error) {
      console.error('Failed to update progress:', error)
    }
  }

  if (loading) return <div className="text-center py-12">Loading your personalized roadmap...</div>

  return (
    <div className="space-y-6">
      <div className="bg-indigo-50 border-l-4 border-indigo-600 p-6 rounded">
        <h1 className="text-2xl font-bold text-indigo-900">Your Personalized Learning Roadmap</h1>
        <p className="text-indigo-700 mt-2">This roadmap is tailored based on your target companies and current skill level. Complete topics to unlock your dream company prospects!</p>
      </div>

      <div className="space-y-4">
        {roadmap?.categories?.map((category) => (
          <div key={category.id} className="card">
            <button
              onClick={() => setExpandedCategory(expandedCategory === category.id ? null : category.id)}
              className="w-full flex items-center justify-between p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition"
            >
              <div className="flex items-center gap-3">
                <div className="text-2xl">{category.emoji}</div>
                <div className="text-left">
                  <h3 className="font-bold text-lg">{category.name}</h3>
                  <p className="text-sm text-gray-500">{category.completedTopics}/{category.totalTopics} topics completed</p>
                </div>
              </div>
              <div className="flex items-center gap-2">
                <div className="text-right">
                  <p className="text-2xl font-bold text-indigo-600">{Math.round((category.completedTopics / category.totalTopics) * 100)}%</p>
                </div>
                <FiChevronDown 
                  className={`transition ${expandedCategory === category.id ? 'rotate-180' : ''}`} 
                  size={24} 
                />
              </div>
            </button>

            {expandedCategory === category.id && (
              <div className="mt-4 space-y-2 pl-4">
                {category.topics?.map((topic) => (
                  <div
                    key={topic.id}
                    className="flex items-center gap-3 p-3 rounded-lg bg-gray-50 hover:bg-gray-100 transition cursor-pointer"
                    onClick={() => handleToggleStatus(topic.id, topic.status)}
                  >
                    {topic.status === 'completed' ? (
                      <FiCheckCircle className="text-green-600" size={20} />
                    ) : (
                      <FiCircle className="text-gray-400" size={20} />
                    )}
                    <div className="flex-1">
                      <p className={`font-medium ${topic.status === 'completed' ? 'text-gray-500 line-through' : 'text-gray-800'}`}>
                        {topic.name}
                      </p>
                      <p className="text-xs text-gray-500">{topic.resources?.length || 0} resources available</p>
                    </div>
                    <span className={`px-2 py-1 rounded text-xs font-semibold ${
                      topic.priority === 'high' ? 'badge-danger' :
                      topic.priority === 'medium' ? 'badge-warning' :
                      'badge-success'
                    }`}>
                      {topic.priority}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}
