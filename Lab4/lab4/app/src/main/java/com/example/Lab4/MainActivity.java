package com.example.Lab4;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 111;
    private static final int PICK_IMAGE_REQUEST_CODE = 222;
    private boolean hasReadContact = false;


    private ListView contactListView;
    private ContactAdapter contactAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactListView = findViewById(R.id.contact_list);
        contactAdapter = new ContactAdapter(this);
        contactListView.setAdapter(contactAdapter);

        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasReadContact) {
            readContact();
            hasReadContact = true;
        }
    }


    private void readContact() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null);

                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        saveContactToAdapter(name, phoneNumber);
                    } while (phoneCursor.moveToNext());
                    phoneCursor.close();
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        contactListView.setOnItemClickListener((parent, view, position, id) -> {
            ContactActivity selectedContactActivity = contactAdapter.getItem(position);
            selectImg(selectedContactActivity);
        });
    }



    private void saveContactToAdapter(String name, String phoneNumber) {
        contactAdapter.addContact(new ContactActivity(name, phoneNumber, null));
    }

    private void selectImg(ContactActivity selectedContactActivity) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
        contactAdapter.setSelectedContact(selectedContactActivity);
    }

    private String convertImgToBase64(Uri imgUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imgUri);
            if (inputStream != null) {
                byte[] buffer = new byte[8192];
                int bytes;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                while ((bytes = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    output.write(buffer, 0, bytes);
                }
                inputStream.close();
                output.flush();
                byte[] imageBytes = output.toByteArray();
                return Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri ImgUri = data.getData();
            String Name = getNameFromDB();
            String Phone = getPhoneFromDB();
            updateImage(ImgUri, Name, Phone);
        }
    }

    private void saveContactToDatabase(String name, String phoneNumber) {
        try (ContactDBHelper contactDbHelper = new ContactDBHelper(this)) {
            contactDbHelper.saveContact(name, phoneNumber);
        } catch (Exception e) {
            // 处理异常，例如记录日志或者抛出新的异常
            e.printStackTrace();
        }
    }
    private String getNameFromDB() {
        try (ContactDBHelper contactDbHelper = new ContactDBHelper(this)) {
            return contactDbHelper.getContactName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getPhoneFromDB() {
        try (ContactDBHelper contactDbHelper = new ContactDBHelper(this)) {
            return contactDbHelper.getPhone();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void saveImageToDatabase(String base64Image) {
        try (ContactDBHelper databaseHelper = new ContactDBHelper(this)) {
            databaseHelper.saveImg(base64Image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateImage(Uri ImgUri, String Name, String Phone) {
        ContactActivity contactActivity = contactAdapter.getSelectedContact();
        contactActivity.setImgUri(ImgUri);
        contactAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "无法使用！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
