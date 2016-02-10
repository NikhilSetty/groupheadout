package com.fartans.keyplus;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fartans.keyplus.Model.KeyValue;
import com.fartans.keyplus.Model.Vault;
import com.fartans.keyplus.db.DbTableStrings;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    ArrayList<Vault> vaultlist;
    ArrayList<KeyValue> keyvaluelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        readfromvault();
        getKeyValues();
    }

    public ArrayList getKeyValues() {
        keyvaluelist=new ArrayList<>();
        ContentResolver contentresolver=getContentResolver();
        Cursor cursor=contentresolver.query(Uri.parse(DbTableStrings.DATA_URI),null,null,null,null);
        if(cursor.moveToNext()){
            while (cursor.moveToNext()){
                KeyValue keyvalue=new KeyValue();
                keyvalue.setName(cursor.getString(cursor.getColumnIndex(DbTableStrings.KEY)));
                keyvalue.setValue(cursor.getString(cursor.getColumnIndex(DbTableStrings.VALUE)));
                keyvaluelist.add(keyvalue);
            }
        }
        return keyvaluelist;
    }

    public ArrayList readfromvault() {
        vaultlist = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor=contentResolver.query(Uri.parse(DbTableStrings.VAULT_URI),null,null,null,null);
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()) {
                Vault vault = new Vault();
                vault.setName(cursor.getString(cursor.getColumnIndex(DbTableStrings.VAULT_NAME)));
                vault.setKeyNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbTableStrings.VAULT_ID))));
                vaultlist.add(vault);
            }

        }
        return vaultlist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
