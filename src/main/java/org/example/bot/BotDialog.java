package org.example.bot;

import org.example.DBActions.DBAddressBehavior;
import org.example.DBActions.DBRequestsResponses;
import org.example.DBActions.DBUserBehavior;
import org.example.DBEntities.Address;
import org.example.DBEntities.RequestResponseEntry;
import org.example.DBEntities.UserEntity;

public class BotDialog {
    private String userMessage;
    private String response;
    private final UserEntity user;
    private final long telegramChatId;

    public BotDialog(long telegramChatId) {
        this.telegramChatId = telegramChatId;
        this.user = new DBUserBehavior().getById(telegramChatId);
    }

    public String getAnswer(String userMessage) {
        this.userMessage = userMessage.toLowerCase();
        if (isNewUser()) {
            return registrationMenu();
        }
        return weatherMenu();
    }

    private String registrationMenu() {
        if (userMessage.equals("/start")) {
            return "Добрый день. Пожалуйста зарегистрируйся чтобы использовать данный БОТ. Напиши свое имя или ник.";
        }
        UserEntity user = new UserEntity(telegramChatId, userMessage);
        boolean isInsert = new DBUserBehavior().insert(user);
        return isInsert ? "успешно зарегистрирован с именем " + userMessage : "ошибка регистрации - попробуйте позже";
    }

    private String weatherMenu() {
        if (userMessage.equals("/start")) {
            return "Добрый день " + user.getUsername() + "! Укажи свой город (или более точный адрес по которому показать погоду):";
        } else {
            boolean isUniqueAddress = new DBAddressBehavior().isNewAddress(userMessage);
            if (isUniqueAddress){
                new DBAddressBehavior().insert(new Address(userMessage));
            }
            else if (trySetAnswerFromDB_last6hours()) {
                System.out.println(response);
                //TODO сделай заись в БД
                return "#Меня уже спрашивали недавно о погоде в этом месте : " + response;
            }
            setAnswerFromAPIService();
            return response;
        }
    }
    private boolean trySetAnswerFromDB_last6hours() {
        String historyAnswer = new DBRequestsResponses().getEqualResponse(6, userMessage);
        if (historyAnswer != null) {
            response = historyAnswer;
            return true;
        }
        return false;
    }
    private void setAnswerFromAPIService()
    {
        response = WeatherHelper.getTodayTemperature(userMessage);

        //TODO сделай запись в БД
        RequestResponseEntry rrEntry = new RequestResponseEntry(userMessage, response, user);
        boolean isInsert = new DBRequestsResponses().insert(rrEntry);
        if (isInsert) System.out.println("запрос пользователя и ответ бота сохранены в БД");
    }

    private boolean isNewUser() {
        return new DBUserBehavior().isNewUser(telegramChatId);
    }
}
