package com.sirdratuti.personalaccount.service

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
class MarketService(
    restTemplateBuilder: RestTemplateBuilder,
) {
    private val restTemplate = restTemplateBuilder.build()

    fun buyStocks(id: Long, stocks: Int) {
        restTemplate.postForObject(
            "$HOST/buy-stocks/{id}?stocks={stocks}", null,
            Long::class.java, id, stocks
        )
    }

    fun sellStocks(id: Long, stocks: Int) {
        restTemplate.postForObject(
            "$HOST/add-stocks/{id}?stocks={stocks}", null,
            Long::class.java, id, stocks
        )
    }

    fun getStockPrice(id: Long): Double {
        val response = restTemplate.getForObject(
            "$HOST/get-company/{id}",
            Map::class.java, id
        ) ?: error("no such company")
        return response["stockPrice"] as Double
    }

    private companion object {
        private const val HOST = "http://localhost:8080/stock-market"
    }
}