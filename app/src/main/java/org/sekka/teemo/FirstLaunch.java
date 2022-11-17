package org.sekka.teemo;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class FirstLaunch extends AppCompatActivity {

//    private Executor executor;
//    private BiometricPrompt biometricPrompt;
//    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);


//        BiometricManager biometricManager = BiometricManager.from(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
//                case BiometricManager.BIOMETRIC_SUCCESS:
//                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
//                    break;
//                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                    Log.e("MY_APP_TAG", "No biometric features available on this device.");
//                    break;
//                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
//                    break;
//                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                    // Prompts the user to create credentials that your app accepts.
//                    final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
//                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                            BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
//                    startActivityForResult(enrollIntent, REQUEST_CODE);
//                    break;
//            }
//        }
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
        String hashCode = null;// w  ww  .  j  a va 2 s.c  o m
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

    public void onSubmit(View view) {
        EditText password_edittext = findViewById(R.id.editTextTextPassword);
        String provided_password = password_edittext.getText().toString();

        EditText password_edittext2 = findViewById(R.id.editTextTextPassword2);
        String provided_password_rep = password_edittext2.getText().toString();

        InitDatabase();

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setCancelable(false);
        dlgAlert.setMessage("Dane uwierzytelniające zostały zapisane");
        FirstLaunch that = this;
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent switchActivityIntent = new Intent(that, MainActivity.class);
                        startActivity(switchActivityIntent);
                    }
                });
        AlertDialog alertDialog = dlgAlert.create();
        alertDialog.show();
    }
}