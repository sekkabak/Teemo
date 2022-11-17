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
        // TODO: DEBUG

        if(checkForFirstLaunch() ) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.replace(R.id.fragmentContainer_main, FirstLaunchFragment.class, null);
            transaction.commit();
        } else {

        }
    }

    private boolean checkForFirstLaunch() {
        if(db.getCredentials() == null) {
            return true;
        }

        return false;
    }
}