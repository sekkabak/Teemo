package org.sekka.teemo.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.sekka.teemo.LoggedActivity;
import org.sekka.teemo.data.DatabaseHandler;
import org.sekka.teemo.data.model.LoginCredentials;
import org.sekka.teemo.databinding.FragmentLoginBinding;

import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    public DatabaseHandler db;
    private String logTag = "@@@@@@@@@@@@@@@@@@@@@";

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    LoginCredentials loginCredentials;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        db = new DatabaseHandler(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginCredentials = db.getCredentials();

        if (loginCredentials != null && loginCredentials.is_loginWithBiometrics()) {
            createBiometricLogin(getContext());
            PromptBiometricsLogin();
        }

        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkAuthorization();
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                checkAuthorization();
            }
        });
    }

    private void checkAuthorization() {
        final EditText passwordEditText = binding.password;

        if (Objects.equals(loginCredentials.get_passwd(), passwordEditText.getText().toString())) {
            Toast.makeText(getContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            switchToLoggedIn();
        } else {
            showLoginFailed("fail");
        }
    }

    private void PromptBiometricsLogin() {
        biometricPrompt.authenticate(promptInfo);
    }

    private void createBiometricLogin(Context context) {
        executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(context,
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Log.d(logTag, "Fingerprint auth!");
                BiometricPrompt.CryptoObject ob = result.getCryptoObject();
                if (ob != null)
                    Log.d(logTag, "Signature: " + ob.getSignature());
                Toast.makeText(context,
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                switchToLoggedIn();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(context, "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

    }

    private void switchToLoggedIn() {
        Intent intent = new Intent(getContext(),  LoggedActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(String errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}