package com.example.haji.mycontentprovider;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.net.Uri;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        findViewById(R.id.button_ok_AddItemActivity).setOnClickListener(
                (view)->{
                    // Add a new student record
                    ContentValues values = new ContentValues();
                    values.put(ContentProviderItems.felt_NAME,
                            ((EditText)findViewById(R.id.editTxt_name_AddItemActivity)).getText().toString());

                    values.put(ContentProviderItems.felt_PRICE,
                            ((EditText)findViewById(R.id.editTxt_price_AddItemActivity)).getText().toString());

                    Uri uri = getContentResolver().insert(
                            ContentProviderItems.CONTENT_URI, values);

                    Toast.makeText(getBaseContext(),
                            uri.toString(), Toast.LENGTH_LONG).show();
                }
        );

    }
}
