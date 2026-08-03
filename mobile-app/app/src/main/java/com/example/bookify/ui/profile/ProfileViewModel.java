package com.example.bookify.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    public enum FriendState {
        SELF, NOT_FRIEND, REQUEST_SENT, FRIEND
    }

    private final MutableLiveData<FriendState> friendStateLiveData = new MutableLiveData<>(FriendState.SELF);
    private final MutableLiveData<Integer> selectedTabLiveData = new MutableLiveData<>(2); 
    private final MutableLiveData<Integer> followersCountLiveData = new MutableLiveData<>(1200);

    public LiveData<FriendState> getFriendState() {
        return friendStateLiveData;
    }

    public LiveData<Integer> getSelectedTab() {
        return selectedTabLiveData;
    }

    public LiveData<Integer> getFollowersCount() {
        return followersCountLiveData;
    }

    public void cycleFriendState() {
        FriendState current = friendStateLiveData.getValue();
        if (current == null) current = FriendState.NOT_FRIEND;

        switch (current) {
            case NOT_FRIEND:
                friendStateLiveData.setValue(FriendState.REQUEST_SENT);
                break;
            case REQUEST_SENT:
                friendStateLiveData.setValue(FriendState.FRIEND);
                break;
            case FRIEND:
                friendStateLiveData.setValue(FriendState.SELF);
                break;
            case SELF:
                friendStateLiveData.setValue(FriendState.NOT_FRIEND);
                break;
        }

        Integer currentCount = followersCountLiveData.getValue();
        int count = currentCount != null ? currentCount : 1200;
        if (friendStateLiveData.getValue() == FriendState.FRIEND) {
            followersCountLiveData.setValue(count + 1);
        } else if (current == FriendState.FRIEND) {
            followersCountLiveData.setValue(Math.max(1200, count - 1));
        }
    }

    public void selectTab(int position) {
        selectedTabLiveData.setValue(position);
    }
}
