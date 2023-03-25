package com.sirdratuti.stockmarket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class StockMarketApplication

fun main(args: Array<String>) {
    runApplication<StockMarketApplication>(*args)
}