package com.sirdratuti.personalaccount

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class UserAccountApplication

fun main(args: Array<String>) {
    runApplication<UserAccountApplication>(*args)
}