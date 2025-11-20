package com.build2rise.backend.repository

import com.build2rise.backend.entity.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProjectRepository : JpaRepository<Project, UUID> {

    // Find all projects supported by an investor
    fun findByInvestorId(investorId: UUID): List<Project>

    // Check if investor already supports this founder
    fun findByFounderIdAndInvestorId(founderId: UUID, investorId: UUID): Project?

    // Find who's supporting a specific founder
    fun findByFounderId(founderId: UUID): List<Project>
}