package com.mdreamfever.fstar.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Course(
    @Id val id: Int,
    val studentNumber: String,
    val semester: String,
    val courseId: String,
    val courseName: String,
    val classroom: String,
    val rawWeek: String,
    val courseRow: Int,
    val rowSpan: Int,
    val courseColumn: Int,
    val teacher: String,
    val defaultColor: String,
    val customColor: String,
    val top: Int
)
