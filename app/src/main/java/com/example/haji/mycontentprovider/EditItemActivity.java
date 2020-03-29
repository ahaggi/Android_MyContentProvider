package com.example.haji.mycontentprovider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // TODO lag en EditText for id
        /*
         ********* AFTER UPDATING THE VALUE OF SOME ITEM AND PRESSING "BACK", THE USER WILL BE BACK TO THE "ShowItemsActivity" BUT WITH THE OLD VALUES ********/








        long id = getIntent().getLongExtra("id",1);


        Button btn_save =findViewById(R.id.btn_save);

        btn_save.setOnClickListener(
                (view)->{
                    ContentValues values = new ContentValues();

                    String name = ((EditText)findViewById(R.id.editTxt_name_EditItemAct)).getText().toString();
                    String price = ((EditText)findViewById(R.id.editTxt_price_EditItemAct)).getText().toString();

                    values.put(ContentProviderItems.felt_NAME, name);
                    values.put(ContentProviderItems.felt_PRICE, price);
                    Uri uri = ContentUris.withAppendedId(ContentProviderItems.CONTENT_URI, id);
                    long noUpdated = getContentResolver().update(uri, values, null, null);

                    // TODO return to previous Activity
                }

        );

    }




}
