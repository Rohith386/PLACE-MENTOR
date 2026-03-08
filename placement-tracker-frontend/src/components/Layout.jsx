import { Link, useLocation } from 'react-router-dom'
import { UserButton } from '@clerk/clerk-react'
import { FiHome, FiMap, FiAward, FiMic, FiUser } from 'react-icons/fi'
import './Layout.css'

export default function Layout({ children }) {
  const location = useLocation()

  const navItems = [
    { path: '/', label: 'Dashboard', icon: FiHome },
    { path: '/roadmap', label: 'My Roadmap', icon: FiMap },
    { path: '/companies', label: 'Dream Companies', icon: FiAward },
    { path: '/mock-interview', label: 'Mock Interview', icon: FiMic },
    { path: '/profile', label: 'Profile', icon: FiUser },
  ]

  return (
    <div className="app-container">
      <aside className="sidebar">
        <div className="sidebar-header p-6">
          <h1 className="text-2xl font-bold text-indigo-600">PlacementAI</h1>
          <p className="text-sm text-gray-500 mt-1">Your Career Mentor</p>
        </div>

        <nav className="sidebar-nav">
          {navItems.map(({ path, label, icon: Icon }) => (
            <Link
              key={path}
              to={path}
              className={`nav-item flex items-center gap-3 px-6 py-4 transition ${
                location.pathname === path
                  ? 'bg-indigo-50 text-indigo-600 border-l-4 border-indigo-600'
                  : 'text-gray-700 hover:bg-gray-50'
              }`}
            >
              <Icon size={20} />
              <span>{label}</span>
            </Link>
          ))}
        </nav>
      </aside>

      <div className="main-content flex flex-col flex-1">
        <header className="bg-white shadow-sm border-b">
          <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">
            <h2 className="text-xl font-semibold text-gray-800">
              Placement Preparation Tracker
            </h2>
            <UserButton />
          </div>
        </header>

        <main className="flex-1 p-6">
          <div className="container">{children}</div>
        </main>
      </div>
    </div>
  )
}
