package org.example.DBEntities;

public class DBUser {
    public long id;
    public long telegramChatId;
    public String username;

    public DBUser(long id, long telegramChatId, String username) {
        this.id = id;
        this.telegramChatId = telegramChatId;
        this.username = username;
    }
    @Override
    public String toString() {
        return "DBUser{" +
                "id=" + id +
                ", telegramChatId=" + telegramChatId +
                ", username='" + username + '\'' +
                '}';
    }
}
