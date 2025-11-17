package com.build2rise.backend.controller

import com.build2rise.backend.dto.*
import com.build2rise.backend.service.PostService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = ["*"])
class PostController(
    private val postService: PostService
) {

    /**
     * Create a new post
     * POST /api/posts
     */
    @PostMapping
    fun createPost(
        authentication: Authentication,
        @Valid @RequestBody request: CreatePostRequest
    ): ResponseEntity<PostResponse> {
        return try {
            val userId = authentication.principal as String
            val post = postService.createPost(userId, request)
            ResponseEntity.status(HttpStatus.CREATED).body(post)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * Get all posts (feed)
     * GET /api/posts
     */
    @GetMapping
    fun getAllPosts(): ResponseEntity<FeedResponse> {
        val feed = postService.getAllPosts()
        return ResponseEntity.ok(feed)
    }

    /**
     * Get posts by user ID
     * GET /api/posts/user/{userId}
     */
    @GetMapping("/user/{userId}")
    fun getPostsByUserId(@PathVariable userId: String): ResponseEntity<FeedResponse> {
        return try {
            val feed = postService.getPostsByUserId(userId)
            ResponseEntity.ok(feed)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    /**
     * Get current user's posts
     * GET /api/posts/my-posts
     */
    @GetMapping("/my-posts")
    fun getCurrentUserPosts(authentication: Authentication): ResponseEntity<FeedResponse> {
        return try {
            val userId = authentication.principal as String
            val feed = postService.getPostsByUserId(userId)
            ResponseEntity.ok(feed)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    /**
     * Get post by ID
     * GET /api/posts/{postId}
     */
    @GetMapping("/{postId}")
    fun getPostById(@PathVariable postId: String): ResponseEntity<PostResponse> {
        return try {
            val post = postService.getPostById(postId)
            ResponseEntity.ok(post)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}