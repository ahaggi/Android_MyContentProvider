package com.example.haji.mycontentprovider;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowItemsActivity extends AppCompatActivity {



    String [][] items;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        getData();
        showData();

    }



public void getData(){
    // Retrieve item records
    String URL = "content://com.example.haji.mycontentprovider.ContentProviderItems";

    Uri uri_Students = Uri.parse(URL);
    Cursor c = managedQuery(uri_Students, null, null, null, ContentProviderItems.felt_NAME); //sortBy felt_NAME
    items = new String[c.getCount()][c.getColumnCount()];

    if (c.moveToFirst()) {
        do{
            items[c.getPosition()][0] =  c.getString(c.getColumnIndex( ContentProviderItems.felt_NAME));
            items[c.getPosition()][1] =  c.getString(c.getColumnIndex( ContentProviderItems.felt_ID));
            items[c.getPosition()][2] =  c.getString(c.getColumnIndex( ContentProviderItems.felt_PRICE));

        } while (c.moveToNext());
    }

}

public void showData(){
    HashMap<String,String> item;
    for(int i=0;i<items.length;i++){
        item = new HashMap<String,String>();
        item.put( "name", items[i][0]);
        item.put( "id", items[i][1]);
        item.put( "price", items[i][2]);
        list.add( item );
    }


//  SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
    SimpleAdapter sa = new SimpleAdapter(
            this,  //Context context
            list, // List<? extends Map<String, ?>> data
            R.layout.item_layout, // int resource
            new String[] { "name","id","price" }, // String[] from
            new int[] {R.id.item_name, R.id.item_id, R.id.item_price} //int[] to
    );


    ListView listVw = (ListView)findViewById(R.id.listView_showItems);
    listVw.setAdapter(sa);



    listVw.setOnItemClickListener(

            (AdapterView<?> adapterView, View view, int pos, long l)->{
                Intent intent = new Intent(ShowItemsActivity.this, EditItemActivity.class);

                intent.putExtra("id" ,  Long.valueOf(items[pos][1]));

                startActivity(intent); });


}


}
