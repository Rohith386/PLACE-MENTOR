import { useState, useEffect } from 'react'
import { useUser } from '@clerk/clerk-react'
import { studentService, leetcodeService } from '../services/api'

export default function Profile() {
  const { user } = useUser()
  const [profile, setProfile] = useState(null)
  const [editing, setEditing] = useState(false)
  const [formData, setFormData] = useState({})
  const [loading, setLoading] = useState(true)
  const [profileError, setProfileError] = useState('')
  const [leetcodeStats, setLeetcodeStats] = useState(null)
  const [leetcodeLoading, setLeetcodeLoading] = useState(false)
  const [editingLeetcode, setEditingLeetcode] = useState(false)
  const [leetcodeUsername, setLeetcodeUsername] = useState('')
  const [leetcodeError, setLeetcodeError] = useState('')

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await studentService.getProfile()
        setProfile(response.data)
        setFormData(response.data)
        setLeetcodeUsername(response.data.leetcodeUsername || '')
        setProfileError('')
      } catch (error) {
        console.error('Failed to fetch profile:', error)
        setProfile(null)
        setProfileError(
          error.response?.data?.message || 'Please create your profile first'
        )
      } finally {
        setLoading(false)
      }
    }
    fetchProfile()
  }, [])

  useEffect(() => {
    if (profile?.leetcodeUsername && !editingLeetcode) {
      fetchLeetcodeStats()
    }
  }, [profile?.leetcodeUsername, editingLeetcode])

  const fetchLeetcodeStats = async () => {
    if (!profile?.leetcodeUsername) return
    setLeetcodeLoading(true)
    setLeetcodeError('')
    try {
      const response = await leetcodeService.getStats()
      console.log('LeetCode response:', response.data)
      
      if (response.data.errorMessage) {
        setLeetcodeError(response.data.errorMessage)
        setLeetcodeStats(null)
      } else if (response.data.message && !response.data.username) {
        setLeetcodeError(response.data.message)
        setLeetcodeStats(null)
      } else if (response.data.username) {
        setLeetcodeStats(response.data)
        setLeetcodeError('')
      } else {
        setLeetcodeError('Invalid response format')
        setLeetcodeStats(null)
      }
    } catch (error) {
      console.error('Failed to fetch LeetCode stats:', error)
      const errorMsg = error.response?.data?.errorMessage || 
                       error.response?.data?.error || 
                       error.message ||
                       'Failed to fetch LeetCode stats. Please check username and try again.'
      setLeetcodeError(errorMsg)
      setLeetcodeStats(null)
    } finally {
      setLeetcodeLoading(false)
    }
  }

  const handleSave = async () => {
    try {
      await studentService.updateProfile(formData)
      setProfile(formData)
      setEditing(false)
    } catch (error) {
      console.error('Failed to update profile:', error)
    }
  }

  const handleCreateProfile = async () => {
    try {
      const profileData = {
        email: user?.emailAddresses[0]?.emailAddress || '',
        firstName: user?.firstName || '',
        lastName: user?.lastName || '',
        branch: 'Not specified',
        year: 1,
        skillLevel: 'beginner',
      }
      await studentService.createProfile(profileData)
      const response = await studentService.getProfile()
      setProfile(response.data)
      setFormData(response.data)
      setLeetcodeUsername(response.data.leetcodeUsername || '')
      setProfileError('')
    } catch (error) {
      console.error('Failed to create profile:', error)
      setProfileError('Failed to create profile. Please try again.')
    }
  }

  const handleLeetcodeUpdate = async () => {
    if (!leetcodeUsername.trim()) {
      setLeetcodeError('Username cannot be empty')
      return
    }
    try {
      await leetcodeService.updateUsername(leetcodeUsername)
      setProfile({ ...profile, leetcodeUsername })
      setFormData({ ...formData, leetcodeUsername })
      setEditingLeetcode(false)
      setLeetcodeError('')
    } catch (error) {
      console.error('Failed to update LeetCode username:', error)
      setLeetcodeError(
        error.response?.data?.error || 'Failed to update LeetCode username'
      )
    }
  }

  if (loading) return <div className="text-center py-12">Loading profile...</div>

  if (!profile && profileError) {
    return (
      <div className="space-y-6">
        <div className="bg-gradient-to-r from-indigo-600 to-pink-500 text-white rounded-lg p-6">
          <h1 className="text-3xl font-bold">Your Profile</h1>
        </div>

        <div className="card">
          <div className="text-center py-12">
            <div className="mb-6">
              <div className="text-6xl mb-4">👤</div>
              <h2 className="text-2xl font-bold text-gray-800 mb-2">
                Welcome to PlacementAI!
              </h2>
              <p className="text-gray-600 mb-4">{profileError}</p>
            </div>
            <button
              onClick={handleCreateProfile}
              className="px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 font-semibold"
            >
              Create My Profile
            </button>
          </div>
        </div>
      </div>
    )
  }

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

      {/* LeetCode Stats Section */}
      <div className="card">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-xl font-bold flex items-center gap-2">
            <span className="text-2xl">⚡</span> LeetCode Stats
          </h3>
          {!editingLeetcode && (
            <button
              onClick={() => setEditingLeetcode(true)}
              className="btn-primary"
            >
              {profile?.leetcodeUsername ? 'Update' : 'Add'} Username
            </button>
          )}
        </div>

        {editingLeetcode && (
          <div className="mb-6 p-4 bg-blue-50 rounded-lg space-y-3">
            <div>
              <label className="block text-sm font-semibold mb-2">LeetCode Username</label>
              <input
                type="text"
                value={leetcodeUsername}
                onChange={(e) => setLeetcodeUsername(e.target.value)}
                placeholder="Enter your LeetCode username"
                className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
              />
            </div>
            {leetcodeError && (
              <div className="text-sm text-red-600 bg-red-50 p-3 rounded">
                {leetcodeError}
              </div>
            )}
            <div className="flex gap-3">
              <button
                onClick={handleLeetcodeUpdate}
                className="btn-primary flex-1"
              >
                Save
              </button>
              <button
                onClick={() => {
                  setEditingLeetcode(false)
                  setLeetcodeUsername(profile?.leetcodeUsername || '')
                  setLeetcodeError('')
                }}
                className="flex-1 px-4 py-2 border rounded-lg hover:bg-gray-50"
              >
                Cancel
              </button>
            </div>
          </div>
        )}

        {profile?.leetcodeUsername && (
          <>
            {leetcodeLoading ? (
              <div className="text-center py-8">
                <div className="inline-block">
                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600 mb-3"></div>
                </div>
                <p className="text-gray-600">Loading LeetCode stats for @{profile?.leetcodeUsername}...</p>
              </div>
            ) : leetcodeError ? (
              <div className="text-center py-8">
                <div className="mb-4 text-red-600">
                  <p className="text-lg font-semibold mb-2">⚠️ Error Loading Stats</p>
                  <p className="text-sm mb-4">{leetcodeError}</p>
                  <p className="text-xs text-gray-500 mb-4">
                    Make sure your LeetCode username is correct: <strong>{profile?.leetcodeUsername}</strong>
                  </p>
                </div>
                <div className="flex gap-3 justify-center">
                  <button
                    onClick={fetchLeetcodeStats}
                    className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
                  >
                    Retry
                  </button>
                  <button
                    onClick={() => setEditingLeetcode(true)}
                    className="px-4 py-2 border border-indigo-600 text-indigo-600 rounded-lg hover:bg-indigo-50 transition"
                  >
                    Change Username
                  </button>
                </div>
              </div>
            ) : leetcodeStats ? (
              <div className="space-y-6">
                <div className="flex items-center gap-4 pb-6 border-b">
                  <div>
                    <p className="text-sm text-gray-600">LeetCode Profile</p>
                    <p className="text-xl font-bold">{leetcodeStats.username}</p>
                    {leetcodeStats.rank && (
                      <p className="text-sm text-indigo-600 font-semibold">
                        Rank: #{leetcodeStats.rank}
                      </p>
                    )}
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="p-4 bg-gradient-to-br from-green-50 to-green-100 rounded-lg">
                    <p className="text-sm text-gray-600 mb-2">Total Solved</p>
                    <p className="text-3xl font-bold text-green-600">
                      {leetcodeStats.totalSolved}
                    </p>
                    <p className="text-xs text-gray-600 mt-1">
                      of {leetcodeStats.totalQuestions} problems
                    </p>
                  </div>

                  <div className="p-4 bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg">
                    <p className="text-sm text-gray-600 mb-2">Acceptance Rate</p>
                    <p className="text-3xl font-bold text-blue-600">
                      {leetcodeStats.acceptanceRate?.toFixed(2)}%
                    </p>
                  </div>

                  <div className="p-4 bg-gradient-to-br from-green-50 to-green-100 rounded-lg">
                    <p className="text-sm text-gray-600 mb-2">Easy</p>
                    <p className="text-2xl font-bold text-green-600">
                      {leetcodeStats.easySolved}/{leetcodeStats.easyTotal}
                    </p>
                  </div>

                  <div className="p-4 bg-gradient-to-br from-yellow-50 to-yellow-100 rounded-lg">
                    <p className="text-sm text-gray-600 mb-2">Medium</p>
                    <p className="text-2xl font-bold text-yellow-600">
                      {leetcodeStats.mediumSolved}/{leetcodeStats.mediumTotal}
                    </p>
                  </div>

                  <div className="p-4 bg-gradient-to-br from-red-50 to-red-100 rounded-lg">
                    <p className="text-sm text-gray-600 mb-2">Hard</p>
                    <p className="text-2xl font-bold text-red-600">
                      {leetcodeStats.hardSolved}/{leetcodeStats.hardTotal}
                    </p>
                  </div>

                  {leetcodeStats.contributionPoints && (
                    <div className="p-4 bg-gradient-to-br from-purple-50 to-purple-100 rounded-lg">
                      <p className="text-sm text-gray-600 mb-2">Contribution</p>
                      <p className="text-2xl font-bold text-purple-600">
                        {leetcodeStats.contributionPoints}
                      </p>
                    </div>
                  )}
                </div>

                <div className="text-center">
                  <a
                    href={`https://leetcode.com/${leetcodeStats.username}`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="inline-block px-6 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 font-semibold transition"
                  >
                    View on LeetCode
                  </a>
                </div>
              </div>
            ) : null}
          </>
        )}

        {!profile?.leetcodeUsername && !editingLeetcode && (
          <div className="text-center py-8 text-gray-600">
            <p className="mb-4">No LeetCode username set yet</p>
            <p className="text-sm">Add your username to view your LeetCode stats</p>
          </div>
        )}
      </div>
    </div>
  )
}
