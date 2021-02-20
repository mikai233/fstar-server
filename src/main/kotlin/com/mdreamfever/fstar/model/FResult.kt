package com.mdreamfever.fstar.model

data class FResult(val code: Int? = 200, val message: String? = null, val data: Any? = null) {
    companion object {
        fun success(message: String? = null, data: Any? = null) = FResult(200, message, data)
        fun failed(message: String? = null, data: Any? = null) = FResult(400, message, data)
        fun unauthorized(data: Any? = null) = FResult(401, "未登录或token过期", data)
        fun validateFailed(data: Any? = null) = FResult(401, "参数校验失败", data)
        fun forbidden(data: Any? = null) = FResult(403, "没有权限", data)
    }
}
