package com.build2rise.backend.repository

import com.build2rise.backend.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    @Query(
        """
        SELECT u FROM User u
        WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%'))
        """
    )
    fun searchUsers(@Param("q") query: String): List<User>
}