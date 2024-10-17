package org.example.bot;

import org.example.DBActions.DBRequestsResponses;
import org.example.DBActions.DBUserBehavior;
import org.example.DBEntities.RequestResponseEntry;
import org.example.DBEntities.UserEntity;
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
        long chatId = update.getMessage().getChatId();
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
    private SendMessage getBotAnswer(String userMessage, long chatId) {
        SendMessage botMessage = new SendMessage();
        botMessage.setChatId(chatId);

        String answerText = getAnswerText(userMessage, chatId);
        botMessage.setText(answerText);
        return botMessage;
    }

    private String getAnswerText(String userMessage, long telegramChatId) {
        BotDialog dialog = new BotDialog(telegramChatId);
        return dialog.getAnswer(userMessage);

        //TODO выделить содержимое данной функции в отдельный клас-"ответчик"
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

}
