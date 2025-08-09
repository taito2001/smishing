package com.example.smishingdetectionapp.chat;

/**
 * Represents a chat message in the conversation between user and bot.
 */
public class ChatMessage {

    // Constants to identify message sender type
    public static final int USER = 0;
    public static final int BOT = 1;

    // The content of the message
    private String message;

    // Type of message (USER or BOT)
    private int messageType;

    /**
     * Constructs a ChatMessage with specified content and sender type.
     *
     * @param message The message content.
     * @param messageType The sender type (USER or BOT).
     */
    public ChatMessage(String message, int messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    /**
     * Gets the message content.
     *
     * @return The message text.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the message type.
     *
     * @return 0 if sent by USER, 1 if sent by BOT.
     */
    public int getMessageType() {
        return messageType;
    }
}