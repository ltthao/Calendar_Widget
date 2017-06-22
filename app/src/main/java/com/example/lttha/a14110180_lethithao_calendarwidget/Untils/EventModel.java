package com.example.lttha.a14110180_lethithao_calendarwidget.Untils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lttha on 5/28/2017.
 */

public class EventModel {

    Integer id;

    String title,content,eventDate,eventTime;

    public EventModel(Integer id, String title, String content, String eventDate, String eventTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
