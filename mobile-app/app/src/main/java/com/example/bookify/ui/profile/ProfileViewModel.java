package com.example.bookify.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isFollowingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> selectedTabLiveData = new MutableLiveData<>(2); // Default to "Saved Books" (index 2)
    private final MutableLiveData<Integer> followersCountLiveData = new MutableLiveData<>(1200);

    public LiveData<Boolean> getIsFollowing() {
        return isFollowingLiveData;
    }

    public LiveData<Integer> getSelectedTab() {
        return selectedTabLiveData;
    }

    public LiveData<Integer> getFollowersCount() {
        return followersCountLiveData;
    }

    public void toggleFollowStatus() {
        Boolean current = isFollowingLiveData.getValue();
        boolean isFollowing = current != null && current;
        isFollowingLiveData.setValue(!isFollowing);

        Integer currentCount = followersCountLiveData.getValue();
        int count = currentCount != null ? currentCount : 1200;
        if (!isFollowing) {
            followersCountLiveData.setValue(count + 1);
        } else {
            followersCountLiveData.setValue(Math.max(1200, count - 1));
        }
    }

    public void selectTab(int position) {
        selectedTabLiveData.setValue(position);
    }
}
