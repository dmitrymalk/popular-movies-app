/*
 * Copyright 2016.  Dmitry Malkovich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dmitrymalkovich.android.popularmoviesapp.network;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Trailer implements Parcelable {

    @SuppressWarnings("unused")
    public static final String LOG_TAG = Trailer.class.getSimpleName();

    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("site")
    private String mSite;
    @SerializedName("size")
    private String mSize;

    // Only for createFromParcel
    private Trailer() {
    }

    public String getName() {
        return mName;
    }

    public String getKey() {
        return mKey;
    }

    public String getTrailerUrl() {
        return "http://www.youtube.com/watch?v=" + mKey;
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
        public Trailer createFromParcel(Parcel source) {
            Trailer trailer = new Trailer();
            trailer.mId = source.readString();
            trailer.mKey = source.readString();
            trailer.mName = source.readString();
            trailer.mSite = source.readString();
            trailer.mSize = source.readString();
            return trailer;
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mKey);
        parcel.writeString(mName);
        parcel.writeString(mSite);
        parcel.writeString(mSize);
    }
}
