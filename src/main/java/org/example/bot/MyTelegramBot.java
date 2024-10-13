package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyTelegramBot extends TelegramLongPollingBot {
    private final static String botName = "OV_first_bot";
    private final static String botToken = "7894004374:AAE16CocUk7emE2sKBbM6SWW5xFTAo5dRJQ";

    public MyTelegramBot() {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        System.out.println("chat id =" + chatId);
        String userMessage = update.getMessage().getText();
        SendMessage botMessage = getBotAnswer(userMessage, chatId);
        try {
            this.execute(botMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            System.out.println("возникло исключение при потытке отправки ответа - класс MyTelegramBot");
        }
    }

    private SendMessage getBotAnswer(String userMessage, String chatId) {
        SendMessage botMessage = new SendMessage();
        botMessage.setChatId(chatId);

        String answerText = getAnswerText(userMessage);
        botMessage.setText(answerText);
        return botMessage;
    }

    private String getAnswerText(String userMessage) {
        if(userMessage.equals("/start"))
        {
            return "hello, your message : "+ userMessage +"LOL" + "напиши город в котором хочешь узнать температуру";
        }
        return WeatherHelper.getTodayTemperature(userMessage);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

}
