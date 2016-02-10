package com.fartans.keyplus;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fartans.keyplus.Model.UserModel;
import com.fartans.keyplus.db.DbHandler;
import com.fartans.keyplus.db.DbTableStrings;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText password;
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    String Password;
    Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        applicationContext=getApplicationContext();

        this.setTitle("Login - Key+");

        //name = (EditText)findViewById(R.id.edittext_name);
        password = (EditText)findViewById(R.id.edittext_password);
        loginButton = (Button)findViewById(R.id.btn_login);
        //Name = name.getText().toString();
        Password = password.getText().toString();
        //signupButton = (TextView)findViewById(R.id.link_signup);
        //signupButton.setClickable(true);

        //insertdata();

        //SharedPreferences prefs = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userPassword = "";
                userPassword = password.getText().toString();

                if(!userPassword.matches("")) {
                    UserModel model = new UserModel();
                    model.UserName = "user";
                    model.Password = Long.parseLong(userPassword);
                    if (DbHandler.authUser(getApplicationContext(), model)) {
                        Intent intent = new Intent(LoginActivity.this, VaultActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void insertdata(){
        ContentValues values= new ContentValues();
        values.put(DbTableStrings.USERNAME,"admin");
        values.put(DbTableStrings.PASSWORD, "1234");
        //values.put(DbTableStrings.VAULT_ID,"2");

        Uri uri = getContentResolver().insert(Uri.parse(DbTableStrings.AUTH_URI), values);

        values = new ContentValues();
        values.put(DbTableStrings.VAULT_NAME, "Coke");
        values.put(DbTableStrings.VAULT_PASSWORD, "1234");
        values.put(DbTableStrings.IS_SECURE, 1);
        Uri uri2 = getContentResolver().insert(Uri.parse(DbTableStrings.VAULT_URI), values);

        values = new ContentValues();
        values.put(DbTableStrings.VAULT_NAME, "Pepsi");
        values.put(DbTableStrings.VAULT_PASSWORD, "1234");
        values.put(DbTableStrings.IS_SECURE, 0);
        Uri uri5 = getContentResolver().insert(Uri.parse(DbTableStrings.VAULT_URI), values);

        values = new ContentValues();
        values.put(DbTableStrings.VAULT_NAME, "Fanta");
        values.put(DbTableStrings.VAULT_PASSWORD, "5678");
        values.put(DbTableStrings.IS_SECURE, 1);
        Uri uri6 = getContentResolver().insert(Uri.parse(DbTableStrings.VAULT_URI), values);

        values = new ContentValues();
        values.put(DbTableStrings.KEY, "CardNumber");
        values.put(DbTableStrings.VALUE, "123456789");
        values.put(DbTableStrings.VAULT_ID, "1");
        Uri uri3 = getContentResolver().insert(Uri.parse(DbTableStrings.DATA_URI), values);

        Toast.makeText(getApplicationContext(), "New record inserted" + uri3.toString(), Toast.LENGTH_LONG).show();

    }

    private void registerInBackground(final String UserName, final String UserPassword) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
              
                try {
                    SharedPreferences prefs = getSharedPreferences("UserDetails",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(NAME, UserName);
                    editor.putString(PASSWORD, UserPassword);
                    editor.commit();
                    return true;
                }
                catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean status) {
                if (status==true) {
                    Toast.makeText(applicationContext, "Session Saved", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(applicationContext, "Session Failed", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
      public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
