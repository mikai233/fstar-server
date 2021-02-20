package com.mdreamfever.fstar.controller

import com.mdreamfever.fstar.model.Course
import com.mdreamfever.fstar.model.FResult
import com.mdreamfever.fstar.repository.CourseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v2/course")
class CourseController {
    @Autowired
    private lateinit var courseRepository: CourseRepository

    @GetMapping
    fun getCourse(@RequestParam page: Int, @RequestParam size: Int): FResult {
        val pageRequest = PageRequest.of(page, size)
        return FResult.success(data = courseRepository.findAll(pageRequest))
    }

    @PostMapping
    fun addCourse(@RequestBody courseList: List<Course>, @RequestParam studentNumber: String): FResult {
        courseRepository.deleteAllByStudentNumber(studentNumber)
        val result = courseRepository.saveAll(courseList)
        return FResult.success(data = result)
    }

    @GetMapping("/student/{studentNumber}")
    fun getCourseByStudentNumber(@PathVariable studentNumber: String, @RequestParam semester: String): FResult {
        return FResult.success(data = courseRepository.findAllByStudentNumberAndSemester(studentNumber, semester))
    }
}