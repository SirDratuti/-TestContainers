package com.sirdratuti.personalaccount.controller

import com.sirdratuti.personalaccount.data.UserAccount
import com.sirdratuti.personalaccount.service.UserAccountService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class UserAccountController(
    private val userAccountService: UserAccountService,
) {
    @PostMapping("/add-account")
    fun addAccount(
        @RequestParam balance: Double,
    ) = userAccountService.registerAccount(
        UserAccount(balance = balance)
    )

    @PostMapping("/add-money/{id}")
    fun addMoney(
        @PathVariable id: Long,
        @RequestParam amount: Double,
    ) = userAccountService.increaseBalance(id, amount)

    @GetMapping("/get-account/{id}")
    fun getAccount(
        @PathVariable id: Long,
    ) = userAccountService.getAccount(id)

    @PostMapping("/buy-stocks/{id}")
    fun buyStocks(
        @PathVariable id: Long,
        @RequestParam companyId: Long,
        @RequestParam stocks: Int,
    ) = userAccountService.buyStocks(id, companyId, stocks)

    @PostMapping("/sell-stocks/{id}")
    fun sellStocks(
        @PathVariable id: Long,
        @RequestParam companyId: Long,
        @RequestParam stocks: Int,
    ) = userAccountService.sellStocks(id, companyId, stocks)

    @GetMapping("/stock-balance/{id}")
    fun inStocks(
        @PathVariable id: Long,
    ) = userAccountService.stockBalance(id)
}