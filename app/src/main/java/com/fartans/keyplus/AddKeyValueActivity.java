package com.fartans.keyplus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fartans.keyplus.Model.KeyValue;
import com.fartans.keyplus.db.DbHandler;


public class AddKeyValueActivity extends Activity {
    EditText editTextKey;
    EditText editTextValue;
    Button saveButton;
    int vaultId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_keyvalue);
        Bundle bundle=getIntent().getExtras();
        vaultId=bundle.getInt("vaultId");

        editTextKey=(EditText)findViewById(R.id.edittext_key);
        editTextValue=(EditText)findViewById(R.id.edittext_value);
        saveButton=(Button)findViewById(R.id.btn_add);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String key=editTextKey.getText().toString();
            String value=editTextValue.getText().toString();
            if(!key.matches("") && !value.matches(""))
            {
                KeyValue keyValue=new KeyValue();
                keyValue.setName(key);
                keyValue.setValue(value);
                keyValue.setVaultId(vaultId);
                DbHandler.insertKeyValue(getApplicationContext(), keyValue);
                onBackPressed();
                finish();

            }
                else {
                Toast.makeText(getApplicationContext(), "Please enter all the fields", Toast.LENGTH_LONG).show();
            }
            }
        });
    }
}
