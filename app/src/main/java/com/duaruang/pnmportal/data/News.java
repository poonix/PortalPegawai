package com.duaruang.pnmportal.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by way on 9/17/2017.
 */

public class News implements Parcelable{

    private String title, description,url_download, picture, created_date, modified_date;

    public News(String title, String description, String picture,String url_download, String created_date, String modified_date){
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.url_download = url_download;
        this.created_date = created_date;
        this.modified_date = modified_date;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getPicture(){
        return picture;
    }

    public String getUrlDownload(){
        return url_download;
    }

    public String getCreated_date(){
        return created_date;
    }

    public String getModified_date(){
        return modified_date;
    }

    @Override
    public String toString()
    {
        return String.format(
                "[news: title=%1$s,description=%2$s,picture=%3$s, url_download=%4$s, created_date=%5$s, modified_date=%6$s]",
                title, description,picture,url_download, created_date, modified_date);
    }

    protected News(Parcel in) {
        title = in.readString();
        description = in.readString();
        picture = in.readString();
        url_download = in.readString();
        created_date = in.readString();
        modified_date = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(picture);
        dest.writeString(url_download);
        dest.writeString(created_date);
        dest.writeString(modified_date);
    }
}
