package org.sekka.teemo.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sekka.teemo.R;
import org.sekka.teemo.data.model.Haslo;

import java.util.List;

public class HaslaAdapter extends RecyclerView.Adapter<HaslaAdapter.ViewHolder> {

    private List<Haslo> haslos;

    public HaslaAdapter(List<Haslo> haslos) {
        this.haslos = haslos;
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
        Haslo haslo = haslos.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.hasloNameTextView;
        textView.setText(haslo.getName());
        Button button = holder.hasloShowButton;
//        button.setText(haslo.isOnline() ? "Message" : "Offline");
//        button.setEnabled(haslo.isOnline());
    }

    @Override
    public int getItemCount() {
        return haslos.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView hasloNameTextView;
        public Button hasloShowButton;
        public Button hasloEditButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            hasloNameTextView = (TextView) itemView.findViewById(R.id.haslo_name);
            hasloShowButton = (Button) itemView.findViewById(R.id.haslo_show);
            hasloEditButton = (Button) itemView.findViewById(R.id.haslo_edit);
        }
    }
}