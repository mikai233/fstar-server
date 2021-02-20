package com.mdreamfever.fstar.model

import javax.persistence.*

@Entity
@Table(name = "score_v2")
data class Score(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
    val studentNumber: String,
    val no: String,
    val semester: String,
    val scoreNo: String,
    val name: String,
    val score: String,
    val credit: String,
    val period: String,
    val evaluationMode: String,
    val courseProperty: String,
    val courseNature: String,
    val alternativeCourseNumber: String,
    val alternativeCourseName: String,
    val scoreFlag: String
)
