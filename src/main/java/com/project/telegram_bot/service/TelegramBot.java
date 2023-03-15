package com.project.telegram_bot.service;

import com.project.telegram_bot.config.BotConfig;
import com.project.telegram_bot.model.User;
import com.project.telegram_bot.model.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    final BotConfig config;
    private final long OWNER_ID = 167433450;
    private final long ADMIN_ID = 264128213;

    static final String INFO_MESSAGE = EmojiParser.parseToUnicode(
            "This bot was created to help learning " +
                    "english language with online school Infinitive.\n\n" +
                    "Website: infinitive-spb.ru\n\n" +
                    //инстаграм ссыль заменить на профиль
                    "Instagram: instagram.com/infinitive_online_school?igshid=YmMyMTA2M2Y=\n\n" +
                    "Phone: +7(911)924-36-04");

    public TelegramBot(BotConfig config, UserRepository userRepository) {
        this.config = config;
        this.userRepository = userRepository;

        //Меню чат-бота
        List<BotCommand> listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand("/start", "welcome"));
        listOfCommands.add(new BotCommand("/subscribe", "subscribe to receive content"));
        listOfCommands.add(new BotCommand("/unsubscribe", "cancel subscription"));
        listOfCommands.add(new BotCommand("/send", "send message to all"));
        listOfCommands.add(new BotCommand("/info", "info how to use this bot"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error with list of commands: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            if (messageText.contains("/send")) {
                sendMessageToAllSubscribers(chatId, messageText);
            }

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/subscribe":
                    subscribeUser(update.getMessage());
                    break;
                case "/unsubscribe":
                    unSubscribeUser(update.getMessage());
                    break;
                case "/info":
                    sendMessage(chatId, INFO_MESSAGE);
                    break;
                default:
                    sendMessage(chatId, "Sorry, this command wasn't recognized" + "\uD83E\uDEE4");
            }

        } else if (update.hasMessage() && update.getMessage().hasDocument()) {

            String fileId = update.getMessage().getDocument().getFileId();
            long chatId = update.getMessage().getChatId();

            sendDocumentToAllSubscribers(chatId, fileId);

        } else {
            log.error("Empty message was received");
            throw new NullPointerException();
        }

    }


    private void startCommandReceived(long chatId, String name) {
        String answer = String.format("Hi, %s! Nice to meet you!", name);
        sendMessage(chatId, answer);
        log.info(String.format("Replied to user %s", name));
    }

    private void subscribeUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            Long chatId = message.getChatId();
            Chat chat = message.getChat();

            User user = new User();

            user.setChatID(chatId);
            user.setUserName(chat.getUserName());
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setSubscribedAt(new Timestamp(System.currentTimeMillis()));
            user.setLevel(0);

            userRepository.save(user);
            sendMessage(chatId, "You subscribed successfully!");
            log.info("User subscribed: " + user);
        } else {
            sendMessage(message.getChatId(), "You are already subscribed");
        }
    }

    private void unSubscribeUser(Message message) {
        if (userRepository.findById(message.getChatId()).isPresent()) {
            userRepository.deleteById(message.getChatId());
            sendMessage(message.getChatId(), "You have successfully unsubscribed");
        } else {
            sendMessage(message.getChatId(), "You have not been subscribed");
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error in method .sendMessage()" + e.getMessage());
            throw new RuntimeException();
        }
    }

    private void sendMessageToAllSubscribers(long chatId, String message) {

        if (chatId == OWNER_ID || chatId == ADMIN_ID) {

            String textToSendForAll = EmojiParser.parseToUnicode(message.substring(message.indexOf(" ")));
            Iterable<User> users = userRepository.findAll();

            for (User user : users) {
                sendMessage(user.getChatID(), textToSendForAll);
            }
            log.info(String.format("%d sent message to all", chatId));
        } else {
            sendMessage(chatId, "Sorry, only owner and admin can send messages" + "\uD83D\uDE0E");
        }
    }

    private void sendDocument(long chatId, String fileId) {

        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        InputFile inputFile = new InputFile(fileId);
        sendDocument.setDocument(inputFile);

        try {
            execute(sendDocument);
            log.info(chatId + " sent file");
        } catch (TelegramApiException e) {
            log.error("Error in method .sendDocument()" + e.getMessage());
            throw new RuntimeException();
        }
    }

    private void sendDocumentToAllSubscribers(long chatId, String fileId) {

        if (chatId == OWNER_ID || chatId == ADMIN_ID) {

            Iterable<User> users = userRepository.findAll();

            for (User user : users) {
                sendDocument(user.getChatID(), fileId);
            }

            log.info(String.format("%d sent message to all", chatId));
        } else {
            sendMessage(chatId, "Sorry, only owner and admin can send messages" + "\uD83D\uDE0E");
        }
    }

}