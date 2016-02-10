package com.fartans.keyplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fartans.keyplus.Model.KeyValue;
import com.fartans.keyplus.db.DbHandler;

import java.util.ArrayList;
import java.util.List;

public class KeyValueActivity extends AppCompatActivity {

    ArrayAdapter mAdapter;
    ListView mListView,list2;
    List<KeyValue> keyList;
    int vaultId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyvalue);
        //Get ValutID
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        vaultId=bundle.getInt("vaultId");
        setData();

    }
    public void setData()
    {
        mListView = (ListView) findViewById(R.id.activity_vault_list_view);
        list2=(ListView)findViewById(R.id.list1);
        keyList = new ArrayList<>();
        keyList = DbHandler.getPairsByvaultId(getApplicationContext(), vaultId);

        final List<String> kList = new ArrayList<>();
        int i=0;
        for(KeyValue key:keyList){
            kList.add(key.getName());
        }

        mAdapter = new ArrayAdapter<String>(KeyValueActivity.this,android.R.layout.simple_list_item_1,kList);
        list2.setAdapter(mAdapter);

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final KeyValue keyValuePair = keyList.get(position);
                LayoutInflater li = LayoutInflater.from(KeyValueActivity.this);
                View promptsView = li.inflate(R.layout.alert_view_key, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KeyValueActivity.this);
                final TextView textViewKey = (TextView) promptsView.findViewById(R.id.textViewKey);
                final TextView textViewValue = (TextView) promptsView.findViewById(R.id.textViewValue);

                textViewKey.setText("Key : " + keyValuePair.getName());
                textViewValue.setText("Value : " + keyValuePair.getValue());
                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setMessage("Your Key!");
                alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    }
                );
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_keyvalue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_keyvalue) {
            Intent intent = new Intent(this, AddKeyValueActivity.class);
            intent.putExtra("vaultId",vaultId);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }
}
