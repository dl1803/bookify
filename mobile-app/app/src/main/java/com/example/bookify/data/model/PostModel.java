package com.example.bookify.data.model;

public class PostModel {
    private String userName;
    private String postTime;
    private String postContent;
    private String bookTitle;
    private String bookAuthor;
    private int userAvatarResId;
    private int bookCoverResId;
    private int likesCount;
    private int commentsCount;
    private boolean isLiked;

    public PostModel(String userName, String postTime, String postContent, String bookTitle, String bookAuthor, int userAvatarResId, int bookCoverResId, int likesCount, int commentsCount) {
        this.userName = userName;
        this.postTime = postTime;
        this.postContent = postContent;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.userAvatarResId = userAvatarResId;
        this.bookCoverResId = bookCoverResId;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.isLiked = false;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public int getUserAvatarResId() {
        return userAvatarResId;
    }

    public int getBookCoverResId() {
        return bookCoverResId;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
