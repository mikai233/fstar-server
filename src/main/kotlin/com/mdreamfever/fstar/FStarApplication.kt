package com.mdreamfever.fstar

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.security.Security

@EnableScheduling
@SpringBootApplication
class FStarApplication

fun main(args: Array<String>) {
    Security.addProvider(BouncyCastleProvider())
    runApplication<FStarApplication>(*args)
}
