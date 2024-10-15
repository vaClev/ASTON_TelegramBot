package org.example.DBEntities;

import org.example.DBActions.DBUserBehavior;
import java.sql.Timestamp;
import java.util.Date;

public class RequestResponseEntry {
    private String id;
    private Date datetime;
    private String requestText;
    private String responseText;
    private UserEntity user;

    public RequestResponseEntry(String requestText, String responseText, UserEntity user) {
        this.datetime = new Timestamp(System.currentTimeMillis());
        this.id = datetime.toString()+user.getTelegramChatId();
        this.requestText = requestText;
        this.responseText = responseText;
        this.user = user;
    }

    public RequestResponseEntry(String id, Timestamp datetime, String requestText, String responseText, long userid) {
        this.id = id;
        this.datetime = datetime;
        this.requestText = requestText;
        this.responseText = responseText;
        this.user = new DBUserBehavior().getById(userid);
    }

    public String getId() {
        return id;
    }

    public Date getDatetime() {
        return datetime;
    }
    public String getDatetimeAsString() {
        return datetime.toString();
    }
    public Timestamp getDatetimeAsTimestamp() {
        return new java.sql.Timestamp(datetime.getTime()); // convert gettime from date and assign it to your timestamp.
    }
    public String getRequestText() {
        return requestText;
    }

    public String getResponseText() {
        return responseText;
    }

    public long getUserId() {
        return user.getTelegramChatId();
    }

    @Override
    public String toString() {
        return "RequestResponseEntry{" +
                "datetime=" + datetime +
                ", requestText='" + requestText + '\'' +
                ", responseText='" + responseText + '\'' +
                ", user=" + user +
                '}';
    }


}
