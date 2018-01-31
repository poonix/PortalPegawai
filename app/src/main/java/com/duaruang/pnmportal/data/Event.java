package com.duaruang.pnmportal.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by way on 9/17/2017.
 */

public class Event implements Parcelable {

    private String title, description, picture, start_date, end_date, created_date, modified_date;

    public Event(String title, String description, String picture, String start_date, String end_date, String created_date, String modified_date){
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.start_date = start_date;
        this.end_date = end_date;
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

    public String getStart_date(){
        return start_date;
    }

    public String getEnd_date(){
        return end_date;
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
                "[event: title=%1$s, description=%2$s, picture=%3$s, start_date=%4$s, end_date=%5$s, created_date=%6$s, modified_date=%7$s]",
                title, description, picture, start_date, end_date, created_date, modified_date);
    }

    protected Event(Parcel in) {
        title = in.readString();
        description = in.readString();
        picture = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        created_date = in.readString();
        modified_date = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
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
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeString(created_date);
        dest.writeString(modified_date);
    }
}
