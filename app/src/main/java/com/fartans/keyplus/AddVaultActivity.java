package com.fartans.keyplus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.fartans.keyplus.Model.Vault;
import com.fartans.keyplus.db.DbHandler;

public class AddVaultActivity extends AppCompatActivity {
    EditText editTextName;
    EditText editTextPassword;
    CheckBox checkBoxSetPassword;
    Button addVault;
    android.support.design.widget.TextInputLayout inputLayout;

    private static boolean executeOnce = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vault);

        editTextName = (EditText)findViewById(R.id.edittext_vault_name);
        editTextPassword = (EditText)findViewById(R.id.edittext_vault_password);
        checkBoxSetPassword = (CheckBox)findViewById(R.id.set_vault_password);
        inputLayout = (android.support.design.widget.TextInputLayout)findViewById(R.id.vault_password_edittext);
        addVault = (Button)findViewById(R.id.btn_add_vault);



        inputLayout.setVisibility(View.GONE);

        checkBoxSetPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBoxSetPassword.isChecked()) {
                    inputLayout.setVisibility(View.VISIBLE);
                } else {
                    inputLayout.setVisibility(View.GONE);
                }
            }
        });

        addVault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vaultName = "";
                int vaultPin;
                vaultName = editTextName.getText().toString();
                Vault vault = new Vault();

                if (!vaultName.matches("")) {
                    vault.setName(vaultName);
                    vault.setKeyNumber(0);
                    vault.setIsSecure(0);
                    if (checkBoxSetPassword.isChecked()) {
                        if (!editTextPassword.getText().toString().matches("")) {
                            vaultPin = Integer.parseInt(editTextPassword.getText().toString());
                            vault.setPasscode(vaultPin);
                            vault.setIsSecure(1);
                            DbHandler.insertVault(getApplicationContext(), vault);
                            finish();
                        }
                    } else {
                        vault.setPasscode(0);
                        DbHandler.insertVault(getApplicationContext(), vault);
                        finish();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }
}
