package com.sirdratuti.personalaccount.repository

import com.sirdratuti.personalaccount.data.UserAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository : JpaRepository<UserAccount, Long>