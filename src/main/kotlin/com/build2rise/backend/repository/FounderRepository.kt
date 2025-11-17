package com.build2rise.backend.repository

import com.build2rise.backend.entity.Founder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
@Repository
interface FounderRepository : JpaRepository<Founder, UUID> {
    fun findByUserId(userId: UUID): Founder?
}