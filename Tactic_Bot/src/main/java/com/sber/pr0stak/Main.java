package com.sber.pr0stak;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;


public class Main {
    public static void main(String[] args) {
        String botToken = "8129173180:AAH23z6h_jFEv-OpIGD-RLAA8F-jTNVrYSE";
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new TacticBot(botToken));
            System.out.println("Tactic_Bot successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
