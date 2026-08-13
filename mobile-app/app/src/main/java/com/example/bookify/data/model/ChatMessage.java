package com.example.bookify.data.model;

public class ChatMessage {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_SENT_TEXT = 1;
    public static final int TYPE_RECEIVED_TEXT = 2;
    public static final int TYPE_RECEIVED_IMAGE = 3;

    private String id;
    private int type;
    private String text;
    private String time;
    private String avatarUrl; // For received messages
    private String imageUrl; // For image messages
    private String dateLabel; // For date separators

    public ChatMessage(String id, int type, String text, String time, String avatarUrl, String imageUrl, String dateLabel) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.time = time;
        this.avatarUrl = avatarUrl;
        this.imageUrl = imageUrl;
        this.dateLabel = dateLabel;
    }

    public static ChatMessage createDate(String id, String dateLabel) {
        return new ChatMessage(id, TYPE_DATE, null, null, null, null, dateLabel);
    }

    public static ChatMessage createSentText(String id, String text, String time) {
        return new ChatMessage(id, TYPE_SENT_TEXT, text, time, null, null, null);
    }

    public static ChatMessage createReceivedText(String id, String text, String time, String avatarUrl) {
        return new ChatMessage(id, TYPE_RECEIVED_TEXT, text, time, avatarUrl, null, null);
    }

    public static ChatMessage createReceivedImage(String id, String imageUrl, String time, String avatarUrl) {
        return new ChatMessage(id, TYPE_RECEIVED_IMAGE, null, time, avatarUrl, imageUrl, null);
    }

    public String getId() { return id; }
    public int getType() { return type; }
    public String getText() { return text; }
    public String getTime() { return time; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getImageUrl() { return imageUrl; }
    public String getDateLabel() { return dateLabel; }
}
