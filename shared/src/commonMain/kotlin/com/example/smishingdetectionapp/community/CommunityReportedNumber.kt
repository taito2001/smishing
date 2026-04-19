package com.example.smishingdetectionapp.community
//Keeping track of number of reports for certain smish incidents
data class CommunityReportedNumber(
    val number: String,
    val count: Int,
    val lastReportedDate: String //Updates for time relevancy of attack
)