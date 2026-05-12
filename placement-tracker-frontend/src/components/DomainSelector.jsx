import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import '../pages/Roadmap.css';

const DomainSelector = ({ studentProfile, onDomainSelect }) => {
  const [domains, setDomains] = useState([]);
  const [selectedDomain, setSelectedDomain] = useState(studentProfile?.domain || '');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchAvailableDomains();
  }, []);

  const fetchAvailableDomains = async () => {
    try {
      const response = await api.get('/roadmap/available-domains');
      setDomains(response.data.domains || []);
    } catch (error) {
      console.error('Error fetching domains:', error);
      setMessage('Failed to load available domains');
    }
  };

  const handleDomainSelect = async (domain) => {
    setSelectedDomain(domain);
    setLoading(true);
    setMessage('');

    try {
      const response = await api.post('/roadmap/set-domain', null, {
        params: { domain }
      });
      setMessage('Domain selected successfully!');
      if (onDomainSelect) {
        onDomainSelect(domain);
      }
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      console.error('Error setting domain:', error);
      setMessage('Failed to set domain. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const domainInfo = {
    fullstack: {
      title: 'Full Stack Developer',
      icon: '🌐',
      description: 'Master frontend, backend, and deployment',
      duration: '26 weeks',
      color: 'from-blue-500 to-cyan-500'
    },
    ml: {
      title: 'ML Engineer',
      icon: '🤖',
      description: 'Build intelligent systems with machine learning',
      duration: '30 weeks',
      color: 'from-purple-500 to-pink-500'
    },
    cybersecurity: {
      title: 'Cybersecurity Pro',
      icon: '🔒',
      description: 'Secure systems and networks',
      duration: '27 weeks',
      color: 'from-red-500 to-orange-500'
    },
    cloud: {
      title: 'Cloud Engineer',
      icon: '☁️',
      description: 'Design and manage cloud infrastructure',
      duration: '22 weeks',
      color: 'from-indigo-500 to-blue-500'
    },
    devops: {
      title: 'DevOps Engineer',
      icon: '⚙️',
      description: 'Automate and optimize deployments',
      duration: '20 weeks',
      color: 'from-yellow-500 to-red-500'
    }
  };

  return (
    <div className="domain-selector">
      <div className="selector-header">
        <h2>Choose Your Learning Path</h2>
        <p>Select a domain to get a personalized roadmap</p>
      </div>

      {message && (
        <div className={`message ${message.includes('successfully') ? 'success' : 'error'}`}>
          {message}
        </div>
      )}

      <div className="domains-grid">
        {domains.map((domain) => {
          const info = domainInfo[domain];
          const isSelected = selectedDomain === domain;

          return (
            <div
              key={domain}
              className={`domain-card ${isSelected ? 'selected' : ''}`}
              onClick={() => handleDomainSelect(domain)}
              style={{
                pointerEvents: loading ? 'none' : 'auto',
                opacity: loading && selectedDomain !== domain ? 0.5 : 1
              }}
            >
              <div className={`gradient-bg bg-gradient-to-br ${info.color}`}>
                <div className="domain-icon">{info.icon}</div>
              </div>
              
              <div className="domain-content">
                <h3>{info.title}</h3>
                <p className="description">{info.description}</p>
                <div className="duration">
                  <span className="badge">⏱️ {info.duration}</span>
                </div>
              </div>

              {isSelected && (
                <div className="checkmark">
                  <span>✓</span>
                </div>
              )}
            </div>
          );
        })}
      </div>

      <style jsx>{`
        .domain-selector {
          padding: 2rem 0;
        }

        .selector-header {
          text-align: center;
          margin-bottom: 3rem;
        }

        .selector-header h2 {
          font-size: 2rem;
          color: #1f2937;
          margin-bottom: 0.5rem;
          font-weight: 700;
        }

        .selector-header p {
          color: #6b7280;
          font-size: 1rem;
        }

        .message {
          padding: 1rem;
          margin-bottom: 2rem;
          border-radius: 0.5rem;
          text-align: center;
          font-weight: 500;
        }

        .message.success {
          background-color: #d1fae5;
          color: #065f46;
          border: 1px solid #a7f3d0;
        }

        .message.error {
          background-color: #fee2e2;
          color: #991b1b;
          border: 1px solid #fecaca;
        }

        .domains-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
          gap: 1.5rem;
          margin-bottom: 2rem;
        }

        .domain-card {
          background: white;
          border: 2px solid #e5e7eb;
          border-radius: 1rem;
          padding: 1.5rem;
          cursor: pointer;
          transition: all 0.3s ease;
          position: relative;
          overflow: hidden;
        }

        .domain-card:hover {
          border-color: #3b82f6;
          box-shadow: 0 10px 25px rgba(59, 130, 246, 0.1);
          transform: translateY(-4px);
        }

        .domain-card.selected {
          border-color: #3b82f6;
          background: #f0f9ff;
          box-shadow: 0 10px 25px rgba(59, 130, 246, 0.2);
        }

        .gradient-bg {
          width: 100%;
          height: 100px;
          border-radius: 0.75rem;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-bottom: 1rem;
          color: white;
        }

        .domain-icon {
          font-size: 3rem;
        }

        .domain-content h3 {
          font-size: 1.25rem;
          font-weight: 600;
          color: #1f2937;
          margin-bottom: 0.5rem;
        }

        .description {
          color: #6b7280;
          font-size: 0.95rem;
          margin-bottom: 1rem;
        }

        .duration {
          display: flex;
          align-items: center;
        }

        .badge {
          display: inline-block;
          background-color: #f3f4f6;
          color: #1f2937;
          padding: 0.375rem 0.75rem;
          border-radius: 1rem;
          font-size: 0.85rem;
          font-weight: 500;
        }

        .checkmark {
          position: absolute;
          top: 1rem;
          right: 1rem;
          width: 2rem;
          height: 2rem;
          background-color: #3b82f6;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 1.25rem;
          font-weight: bold;
          animation: popIn 0.3s ease;
        }

        @keyframes popIn {
          0% {
            transform: scale(0);
          }
          100% {
            transform: scale(1);
          }
        }

        @media (max-width: 768px) {
          .selector-header h2 {
            font-size: 1.5rem;
          }

          .domains-grid {
            grid-template-columns: 1fr;
          }
        }
      `}</style>
    </div>
  );
};

export default DomainSelector;
