package org.sekka.teemo.data;

import org.sekka.teemo.data.model.LoginCredentials;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoginCredentials> login(String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoginCredentials fakeUser =
                    new LoginCredentials(0, "test", "test", true);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}