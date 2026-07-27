package com.example.bookify.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookify.data.model.PostModel;
import com.example.bookify.data.repository.PostRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final PostRepository repository;
    private final LiveData<List<PostModel>> postsLiveData;

    public HomeViewModel() {
        repository = PostRepository.getInstance();
        postsLiveData = repository.getHomeFeedPosts();
    }

    public LiveData<List<PostModel>> getPosts() {
        return postsLiveData;
    }

    public void createNewPost(String content, String bookTitle, String bookAuthor, int bookCoverResId) {
        repository.createPost(content, bookTitle, bookAuthor, bookCoverResId);
    }

    public void toggleLike(int position) {
        repository.toggleLike(position);
    }
}
