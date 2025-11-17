package com.build2rise.backend.repository

import com.build2rise.backend.entity.Investor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface InvestorRepository : JpaRepository<Investor, UUID>{
    fun findByUserId(userId: UUID): Investor?
}