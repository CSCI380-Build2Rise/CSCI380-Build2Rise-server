package com.build2rise.backend.controller

import com.build2rise.backend.dto.*
import com.build2rise.backend.service.ProjectService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = ["*"])
class ProjectController(
    private val projectService: ProjectService
) {

    /**
     * Support a founder's project
     * POST /api/projects/support
     */
    @PostMapping("/support")
    fun supportProject(
        authentication: Authentication,
        @Valid @RequestBody request: SupportProjectRequest
    ): ResponseEntity<ProjectResponse> {
        return try {
            val investorUserId = authentication.principal as String
            val project = projectService.supportProject(investorUserId, request.founderUserId, request.status)
            ResponseEntity.status(HttpStatus.CREATED).body(project)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * Get investor's supported projects
     * GET /api/projects/investor
     */
    @GetMapping("/investor")
    fun getInvestorProjects(authentication: Authentication): ResponseEntity<ProjectsListResponse> {
        return try {
            val userId = authentication.principal as String
            val projects = projectService.getInvestorProjects(userId)
            ResponseEntity.ok(projects)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    /**
     * Get founder's supporters
     * GET /api/projects/founder
     */
    @GetMapping("/founder")
    fun getFounderSupporters(authentication: Authentication): ResponseEntity<ProjectsListResponse> {
        return try {
            val userId = authentication.principal as String
            val projects = projectService.getFounderSupporters(userId)
            ResponseEntity.ok(projects)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    /**
     * Update project status
     * PUT /api/projects/{projectId}/status
     */
    @PutMapping("/{projectId}/status")
    fun updateProjectStatus(
        authentication: Authentication,
        @PathVariable projectId: String,
        @Valid @RequestBody request: UpdateProjectStatusRequest
    ): ResponseEntity<ProjectResponse> {
        return try {
            val userId = authentication.principal as String
            val project = projectService.updateProjectStatus(userId, projectId, request.status)
            ResponseEntity.ok(project)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * Check if investor is supporting a founder
     * GET /api/projects/check/{founderUserId}
     */
    @GetMapping("/check/{founderUserId}")
    fun checkSupporting(
        authentication: Authentication,
        @PathVariable founderUserId: String
    ): ResponseEntity<Map<String, Boolean>> {
        return try {
            val investorUserId = authentication.principal as String
            val isSupporting = projectService.isSupporting(investorUserId, founderUserId)
            ResponseEntity.ok(mapOf("isSupporting" to isSupporting))
        } catch (e: Exception) {
            ResponseEntity.ok(mapOf("isSupporting" to false))
        }
    }
}