package com.sirdratuti.personalaccount.service

import com.sirdratuti.personalaccount.data.UserAccount
import com.sirdratuti.personalaccount.data.addBalance
import com.sirdratuti.personalaccount.data.removeBalance
import com.sirdratuti.personalaccount.repository.UserAccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserAccountService(
    private val userAccountRepository: UserAccountRepository,
    private val marketService: MarketService,
) {
    fun getAccount(
        accountId: Long,
    ): UserAccount = userAccountRepository.findById(accountId).getOrNull() ?: error("no such account")

    fun registerAccount(
        userAccount: UserAccount,
    ): UserAccount = userAccountRepository.save(userAccount)

    fun increaseBalance(
        accountId: Long,
        money: Double,
    ): UserAccount {
        val userAccount = userAccountRepository.findById(accountId).getOrNull() ?: error("no such account")
        userAccount.addBalance(money)
        return userAccountRepository.save(userAccount)
    }

    fun buyStocks(
        accountId: Long,
        companyId: Long,
        stocks: Int,
    ): UserAccount {
        val userAccount = userAccountRepository.findById(accountId).getOrNull() ?: error("no such account")
        val stockPrice = marketService.getStockPrice(companyId)
        check(userAccount.balance >= stockPrice * stocks) { "not enough balance to buy" }
        marketService.buyStocks(companyId, stocks)
        userAccount.apply {
            val stocksAmount = stocksMap.getOrDefault(companyId, 0)
            stocksMap[companyId] = stocksAmount + stocks
            removeBalance(stockPrice * stocks)
        }
        return userAccountRepository.save(userAccount)
    }

    fun sellStocks(
        accountId: Long,
        companyId: Long,
        stocks: Int,
    ): UserAccount {
        val userAccount = userAccountRepository.findById(accountId).getOrNull() ?: error("no such account")
        val availableStocks = userAccount.stocksMap.getOrElse(companyId) { error("no such company") }
        check(availableStocks >= stocks) { "not enough stocks available" }
        val stockPrice = marketService.getStockPrice(companyId)
        marketService.sellStocks(companyId, stocks)
        userAccount.apply {
            stocksMap.computeIfPresent(companyId) { _, amount -> amount - stocks }
            addBalance(stocks * stockPrice)
        }
        return userAccountRepository.save(userAccount)
    }

    fun stockBalance(
        accountId: Long,
    ): Double {
        val userAccount = userAccountRepository.findById(accountId).getOrNull() ?: error("no such account")
        return userAccount.stocksMap.entries.fold(0.0) { acc, (stockId, stockAmount) ->
            val price = marketService.getStockPrice(stockId)
            acc + price * stockAmount
        }
    }
}