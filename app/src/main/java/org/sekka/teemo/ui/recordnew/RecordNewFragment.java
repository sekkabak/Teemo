package org.sekka.teemo.ui.recordnew;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sekka.teemo.R;
import org.sekka.teemo.data.DatabaseHandler;
import org.sekka.teemo.data.model.StoredCredential;
import org.sekka.teemo.databinding.FragmentHomeBinding;
import org.sekka.teemo.databinding.FragmentRecordNewBinding;
import org.sekka.teemo.ui.home.StoredCredentialsAdapter;

public class RecordNewFragment extends Fragment {

    public DatabaseHandler db;
    private int id;
    private int visibility;
    private StoredCredential storedCredentials;
    private FragmentRecordNewBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecordNewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = new DatabaseHandler(getContext());
        id = getArguments().getInt("recordID");
        visibility = getArguments().getInt("visibility");

        binding.idBox.setText(Integer.toString(id));
        NavController navController = NavHostFragment.findNavController(this);
        binding.save.setVisibility(View.VISIBLE);
        if(visibility < 0)
        {
            binding.save.setVisibility(View.GONE);
        }

        if (id == -1) {
            binding.save.setOnClickListener(v -> {
                db.addStoredCredentials(new StoredCredential(binding.name.getText().toString(), binding.password.getText().toString(), binding.desc.getText().toString()));
                navController.navigate(R.id.action_record_new_to_nav_home);
            });
        }
        else {
            StoredCredential storedCredential = db.getStoredCredentials(id);
            binding.name.setText(storedCredential.getName());
            binding.password.setText(storedCredential.getPassword());
            binding.desc.setText(storedCredential.getDescription());
            binding.save.setOnClickListener(v -> {
                storedCredential.setName(binding.name.getText().toString());
                storedCredential.setPassword(binding.password.getText().toString());
                storedCredential.setDescription(binding.desc.getText().toString());
                db.updateStoredCredentials(storedCredential);
                navController.navigate(R.id.action_record_new_to_nav_home);
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}