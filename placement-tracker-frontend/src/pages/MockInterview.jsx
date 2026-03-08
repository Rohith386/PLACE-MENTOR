import { useState } from 'react'
import { mockInterviewService } from '../services/api'
import { FiPlay, FiClock, FiCheckCircle } from 'react-icons/fi'

export default function MockInterview() {
  const [interviewType, setInterviewType] = useState(null)
  const [interviewing, setInterviewing] = useState(false)
  const [currentQuestion, setCurrentQuestion] = useState(null)
  const [answer, setAnswer] = useState('')
  const [feedback, setFeedback] = useState(null)

  const interviewTypes = [
    { id: 'dsa', name: 'DSA Coding Round', duration: '60 min', icon: '💻' },
    { id: 'behavioral', name: 'Behavioral Interview', duration: '30 min', icon: '🎤' },
    { id: 'hr', name: 'HR Round', duration: '20 min', icon: '👔' },
  ]

  const handleStartInterview = async (type) => {
    try {
      const response = await mockInterviewService.startInterview(type)
      setInterviewType(type)
      setCurrentQuestion(response.data.firstQuestion)
      setInterviewing(true)
    } catch (error) {
      console.error('Failed to start interview:', error)
    }
  }

  const handleSubmitAnswer = async () => {
    try {
      await mockInterviewService.submitAnswer(
        currentQuestion.interviewId,
        currentQuestion.id,
        answer
      )
      // Get feedback
      const feedbackRes = await mockInterviewService.getInterviewFeedback(currentQuestion.interviewId)
      setFeedback(feedbackRes.data)
      setInterviewing(false)
    } catch (error) {
      console.error('Failed to submit answer:', error)
    }
  }

  return (
    <div className="space-y-6">
      <div className="bg-purple-50 border-l-4 border-purple-500 p-6 rounded">
        <h1 className="text-2xl font-bold text-purple-900">Mock Interview System</h1>
        <p className="text-purple-700 mt-2">Practice with AI-powered mock interviews to boost your confidence!</p>
      </div>

      {!interviewing && !feedback && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {interviewTypes.map((type) => (
            <div key={type.id} className="card hover:shadow-lg transition cursor-pointer" onClick={() => handleStartInterview(type.id)}>
              <div className="text-4xl mb-4">{type.icon}</div>
              <h3 className="text-lg font-bold mb-2">{type.name}</h3>
              <div className="flex items-center gap-2 text-gray-500 mb-4">
                <FiClock size={16} />
                <span>{type.duration}</span>
              </div>
              <button className="btn-primary w-full flex items-center justify-center gap-2">
                <FiPlay size={16} />
                Start Interview
              </button>
            </div>
          ))}
        </div>
      )}

      {interviewing && currentQuestion && (
        <div className="card">
          <h2 className="text-2xl font-bold mb-6">Question {currentQuestion.questionNumber}</h2>
          <div className="bg-gray-50 p-6 rounded-lg mb-6">
            <p className="text-lg font-semibold mb-3">{currentQuestion.question}</p>
            {currentQuestion.context && (
              <p className="text-gray-600 text-sm">{currentQuestion.context}</p>
            )}
          </div>

          <div className="space-y-4">
            <label className="block">
              <span className="text-sm font-semibold text-gray-700">Your Answer:</span>
              <textarea
                value={answer}
                onChange={(e) => setAnswer(e.target.value)}
                className="w-full mt-2 p-3 border rounded-lg focus:ring-2 focus:ring-purple-500 outline-none"
                rows="6"
                placeholder="Type your answer here..."
              />
            </label>
            <button
              onClick={handleSubmitAnswer}
              className="btn-primary w-full"
            >
              Submit Answer
            </button>
          </div>
        </div>
      )}

      {feedback && (
        <div className="card">
          <div className="flex items-center gap-2 mb-6">
            <FiCheckCircle className="text-green-600" size={24} />
            <h2 className="text-2xl font-bold">Interview Feedback</h2>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div className="bg-green-50 p-4 rounded-lg">
              <h3 className="font-bold text-green-900 mb-2">Strengths:</h3>
              <ul className="list-disc list-inside space-y-1">
                {feedback.strengths?.map((strength, idx) => (
                  <li key={idx} className="text-green-800">{strength}</li>
                ))}
              </ul>
            </div>

            <div className="bg-amber-50 p-4 rounded-lg">
              <h3 className="font-bold text-amber-900 mb-2">Areas to Improve:</h3>
              <ul className="list-disc list-inside space-y-1">
                {feedback.improvements?.map((improvement, idx) => (
                  <li key={idx} className="text-amber-800">{improvement}</li>
                ))}
              </ul>
            </div>
          </div>

          <div className="mb-6">
            <h3 className="font-bold text-gray-800 mb-2">Overall Score: {feedback.score}/100</h3>
            <div className="w-full bg-gray-200 rounded-full h-3">
              <div
                className="bg-indigo-600 h-3 rounded-full"
                style={{ width: `${feedback.score}%` }}
              />
            </div>
          </div>

          <button
            onClick={() => {
              setFeedback(null)
              setAnswer('')
            }}
            className="btn-secondary w-full"
          >
            Take Another Interview
          </button>
        </div>
      )}
    </div>
  )
}
