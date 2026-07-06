import { useState, useEffect } from 'react'
import { mockInterviewService } from '../services/api'
import { FiPlay, FiClock, FiCheckCircle, FiArrowRight, FiRotateCw } from 'react-icons/fi'

export default function MockInterview() {
  const [interviewType, setInterviewType] = useState(null)
  const [interviewing, setInterviewing] = useState(false)
  const [currentQuestion, setCurrentQuestion] = useState(null)
  const [currentInterviewId, setCurrentInterviewId] = useState(null)
  const [answer, setAnswer] = useState('')
  const [feedback, setFeedback] = useState(null)
  const [history, setHistory] = useState([])
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0)
  const [allQuestions, setAllQuestions] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [showHistory, setShowHistory] = useState(false)

  const interviewTypes = [
    { id: 'dsa', name: 'DSA Coding Round', duration: '60 min', icon: '💻' },
    { id: 'behavioral', name: 'Behavioral Interview', duration: '30 min', icon: '🎤' },
    { id: 'hr', name: 'HR Round', duration: '20 min', icon: '👔' },
  ]

  useEffect(() => {
    loadInterviewHistory()
  }, [])

  const loadInterviewHistory = async () => {
    try {
      const response = await mockInterviewService.getInterviewHistory()
      setHistory(response.data)
    } catch (error) {
      console.error('Failed to load interview history:', error)
    }
  }

  const handleStartInterview = async (type) => {
    setLoading(true)
    setError(null)
    try {
      const response = await mockInterviewService.startInterview(type)
      const { interview, firstQuestion } = response.data
      
      setInterviewType(type)
      setCurrentInterviewId(interview.id)
      setCurrentQuestion(firstQuestion)
      setCurrentQuestionIndex(0)
      setAllQuestions([firstQuestion])
      setAnswer('')
      setFeedback(null)
      setInterviewing(true)
    } catch (err) {
      setError('Failed to start interview. Please try again.')
      console.error('Failed to start interview:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmitAnswer = async () => {
    if (!answer.trim()) {
      setError('Please provide an answer before submitting.')
      return
    }

    setLoading(true)
    setError(null)
    try {
      const response = await mockInterviewService.submitAnswer(
        currentInterviewId,
        currentQuestion.id,
        answer
      )

      const { nextQuestion } = response.data

      if (nextQuestion && nextQuestion !== 'completed') {
        // Move to next question
        setCurrentQuestion(nextQuestion)
        setAllQuestions([...allQuestions, nextQuestion])
        setCurrentQuestionIndex(currentQuestionIndex + 1)
        setAnswer('')
      } else {
        // Interview completed
        const feedbackRes = await mockInterviewService.getInterviewFeedback(currentInterviewId)
        setFeedback(feedbackRes.data)
        setInterviewing(false)
        loadInterviewHistory()
      }
    } catch (err) {
      setError('Failed to submit answer. Please try again.')
      console.error('Failed to submit answer:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleSkipQuestion = async () => {
    // Move to next question without answering
    try {
      const nextQuestionResponse = await mockInterviewService.submitAnswer(
        currentInterviewId,
        currentQuestion.id,
        "Skipped"
      )
      
      const { nextQuestion } = nextQuestionResponse.data
      if (nextQuestion && nextQuestion !== 'completed') {
        setCurrentQuestion(nextQuestion)
        setAllQuestions([...allQuestions, nextQuestion])
        setCurrentQuestionIndex(currentQuestionIndex + 1)
        setAnswer('')
      } else {
        const feedbackRes = await mockInterviewService.getInterviewFeedback(currentInterviewId)
        setFeedback(feedbackRes.data)
        setInterviewing(false)
        loadInterviewHistory()
      }
    } catch (err) {
      setError('Failed to move to next question.')
      console.error('Error:', err)
    }
  }

  const resetInterview = () => {
    setInterviewType(null)
    setCurrentQuestion(null)
    setCurrentInterviewId(null)
    setAnswer('')
    setFeedback(null)
    setCurrentQuestionIndex(0)
    setAllQuestions([])
    setError(null)
    loadInterviewHistory()
  }

  return (
    <div className="space-y-6">
      <div className="bg-purple-50 border-l-4 border-purple-500 p-6 rounded">
        <h1 className="text-2xl font-bold text-purple-900">Mock Interview System</h1>
        <p className="text-purple-700 mt-2">Practice with AI-powered mock interviews to boost your confidence!</p>
      </div>

      {error && (
        <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded">
          <p className="text-red-700">{error}</p>
        </div>
      )}

      {!interviewing && !feedback && !showHistory && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {interviewTypes.map((type) => (
              <div key={type.id} className="card hover:shadow-lg transition cursor-pointer" onClick={() => handleStartInterview(type.id)}>
                <div className="text-4xl mb-4">{type.icon}</div>
                <h3 className="text-lg font-bold mb-2">{type.name}</h3>
                <div className="flex items-center gap-2 text-gray-500 mb-4">
                  <FiClock size={16} />
                  <span>{type.duration}</span>
                </div>
                <button className="btn-primary w-full flex items-center justify-center gap-2" disabled={loading}>
                  <FiPlay size={16} />
                  {loading ? 'Loading...' : 'Start Interview'}
                </button>
              </div>
            ))}
          </div>

          {history.length > 0 && (
            <div className="card">
              <h2 className="text-xl font-bold mb-4">Interview History</h2>
              <button 
                onClick={() => setShowHistory(true)}
                className="btn-secondary text-purple-600 hover:bg-purple-50"
              >
                View {history.length} Past Interview{history.length !== 1 ? 's' : ''}
              </button>
            </div>
          )}
        </div>
      )}

      {interviewing && currentQuestion && (
        <div className="card">
          <div className="mb-6 flex justify-between items-center">
            <h2 className="text-2xl font-bold">Question {currentQuestionIndex + 1}</h2>
            <span className="text-sm text-gray-600">Difficulty: {'⭐'.repeat(currentQuestion.difficulty)}</span>
          </div>

          <div className="bg-gray-50 p-6 rounded-lg mb-6">
            <p className="text-lg font-semibold mb-3">{currentQuestion.question}</p>
            {currentQuestion.context && (
              <p className="text-gray-600 text-sm italic">{currentQuestion.context}</p>
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
                disabled={loading}
              />
            </label>

            <div className="flex gap-3">
              <button
                onClick={handleSubmitAnswer}
                className="btn-primary flex-1 flex items-center justify-center gap-2"
                disabled={loading || !answer.trim()}
              >
                <FiCheckCircle size={16} />
                {loading ? 'Submitting...' : 'Submit Answer'}
              </button>
              <button
                onClick={handleSkipQuestion}
                className="btn-secondary flex-1 flex items-center justify-center gap-2"
                disabled={loading}
              >
                <FiArrowRight size={16} />
                Skip Question
              </button>
            </div>
          </div>
        </div>
      )}

      {feedback && !interviewing && (
        <div className="card">
          <div className="text-center mb-6">
            <div className="text-5xl mb-3">🎉</div>
            <h2 className="text-3xl font-bold text-green-600">Interview Completed!</h2>
            <p className="text-gray-600 mt-2">Great job completing the {interviewType} round</p>
          </div>

          <div className="bg-blue-50 border-l-4 border-blue-500 p-6 rounded mb-6">
            <div className="grid grid-cols-3 gap-4 text-center">
              <div>
                <p className="text-3xl font-bold text-blue-600">{feedback.score || 0}</p>
                <p className="text-gray-700 text-sm">Overall Score</p>
              </div>
              <div>
                <p className="text-3xl font-bold text-blue-600">{feedback.answeredQuestions}/{feedback.totalQuestions}</p>
                <p className="text-gray-700 text-sm">Questions Answered</p>
              </div>
              <div>
                <p className="text-3xl font-bold text-blue-600">{Math.round((feedback.answeredQuestions/feedback.totalQuestions)*100)}%</p>
                <p className="text-gray-700 text-sm">Completion Rate</p>
              </div>
            </div>
          </div>

          {feedback.feedback && (
            <div className="bg-purple-50 p-4 rounded mb-6">
              <h3 className="font-bold text-purple-900 mb-2">Feedback</h3>
              <p className="text-purple-800 text-sm">{feedback.feedback}</p>
            </div>
          )}

          <button
            onClick={resetInterview}
            className="btn-primary w-full flex items-center justify-center gap-2"
          >
            <FiRotateCw size={16} />
            Take Another Interview
          </button>
        </div>
      )}

      {showHistory && (
        <div className="card">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold">Interview History</h2>
            <button 
              onClick={() => setShowHistory(false)}
              className="text-gray-500 hover:text-gray-700 text-2xl"
            >
              ✕
            </button>
          </div>

          {history.length === 0 ? (
            <p className="text-gray-600">No interviews completed yet. Start one now!</p>
          ) : (
            <div className="space-y-3">
              {history.map((interview, idx) => (
                <div key={idx} className="border rounded-lg p-4 hover:bg-gray-50 transition">
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="font-bold text-lg capitalize">{interview.type} Interview</h3>
                      <p className="text-gray-600 text-sm">
                        {new Date(interview.startedAt).toLocaleDateString()}
                      </p>
                    </div>
                    <div className="text-right">
                      <p className="text-2xl font-bold text-purple-600">{interview.score || 0}</p>
                      <p className="text-gray-600 text-sm">Score</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}

          <button 
            onClick={() => setShowHistory(false)}
            className="btn-secondary w-full mt-4"
          >
            Close
          </button>
        </div>
      )}
    </div>
  )
}
