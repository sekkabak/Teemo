package org.sekka.teemo;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.*;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.util.List;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FirstLaunch extends AppCompatActivity {
    private String logTag = "@@@@@@@@@@@@@@@@@@@@@";
    private CheckBox biometrics_checkBox;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        biometrics_checkBox = findViewById(R.id.biometric_checkBox);
        checkForBiometrics();

        if(biometrics_checkBox.isChecked()) createBiometricLogin();
    }
    private void checkForBiometrics() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
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
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e(logTag, "0");
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
//                    startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                Log.e(logTag, "1");
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Log.e(logTag, "2");
                break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                Log.e(logTag, "3");
                break;
        }
    }

    private void setBiometricCheckbox(boolean isActive) {
        if (isActive) {
            biometrics_checkBox.setChecked(true);
            biometrics_checkBox.setTextColor(Color.GREEN);
        }
    }

    private void InitDatabase() {
        DatabaseHandler db = new DatabaseHandler(this);

        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addContact(new Contact("Ravi", "9100000000"));
        db.addContact(new Contact("Srinivas", "9199999999"));
        db.addContact(new Contact("Tommy", "9522222222"));
        db.addContact(new Contact("Karthik", "9533333333"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " +
                    cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
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
        BiometricPrompt.CryptoObject cryptoObject = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                cryptoObject = new BiometricPrompt.CryptoObject(getEncryptCipher(createKey()));
            }
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(logTag, "biometric error1.");
        } catch (NoSuchPaddingException e) {
            Log.e(logTag, "biometric error2.");
        } catch (NoSuchAlgorithmException e) {
            Log.e(logTag, "biometric error3.");
        } catch (InvalidKeyException e) {
            Log.e(logTag, "biometric error4.");
        } catch (NoSuchProviderException e) {
            Log.e(logTag, "biometric error5.");
        }
        biometricPrompt.authenticate(promptInfo, cryptoObject);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private SecretKey createKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String algorithm = KeyProperties.KEY_ALGORITHM_AES;
        String provider = "AndroidKeyStore";
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, provider);
        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder("MY_KEY", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)
                .build();

        keyGenerator.init(keyGenParameterSpec);
        return keyGenerator.generateKey();
    }

    private Cipher getEncryptCipher(Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = KeyProperties.KEY_ALGORITHM_AES;
        String blockMode = KeyProperties.BLOCK_MODE_CBC;
        String padding = KeyProperties.ENCRYPTION_PADDING_PKCS7;
        Cipher cipher = Cipher.getInstance(algorithm+"/"+blockMode+"/"+padding);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }

    private void createBiometricLogin() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(FirstLaunch.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
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
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                BackToLogin();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
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

    public void onSubmit(View view) {
        EditText password_edittext = findViewById(R.id.editTextTextPassword);
        String provided_password = password_edittext.getText().toString();

        EditText password_edittext2 = findViewById(R.id.editTextTextPassword2);
        String provided_password_rep = password_edittext2.getText().toString();

        InitDatabase();
        
        if(biometrics_checkBox.isChecked()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setCancelable(false);
            dlgAlert.setMessage("Dane uwierzytelniające zostały zapisane,\nczy chcesz ustawić uwierzytelnienie biometryczne?");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PromptBiometricsLogin();
                        }
                    });
            dlgAlert.setNegativeButton("Nie",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            BackToLogin();
                        }
                    });
            AlertDialog alertDialog = dlgAlert.create();
            alertDialog.show();
        }
        else {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setCancelable(false);
            dlgAlert.setMessage("Dane uwierzytelniające zostały zapisane");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            BackToLogin();
                        }
                    });
            AlertDialog alertDialog = dlgAlert.create();
            alertDialog.show();
        }
    }

    private void BackToLogin() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }
}