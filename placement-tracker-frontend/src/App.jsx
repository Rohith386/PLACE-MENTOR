import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { SignedIn, SignedOut, RedirectToSignIn, useUser } from '@clerk/clerk-react'
import { useEffect } from 'react'
import Layout from './components/Layout'
import Dashboard from './pages/Dashboard'
import Roadmap from './pages/Roadmap'
import DreamCompanies from './pages/DreamCompanies'
import MockInterview from './pages/MockInterview'
import Profile from './pages/Profile'
import './App.css'

function App() {
  const { user } = useUser()

  useEffect(() => {
    if (user?.id) {
      localStorage.setItem('clerkUserId', user.id)
      console.log('✅ Clerk User ID stored:', user.id)
    }
  }, [user])

  return (
    <Router>
      <SignedOut>
        <RedirectToSignIn />
      </SignedOut>
      <SignedIn>
        <Layout>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/roadmap" element={<Roadmap />} />
            <Route path="/companies" element={<DreamCompanies />} />
            <Route path="/mock-interview" element={<MockInterview />} />
            <Route path="/profile" element={<Profile />} />
          </Routes>
        </Layout>
      </SignedIn>
    </Router>
  )
}

export default App
