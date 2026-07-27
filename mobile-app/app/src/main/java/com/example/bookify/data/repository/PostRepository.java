package com.example.bookify.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bookify.R;
import com.example.bookify.data.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {

    private static PostRepository instance;

    private PostRepository() {}

    public static synchronized PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
        }
        return instance;
    }

    public LiveData<List<PostModel>> getHomeFeedPosts() {
        MutableLiveData<List<PostModel>> liveData = new MutableLiveData<>();
        List<PostModel> posts = new ArrayList<>();

        posts.add(new PostModel(
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

        posts.add(new PostModel(
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

        posts.add(new PostModel(
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

        posts.add(new PostModel(
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

        liveData.setValue(posts);
        return liveData;
    }
}
