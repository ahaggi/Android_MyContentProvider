package com.example.haji.mycontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import java.util.HashMap;

/**
 * Created by haji on 2/10/18.
 */

public class ContentProviderItems extends ContentProvider {

    /**
     *
     * First of all you need to create a Content Provider class that extends the ContentProviderbaseclass.
     * Second, you need to define your content provider URI address which will be used to access the content.
     * Next you will need to create your own database to keep the content. Usually, Android uses SQLite database and framework needs to override onCreate() method which will use SQLite Open Helper method to create or open the provider's database. When your application is launched, the onCreate() handler of each of its Content Providers is called on the main application thread.
     * Next you will have to implement Content Provider queries to perform different database specific operations.
     * Finally register your Content Provider in your activity file using <provider> tag.
     *
     * */


    /** OBS PROVIDER_NAME ER pakkeNavn + classNavn*/
    static final String PROVIDER_NAME = "com.example.haji.mycontentprovider.ContentProviderItems";
    static final String URL ="content://" + PROVIDER_NAME + "/items";
    static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> ITEM_PROJECTION_MAP;

    static final int ALL_ITEMS =  1;
    static final int SPESIFIC_ITEM_ID = 2;

    static final UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);;
    static {
        //uri for all items
        uriMatcher.addURI(PROVIDER_NAME , "items" , ALL_ITEMS);

        //uri for spesific item
        uriMatcher.addURI(PROVIDER_NAME , "items/#" , SPESIFIC_ITEM_ID);
    }


    /**
     * Database specific constant declarations
     */

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Warehouse";
    static final String ITEMS_TABLE_NAME = "items";
    static final int DATABASE_VERSION = 1;
    static final String felt_ID = "_id";
    static final String felt_NAME = "_name";
    static final String felt_PRICE = "_price";

    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + ITEMS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " _name TEXT NOT NULL, " +
                    " _price TEXT NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    private static class DatabaseHelper extends SQLiteOpenHelper {
                DatabaseHelper(Context context) {
                    super(context, DATABASE_NAME, null, DATABASE_VERSION);
                }

                @Override
                public void onCreate(SQLiteDatabase db) {
                    db.execSQL(CREATE_DB_TABLE);
                }

                @Override
                // Denne blir kjørt når vi forandre på DB struktur "for eks. dbVersion blir 2 istedenfor 1 "
                // pga at vi lanserer en ny version av vår app ,,, les mer!!

                // SE How To: Android SQLite onUpgrade()
                // https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    db.execSQL("DROP TABLE IF EXISTS" +" "+  ITEMS_TABLE_NAME);
                    onCreate(db);
                }
    }


    /*************************************************************************************/
    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        SQLiteQueryBuilder qBldr = new SQLiteQueryBuilder();
        qBldr.setTables(ITEMS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ALL_ITEMS:
                qBldr.setProjectionMap(ITEM_PROJECTION_MAP);
                break;

            case SPESIFIC_ITEM_ID:
                qBldr.appendWhere( felt_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = felt_NAME;
        }

        Cursor c = qBldr.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Override
    public String getType( Uri uri) {

        switch (uriMatcher.match(uri)){
            /**
             * Get all items records
             */
            case ALL_ITEMS:
                return "vnd.android.cursor.dir/" + ITEMS_TABLE_NAME;
            /**
             * Get a particular item
             */
            case SPESIFIC_ITEM_ID:
                return "vnd.android.cursor.item/" + ITEMS_TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
   }





    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }






    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(	ITEMS_TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }







    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case ALL_ITEMS:
                count = db.delete(ITEMS_TABLE_NAME, selection, selectionArgs);
                break;

            case SPESIFIC_ITEM_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( ITEMS_TABLE_NAME, felt_ID +  " = " + id +
                                (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }






    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {

            int count = 0;
            switch (uriMatcher.match(uri)) {
                case ALL_ITEMS:
                    count = db.update(ITEMS_TABLE_NAME, values, selection, selectionArgs);
                    break;

                case SPESIFIC_ITEM_ID:
                    count = db.update(ITEMS_TABLE_NAME, values,
                            felt_ID + " = " + uri.getPathSegments().get(1) +
                                    (!TextUtils.isEmpty(selection) ?
                                    "AND (" +selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri );
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
    }





}
