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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);


    }


    public void onSubmit(View view) {
//        EditText password_edittext = findViewById(R.id.editTextTextPassword);
//        String provided_password = password_edittext.getText().toString();
//
//        EditText password_edittext2 = findViewById(R.id.editTextTextPassword2);
//        String provided_password_rep = password_edittext2.getText().toString();
//
//        InitDatabase();
//
//        if(biometrics_checkBox.isChecked()) {
//            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//            dlgAlert.setCancelable(false);
//            dlgAlert.setMessage("Dane uwierzytelniające zostały zapisane,\nczy chcesz ustawić uwierzytelnienie biometryczne?");
//            dlgAlert.setPositiveButton("Ok",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            PromptBiometricsLogin();
//                        }
//                    });
//            dlgAlert.setNegativeButton("Nie",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            BackToLogin();
//                        }
//                    });
//            AlertDialog alertDialog = dlgAlert.create();
//            alertDialog.show();
//        }
//        else {
//            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//            dlgAlert.setCancelable(false);
//            dlgAlert.setMessage("Dane uwierzytelniające zostały zapisane");
//            dlgAlert.setPositiveButton("Ok",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            BackToLogin();
//                        }
//                    });
//            AlertDialog alertDialog = dlgAlert.create();
//            alertDialog.show();
//        }
    }


}