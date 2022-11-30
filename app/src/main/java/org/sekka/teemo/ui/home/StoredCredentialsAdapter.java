package org.sekka.teemo.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import org.sekka.teemo.R;
import org.sekka.teemo.data.model.StoredCredential;

import java.util.List;

public class StoredCredentialsAdapter extends RecyclerView.Adapter<StoredCredentialsAdapter.ViewHolder> {

    public List<StoredCredential> storedCredentials;
    private HomeFragment homeFragment;

    public StoredCredentialsAdapter(List<StoredCredential> storedCredentials, HomeFragment homeFragment) {
        this.storedCredentials = storedCredentials;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_hasla, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        StoredCredential storedCredential = storedCredentials.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.hasloNameTextView;
        textView.setText(storedCredential.getName());
        Button button = holder.hasloShowButton;


        NavController navController = homeFragment.navController;
        holder.hasloShowButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("recordID", storedCredential.getID());
            bundle.putInt("visibility", -1);
            navController.navigate(R.id.action_nav_home_to_record_new,bundle);
        });

        holder.hasloEditButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("recordID", storedCredential.getID());
            navController.navigate(R.id.action_nav_home_to_record_new,bundle);
        });

        holder.hasloDeleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Usuwanie")
                    .setMessage("Czy na pewno chcesz usunąć \""+ storedCredential.getName() +"\"?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> homeFragment.DeleteStoredCred(storedCredential.getID()))
                    .setNegativeButton(android.R.string.no, null).show();
        });

    }

    @Override
    public int getItemCount() {
        return storedCredentials.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView hasloNameTextView;
        public Button hasloShowButton;
        public Button hasloEditButton;
        public Button hasloDeleteButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            hasloNameTextView = (TextView) itemView.findViewById(R.id.haslo_name);
            hasloShowButton = (Button) itemView.findViewById(R.id.haslo_show);
            hasloEditButton = (Button) itemView.findViewById(R.id.haslo_edit);
            hasloDeleteButton = (Button) itemView.findViewById(R.id.haslo_delete);
        }
    }
}