package com.mdreamfever.fstar.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdreamfever.fstar.utils.AesCryptUtil
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice
import java.io.InputStream
import java.lang.reflect.Type

@RestControllerAdvice
class DecryptRequestBodyAdvice : RequestBodyAdvice {
    override fun afterBodyRead(p0: Any, p1: HttpInputMessage, p2: MethodParameter, p3: Type, p4: Class<out HttpMessageConverter<*>>): Any {
        return p0
    }

    override fun beforeBodyRead(p0: HttpInputMessage, p1: MethodParameter, p2: Type, p3: Class<out HttpMessageConverter<*>>): HttpInputMessage {
        return object : HttpInputMessage {
            override fun getHeaders(): HttpHeaders {
                return p0.headers
            }

            override fun getBody(): InputStream {
                val var1 = p0.body.bufferedReader(Charsets.UTF_8).use { it.readText() }
                val mapper = ObjectMapper()
                val var3 = mapper.readValue(var1, Map::class.java)
                val data = var3["data"] as String
                val var2 = AesCryptUtil.decryptS(data)
                return var2.byteInputStream(Charsets.UTF_8)
            }
        }
    }

    override fun handleEmptyBody(p0: Any?, p1: HttpInputMessage, p2: MethodParameter, p3: Type, p4: Class<out HttpMessageConverter<*>>): Any? {
        return p0
    }

    override fun supports(p0: MethodParameter, p1: Type, p2: Class<out HttpMessageConverter<*>>): Boolean {
        return p0.hasParameterAnnotation(RequestBody::class.java) && p0.hasMethodAnnotation(FStarEncrypt::class.java)
    }
}