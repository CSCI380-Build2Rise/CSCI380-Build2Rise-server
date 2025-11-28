package com.build2rise.backend.service

import com.build2rise.backend.dto.*
import com.build2rise.backend.entity.Post
import com.build2rise.backend.entity.PostLike
import com.build2rise.backend.entity.PostComment
import com.build2rise.backend.entity.PostShare
import com.build2rise.backend.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PostInteractionService(
    private val postRepository: PostRepository,
    private val likeRepository: PostLikeRepository,
    private val commentRepository: PostCommentRepository,
    private val shareRepository: PostShareRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun toggleLike(postId: UUID, userId: UUID): PostInteractionResponse {
        val post = getPost(postId)

        val existing = likeRepository.findByPostIdAndUserId(postId, userId)

        val likedNow = if (existing != null) {
            likeRepository.delete(existing)
            post.likeCount -= 1
            false
        } else {
            likeRepository.save(PostLike(post = post, userId = userId))
            post.likeCount += 1
            true
        }

        postRepository.save(post)
        return buildInteractionResponse(post, likedNow)
    }

    @Transactional
    fun addComment(postId: UUID, userId: UUID, content: String): CommentDto {
        val post = getPost(postId)

        val saved = commentRepository.save(
            PostComment(
                post = post,
                userId = userId,
                content = content
            )
        )

        post.commentCount += 1
        postRepository.save(post)

        val authorName = getAuthorName(saved.userId)

        return CommentDto(
            id = saved.id!!,
            userId = saved.userId,
            authorName = authorName,
            content = saved.content,
            createdAt = saved.createdAt
        )
    }


    @Transactional(readOnly = true)
    fun getComments(postId: UUID): List<CommentDto> =
        commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
            .map {
                CommentDto(
                    id = it.id!!,
                    userId = it.userId,
                    authorName = getAuthorName(it.userId),
                    content = it.content,
                    createdAt = it.createdAt
                )
            }


    @Transactional
    fun recordShare(postId: UUID, userId: UUID): PostInteractionResponse {
        val post = getPost(postId)

        shareRepository.save(PostShare(post = post, userId = userId))
        post.shareCount += 1
        postRepository.save(post)

        val likedByUser = likeRepository.findByPostIdAndUserId(postId, userId) != null
        return buildInteractionResponse(post, likedByUser)
    }

    private fun getPost(postId: UUID): Post =
        postRepository.findById(postId)
            .orElseThrow { RuntimeException("Post $postId not found") }

    private fun buildInteractionResponse(
        post: Post,
        likedByCurrentUser: Boolean
    ): PostInteractionResponse =
        PostInteractionResponse(
            postId = post.id!!,
            likeCount = post.likeCount,
            commentCount = post.commentCount,
            shareCount = post.shareCount,
            likedByCurrentUser = likedByCurrentUser
        )
    @Transactional(readOnly = true)
    fun getInteractionStatus(postId: UUID, userId: UUID): PostInteractionResponse {
        val post = getPost(postId)

        val likedByUser = likeRepository.findByPostIdAndUserId(postId, userId) != null

        return buildInteractionResponse(post, likedByUser)
    }
    private fun getAuthorName(userId: UUID): String {
        val user = userRepository.findById(userId).orElse(null) ?: return "User"

        // 🔁 adjust these fields to match your User entity
        val first = user.firstName ?: ""
        val last = user.lastName ?: ""
        val full = "$first $last".trim()

        return if (full.isNotEmpty()) full else "User"
    }

}


