package com.mdreamfever.fstar.controller

import com.mdreamfever.fstar.model.FResult
import com.mdreamfever.fstar.model.Score
import com.mdreamfever.fstar.repository.ScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v2/score")
class ScoreController {

    @Autowired
    private lateinit var scoreRepository: ScoreRepository

    @GetMapping("/student/{studentNumber}")
    fun getScoreByStudentNumber(@PathVariable studentNumber: String): FResult {
        return FResult.success(data = scoreRepository.findAllByStudentNumber(studentNumber))
    }

    @GetMapping("/class/{classNumber}")
    fun getScoreByClassNumber(@PathVariable classNumber: String): FResult {
        return FResult.success(data = scoreRepository.findAllByStudentNumberStartingWith(classNumber))
    }

    @GetMapping("/name/{scoreName}")
    fun getScoreByScoreName(
        @PathVariable scoreName: String,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): FResult {
        val pageRequest = PageRequest.of(page, size)
        return FResult.success(data = scoreRepository.findAllByName(scoreName, pageRequest))
    }

    @PostMapping
    @Transactional
    fun addScore(@RequestBody scores: List<Score>): FResult {
        if (scores.isNotEmpty()) {
            scoreRepository.deleteAllByStudentNumber(scores.first().studentNumber)
        }
        val result = scoreRepository.saveAll(scores)
        return FResult.success(data = result)
    }

    @DeleteMapping("/student/{studentNumber}")
    fun deleteScoreByStudentNumber(@PathVariable studentNumber: String): FResult {
        scoreRepository.deleteAllByStudentNumber(studentNumber)
        return FResult.success()
    }
}