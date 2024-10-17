package org.example.bot;

import org.example.DBActions.DBRequestsResponses;
import org.example.DBActions.DBUserBehavior;
import org.example.DBEntities.RequestResponseEntry;
import org.example.DBEntities.UserEntity;

public class BotDialog {
    private String userMessage;
    private final long telegramChatId;
    private final String NO_DATA_IN_DATABASE = "нет данных в базе";

    public BotDialog(long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public String getAnswer(String userMessage) {
        this.userMessage = userMessage.toLowerCase();
        if (isNewUser()){
            return registrationMenu();
        }
        return weatherMenu();
    }

    private String registrationMenu() {
        if(userMessage.equals("/start")){
            return "Добрый день. Пожалуйста зарегистрируйся чтобы использовать данный БОТ. Напиши свое имя или ник.";
        }
        UserEntity user = new UserEntity(telegramChatId, userMessage);
        boolean isInsert = new DBUserBehavior().insert(user);
        return isInsert? "успешно зарегистрирован с именем "+userMessage :"ошибка регистрации - попробуйте позже";
    }

    private String weatherMenu() {
        UserEntity user = new DBUserBehavior().getById(telegramChatId);
        if(userMessage.equals("/start")) //!isNewUser
        {
            return "Добрый день " + user.getUsername() +"! Укажи свой город (или более точный адрес по которому показать погоду):";
        }
        else {
            //проверить кто-то уже спрашивал в последние 6 часов тоже самое?
            //да -- вернуть из базы готовый ответ
            String response = getAnswerFromDB_last6hours();
            System.out.println(response);
            if(response.equals(NO_DATA_IN_DATABASE)){
                response = WeatherHelper.getTodayTemperature(userMessage);
                RequestResponseEntry rrEntry = new RequestResponseEntry(userMessage, response, user);
                boolean isInsert = new DBRequestsResponses().insert(rrEntry);
                if(isInsert) System.out.println("запрос пользователя и ответ бота сохранены в БД");
            }
            else{
                response = "Меня уже спрашивали недавно о погоде в этом месте : "+ response;
            }
            return response;
        }
    }
    private String getAnswerFromDB_last6hours()
    {
        String historyAnswer = new DBRequestsResponses().getEqualResponse(6,userMessage);
        if(historyAnswer!=null) return historyAnswer;
        return NO_DATA_IN_DATABASE;
    }
    private boolean isNewUser()
    {
        return new DBUserBehavior().isNewUser(telegramChatId);
    }
}
