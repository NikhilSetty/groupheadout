package com.fartans.keyplus.ui.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.internal.widget.ListViewCompat;
import android.view.View;
import android.widget.TextView;

import com.fartans.keyplus.Model.AppKeyModel;
import com.fartans.keyplus.R;
import com.fartans.keyplus.commons.SecureKeySharedPreferences;
import com.fartans.keyplus.ui.activity.adapter.KeyPreferencesAdapter;

import java.util.List;

/**
 * @author hitesh.sethiya on 06/02/16.
 */
public class KeyPreferencesActivity extends Activity {

    //All the data set elements
    private static List<AppKeyModel> mListOfKeysAndApps;

    // All the UI elements
    private ListViewCompat mAppsListView;
    private FloatingActionButton mAddOrEditFloatingActionsButton;
    private TextView mMessageTextView;

    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@link #setContentView(int)} to inflate the
     * activity's UI, using {@link #findViewById} to programmatically interact
     * with widgets in the UI, calling
     * {@link #managedQuery(Uri, String[], String, String[], String)} to retrieve
     * cursors for data being displayed, etc.
     * <p>
     * <p>You can call {@link #finish} from within this function, in
     * which case onDestroy() will be immediately called without any of the rest
     * of the activity lifecycle ({@link #onStart}, {@link #onResume},
     * {@link #onPause}, etc) executing.
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @see #onStart
     * @see #onSaveInstanceState
     * @see #onRestoreInstanceState
     * @see #onPostCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_key_preferences);
        inflateLayout();
        getDataFromSharedPreference();
        if(mListOfKeysAndApps != null && !mListOfKeysAndApps.isEmpty()) {
            mAddOrEditFloatingActionsButton.setImageResource(R.drawable.pencil_white);
            mAddOrEditFloatingActionsButton.setOnClickListener(onEditPrefClickListener);
            KeyPreferencesAdapter keyPreferencesAdapter = new KeyPreferencesAdapter(this,R.layout.app_key_list_item,mListOfKeysAndApps);
            mAppsListView.setAdapter(keyPreferencesAdapter);
        } else {
            showMessage(true,"No Preferences set");
            mAddOrEditFloatingActionsButton.setImageResource(R.drawable.add_white);
            mAddOrEditFloatingActionsButton.setOnClickListener(onAddPrefClickListener);
            mAddOrEditFloatingActionsButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * To associate all the views to the activity.
     */
    private void inflateLayout() {
        mAppsListView = (ListViewCompat) findViewById(R.id.apps_list_view);
        mAddOrEditFloatingActionsButton = (FloatingActionButton) findViewById(R.id.edit_keys_fab);
        mMessageTextView = (TextView) findViewById(R.id.message_add_key_pref_tv);
    }

    private void getDataFromSharedPreference() {
        if(SecureKeySharedPreferences.getInstance(KeyPreferencesActivity.this) != null) {
            mListOfKeysAndApps = SecureKeySharedPreferences.getInstance(KeyPreferencesActivity.this).getAppKeyPreferences();
        }
    }

    private View.OnClickListener onEditPrefClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener onAddPrefClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void showMessage(boolean show, String message) {
        if(show){
            if(message != null && !message.isEmpty()) {
                mMessageTextView.setText(message);
            }
            mMessageTextView.setVisibility(View.VISIBLE);
            mAppsListView.setVisibility(View.GONE);
            mAddOrEditFloatingActionsButton.setVisibility(View.GONE);
        } else {
            mMessageTextView.setVisibility(View.GONE);
            mAppsListView.setVisibility(View.VISIBLE);
            mAddOrEditFloatingActionsButton.setVisibility(View.VISIBLE);
        }
    }
}
