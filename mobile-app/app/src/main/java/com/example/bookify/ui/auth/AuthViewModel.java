package com.example.bookify.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookify.data.model.UserModel;
import com.example.bookify.data.remote.dto.ApiResponse;
import com.example.bookify.data.remote.dto.UserCreationRequest;
import com.example.bookify.data.repository.AuthRepository;
import com.google.gson.Gson;

import java.util.Calendar;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);
    private final MutableLiveData<UserModel> authSuccessUser = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>(false);

    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<UserModel> getAuthSuccessUser() {
        return authSuccessUser;
    }

    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }

    public void login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            errorMessage.setValue("Please enter a username");
            return;
        }
        if (username.trim().length() < 4) {
            errorMessage.setValue("Username must be at least 4 characters");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            errorMessage.setValue("Please enter a password");
            return;
        }
        if (password.length() < 6) {
            errorMessage.setValue("Password must be at least 6 characters");
            return;
        }

        isLoading.setValue(true);
        errorMessage.setValue(null);

        compositeDisposable.add(
                authRepository.login(username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    isLoading.setValue(false);
                                    authSuccessUser.setValue(user);
                                },
                                throwable -> {
                                    isLoading.setValue(false);
                                    if (throwable instanceof HttpException) {
                                        try {
                                            String errorBody = ((HttpException) throwable).response().errorBody().string();
                                            ApiResponse<?> apiResponse = new Gson().fromJson(errorBody, ApiResponse.class);
                                            errorMessage.setValue(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Login failed");
                                        } catch (Exception e) {
                                            errorMessage.setValue("Login failed: " + throwable.getMessage());
                                        }
                                    } else {
                                        errorMessage.setValue(throwable.getMessage() != null ? throwable.getMessage() : "An error occurred");
                                    }
                                }
                        )
        );
    }

    public void register(String username, String email, String firstName, String lastName, String dob, String city, String password, String confirmPassword, boolean termsAccepted) {
        if (username == null || username.trim().isEmpty()) {
            errorMessage.setValue("Username cannot be empty");
            return;
        }
        if (username.trim().length() < 4) {
            errorMessage.setValue("Username must be at least 4 characters");
            return;
        }
        if (email == null || email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.setValue("Please enter a valid email");
            return;
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            errorMessage.setValue("First name cannot be empty");
            return;
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            errorMessage.setValue("Last name cannot be empty");
            return;
        }
        if (dob == null || dob.trim().isEmpty()) {
            errorMessage.setValue("Please select your date of birth");
            return;
        }
        if (!isAgeAtLeast18(dob)) {
            errorMessage.setValue("You must be 18 years or older");
            return;
        }
        if (city == null || city.trim().isEmpty()) {
            errorMessage.setValue("City cannot be empty");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            errorMessage.setValue("Password cannot be empty");
            return;
        }
        if (password.length() < 6) {
            errorMessage.setValue("Password must be at least 6 characters");
            return;
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            errorMessage.setValue("Confirm password cannot be empty");
            return;
        }
        if (!password.equals(confirmPassword)) {
            errorMessage.setValue("Passwords do not match");
            return;
        }
        if (!termsAccepted) {
            errorMessage.setValue("You must agree to the Terms of Service and Privacy Policy");
            return;
        }

        isLoading.setValue(true);
        errorMessage.setValue(null);

        String formattedDob = formatDob(dob);

        UserCreationRequest request = new UserCreationRequest(
                username, password, email, firstName, lastName, formattedDob, city
        );

        compositeDisposable.add(
                authRepository.register(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    isLoading.setValue(false);
                                    if (response.getCode() == 1000) {
                                        registerSuccess.setValue(true);
                                    } else {
                                        errorMessage.setValue(response.getMessage());
                                    }
                                },
                                throwable -> {
                                    isLoading.setValue(false);
                                    errorMessage.setValue(throwable.getMessage() != null ? throwable.getMessage() : "An error occurred");
                                }
                        )
        );
    }

    private String formatDob(String dobStr) {
        try {
            String[] parts = dobStr.split("/");
            if (parts.length == 3) {
                return String.format("%s-%s-%s", parts[2], parts[1], parts[0]);
            }
        } catch (Exception ignored) {}
        return dobStr;
    }

    private boolean isAgeAtLeast18(String dobStr) {
        if (dobStr == null || dobStr.trim().isEmpty()) return false;
        try {
            String[] parts = dobStr.split("/");
            if (parts.length != 3) return false;
            int day = Integer.parseInt(parts[0].trim());
            int month = Integer.parseInt(parts[1].trim()) - 1;
            int year = Integer.parseInt(parts[2].trim());

            Calendar today = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.set(year, month, day);

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age >= 18;
        } catch (Exception e) {
            return false;
        }
    }
}
