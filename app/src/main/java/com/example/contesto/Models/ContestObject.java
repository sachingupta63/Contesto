package com.example.contesto.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.contesto.Utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = Constants.CONTEST_OBJECT_TABLE_NAME)
public class ContestObject {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @SerializedName("site")
    @Expose
    private String platform;
    @SerializedName("name")
    @Expose
    private String title;

    @SerializedName("start_time")
    @Expose
    private String start;

    @SerializedName("end_time")
    @Expose
    private String end;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("url")
    @Expose
    private String link;

    @SerializedName("status")
    @Expose
    private String status;


    public ContestObject() {
    }

    public ContestObject(String title, String start, String end, String duration, String link, String status, String platform) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.link = link;
        this.status = status;
        this.platform = platform;
    }

    public ContestObject(String platform, String contestId, String title, String start, String end, String duration, String link, String status) {
        this.platform = platform;
//        this.contestId = contestId;
        this.title = title;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.link = link;
        this.status = status;
    }

    public int get_id() {
        return _id;
    }

    public String getPlatform() {
        return platform;
    }

    public String getTitle() {
        return title;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getDuration() {
        return duration;
    }

    public String getLink() {
        return link;
    }

    public String getStatus() {
        return status;
    }


    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }


}
