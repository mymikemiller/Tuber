package com.phistrix.tuber;

import android.content.Context;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.common.collect.Lists;

/**
 * Created by phist on 1/11/2017.
 */

public class PlaylistManager {
    private YouTube mYouTube;
    private String mPlaylistId;

    public PlaylistManager(YouTube youTube, String playlistId) {
        mYouTube = youTube;
        mPlaylistId = playlistId;

    }
}
