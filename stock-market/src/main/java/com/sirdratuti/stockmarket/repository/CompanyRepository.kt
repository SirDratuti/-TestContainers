package com.sirdratuti.stockmarket.repository

import com.sirdratuti.stockmarket.data.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository : JpaRepository<Company, Long>