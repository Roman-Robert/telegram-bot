package com.project.telegram_bot.constant;

import com.vdurmont.emoji.EmojiParser;

public class Constant {

    public static String INFO_MESSAGE = EmojiParser.parseToUnicode("Бот умеет делать много классных штук, " +
            "типа проверить твои знания в коротком тесте, записать тебя на тестовое занятие, напомнить тебе о " +
            "важных событиях и даже иногда присылать полезную инфу для изучения языка. " +
            "Ну что, заходи, не пожалеешь!\n" +
            "P.s. или пожалеешь, но это не точно.\n" +
            "В любом случае, попробовать стоит.\n\n" +
            "VK: https://vk.com/infinitive_online\n\n" +
            "Instagram: instagram.com/infinitive_online_school?igshid=YmMyMTA2M2Y=\n\n" +
            "Phone: +7(981)9347260\n\n" +
            "Whats App: https://wa.me/message/66F2CVQK7NLOK1");

    public static String ADMIN_ONLY = "Упс! Только админ может отправлять сообщения";

    public static String WELCOME_MSG = "Hi, %s! Nice to meet you!\uD83D\uDC4B" +
            "\nНажимай на кнопку Menu, чтобы узнать, что может бот." +
            "\nПодпишись на бота, чтобы получать полезную информацию." +
            "\nEnjoy your studies \uD83D\uDC9B\uD83D\uDC9A";
}
