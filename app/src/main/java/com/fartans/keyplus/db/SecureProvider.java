package com.fartans.keyplus.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

public class SecureProvider extends ContentProvider {
    // fields for my content provider
    static final String AUTHORITY = "com.phoenix.securekey.db.SecureProvider";
    static final String pacakge = "com.phoenix.securekey";
    static final String URL = "content://" + AUTHORITY + "/data";
    static final Uri CONTENT_URI = Uri.parse(URL);


    // integer values used in content URI
    static final int DATA_TABLE = 1;
    static final int AUTH_TABLE = 2;
    static final int VAULT_TABLE = 3;
    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    DBHelper dbHelper;

    // projection map for a query
    private static HashMap<String, String> BirthMap;

    // maps content URI "patterns" to the integer values that were set above
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DbTableStrings.TABLE_NAME_DATA, DATA_TABLE);
        uriMatcher.addURI(AUTHORITY, DbTableStrings.TABLE_NAME_AUTHENTICATION,AUTH_TABLE);
        uriMatcher.addURI(AUTHORITY, DbTableStrings.TABLE_NAME_VAULT,VAULT_TABLE);
    }

    // database declarations
    private SQLiteDatabase database;

    // class that creates and manages the provider's database
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DbTableStrings.DATABASE_NAME, null, DbTableStrings.DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(Schema.CREATE_AUTH_TABLE);
            db.execSQL(Schema.CREATE_DATA_TABLE);
            db.execSQL(Schema.CREATE_VAULT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            Log.w(DBHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ". Old data will be destroyed");
/*            db.execSQL("DROP TABLE IF EXISTS " + Schema.CREATE_AUTH_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Schema.CREATE_DATA_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Schema.CREATE_VAULT);
            onCreate(db);*/
        }

    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        Context context = getContext();
        dbHelper = new DBHelper(context);
        // permissions to be writable
        database = dbHelper.getWritableDatabase();

        if (database == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // the TABLE_NAME to query on
        switch (Uri.parse(uri.toString()).toString()){
            case DbTableStrings.DATA_URI:
                queryBuilder.setTables(DbTableStrings.TABLE_NAME_DATA);
                break;
            case DbTableStrings.AUTH_URI:
                queryBuilder.setTables(DbTableStrings.TABLE_NAME_AUTHENTICATION);
                break;
            case DbTableStrings.VAULT_URI:
                queryBuilder.setTables(DbTableStrings.TABLE_NAME_VAULT);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }


        Cursor cursor = queryBuilder.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub

        /*long id = database.insert(DbTableStrings.TABLE_NAME_DATA,
                null, values);*/
        /*int uriType = sURIMatcher.match(uri);
        long id = 0;*/
        long id =0;
        switch (Uri.parse(uri.toString()).toString()) {
            case DbTableStrings.DATA_URI:
                id = database.insert(DbTableStrings.TABLE_NAME_DATA,
                        null, values);
                break;
            case DbTableStrings.AUTH_URI:
                id = database.insert(DbTableStrings.TABLE_NAME_AUTHENTICATION, null, values);
                break;
            case DbTableStrings.VAULT_URI:
                id = database.insert(DbTableStrings.TABLE_NAME_VAULT, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
//        long row = database.insert(uri.toString(), "", values);
//
//        // If record is added successfully
//        if (row > 0) {
//            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
//            getContext().getContentResolver().notifyChange(newUri, null);
//            return newUri;
//        }
//        throw new SQLException("Fail to add a new record into " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(uri.toString() + "/" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
//        int count = 0;
        //database.delete()
        return 0;


    }


}
