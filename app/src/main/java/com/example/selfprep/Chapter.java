package com.example.selfprep;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Chapter implements Parcelable {
    String name;
    int index;
    int topics;
    ArrayList<Integer> questions;
    int total;
    boolean done;

    public Chapter(String name, int index, int topics, ArrayList<Integer>questions, int total, boolean done) {

        this.name = name;
        this.index = index;
        this.topics = topics;
        this.questions = questions;
        this.total = total;
        this.done = done;
    }

    protected Chapter(Parcel in) {
        name = in.readString();
        index = in.readInt();
        topics = in.readInt();
        total = in.readInt();
        done = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(index);
        dest.writeInt(topics);
        dest.writeInt(total);
        dest.writeByte((byte) (done ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}