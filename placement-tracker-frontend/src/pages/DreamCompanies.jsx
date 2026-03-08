import { useState, useEffect } from 'react'
import { companyService } from '../services/api'
import { FiX, FiPlus } from 'react-icons/fi'
import { PieChart, Pie, Cell, ResponsiveContainer } from 'recharts'

export default function DreamCompanies() {
  const [companies, setCompanies] = useState([])
  const [selectedCompanies, setSelectedCompanies] = useState([])
  const [loading, setLoading] = useState(true)
  const [showModal, setShowModal] = useState(false)

  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        const response = await companyService.getAllCompanies()
        setCompanies(response.data)
      } catch (error) {
        console.error('Failed to fetch companies:', error)
      } finally {
        setLoading(false)
      }
    }
    fetchCompanies()
  }, [])

  const handleAddCompany = async (companyId) => {
    try {
      await companyService.addStudentCompany(companyId)
      setSelectedCompanies([...selectedCompanies, companies.find(c => c.id === companyId)])
    } catch (error) {
      console.error('Failed to add company:', error)
    }
  }

  const handleRemoveCompany = async (companyId) => {
    try {
      await companyService.removeStudentCompany(companyId)
      setSelectedCompanies(selectedCompanies.filter(c => c.id !== companyId))
    } catch (error) {
      console.error('Failed to remove company:', error)
    }
  }

  const probabilityData = selectedCompanies.map(c => ({
    name: c.name,
    value: c.probability || 0
  }))

  if (loading) return <div className="text-center py-12">Loading companies...</div>

  return (
    <div className="space-y-6">
      <div className="bg-pink-50 border-l-4 border-pink-500 p-6 rounded">
        <h1 className="text-2xl font-bold text-pink-900">Dream Companies</h1>
        <p className="text-pink-700 mt-2">Select your target companies and track your probability of cracking each one!</p>
      </div>

      {/* Probability Chart */}
      {selectedCompanies.length > 0 && (
        <div className="card">
          <h2 className="text-xl font-bold mb-6">Your Crack Probability by Company</h2>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={probabilityData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, value }) => `${name} - ${value}%`}
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
              >
                {probabilityData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={['#6366f1', '#ec4899', '#10b981', '#f59e0b', '#ef4444'][index % 5]} />
                ))}
              </Pie>
            </PieChart>
          </ResponsiveContainer>
        </div>
      )}

      {/* Selected Companies */}
      <div className="card">
        <h2 className="text-xl font-bold mb-4">Your Selected Companies</h2>
        {selectedCompanies.length === 0 ? (
          <p className="text-gray-500">Add companies to get started!</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {selectedCompanies.map((company) => (
              <div key={company.id} className="border rounded-lg p-4 relative">
                <button
                  onClick={() => handleRemoveCompany(company.id)}
                  className="absolute top-2 right-2 text-gray-400 hover:text-red-600"
                >
                  <FiX size={20} />
                </button>
                <h3 className="font-bold text-lg mb-3">{company.name}</h3>
                <div className="space-y-2">
                  <div className="flex justify-between items-center">
                    <span className="text-gray-600">Crack Probability</span>
                    <span className="text-2xl font-bold text-indigo-600">{company.probability}%</span>
                  </div>
                  <p className="text-sm text-gray-500">{company.interviewQuestions?.length || 0} frequent questions</p>
                  <button className="w-full mt-3 text-indigo-600 hover:text-indigo-800 font-semibold text-sm">
                    View Questions →
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Add Companies Modal */}
      <div className="card">
        <button
          onClick={() => setShowModal(!showModal)}
          className="btn-primary flex items-center gap-2 mb-4"
        >
          <FiPlus /> Add More Companies
        </button>

        {showModal && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {companies.filter(c => !selectedCompanies.find(s => s.id === c.id)).map((company) => (
              <div key={company.id} className="border rounded-lg p-4">
                <h3 className="font-bold text-lg mb-2">{company.name}</h3>
                <button
                  onClick={() => handleAddCompany(company.id)}
                  className="w-full bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg transition"
                >
                  Add to My List
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
