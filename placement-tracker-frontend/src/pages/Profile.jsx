import { useState, useEffect } from 'react'
import { useUser } from '@clerk/clerk-react'
import { studentService } from '../services/api'

export default function Profile() {
  const { user } = useUser()
  const [profile, setProfile] = useState(null)
  const [editing, setEditing] = useState(false)
  const [formData, setFormData] = useState({})
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await studentService.getProfile()
        setProfile(response.data)
        setFormData(response.data)
      } catch (error) {
        console.error('Failed to fetch profile:', error)
      } finally {
        setLoading(false)
      }
    }
    fetchProfile()
  }, [])

  const handleSave = async () => {
    try {
      await studentService.updateProfile(formData)
      setProfile(formData)
      setEditing(false)
    } catch (error) {
      console.error('Failed to update profile:', error)
    }
  }

  if (loading) return <div className="text-center py-12">Loading profile...</div>

  return (
    <div className="space-y-6">
      <div className="bg-gradient-to-r from-indigo-600 to-pink-500 text-white rounded-lg p-6">
        <h1 className="text-3xl font-bold">Your Profile</h1>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="card">
          <div className="text-center">
            <img
              src={user?.imageUrl}
              alt="Profile"
              className="w-24 h-24 rounded-full mx-auto mb-4"
            />
            <h2 className="text-2xl font-bold">{user?.firstName} {user?.lastName}</h2>
            <p className="text-gray-600">{user?.emailAddresses[0]?.emailAddress}</p>
          </div>
        </div>

        <div className="lg:col-span-2 card">
          {!editing ? (
            <div>
              <div className="flex justify-between items-center mb-6">
                <h3 className="text-xl font-bold">Profile Information</h3>
                <button
                  onClick={() => setEditing(true)}
                  className="btn-primary"
                >
                  Edit Profile
                </button>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="text-sm font-semibold text-gray-600">Branch</label>
                  <p className="text-lg">{profile?.branch || 'Not specified'}</p>
                </div>
                <div>
                  <label className="text-sm font-semibold text-gray-600">Year</label>
                  <p className="text-lg">{profile?.year || 'Not specified'}</p>
                </div>
                <div>
                  <label className="text-sm font-semibold text-gray-600">Current Skill Level</label>
                  <p className="text-lg">{profile?.skillLevel || 'Not specified'}</p>
                </div>
              </div>
            </div>
          ) : (
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-semibold mb-2">Branch</label>
                <input
                  type="text"
                  value={formData.branch || ''}
                  onChange={(e) => setFormData({ ...formData, branch: e.target.value })}
                  className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                />
              </div>
              <div>
                <label className="block text-sm font-semibold mb-2">Year</label>
                <select
                  value={formData.year || ''}
                  onChange={(e) => setFormData({ ...formData, year: e.target.value })}
                  className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                >
                  <option value="">Select Year</option>
                  <option value="1">1st Year</option>
                  <option value="2">2nd Year</option>
                  <option value="3">3rd Year</option>
                  <option value="4">4th Year</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-semibold mb-2">Skill Level</label>
                <select
                  value={formData.skillLevel || ''}
                  onChange={(e) => setFormData({ ...formData, skillLevel: e.target.value })}
                  className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                >
                  <option value="">Select Level</option>
                  <option value="beginner">Beginner</option>
                  <option value="intermediate">Intermediate</option>
                  <option value="advanced">Advanced</option>
                </select>
              </div>
              <div className="flex gap-3">
                <button onClick={handleSave} className="btn-primary flex-1">Save</button>
                <button
                  onClick={() => {
                    setEditing(false)
                    setFormData(profile)
                  }}
                  className="flex-1 px-4 py-2 border rounded-lg hover:bg-gray-50"
                >
                  Cancel
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
