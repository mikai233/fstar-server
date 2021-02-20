package com.mdreamfever.fstar.repository

import com.mdreamfever.fstar.model.Score
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface ScoreRepository : JpaRepository<Score, String> {
    fun deleteAllByStudentNumber(studentNumber: String)
    fun deleteAllByStudentNumberStartingWith(classNumber: String)
    fun deleteByScoreNo(courseId: String)
    fun findAllByStudentNumber(studentNumber: String): List<Score>
    fun findAllByStudentNumberStartingWith(classNumber: String): List<Score>
    fun findAllByName(scoreName: String, pageable: Pageable): Page<Score>
    override fun findAll(pageable: Pageable): Page<Score>
}