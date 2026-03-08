import './index.css'
import React from 'react'
import ReactDOM from 'react-dom/client'
import { ClerkProvider } from '@clerk/clerk-react'
import App from './App'

const PUBLISHABLE_KEY = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY

if (!PUBLISHABLE_KEY) {
  throw new Error('Missing Publishable Key')
}

// Store Clerk user ID when app loads
window.addEventListener('load', () => {
  const clerkUser = window.__clerk?.user
  if (clerkUser?.id) {
    localStorage.setItem('clerkUserId', clerkUser.id)
  }
})

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ClerkProvider publishableKey={PUBLISHABLE_KEY} afterSignInUrl="/dashboard">
      <App />
    </ClerkProvider>
  </React.StrictMode>
)
