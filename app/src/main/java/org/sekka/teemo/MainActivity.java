package org.sekka.teemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);


        if(checkForFirstLaunch() ) {
            Intent switchActivityIntent = new Intent(this, FirstLaunch.class);
            startActivity(switchActivityIntent);
        } else {

        }
    }

    private void deleteDatabase() {

    }

    private boolean checkForFirstLaunch() {
        DatabaseHandler db = new DatabaseHandler(this);
        if(db.getAllContacts().isEmpty()) {
            return true;
        }

        return false;
    }


    public void onSubmit(View view) {
        EditText password_edittext = findViewById(R.id.editTextTextPassword);
        String provided_password = password_edittext.getText().toString();

//        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//        dlgAlert.setMessage("Password: " + provided_password + "\nSha256 of password: " + sha256String(provided_password));
//        dlgAlert.setTitle("App Title");
//        dlgAlert.setPositiveButton("OK", null);
//        dlgAlert.setCancelable(true);
//        dlgAlert.setPositiveButton("Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //dismiss the dialog
//                    }
//                });
//        dlgAlert.create().show();
    }
}