package com.example.smishingdetectionapp

data class FaqItem (
    val question: String,
    val answer: String,
    val isExpanded: Boolean = false
)
