package org.sekka.teemo.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import org.sekka.teemo.data.LoginRepository;
import org.sekka.teemo.data.Result;
import org.sekka.teemo.data.model.LoggedInUser;
import org.sekka.teemo.R;
import org.sekka.teemo.data.model.LoginCredentials;

//public class LoginViewModel extends ViewModel {
//
//    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
//    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
//    private LoginRepository loginRepository;
//
//    LoginViewModel(LoginRepository loginRepository) {
//        this.loginRepository = loginRepository;
//    }
//
//    LiveData<LoginFormState> getLoginFormState() {
//        return loginFormState;
//    }
//
//    LiveData<LoginResult> getLoginResult() {
//        return loginResult;
//    }
//
//    public void login(String username, String password) {
//        // can be launched in a separate asynchronous job
//        Result<LoginCredentials> result = loginRepository.login(username, password);
//
//        if (result instanceof Result.Success) {
//            LoginCredentials data = ((Result.Success<LoginCredentials>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
//    }
//
//    public void loginDataChanged(String username, String password) {
//       if (!isPasswordValid(password)) {
//            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
//        } else {
//            loginFormState.setValue(new LoginFormState(true));
//        }
//    }
//
//    // A placeholder password validation check
//    private boolean isPasswordValid(String password) {
//        return password != null && password.trim().length() > 5;
//    }
//}