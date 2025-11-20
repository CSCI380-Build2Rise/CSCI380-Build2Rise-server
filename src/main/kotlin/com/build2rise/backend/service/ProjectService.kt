package com.build2rise.backend.service

import com.build2rise.backend.dto.*
import com.build2rise.backend.entity.Project
import com.build2rise.backend.repository.ProjectRepository
import com.build2rise.backend.repository.FounderRepository
import com.build2rise.backend.repository.InvestorRepository
import com.build2rise.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val founderRepository: FounderRepository,
    private val investorRepository: InvestorRepository,
    private val userRepository: UserRepository
) {

    /**
     * Investor expresses interest in supporting a founder
     */
    @Transactional
    fun supportProject(investorUserId: String, founderUserId: String, status: String = "interested"): ProjectResponse {
        val investorUuid = UUID.fromString(investorUserId)
        val founderUuid = UUID.fromString(founderUserId)

        // Get investor and founder profiles
        val investor = investorRepository.findByUserId(investorUuid)
            ?: throw IllegalArgumentException("Investor profile not found")

        val founder = founderRepository.findByUserId(founderUuid)
            ?: throw IllegalArgumentException("Founder profile not found")

        // Check if already supporting
        val existingProject = projectRepository.findByFounderIdAndInvestorId(founder.id!!, investor.id!!)
        if (existingProject != null) {
            throw IllegalArgumentException("Already supporting this founder")
        }

        // Create project support record
        val project = Project(
            founderId = founder.id!!,
            investorId = investor.id!!,
            status = status
        )

        val savedProject = projectRepository.save(project)

        val founderUser = userRepository.findById(founderUuid).orElse(null)
        val investorUser = userRepository.findById(investorUuid).orElse(null)

        return ProjectResponse(
            id = savedProject.id.toString(),
            founderId = savedProject.founderId.toString(),
            founderName = "${founderUser?.firstName ?: ""} ${founderUser?.lastName ?: ""}".trim(),
            founderStartupName = founder.startupName,
            founderIndustry = founder.industry,
            investorId = savedProject.investorId.toString(),
            investorName = "${investorUser?.firstName ?: ""} ${investorUser?.lastName ?: ""}".trim(),
            status = savedProject.status,
            createdAt = savedProject.createdAt.toString(),
            updatedAt = savedProject.updatedAt.toString()
        )
    }

    /**
     * Get investor's supported projects
     */
    fun getInvestorProjects(userId: String): ProjectsListResponse {
        val userUuid = UUID.fromString(userId)

        val investor = investorRepository.findByUserId(userUuid)
            ?: throw IllegalArgumentException("Investor profile not found")

        val projects = projectRepository.findByInvestorId(investor.id!!)

        val projectResponses = projects.map { project ->
            val founder = founderRepository.findById(project.founderId).orElse(null)
            val founderUser = founder?.userId?.let { userRepository.findById(it).orElse(null) }
            val investorUser = userRepository.findById(userUuid).orElse(null)

            ProjectResponse(
                id = project.id.toString(),
                founderId = project.founderId.toString(),
                founderName = "${founderUser?.firstName ?: ""} ${founderUser?.lastName ?: ""}".trim(),
                founderStartupName = founder?.startupName,
                founderIndustry = founder?.industry,
                investorId = project.investorId.toString(),
                investorName = "${investorUser?.firstName ?: ""} ${investorUser?.lastName ?: ""}".trim(),
                status = project.status,
                createdAt = project.createdAt.toString(),
                updatedAt = project.updatedAt.toString()
            )
        }

        return ProjectsListResponse(
            projects = projectResponses,
            totalCount = projectResponses.size
        )
    }

    /**
     * Get who's supporting a founder
     */
    fun getFounderSupporters(userId: String): ProjectsListResponse {
        val userUuid = UUID.fromString(userId)

        val founder = founderRepository.findByUserId(userUuid)
            ?: throw IllegalArgumentException("Founder profile not found")

        val projects = projectRepository.findByFounderId(founder.id!!)

        val projectResponses = projects.map { project ->
            val investor = investorRepository.findById(project.investorId).orElse(null)
            val investorUser = investor?.userId?.let { userRepository.findById(it).orElse(null) }
            val founderUser = userRepository.findById(userUuid).orElse(null)

            ProjectResponse(
                id = project.id.toString(),
                founderId = project.founderId.toString(),
                founderName = "${founderUser?.firstName ?: ""} ${founderUser?.lastName ?: ""}".trim(),
                founderStartupName = founder.startupName,
                founderIndustry = founder.industry,
                investorId = project.investorId.toString(),
                investorName = "${investorUser?.firstName ?: ""} ${investorUser?.lastName ?: ""}".trim(),
                status = project.status,
                createdAt = project.createdAt.toString(),
                updatedAt = project.updatedAt.toString()
            )
        }

        return ProjectsListResponse(
            projects = projectResponses,
            totalCount = projectResponses.size
        )
    }

    /**
     * Update project status
     */
    @Transactional
    fun updateProjectStatus(investorUserId: String, projectId: String, status: String): ProjectResponse {
        val investorUuid = UUID.fromString(investorUserId)
        val projectUuid = UUID.fromString(projectId)

        val investor = investorRepository.findByUserId(investorUuid)
            ?: throw IllegalArgumentException("Investor profile not found")

        val project = projectRepository.findById(projectUuid)
            .orElseThrow { IllegalArgumentException("Project not found") }

        // Verify this investor owns this project
        if (project.investorId != investor.id) {
            throw IllegalArgumentException("Unauthorized to update this project")
        }

        val updatedProject = project.copy(
            status = status,
            updatedAt = LocalDateTime.now()
        )

        val saved = projectRepository.save(updatedProject)

        val founder = founderRepository.findById(saved.founderId).orElse(null)
        val founderUser = founder?.userId?.let { userRepository.findById(it).orElse(null) }
        val investorUser = userRepository.findById(investorUuid).orElse(null)

        return ProjectResponse(
            id = saved.id.toString(),
            founderId = saved.founderId.toString(),
            founderName = "${founderUser?.firstName ?: ""} ${founderUser?.lastName ?: ""}".trim(),
            founderStartupName = founder?.startupName,
            founderIndustry = founder?.industry,
            investorId = saved.investorId.toString(),
            investorName = "${investorUser?.firstName ?: ""} ${investorUser?.lastName ?: ""}".trim(),
            status = saved.status,
            createdAt = saved.createdAt.toString(),
            updatedAt = saved.updatedAt.toString()
        )
    }

    /**
     * Check if investor is already supporting a founder
     */
    fun isSupporting(investorUserId: String, founderUserId: String): Boolean {
        val investorUuid = UUID.fromString(investorUserId)
        val founderUuid = UUID.fromString(founderUserId)

        val investor = investorRepository.findByUserId(investorUuid) ?: return false
        val founder = founderRepository.findByUserId(founderUuid) ?: return false

        return projectRepository.findByFounderIdAndInvestorId(founder.id!!, investor.id!!) != null
    }
}