package com.pac.imonline.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pac.imonline.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> contactList;
    private ContactAdapterEventListener eventListener;

    public ContactAdapter(ContactAdapterEventListener eventListener) {
        this.contactList = new ArrayList<>();
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Contact contact = this.contactList.get(position);

        holder.textViewContactName.setText(contact.getName());

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.foto1)
                .into(holder.imageViewContactAvatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventListener != null) eventListener.onContactClicked(contact.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void refreshContactList(List<Contact> newcontactList) {
        contactList = newcontactList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewContactName;
        private ImageView imageViewContactAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewContactName = itemView.findViewById(R.id.textViewContactName);
            this.imageViewContactAvatar = itemView.findViewById(R.id.imageViewContactAvatar);
        }
    }

    public interface ContactAdapterEventListener {
        void onContactClicked(long contactId);
    }
}
