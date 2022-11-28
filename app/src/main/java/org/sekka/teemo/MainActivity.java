package org.sekka.teemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.sekka.teemo.data.DatabaseHandler;
import org.sekka.teemo.data.model.LoginCredentials;
import org.sekka.teemo.ui.login.FirstLaunchFragment;
import org.sekka.teemo.ui.login.LoginFragment;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    public DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        // TODO: DEBUG
//        db.DeleteDatabase(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        if(checkForFirstLaunch() ) {
            transaction.replace(R.id.fragmentContainer_main, FirstLaunchFragment.class, null);
        } else {
            transaction.replace(R.id.fragmentContainer_main, LoginFragment.class, null);
        }
        transaction.commit();
    }

    private boolean checkForFirstLaunch() {
        if(db.getCredentials() == null) {
            return true;
        }

        return false;
    }
}