package com.example.Lab4;

// Contact.java

import android.net.Uri;

public class ContactActivity {

    private String Name;
    private String Phone;
    private Uri imgUri;

    public ContactActivity(String Name, String Phone, Uri imgUri) {
        this.Name = Name;
        this.Phone = Phone;
        this.imgUri = imgUri;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }
}

