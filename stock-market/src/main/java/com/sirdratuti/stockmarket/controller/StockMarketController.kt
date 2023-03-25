package com.sirdratuti.stockmarket.controller

import com.sirdratuti.stockmarket.data.Company
import com.sirdratuti.stockmarket.service.StockMarketService
import org.springframework.web.bind.annotation.*

@RequestMapping("stock-market")
@RestController
class StockMarketController(
    private val stockMarketService: StockMarketService,
) {
    @PostMapping("/add-company")
    fun addCompany(
        @RequestParam id: Long,
        @RequestParam stocks: Int,
        @RequestParam price: Double,
    ) = stockMarketService.addCompany(
        Company(id, stocks, price)
    )

    @PostMapping("/add-stocks/{id}")
    fun addStocks(
        @PathVariable id: Long,
        @RequestParam stocks: Int,
    ) = stockMarketService.addStocks(id, stocks)

    @GetMapping("/get-companies")
    fun getCompany(): List<Company> =
        stockMarketService.getCompanies()

    @GetMapping("/get-company/{id}")
    fun getCompany(
        @PathVariable id: Long,
    ) = stockMarketService.getCompany(id)

    @PostMapping("/buy-stocks/{id}")
    fun buyStocks(
        @PathVariable id: Long,
        @RequestParam stocks: Int,
    ) = stockMarketService.buyStocks(id, stocks)

    @PostMapping("/update-price/{id}")
    fun updateStockPrice(
        @PathVariable id: Long,
        @RequestParam price: Double,
    ) = stockMarketService.updateStockPrice(id, price)
}