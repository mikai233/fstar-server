package com.mdreamfever.fstar.config

import com.mdreamfever.fstar.model.FResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handler(e: Exception): FResult {
        e.printStackTrace()
        return FResult(401, e.message)
    }
}