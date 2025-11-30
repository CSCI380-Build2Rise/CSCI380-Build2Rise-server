package com.build2rise.backend.service

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class SupabaseStorageService(
    @Value("\${supabase.url}") private val supabaseUrl: String,
    @Value("\${supabase.key}") private val supabaseKey: String,
    @Value("\${supabase.storage.bucket}") private val bucketName: String
) {
    private val client = OkHttpClient()

    /**
     * Upload a file to Supabase Storage
     * Returns the public URL of the uploaded file
     */
    fun uploadFile(file: MultipartFile, userId: UUID): String {

        // Validate file
        if (file.isEmpty) {
            throw IllegalArgumentException("File cannot be empty")
        }

        // Validate file type
        val contentType = file.contentType ?: throw IllegalArgumentException("Unknown file type")
        if (!isValidMediaType(contentType)) {
            throw IllegalArgumentException("Invalid file type. Only images and videos are allowed.")
        }

        // Generate unique filename
        val extension = getFileExtension(file.originalFilename ?: "file")
        val fileName = "${userId}_${System.currentTimeMillis()}$extension"
        val filePath = "posts/$fileName"


        try {
            // Prepare request body
            val requestBody = file.bytes.toRequestBody(contentType.toMediaType())

            // Build request
            val request = Request.Builder()
                .url("$supabaseUrl/storage/v1/object/$bucketName/$filePath")
                .header("Authorization", "Bearer $supabaseKey")
                .header("Content-Type", contentType)
                .post(requestBody)
                .build()

            // Execute upload
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: "Unknown error"
                    throw RuntimeException("Upload failed: $errorBody")
                }
            }

            // Return public URL
            return "$supabaseUrl/storage/v1/object/public/$bucketName/$filePath"

        } catch (e: Exception) {
            throw RuntimeException("Failed to upload file: ${e.message}", e)
        }
    }

    /**
     * Delete a file from Supabase Storage
     */
    fun deleteFile(fileUrl: String): Boolean {
        try {
            // Extract file path from URL
            val filePath = extractFilePathFromUrl(fileUrl)

            val request = Request.Builder()
                .url("$supabaseUrl/storage/v1/object/$bucketName/$filePath")
                .header("Authorization", "Bearer $supabaseKey")
                .delete()
                .build()

            client.newCall(request).execute().use { response ->
                return response.isSuccessful
            }
        } catch (e: Exception) {
            return false
        }
    }

    private fun isValidMediaType(contentType: String): Boolean {
        return contentType.startsWith("image/") || contentType.startsWith("video/")
    }

    private fun getFileExtension(filename: String): String {
        val lastDot = filename.lastIndexOf('.')
        return if (lastDot > 0) filename.substring(lastDot) else ""
    }

    private fun extractFilePathFromUrl(url: String): String {
        // Extract path from: https://xxx.supabase.co/storage/v1/object/public/POST-MEDIA/posts/file.jpg
        val prefix = "$supabaseUrl/storage/v1/object/public/$bucketName/"
        return url.removePrefix(prefix)
    }
}