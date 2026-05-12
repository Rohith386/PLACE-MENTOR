import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import '../pages/Roadmap.css';

const RoadmapViewer = ({ domain, studentProfile }) => {
  const [roadmap, setRoadmap] = useState(null);
  const [loading, setLoading] = useState(true);
  const [expandedPhase, setExpandedPhase] = useState(0);
  const [topicStatuses, setTopicStatuses] = useState({});
  const [error, setError] = useState('');

  useEffect(() => {
    if (domain) {
      fetchRoadmap();
    }
  }, [domain]);

  const fetchRoadmap = async () => {
    try {
      setLoading(true);
      const response = await api.get(`/roadmap/domain/${domain}`);
      setRoadmap(response.data);
      setError('');
    } catch (error) {
      console.error('Error fetching roadmap:', error);
      setError('Failed to load roadmap. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleTopicStatusChange = async (topicId, newStatus) => {
    try {
      await api.put(`/roadmap/topic/${topicId}/status`, null, {
        params: { status: newStatus }
      });
      setTopicStatuses(prev => ({
        ...prev,
        [topicId]: newStatus
      }));
    } catch (error) {
      console.error('Error updating topic status:', error);
    }
  };

  if (loading) {
    return (
      <div className="roadmap-viewer">
        <div className="loading-spinner">
          <div className="spinner"></div>
          <p>Loading your personalized roadmap...</p>
        </div>
      </div>
    );
  }

  if (error || !roadmap) {
    return (
      <div className="roadmap-viewer">
        <div className="error-message">
          <p>❌ {error || 'Unable to load roadmap'}</p>
        </div>
      </div>
    );
  }

  const getStatusColor = (status) => {
    switch (status) {
      case 'completed':
        return '#10b981';
      case 'in-progress':
        return '#f59e0b';
      case 'not-started':
        return '#9ca3af';
      default:
        return '#d1d5db';
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'completed':
        return '✓';
      case 'in-progress':
        return '⏳';
      case 'not-started':
        return '○';
      default:
        return '-';
    }
  };

  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'high':
        return '#ef4444';
      case 'medium':
        return '#f59e0b';
      case 'low':
        return '#3b82f6';
      default:
        return '#9ca3af';
    }
  };

  return (
    <div className="roadmap-viewer">
      <div className="roadmap-header">
        <h1>{roadmap.title}</h1>
        <p className="description">{roadmap.description}</p>

        <div className="roadmap-stats">
          <div className="stat">
            <span className="label">Skill Level</span>
            <span className="value">{roadmap.skillLevel}</span>
          </div>
          <div className="stat">
            <span className="label">Estimated Duration</span>
            <span className="value">{roadmap.estimatedDuration} weeks</span>
          </div>
          <div className="stat">
            <span className="label">Total Topics</span>
            <span className="value">{roadmap.totalTopics}</span>
          </div>
          <div className="stat">
            <span className="label">Progress</span>
            <span className="value">{roadmap.completionPercentage}%</span>
          </div>
        </div>

        <div className="progress-bar">
          <div
            className="progress-fill"
            style={{ width: `${roadmap.completionPercentage}%` }}
          ></div>
        </div>
      </div>

      <div className="phases-container">
        {roadmap.phases.map((phase, phaseIndex) => (
          <div key={phaseIndex} className="phase-card">
            <div
              className="phase-header"
              onClick={() => setExpandedPhase(expandedPhase === phaseIndex ? -1 : phaseIndex)}
            >
              <div className="phase-info">
                <h2 className="phase-title">
                  Phase {phase.order}: {phase.phaseName}
                </h2>
                <p className="phase-description">{phase.description}</p>
              </div>
              <div className="phase-meta">
                <span className="duration-badge">⏱️ {phase.weekDuration} weeks</span>
                <span className={`expand-icon ${expandedPhase === phaseIndex ? 'expanded' : ''}`}>
                  ▼
                </span>
              </div>
            </div>

            {expandedPhase === phaseIndex && (
              <div className="phase-topics">
                {phase.topics.map((topic, topicIndex) => (
                  <div key={topicIndex} className="topic-item">
                    <div className="topic-header">
                      <div className="topic-info">
                        <div className="topic-status-selector">
                          <select
                            value={topicStatuses[topic.id] || topic.status || 'not-started'}
                            onChange={(e) => handleTopicStatusChange(topic.id, e.target.value)}
                            className="status-select"
                          >
                            <option value="not-started">Not Started</option>
                            <option value="in-progress">In Progress</option>
                            <option value="completed">Completed</option>
                          </select>
                          <span
                            className="status-indicator"
                            style={{
                              backgroundColor: getStatusColor(
                                topicStatuses[topic.id] || topic.status || 'not-started'
                              )
                            }}
                            title={topicStatuses[topic.id] || topic.status || 'not-started'}
                          >
                            {getStatusIcon(topicStatuses[topic.id] || topic.status || 'not-started')}
                          </span>
                        </div>

                        <div className="topic-details">
                          <h4 className="topic-name">{topic.topicName}</h4>
                          <p className="topic-description">{topic.description}</p>
                          <span
                            className="priority-badge"
                            style={{
                              borderColor: getPriorityColor(topic.priority)
                            }}
                          >
                            {topic.priority.charAt(0).toUpperCase() + topic.priority.slice(1)} Priority
                          </span>
                        </div>
                      </div>
                    </div>

                    <div className="topic-resources">
                      {topic.resources && topic.resources.length > 0 && (
                        <div className="resources-section">
                          <span className="section-title">📚 Resources:</span>
                          <div className="resources-list">
                            {topic.resources.map((resource, idx) => (
                              <a key={idx} href={resource} target="_blank" rel="noopener noreferrer" className="resource-link">
                                {resource}
                              </a>
                            ))}
                          </div>
                        </div>
                      )}

                      {topic.practiceProblems && topic.practiceProblems.length > 0 && (
                        <div className="resources-section">
                          <span className="section-title">💪 Practice Problems:</span>
                          <div className="resources-list">
                            {topic.practiceProblems.map((problem, idx) => (
                              <a key={idx} href={problem} target="_blank" rel="noopener noreferrer" className="resource-link">
                                {problem}
                              </a>
                            ))}
                          </div>
                        </div>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>

      <style jsx>{`
        .roadmap-viewer {
          max-width: 1200px;
          margin: 0 auto;
          padding: 2rem 1rem;
        }

        .loading-spinner {
          text-align: center;
          padding: 3rem;
        }

        .spinner {
          border: 4px solid #f3f4f6;
          border-top: 4px solid #3b82f6;
          border-radius: 50%;
          width: 40px;
          height: 40px;
          animation: spin 1s linear infinite;
          margin: 0 auto 1rem;
        }

        @keyframes spin {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }

        .error-message {
          background-color: #fee2e2;
          color: #991b1b;
          padding: 1rem;
          border-radius: 0.5rem;
          text-align: center;
          font-weight: 500;
        }

        .roadmap-header {
          margin-bottom: 3rem;
        }

        .roadmap-header h1 {
          font-size: 2rem;
          color: #1f2937;
          margin-bottom: 0.5rem;
          font-weight: 700;
        }

        .description {
          color: #6b7280;
          font-size: 1.05rem;
          margin-bottom: 2rem;
        }

        .roadmap-stats {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
          gap: 1rem;
          margin-bottom: 2rem;
        }

        .stat {
          background: white;
          border: 1px solid #e5e7eb;
          border-radius: 0.75rem;
          padding: 1rem;
          display: flex;
          flex-direction: column;
        }

        .stat .label {
          font-size: 0.85rem;
          color: #6b7280;
          text-transform: uppercase;
          letter-spacing: 0.05em;
          margin-bottom: 0.5rem;
        }

        .stat .value {
          font-size: 1.5rem;
          color: #1f2937;
          font-weight: 600;
        }

        .progress-bar {
          height: 12px;
          background-color: #f3f4f6;
          border-radius: 1rem;
          overflow: hidden;
          margin-bottom: 2rem;
        }

        .progress-fill {
          height: 100%;
          background: linear-gradient(90deg, #3b82f6, #06b6d4);
          border-radius: 1rem;
          transition: width 0.3s ease;
        }

        .phases-container {
          display: grid;
          grid-template-columns: 1fr;
          gap: 1.5rem;
        }

        .phase-card {
          background: white;
          border: 1px solid #e5e7eb;
          border-radius: 1rem;
          overflow: hidden;
          box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
          transition: box-shadow 0.3s ease;
        }

        .phase-card:hover {
          box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .phase-header {
          padding: 1.5rem;
          cursor: pointer;
          display: flex;
          justify-content: space-between;
          align-items: center;
          background-color: #f9fafb;
          border-bottom: 2px solid #e5e7eb;
          transition: background-color 0.3s ease;
        }

        .phase-header:hover {
          background-color: #f3f4f6;
        }

        .phase-info {
          flex: 1;
        }

        .phase-title {
          font-size: 1.25rem;
          color: #1f2937;
          font-weight: 600;
          margin-bottom: 0.5rem;
        }

        .phase-description {
          color: #6b7280;
          font-size: 0.95rem;
        }

        .phase-meta {
          display: flex;
          align-items: center;
          gap: 1rem;
        }

        .duration-badge {
          background-color: #dbeafe;
          color: #1e40af;
          padding: 0.375rem 0.75rem;
          border-radius: 0.5rem;
          font-size: 0.85rem;
          font-weight: 500;
          white-space: nowrap;
        }

        .expand-icon {
          color: #6b7280;
          transition: transform 0.3s ease;
          font-size: 0.75rem;
        }

        .expand-icon.expanded {
          transform: rotate(180deg);
        }

        .phase-topics {
          padding: 1.5rem;
          background: #fafafa;
          display: grid;
          grid-template-columns: 1fr;
          gap: 1rem;
        }

        .topic-item {
          background: white;
          border: 1px solid #e5e7eb;
          border-radius: 0.75rem;
          padding: 1rem;
          transition: all 0.3s ease;
        }

        .topic-item:hover {
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }

        .topic-header {
          display: flex;
          gap: 1rem;
          margin-bottom: 0.75rem;
        }

        .topic-info {
          flex: 1;
          display: flex;
          gap: 1rem;
          align-items: flex-start;
        }

        .topic-status-selector {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          flex-shrink: 0;
        }

        .status-select {
          padding: 0.375rem 0.5rem;
          border: 1px solid #d1d5db;
          border-radius: 0.375rem;
          font-size: 0.85rem;
          cursor: pointer;
          background-color: white;
          color: #1f2937;
        }

        .status-indicator {
          width: 28px;
          height: 28px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-weight: bold;
          font-size: 0.75rem;
        }

        .topic-details {
          flex: 1;
        }

        .topic-name {
          font-size: 1rem;
          color: #1f2937;
          font-weight: 600;
          margin-bottom: 0.25rem;
        }

        .topic-description {
          color: #6b7280;
          font-size: 0.9rem;
          margin-bottom: 0.5rem;
        }

        .priority-badge {
          display: inline-block;
          padding: 0.25rem 0.75rem;
          border-radius: 1rem;
          font-size: 0.75rem;
          font-weight: 600;
          border: 2px solid;
          color: #1f2937;
          background-color: white;
        }

        .topic-resources {
          margin-top: 0.75rem;
        }

        .resources-section {
          margin-top: 0.5rem;
        }

        .section-title {
          font-size: 0.85rem;
          font-weight: 600;
          color: #1f2937;
        }

        .resources-list {
          margin-top: 0.25rem;
          display: flex;
          flex-wrap: wrap;
          gap: 0.5rem;
        }

        .resource-link {
          font-size: 0.8rem;
          color: #3b82f6;
          text-decoration: none;
          padding: 0.25rem 0.5rem;
          background-color: #dbeafe;
          border-radius: 0.25rem;
          transition: all 0.3s ease;
        }

        .resource-link:hover {
          color: #1e40af;
          background-color: #bfdbfe;
        }

        @media (max-width: 768px) {
          .roadmap-header h1 {
            font-size: 1.5rem;
          }

          .roadmap-stats {
            grid-template-columns: repeat(2, 1fr);
          }

          .phase-header {
            flex-direction: column;
            align-items: flex-start;
          }

          .phase-meta {
            width: 100%;
            justify-content: space-between;
            margin-top: 0.5rem;
          }

          .topic-info {
            flex-direction: column;
          }
        }
      `}</style>
    </div>
  );
};

export default RoadmapViewer;
