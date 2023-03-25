package com.sirdratuti.stockmarket.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class Company(
    @Id @GeneratedValue var id: Long = 0,
    var stocks: Int = 0,
    var price: Double = -1.0,
)

fun Company.addStocks(amount: Int) =
    takeIf { amount >= 0 }
        ?.let { stocks += amount }
        ?: error("negative amount of stocks")

fun Company.buyStocks(amount: Int) =
    takeIf { amount <= stocks }
        ?.let { stocks -= amount }
        ?: error("not enough stocks")