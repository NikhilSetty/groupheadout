package com.fartans.keyplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.fartans.keyplus.Model.Vault;
import com.fartans.keyplus.db.DbHandler;

import java.util.ArrayList;
import java.util.List;

public class VaultActivity extends AppCompatActivity {
    ListView mListView;
    private List<Vault> vaultList;
    VaultListAdapter mAdapter;
    RelativeLayout mErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        mListView = (ListView) findViewById(R.id.activity_vault_list_view);
        mErrorLayout = (RelativeLayout) findViewById(R.id.error_view);
    }

    @Override
    protected void onStart(){
        super.onStart();

        List<Vault> vaults = DbHandler.readfromvaultwithKeyNumber(getApplicationContext());

        mListView.setAdapter(new VaultListAdapter(this, new ArrayList<Vault>()));

        if(vaults == null || vaults.size() == 0) {
            Point displaySize = new Point();
            this.getWindowManager().getDefaultDisplay().getRealSize(displaySize);

            /*new EasyDialog(VaultActivity.this)
                    .setLayoutResourceId(R.layout.layout_tip_content_horizontal)
                    .setBackgroundColor(VaultActivity.this.getResources().getColor(R.color.background_color_black))
                    .setLocation(new int[]{displaySize.x - 200, getActionBarHeight() + getStatusBarHeight() + 200})
                    .setGravity(EasyDialog.GRAVITY_BOTTOM)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(false)
                    .setMarginLeftAndRight(24, 24)
                    .setOutsideColor(R.color.primaryColor)
                    .show();*/

            mListView.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.VISIBLE);
            StaticHelper.IsFromSignUp = true;
        }else {
            PopulateListView();
            mErrorLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    public void PopulateListView(){
        vaultList = new ArrayList<>();

        vaultList = DbHandler.readfromvaultwithKeyNumber(getApplicationContext());

        mAdapter = new VaultListAdapter(this, vaultList);
        mListView.setAdapter(mAdapter);


        //List item listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Vault vault = vaultList.get(position);

                if(vault.getIsSecure() == Vault.SECURE_VAULT){
                    LayoutInflater li = LayoutInflater.from(VaultActivity.this);
                    View promptsView = li.inflate(R.layout.alert_vault_activity, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VaultActivity.this);
                    final EditText input = (EditText) promptsView.findViewById(R.id.editTextPassword);

                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder.setMessage("Authenticate to SecureKey!");
                    alertDialogBuilder.setPositiveButton("Login",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String enteredPassword = input.getText().toString();
                                    if (!enteredPassword.equals("") && enteredPassword.equals(Long.toString(vault.getPasscode()))) {
                                        Toast.makeText(getApplicationContext(), "Vault Login Successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), KeyValueActivity.class);
                                        intent.putExtra("vaultId", vault.getId());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Vault Login Failed, Try Again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );

                    alertDialogBuilder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    );
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else {

                    Intent intent = new Intent(getApplicationContext(), KeyValueActivity.class);
                    intent.putExtra("vaultId", vaultList.get(position).getId());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vault, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_key) {
            Intent intent = new Intent(VaultActivity.this, AddVaultActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onStart();
    }

    private int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getActionBarHeight()
    {
        return this.getSupportActionBar().getHeight();
    }
}
