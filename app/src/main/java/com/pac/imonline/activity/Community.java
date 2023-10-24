package com.pac.imonline.activity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "communities")
public class Community implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String name;
    private String photoUrl;
    private String bannerUrl;

    public Community(String name, String photoUrl, String bannerUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.bannerUrl = bannerUrl;
    }

    protected Community(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        photoUrl = in.readString();
        bannerUrl = in.readString();
    }

    public static final Creator<Community> CREATOR = new Creator<Community>() {
        @Override
        public Community createFromParcel(Parcel in) {
            return new Community(in);
        }

        @Override
        public Community[] newArray(int size) {
            return new Community[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(photoUrl);
        dest.writeString(bannerUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
