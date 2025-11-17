package com.build2rise.backend.service

import com.build2rise.backend.dto.*
import com.build2rise.backend.entity.Message
import com.build2rise.backend.repository.MessageRepository
import com.build2rise.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) {

    /**
     * Send a message
     */
    @Transactional
    fun sendMessage(senderId: String, request: SendMessageRequest): MessageResponse {
        val senderUuid = UUID.fromString(senderId)
        val receiverUuid = UUID.fromString(request.receiverId)

        // Verify both users exist
        val sender = userRepository.findById(senderUuid).orElseThrow {
            IllegalArgumentException("Sender not found")
        }

        val receiver = userRepository.findById(receiverUuid).orElseThrow {
            IllegalArgumentException("Receiver not found")
        }

        // Create message
        val message = Message(
            senderId = senderUuid,
            receiverId = receiverUuid,
            content = request.content
        )

        val savedMessage = messageRepository.save(message)

        return MessageResponse(
            id = savedMessage.id.toString(),
            senderId = savedMessage.senderId.toString(),
            senderFirstName = sender.firstName,
            senderLastName = sender.lastName,
            receiverId = savedMessage.receiverId.toString(),
            receiverFirstName = receiver.firstName,
            receiverLastName = receiver.lastName,
            content = savedMessage.content,
            readStatus = savedMessage.readStatus,
            timestamp = savedMessage.timestamp.toString()
        )
    }

    /**
     * Get conversation between two users
     */
    fun getConversation(user1Id: String, user2Id: String): ConversationDetailResponse {
        val user1Uuid = UUID.fromString(user1Id)
        val user2Uuid = UUID.fromString(user2Id)

        val otherUser = userRepository.findById(user2Uuid).orElseThrow {
            IllegalArgumentException("User not found")
        }

        val messages = messageRepository.findConversationBetween(user1Uuid, user2Uuid)

        val messageResponses = messages.map { message ->
            val sender = userRepository.findById(message.senderId).orElse(null)
            val receiver = userRepository.findById(message.receiverId).orElse(null)

            MessageResponse(
                id = message.id.toString(),
                senderId = message.senderId.toString(),
                senderFirstName = sender?.firstName,
                senderLastName = sender?.lastName,
                receiverId = message.receiverId.toString(),
                receiverFirstName = receiver?.firstName,
                receiverLastName = receiver?.lastName,
                content = message.content,
                readStatus = message.readStatus,
                timestamp = message.timestamp.toString()
            )
        }

        return ConversationDetailResponse(
            otherUser = UserInfo(
                userId = otherUser.id.toString(),
                firstName = otherUser.firstName,
                lastName = otherUser.lastName,
                userType = otherUser.userType,
                profileImageUrl = otherUser.profileImageUrl
            ),
            messages = messageResponses
        )
    }

    /**
     * Get all conversations for a user
     */
    fun getUserConversations(userId: String): List<ConversationResponse> {
        val userUuid = UUID.fromString(userId)

        val messages = messageRepository.findConversations(userUuid)

        // Group messages by conversation partner
        val conversationMap = mutableMapOf<UUID, Message>()

        messages.forEach { message ->
            val otherUserId = if (message.senderId == userUuid) {
                message.receiverId
            } else {
                message.senderId
            }

            // Keep only the most recent message per conversation
            if (!conversationMap.containsKey(otherUserId)) {
                conversationMap[otherUserId] = message
            }
        }

        return conversationMap.map { (otherUserId, lastMessage) ->
            val otherUser = userRepository.findById(otherUserId).orElse(null)

            ConversationResponse(
                userId = otherUserId.toString(),
                firstName = otherUser?.firstName,
                lastName = otherUser?.lastName,
                userType = otherUser?.userType ?: "unknown",
                lastMessage = lastMessage.content,
                lastMessageTime = lastMessage.timestamp.toString(),
                unreadCount = 0 // TODO: Implement unread count
            )
        }
    }
}
