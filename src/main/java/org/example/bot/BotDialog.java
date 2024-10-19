package org.example.bot;

import org.example.DBActions.DBAddressBehavior;
import org.example.DBActions.DBRequestsResponses;
import org.example.DBActions.DBUserBehavior;
import org.example.DBEntities.Address;
import org.example.DBEntities.RequestResponseEntry;
import org.example.DBEntities.UserEntity;

import java.util.LinkedList;

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
            if (isUniqueAddress) {
                new DBAddressBehavior().insert(new Address(userMessage));
                setAnswerFromAPIService();
            } else if (trySetAnswerFromDB_last6hours()) {
                response = "#Меня уже спрашивали недавно о погоде в этом месте : " + response;
            } else {
                setAnswerFromAPIService();
            }
            insertRequestResponseToDB(userMessage, response, user);
            return response;
        }
    }

    private void insertRequestResponseToDB(String userMessage, String response, UserEntity user) {
        RequestResponseEntry rr = new RequestResponseEntry(userMessage, response, user);
        boolean isInsert = new DBRequestsResponses().insert(rr);
        if (isInsert) {
            System.out.println("запрос пользователя и ответ бота записаны в базу данных");
        }
    }
    private boolean trySetAnswerFromDB_last6hours() {
        LinkedList<String> historyAnswers = new DBRequestsResponses().getEqualResponses(6, userMessage);
        if (historyAnswers == null) {
            return false;
        }
        for (var historyAnswer : historyAnswers) {
            if (historyAnswer.toUpperCase().contains("#Меня уже спрашивали".toUpperCase())) {
                continue;
            }
            response = historyAnswer;
            return true;
        }
        return false;
    }
    private void setAnswerFromAPIService() {
        response = WeatherHelper.getTodayTemperature(userMessage);
    }
    private boolean isNewUser() {
        return new DBUserBehavior().isNewUser(telegramChatId);
    }
}
