package com.mdreamfever.fstar.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdreamfever.fstar.model.FResult
import com.mdreamfever.fstar.utils.AesCryptUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class EncryptResponseBodyAdvice : ResponseBodyAdvice<Any> {
    val logger: Logger = LoggerFactory.getLogger(EncryptResponseBodyAdvice::class.java)
    override fun supports(p0: MethodParameter, p1: Class<out HttpMessageConverter<*>>): Boolean {
        return p0.hasMethodAnnotation(FStarEncrypt::class.java)
    }

    override fun beforeBodyWrite(
        p0: Any?,
        p1: MethodParameter,
        p2: MediaType,
        p3: Class<out HttpMessageConverter<*>>,
        p4: ServerHttpRequest,
        p5: ServerHttpResponse
    ): Any? {
        val mapper = ObjectMapper()
        val obj = mapper.writeValueAsString(p0)
        val encrypt = AesCryptUtil.encryptS(obj)
        return FResult.success(data = encrypt)
    }
}