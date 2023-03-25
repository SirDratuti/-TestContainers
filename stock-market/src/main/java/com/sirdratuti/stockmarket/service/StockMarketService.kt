package com.sirdratuti.stockmarket.service

import com.sirdratuti.stockmarket.data.Company
import com.sirdratuti.stockmarket.data.addStocks
import com.sirdratuti.stockmarket.data.buyStocks
import com.sirdratuti.stockmarket.repository.CompanyRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class StockMarketService(
    private val companyRepository: CompanyRepository,
) {
    fun addCompany(company: Company) = companyRepository.save(company)

    fun getCompany(id: Long): Company = companyRepository.findById(id).getOrNull() ?: error("no such company")

    fun getCompanies(): List<Company> = companyRepository.findAll()

    fun addStocks(
        companyId: Long,
        stocks: Int,
    ): Company = with(companyRepository) {
        val company = findById(companyId).getOrNull() ?: error("no such company")
        company.addStocks(stocks)
        save(company)
    }

    fun buyStocks(
        companyId: Long,
        stocks: Int,
    ): Company = with(companyRepository) {
        val company = findById(companyId).getOrNull() ?: error("no such company")
        company.buyStocks(stocks)
        save(company)
    }

    fun updateStockPrice(
        companyId: Long,
        price: Double,
    ): Company = with(companyRepository) {
        val company = findById(companyId).getOrNull() ?: error("no such company")
        company.price = price
        save(company)
    }
}