import { useState, useEffect } from 'react'
import { roadmapService, api } from '../services/api'
import { useStudent } from '../hooks/useStudent'
import DomainSelector from '../components/DomainSelector'
import RoadmapViewer from '../components/RoadmapViewer'
import { FiCheckCircle, FiCircle, FiChevronDown } from 'react-icons/fi'
import './Roadmap.css'

export default function Roadmap() {
  const { student, loading: studentLoading } = useStudent()
  const [roadmap, setRoadmap] = useState(null)
  const [expandedCategory, setExpandedCategory] = useState(null)
  const [loading, setLoading] = useState(true)
  const [showDomainSelector, setShowDomainSelector] = useState(false)
  const [selectedDomain, setSelectedDomain] = useState(null)

  useEffect(() => {
    if (student) {
      setSelectedDomain(student.domain)
      // Show domain selector if no domain is set
      if (!student.domain) {
        setShowDomainSelector(true)
      } else {
        setShowDomainSelector(false)
      }
      // Fetch roadmap if exists
      fetchRoadmap()
    }
  }, [student])

  const fetchRoadmap = async () => {
    try {
      setLoading(true)
      const response = await roadmapService.getRoadmap()
      setRoadmap(response.data)
    } catch (error) {
      console.error('Failed to fetch roadmap:', error)
    } finally {
      setLoading(false)
    }
  }

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

  const handleDomainSelect = (domain) => {
    setSelectedDomain(domain)
    setShowDomainSelector(false)
  }

  if (studentLoading || loading) return <div className="text-center py-12">Loading...</div>

  return (
    <div className="space-y-6">
      {/* Show domain selector if no domain is selected */}
      {showDomainSelector && !selectedDomain && (
        <DomainSelector studentProfile={student} onDomainSelect={handleDomainSelect} />
      )}

      {/* Show roadmap viewer if domain is selected */}
      {selectedDomain && !showDomainSelector && (
        <>
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold">Your {selectedDomain} Roadmap</h1>
            <button
              onClick={() => setShowDomainSelector(true)}
              className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
            >
              Change Domain
            </button>
          </div>
          <RoadmapViewer domain={selectedDomain} studentProfile={student} />
        </>
      )}

      {/* Show traditional roadmap if available */}
      {roadmap && !showDomainSelector && (
        <div className="space-y-4">
          <div className="bg-indigo-50 border-l-4 border-indigo-600 p-6 rounded">
            <h2 className="text-xl font-bold text-indigo-900">Learning Topics</h2>
            <p className="text-indigo-700 mt-2">Track your progress on these essential topics</p>
          </div>

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
      )}
    </div>
  )
}
            )}
          </div>
        ))}
      </div>
    </div>
  )
}
