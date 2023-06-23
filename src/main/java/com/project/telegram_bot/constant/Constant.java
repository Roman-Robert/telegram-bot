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
            "Telegram: @Infinitiveonline\n\n" +
            "Whats App: https://wa.me/message/66F2CVQK7NLOK1");

    public static String ADMIN_ONLY = "Упс! Только админ может отправлять сообщения";

    public static String WELCOME_MSG = "Hi, %s! Nice to meet you!\uD83D\uDC4B" +
            "\nНажимай на кнопку Menu, чтобы узнать, что может бот." +
            "\nПодпишись на бота, чтобы получать полезную информацию." +
            "\nEnjoy your studies \uD83D\uDC9B\uD83D\uDC9A";

    public static String SCORE_1 = "Скорее всего твой опыт в изучении английского языка не очень продолжительный, " +
            "либо ты учился достаточно давно. Можем предположить, что твой уровень False Beginner или Elementary. " +
            "Для более точного определения, запишись на бесплатное устное тестирование с методистом \uD83D\uDE09" +
            "\n\uD83D\uDC49Для записи пиши нам @Infinitiveonline";

    public static String SCORE_2 = "Совсем не плохо, скорее всего твой уровень Elementary + или Pre-intermediate. " +
            "Хочешь узнать точнее? Проходи бесплатное устное тестирование с методистом\uD83D\uDC4C" +
            "\n\uD83D\uDC49Для записи пиши нам @Infinitiveonline";

    public static String SCORE_3 = "Весьма похвально! Твои теоретические знания английского впечатляют. " +
            "Мы готовы предположить, что твой уровень английского примерно Intermediate или выше. " +
            "Готов проверить себя на бесплатном устном тестировании с методистом?!" +
            "\n\uD83D\uDC49Для записи пиши нам @Infinitiveonline";

    public static String WEBINAR = "Чтобы записаться на бесплатный вебинар: " +
            "\"Английский язык и лето: прокачай свои навыки, не потеряв отпуск!\" \uD83C\uDF1E\uD83D\uDCDA " +
            "напиши \"ВЕБИНАР\" в наши сообщения @InfinitiveOnline.\n" +
            "Что ты получишь?\n" +
            "60 минут концентрированной информации:\n" +
            "Теория - почему заниматься летом эффективнее?\n" +
            "Практика - участие в интерактивной активности для разгона своего английского.\n" +
            "Скидки и подарки для участников.";
}
