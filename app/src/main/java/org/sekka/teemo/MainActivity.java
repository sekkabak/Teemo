package org.sekka.teemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;

import org.sekka.teemo.data.DatabaseHandler;
import org.sekka.teemo.ui.login.FirstLaunchFragment;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer_main, FirstLaunchFragment.class, null);
        transaction.commit();

        // TODO: DEBUG
        db.DeleteDatabase(this);
//
//        if(checkForFirstLaunch() ) {
//            Intent switchActivityIntent = new Intent(this, FirstLaunch.class);
//            startActivity(switchActivityIntent);
//        } else {
//
//        }
    }

    private void deleteDatabase() {

    }

    private boolean checkForFirstLaunch() {
        db = new DatabaseHandler(this);
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