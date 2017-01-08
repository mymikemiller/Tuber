package com.phistrix.tuber;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.common.collect.Lists;

public class MainActivity extends AppCompatActivity {
    public static final String ACCOUNT_KEY = "accountName";
    private static final int REQUEST_ACCOUNT_PICKER = 0;
    private static final int REQUEST_PLAYLIST_PICKER = 1;

    final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();
    final JsonFactory mJsonFactory = new GsonFactory();

    GoogleAccountCredential mCredential;
    YouTube mYoutube;
    private String mChosenAccountName;
    private String mPlaylistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mChosenAccountName = savedInstanceState.getString(ACCOUNT_KEY);
            mPlaylistId = savedInstanceState.getString(PlaylistsActivity.PLAYLIST_KEY);
        } else {
            loadAccount();
            loadPlaylist();
        }

        mCredential =
                GoogleAccountCredential.usingOAuth2(getApplicationContext(), Lists.newArrayList(Auth.SCOPES));
        mCredential.setSelectedAccountName(mChosenAccountName);
        mCredential.setBackOff(new ExponentialBackOff());

        String appName = getResources().getString(R.string.app_name);
        mYoutube = new YouTube.Builder(mTransport, mJsonFactory, mCredential).setApplicationName(
                        appName).build();
    }

    private void chooseAccount() {
        startActivityForResult(mCredential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }
    private void choosePlaylist() {
        Intent playlistPickerIntent = new Intent(this, PlaylistsActivity.class);
        playlistPickerIntent.putExtra(ACCOUNT_KEY, mChosenAccountName);
        startActivityForResult(playlistPickerIntent,
                REQUEST_PLAYLIST_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null
                        && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(
                            AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mChosenAccountName = accountName;
                        mCredential.setSelectedAccountName(accountName);
                        saveAccount();
                    }
                }
            case REQUEST_PLAYLIST_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null
                    && data.getExtras() != null) {

                    TuberPlaylist tuberPlaylist =
                            (TuberPlaylist)data.getExtras().getParcelable(
                                     PlaylistsActivity.PLAYLIST_KEY);

                    mPlaylistId = tuberPlaylist.getId();
                    savePlaylist();

                    Toast.makeText(getApplicationContext(), "Playlist Selected: " +
                            tuberPlaylist.getTitle(), Toast.LENGTH_LONG).show();
                }


                break;
        }
    }

    private void loadAccount() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        mChosenAccountName = sp.getString(ACCOUNT_KEY, null);
        invalidateOptionsMenu();
    }
    private void loadPlaylist() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        mPlaylistId = sp.getString(PlaylistsActivity.PLAYLIST_KEY, null);
        invalidateOptionsMenu();
    }

    private void saveAccount() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        sp.edit().putString(ACCOUNT_KEY, mChosenAccountName).commit();
    }
    private void savePlaylist() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        sp.edit().putString(PlaylistsActivity.PLAYLIST_KEY, mPlaylistId).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ACCOUNT_KEY, mChosenAccountName);
        outState.putString(PlaylistsActivity.PLAYLIST_KEY, mPlaylistId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_accounts:
                chooseAccount();
                return true;
            case R.id.menu_playlists:
                choosePlaylist();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
