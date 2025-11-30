package com.build2rise.backend.controller

import com.build2rise.backend.dto.*
import com.build2rise.backend.service.PostService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import com.build2rise.backend.service.PostInteractionService
import java.util.UUID
import com.build2rise.backend.dto.PostInteractionResponse
import com.build2rise.backend.dto.AddCommentRequest
import com.build2rise.backend.dto.CommentDto
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = ["*"])
class PostController(
    private val postService: PostService,
    private val postInteractionService: PostInteractionService
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
     * Create a new post with optional media file
     * POST /api/posts/upload
     */
    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createPostWithMedia(
        authentication: Authentication,
        @RequestParam(value = "description", required = false) description: String?,
        @RequestParam(value = "file", required = false) file: MultipartFile?
    ): ResponseEntity<PostResponse> {
        return try {
            println("🔹 Upload request received")
            println("🔹 Description: $description")
            println("🔹 File present: ${file != null}")
            println("🔹 File empty: ${file?.isEmpty}")
            println("🔹 File name: ${file?.originalFilename}")
            println("🔹 File size: ${file?.size} bytes")
            println("🔹 Content type: ${file?.contentType}")

            val userId = authentication.principal as String
            println("🔹 User ID: $userId")

            val post = postService.createPostWithMedia(userId, description, file)
            println("✅ Post created successfully: ${post.id}")

            ResponseEntity.status(HttpStatus.CREATED).body(post)
        } catch (e: IllegalArgumentException) {
            println("❌ IllegalArgumentException: ${e.message}")
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: Exception) {
            println("❌ Exception: ${e.message}")
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
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
    @PostMapping("/{postId}/like")
    fun toggleLike(
        @PathVariable postId: UUID,
        authentication: Authentication
    ): PostInteractionResponse {
        // principal is already a String userId in your createPost endpoint
        val userId = UUID.fromString(authentication.principal as String)
        return postInteractionService.toggleLike(postId, userId)
    }

    @PostMapping("/{postId}/comments")
    fun addComment(
        @PathVariable postId: UUID,
        authentication: Authentication,
        @RequestBody request: AddCommentRequest    // now only needs content
    ): CommentDto {
        val userId = UUID.fromString(authentication.principal as String)
        return postInteractionService.addComment(postId, userId, request.content)
    }
    @GetMapping("/{postId}/comments")
    fun getComments(
        @PathVariable postId: UUID
    ): List<CommentDto> {
        return postInteractionService.getComments(postId)
    }
    @PostMapping("/{postId}/share")
    fun sharePost(
        @PathVariable postId: UUID,
        authentication: Authentication,
    ): PostInteractionResponse {
        val userId = UUID.fromString(authentication.principal as String)
        return postInteractionService.recordShare(postId, userId)
    }
    @GetMapping("/{postId}/interactions")
    fun getPostInteractions(
        @PathVariable postId: UUID,
        authentication: Authentication
    ): PostInteractionResponse {
        val userId = UUID.fromString(authentication.principal as String)
        return postInteractionService.getInteractionStatus(postId, userId)
    }






}