package com.example.Lab4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lab4.R;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<ContactActivity> {

    private ContactActivity selectedContactActivity;

    public void setSelectedContact(ContactActivity selectedContactActivity) {
        this.selectedContactActivity = selectedContactActivity;
        notifyDataSetChanged();
    }

    public ContactAdapter(Context context) {
        super(context, 0, new ArrayList<>());
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_contact, parent, false);
        }

        ContactActivity currentContactActivity = getItem(position);

        TextView NameView = itemView.findViewById(R.id.text_name);
        TextView PhoneView = itemView.findViewById(R.id.text_phone);
        ImageView ImgView = itemView.findViewById(R.id.imageview);

        if (currentContactActivity != null) {
            NameView.setText("姓名: " + currentContactActivity.getName());
            PhoneView.setText("手机号: " + currentContactActivity.getPhone());

            Uri imageUri = currentContactActivity.getImgUri();
            if (imageUri != null) {
                ImgView.setImageURI(imageUri);
            } else {
                ImgView.setImageResource(R.drawable.ic_launcher_background); // Placeholder image
            }
        }

        return itemView;
    }

    public void addContact(ContactActivity contactActivity) {
        add(contactActivity);
    }

    public ContactActivity getSelectedContact() {
        return selectedContactActivity;
    }
}


