package com.example.bookify.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookify.data.model.UserModel;
import com.example.bookify.data.repository.AuthRepository;

import java.util.Calendar;

public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);
    private final MutableLiveData<UserModel> authSuccessUser = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>(false);

    public AuthViewModel() {
        this.authRepository = AuthRepository.getInstance();
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
            errorMessage.setValue("Vui lòng nhập Username");
            return;
        }
        if (username.trim().length() < 4) {
            errorMessage.setValue("Username phải chứa ít nhất 4 ký tự");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            errorMessage.setValue("Vui lòng nhập Mật khẩu");
            return;
        }
        if (password.length() < 6) {
            errorMessage.setValue("Mật khẩu phải chứa ít nhất 6 ký tự");
            return;
        }

        isLoading.setValue(true);
        errorMessage.setValue(null);

        authRepository.login(username, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(UserModel user) {
                isLoading.setValue(false);
                authSuccessUser.setValue(user);
            }

            @Override
            public void onError(String err) {
                isLoading.setValue(false);
                errorMessage.setValue(err);
            }
        });
    }

    public void register(String username, String firstName, String lastName, String dob, String city, String password, boolean termsAccepted) {
        if (username == null || username.trim().isEmpty()) {
            errorMessage.setValue("Username không được để trống");
            return;
        }
        if (username.trim().length() < 4) {
            errorMessage.setValue("Username phải chứa ít nhất 4 ký tự");
            return;
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            errorMessage.setValue("Tên không được để trống");
            return;
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            errorMessage.setValue("Họ không được để trống");
            return;
        }
        if (dob == null || dob.trim().isEmpty()) {
            errorMessage.setValue("Vui lòng chọn ngày sinh");
            return;
        }
        if (!isAgeAtLeast18(dob)) {
            errorMessage.setValue("Bạn phải từ 18 tuổi trở lên");
            return;
        }
        if (city == null || city.trim().isEmpty()) {
            errorMessage.setValue("Thành phố không được để trống");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            errorMessage.setValue("Mật khẩu không được để trống");
            return;
        }
        if (password.length() < 6) {
            errorMessage.setValue("Mật khẩu phải chứa ít nhất 6 ký tự");
            return;
        }
        if (!termsAccepted) {
            errorMessage.setValue("Bạn phải đồng ý với Điều khoản dịch vụ và Chính sách bảo mật");
            return;
        }

        isLoading.setValue(true);
        errorMessage.setValue(null);

        UserModel newUser = new UserModel(username, firstName, lastName, dob, city);

        authRepository.register(newUser, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(UserModel user) {
                isLoading.setValue(false);
                registerSuccess.setValue(true);
            }

            @Override
            public void onError(String err) {
                isLoading.setValue(false);
                errorMessage.setValue(err);
            }
        });
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
