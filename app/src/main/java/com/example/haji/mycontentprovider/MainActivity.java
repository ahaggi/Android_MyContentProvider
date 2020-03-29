package com.example.haji.mycontentprovider;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_addItem = findViewById(R.id.button_addItem_main);
        Button button_showItem = findViewById(R.id.button_showItem_main);
        Button button_editItem_main = findViewById(R.id.button_editItem_main);


        button_addItem.setOnClickListener(
                (view)->{
                    Intent intent = new Intent(MainActivity.this , AddItemActivity.class);
                    startActivity(intent);
                }
        );


        button_showItem.setOnClickListener(
                (view)->{
                    Intent intent = new Intent(MainActivity.this , ShowItemsActivity.class);
                    startActivity(intent);

                }
        );

        button_editItem_main.setOnClickListener(
                (view)->{
                    Intent intent = new Intent(MainActivity.this , EditItemActivity.class);
                    startActivity(intent);

                }
        );

    }


}


