package org.example;

import org.example.bot.MyTelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BotServerApp {
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyTelegramBot());
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            System.out.println("возникло TelegramApiException в классе BotServerApp - функция Main");
        }
    }
}