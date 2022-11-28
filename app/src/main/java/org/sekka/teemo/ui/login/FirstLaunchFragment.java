package org.sekka.teemo.ui.login;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.sekka.teemo.data.DatabaseHandler;
import org.sekka.teemo.data.model.LoginCredentials;
import org.sekka.teemo.databinding.FragmentFirstLaunchBinding;

import org.sekka.teemo.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

public class FirstLaunchFragment extends Fragment {
    private FragmentFirstLaunchBinding binding;

    public DatabaseHandler db;
    private String password;

    private String logTag = "@@@@@@@@@@@@@@@@@@@@@";
    private CheckBox biometrics_checkBox;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentFirstLaunchBinding.inflate(inflater, container, false);

        db = new DatabaseHandler(getContext());

        return binding.getRoot();
    }

    private boolean areCredentialsValid(String password1, String password2) {
        return password1.length() >= 4 && password1.equals(password2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText password1EditText = binding.password1;
        final EditText password2EditText = binding.password2;
        final Button loginButton = binding.login;

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* ignore */ }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { /* ignore */ }

            @Override
            public void afterTextChanged(Editable s) {
                loginButton.setEnabled(areCredentialsValid(password1EditText.getText().toString(), password2EditText.getText().toString()));
            }
        };
        password1EditText.addTextChangedListener(afterTextChangedListener);
        password2EditText.addTextChangedListener(afterTextChangedListener);
        password2EditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    password = password1EditText.getText().toString();
                    submitCredentials();
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = password1EditText.getText().toString();
                submitCredentials();
            }
        });


        biometrics_checkBox = binding.biometricCheckBox;
        checkForBiometrics();
        password = password1EditText.getText().toString();
        if(biometrics_checkBox.isChecked()) createBiometricLogin();
    }

    private void submitCredentials() {
        final ProgressBar loadingProgressBar = binding.loading;
        final EditText password1EditText = binding.password1;
        final EditText password2EditText = binding.password2;

        if (!areCredentialsValid(password1EditText.getText().toString(), password2EditText.getText().toString())) {
            showLoginFailed("Podane dane nie zgadzają się");
            return;
        }
        loadingProgressBar.setVisibility(View.VISIBLE);

        // TODO: zapisać dane password w bazie danych

        if(biometrics_checkBox.isChecked()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
            dlgAlert.setCancelable(false);
            dlgAlert.setMessage("Dane uwierzytelniające zostały zapisane,\nczy chcesz ustawić uwierzytelnienie biometryczne?");
            dlgAlert.setPositiveButton("Tak",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PromptBiometricsLogin();
                        }
                    });
            dlgAlert.setNegativeButton("Nie",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            password = password1EditText.getText().toString();
                            addCredentials(password, false);
                            BackToLogin();
                        }
                    });
            AlertDialog alertDialog = dlgAlert.create();
            alertDialog.show();
        }
        else {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
            dlgAlert.setCancelable(false);
            dlgAlert.setMessage("Dane uwierzytelniające zostały zapisane");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            password = password1EditText.getText().toString();
                            addCredentials(password, false);
                            BackToLogin();
                        }
                    });
            AlertDialog alertDialog = dlgAlert.create();
            alertDialog.show();
        }
        loadingProgressBar.setVisibility(View.INVISIBLE);
    }

    private void checkForBiometrics() {
        BiometricManager biometricManager = BiometricManager.from(getActivity());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                setBiometricCheckbox(true);
                Log.d(logTag, "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e(logTag, "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(logTag, "Biometric features are currently unavailable.");
                break;
//            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e(logTag, "0");
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
//                    startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }
    }

    private void setBiometricCheckbox(boolean isActive) {
        if (isActive) {
            biometrics_checkBox.setChecked(true);
            biometrics_checkBox.setTextColor(Color.GREEN);
        }
    }

    public static String sha256String(String source) {
        byte[] hash = null;
        String hashCode = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(source.getBytes());
        } catch (NoSuchAlgorithmException e) {
            Log.e("", "Can't calculate SHA-256");
        }

        if (hash != null) {
            StringBuilder hashBuilder = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() == 1) {
                    hashBuilder.append("0");
                    hashBuilder.append(hex.charAt(hex.length() - 1));
                } else {
                    hashBuilder.append(hex.substring(hex.length() - 2));
                }
            }
            hashCode = hashBuilder.toString();
        }

        return hashCode;
    }

    private void PromptBiometricsLogin() {
        biometricPrompt.authenticate(promptInfo);
    }

    private void createBiometricLogin() {
        executor = ContextCompat.getMainExecutor(getActivity());
        biometricPrompt = new BiometricPrompt(FirstLaunchFragment.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getContext(),
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
                Toast.makeText(getContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                addCredentials(password, true);
                BackToLogin();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getContext(), "Authentication failed",
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

    private void addCredentials(String password, boolean isUsingBiometrics) {
        LoginCredentials loginCredentials = new LoginCredentials();
        loginCredentials.set_loginWithBiometrics(isUsingBiometrics);
        loginCredentials.set_name("main");
        loginCredentials.set_passwd(password);

        db.addCredentials(loginCredentials);
    }

    private void BackToLogin() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer_main, LoginFragment.class, null);
        transaction.commit();
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