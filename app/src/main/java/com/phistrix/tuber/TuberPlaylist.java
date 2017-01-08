package com.phistrix.tuber;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.services.youtube.model.Playlist;

/**
 * Created by phist on 1/5/2017.
 */

public class TuberPlaylist implements Parcelable {
    private String mTitle;
    private String mId;

    public TuberPlaylist(Playlist playlist) {
        mTitle = playlist.getSnippet().getTitle();
        mId = playlist.getId();
    }

    private TuberPlaylist(Parcel in) {
        mTitle = in.readString();
        mId = in.readString();
    }

    public String getTitle() { return mTitle; }
    public String getId() { return mId; }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mId);
    }

    public static final Parcelable.Creator<TuberPlaylist> CREATOR
            = new Parcelable.Creator<TuberPlaylist>() {

        @Override
        public TuberPlaylist createFromParcel(Parcel in) {
            return new TuberPlaylist(in);
        }

        @Override
        public TuberPlaylist[] newArray(int size) {
            return new TuberPlaylist[size];
        }
    };
}
