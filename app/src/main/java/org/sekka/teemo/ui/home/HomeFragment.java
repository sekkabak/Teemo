package org.sekka.teemo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sekka.teemo.R;
import org.sekka.teemo.data.DatabaseHandler;
import org.sekka.teemo.data.model.StoredCredential;
import org.sekka.teemo.databinding.FragmentHomeBinding;
import org.sekka.teemo.ui.recordnew.RecordNewFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<StoredCredential> storedCredentials;
    private FragmentHomeBinding binding;
    public DatabaseHandler db;
    public NavController navController;
    StoredCredentialsAdapter storedCredentialsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        db = new DatabaseHandler(getContext());
        View root = binding.getRoot();

        // TODO
        RecyclerView recyclerView = (RecyclerView) binding.lista;
//        storedCredentials = StoredCredential.createStoredCredentialList(20);
        storedCredentials = db.getStoredCredentials();

        storedCredentialsAdapter = new StoredCredentialsAdapter(storedCredentials, this);
        recyclerView.setAdapter(storedCredentialsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        navController = NavHostFragment.findNavController(this);
        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("recordID", -1);
                navController.navigate(R.id.action_nav_home_to_record_new,bundle);
            }
        });

        return root;
    }

    public void DeleteStoredCred(int id) {
        db.deleteStoredCredentials(id);
        storedCredentialsAdapter.storedCredentials = db.getStoredCredentials();
        storedCredentialsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}