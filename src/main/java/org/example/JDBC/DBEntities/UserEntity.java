package org.example.JDBC.DBEntities;

public class UserEntity {
    private long id;
    private long telegramChatId;
    private String username;

    public UserEntity(long id, long telegramChatId, String username) {
        this.id = id;
        this.telegramChatId = telegramChatId;
        this.username = username;
    }
    public UserEntity( long telegramChatId, String username) {
        this.telegramChatId = telegramChatId;
        this.username = username;
    }
    public long getTelegramChatId() {
        return telegramChatId;
    }
    public String getUsername() {
        return username;
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
