package com.example.smishingdetectionapp.community
//Content for posts in community section
data class CommunityPost(
    val id: Int = -1,
    val username: String,
    val date: String,
    val posttitle: String,
    val postdescription: String,
    val likes: Int,
    val comments: Int
)