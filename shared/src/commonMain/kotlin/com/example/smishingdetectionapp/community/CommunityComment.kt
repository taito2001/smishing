package com.example.smishingdetectionapp.community
//Content for Comments on posts in community section
data class CommunityComment(
    val commentId: Int = -1,
    val postId: Int,
    val user: String,
    val date: String,
    val commentText: String
)