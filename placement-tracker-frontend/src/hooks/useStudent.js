import { useEffect, useState } from 'react'
import { useStudentStore } from '../store/studentStore'
import { studentService } from '../services/api'

export const useStudent = () => {
  const [loading, setLoading] = useState(true)
  const { student, setStudent } = useStudentStore()

  useEffect(() => {
    const fetchStudent = async () => {
      try {
        const response = await studentService.getProfile()
        setStudent(response.data)
      } catch (error) {
        console.error('Failed to fetch student profile:', error)
      } finally {
        setLoading(false)
      }
    }

    if (!student) {
      fetchStudent()
    } else {
      setLoading(false)
    }
  }, [student, setStudent])

  return { student, loading }
}

export const useProgress = () => {
  const [loading, setLoading] = useState(true)
  const { progress, setProgress } = useStudentStore()

  useEffect(() => {
    const fetchProgress = async () => {
      try {
        const response = await studentService.getProgress()
        setProgress(response.data)
      } catch (error) {
        console.error('Failed to fetch progress:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchProgress()
  }, [setProgress])

  return { progress, loading }
}
