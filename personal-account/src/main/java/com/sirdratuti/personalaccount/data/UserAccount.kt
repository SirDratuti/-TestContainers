package com.sirdratuti.personalaccount.data

import jakarta.persistence.*

@Entity
data class UserAccount(
    @Id @GeneratedValue var id: Long = 0,
    var balance: Double = 0.0,
) {
    @ElementCollection
    @CollectionTable(
        name = "account_stock_join",
        joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")],
    )
    @MapKeyColumn(name = "stock_id")
    var stocksMap: MutableMap<Long, Int> = mutableMapOf()
}


fun UserAccount.addBalance(amount: Double) = apply {
    require(amount >= 0.0) { "can't add negative balance" }
    balance += amount
}

fun UserAccount.removeBalance(amount: Double) = apply {
    require(amount >= 0.0) { "cant remove negative balance" }
    require(amount <= balance) { "not enough money to remove" }
    balance -= amount
}