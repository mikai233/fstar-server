package com.mdreamfever.fstar.repository

import com.mdreamfever.fstar.model.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface CourseRepository : JpaRepository<Course, Int> {
    fun findAllByStudentNumberAndSemester(studentNumber: String, semester: String): List<Course>
    fun deleteAllByStudentNumber(studentNumber: String)
}