package com.mdreamfever.fstar.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("clientVersion", description = "客户端数据类")
data class ClientVersion(@ApiModelProperty("客户端软件版本") val version: String, @ApiModelProperty("构建号") val buildNumber: String, @ApiModelProperty("安卓ID") val androidId: String, val brand: String, val device: String, @ApiModelProperty("安卓版本") val androidVersion: String, val model: String, val product: String, @ApiModelProperty("平台") val platform: String)