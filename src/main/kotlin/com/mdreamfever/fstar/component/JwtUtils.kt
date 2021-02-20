package com.mdreamfever.fstar.component

import com.mdreamfever.fstar.config.CustomConfig
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {
    private val logger = LoggerFactory.getLogger(javaClass)
    val CLAIM_KEY_USERNAME = "sub"
    val CLAIM_KEY_CREATED = "created"

    @Autowired
    private lateinit var customConfig: CustomConfig

    /**
     * 根据负责生成JWT的token
     */
    private fun generateToken(claims: Map<String, Any>): String {
        return Jwts.builder().run {
            setClaims(claims)
            setExpiration(generateExpirationDate())
            signWith(SignatureAlgorithm.HS512, customConfig.tokenSecret)
            compact()
        }
    }

    /**
     * 从token中获取JWT中的负载
     */
    private fun getClaimsFromToken(token: String): Claims? {
        return try {
            Jwts.parser()
                .setSigningKey(customConfig.tokenSecret)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            logger.info("JWT格式验证失败:{}", token)
            null
        }
    }

    /**
     * 生成token的过期时间
     */
    private fun generateExpirationDate(): Date {
        return Date(System.currentTimeMillis() + customConfig.tokenExpiration * 1000)
    }

    /**
     * 从token中获取登录用户名
     */
    fun getUserNameFromToken(token: String): String? {
        return try {
            val claims = getClaimsFromToken(token)
            print(claims)
            claims?.get(CLAIM_KEY_USERNAME) as String
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 验证token是否还有效
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUserNameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    /**
     * 判断token是否已经失效
     */
    private fun isTokenExpired(token: String): Boolean {
        val expiredDate: Date = getExpiredDateFromToken(token)
        return expiredDate.before(Date())
    }

    /**
     * 从token中获取过期时间
     */
    private fun getExpiredDateFromToken(token: String): Date {
        val claims = getClaimsFromToken(token)
        return claims!!.expiration
    }

    /**
     * 根据用户信息生成token
     */
    fun generateToken(userDetails: UserDetails): String {
        val claims = mapOf(CLAIM_KEY_USERNAME to userDetails.username, CLAIM_KEY_CREATED to Date())
        return generateToken(claims)
    }

    /**
     * 判断token是否可以被刷新
     */
    fun canRefresh(token: String): Boolean {
        return !isTokenExpired(token)
    }

    /**
     * 刷新token
     */
    fun refreshToken(token: String): String {
        val claims = getClaimsFromToken(token)
        claims!![CLAIM_KEY_CREATED] = Date()
        return generateToken(claims)
    }
}