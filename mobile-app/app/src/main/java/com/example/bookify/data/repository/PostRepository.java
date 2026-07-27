package com.example.bookify.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bookify.R;
import com.example.bookify.data.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {

    private static PostRepository instance;
    private final MutableLiveData<List<PostModel>> postsLiveData = new MutableLiveData<>();
    private final List<PostModel> postsList = new ArrayList<>();

    private PostRepository() {
        initDefaultPosts();
    }

    public static synchronized PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
        }
        return instance;
    }

    private void initDefaultPosts() {
        postsList.add(new PostModel(
                "Emily Reader",
                "2 hours ago",
                "Just finished re-reading 'The Silent Echo'. Arthur Pendelton's storytelling is absolutely captivating and mysterious!",
                "The Silent Echo",
                "Arthur Pendelton",
                R.drawable.profile_avatar,
                R.drawable.book_cover_1,
                42,
                8
        ));

        postsList.add(new PostModel(
                "Elena Vance",
                "5 hours ago",
                "Exploring the deep roots of philosophy and nature today. A peaceful afternoon with an insightful book.",
                "Roots of Thought",
                "Elena Vance",
                R.drawable.profile_avatar,
                R.drawable.book_cover_2,
                18,
                3
        ));

        postsList.add(new PostModel(
                "Marcus Thorne",
                "1 day ago",
                "A modern masterpiece about life currents and human connections. Highly recommended for every literature lover!",
                "Currents",
                "Marcus Thorne",
                R.drawable.profile_avatar,
                R.drawable.book_cover_3,
                95,
                14
        ));

        postsList.add(new PostModel(
                "Sarah Jenkins",
                "2 days ago",
                "Revisiting classic themes of time, nostalgia, and memory. Couldn't put it down!",
                "Lost Time",
                "Sarah Jenkins",
                R.drawable.profile_avatar,
                R.drawable.book_cover_4,
                63,
                11
        ));

        postsLiveData.setValue(new ArrayList<>(postsList));
    }

    public LiveData<List<PostModel>> getHomeFeedPosts() {
        return postsLiveData;
    }

    public void createPost(String content, String bookTitle, String bookAuthor, int bookCoverResId) {
        PostModel newPost = new PostModel(
                "Emily Reader",
                "Just now",
                content,
                bookTitle != null ? bookTitle : "The Silent Echo",
                bookAuthor != null ? bookAuthor : "Arthur Pendelton",
                R.drawable.profile_avatar,
                bookCoverResId != 0 ? bookCoverResId : R.drawable.book_cover_1,
                0,
                0
        );
        postsList.add(0, newPost);
        postsLiveData.setValue(new ArrayList<>(postsList));
    }

    public void toggleLike(int position) {
        if (position >= 0 && position < postsList.size()) {
            PostModel post = postsList.get(position);
            if (post.isLiked()) {
                post.setLiked(false);
                post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
            } else {
                post.setLiked(true);
                post.setLikesCount(post.getLikesCount() + 1);
            }
            postsLiveData.setValue(new ArrayList<>(postsList));
        }
    }
}
