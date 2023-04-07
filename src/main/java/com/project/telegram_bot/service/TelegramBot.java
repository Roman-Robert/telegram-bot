package com.project.telegram_bot.service;

import com.project.telegram_bot.config.BotConfig;
import com.project.telegram_bot.model.User;
import com.project.telegram_bot.model.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private final UserRepository userRepository;
    final BotConfig config;
    private final long OWNER_ID = 167433450;
    private final long ADMIN_ID = 264128213;
    int testResult = 0;
    int questionNumber = 0;
    List<String> personalResults = new ArrayList<>();

    static final String INFO_MESSAGE = EmojiParser.parseToUnicode("This bot was created to help learning " + "english language with online school Infinitive.\n\n" + "Website: infinitive-spb.ru\n\n" +
            //инстаграм ссыль заменить на профиль
            "Instagram: instagram.com/infinitive_online_school?igshid=YmMyMTA2M2Y=\n\n" + "Phone: +7(911)924-36-04");

    public TelegramBot(BotConfig config, UserRepository userRepository) {
        this.config = config;
        this.userRepository = userRepository;

        //Меню чат-бота
        List<BotCommand> listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand("/start", "welcome"));
        listOfCommands.add(new BotCommand("/subscribe", "subscribe to receive content"));
        listOfCommands.add(new BotCommand("/unsubscribe", "cancel subscription"));
        //listOfCommands.add(new BotCommand("/send", "send message to all"));  //Only owner and admin know about this function
        listOfCommands.add(new BotCommand("/test", "find out your level of English"));
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

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


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
                case "/test":
                    testEnglishLevelQuestion(update.getMessage().getChatId());
                    break;
                case "/info":
                    sendMessage(chatId, INFO_MESSAGE);
                    break;
                default:
                    if (update.getMessage().getChatId() == OWNER_ID || update.getMessage().getChatId() == ADMIN_ID) {
                        log.info("Owner or admin sent something");
                    } else {
                        sendMessage(chatId, "Sorry, this command wasn't recognized" + "\uD83E\uDEE4");
                    }
            }
//Sending MESSAGE
            if (messageText.contains("/send")) {
                sendMessageToAllSubscribers(chatId, messageText);
            }

//Sending DOCUMENT
        } else if (update.hasMessage() && update.getMessage().hasDocument()) {

            String fileId = update.getMessage().getDocument().getFileId();
            long chatId = update.getMessage().getChatId();
            String caption = update.getMessage().getCaption();

            sendDocumentToAllSubscribers(chatId, fileId, caption);

//Sending PHOTO
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            long chatId = update.getMessage().getChatId();
            String caption = update.getMessage().getCaption();
            var photos = update.getMessage().getPhoto();
            String photoId = Objects.requireNonNull(photos.stream().findFirst().orElse(null)).getFileId();

            sendPhotoToAllSubscribers(chatId, photoId, caption);

//Check message for call back query(for test answers)
        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            int messageID = update.getCallbackQuery().getMessage().getMessageId();
            long chatID = update.getCallbackQuery().getMessage().getChatId();

            EditMessageText message = new EditMessageText();
            message.setChatId(chatID);
            message.setMessageId(messageID);

            if (callBackData.equals(TestEnglishLevel.YES_BUTTON)) {
                message.setText("Perfect! Let's go!");
                execute(message);

                Thread.sleep(1000);
                message.setText("3");
                execute(message);

                Thread.sleep(1000);
                message.setText("2");
                execute(message);

                Thread.sleep(1000);
                message.setText("1");
                execute(message);

                Thread.sleep(1000);
                message.setText("GO!");
                execute(message);

                //задаем первый вопрос
                questionNumber = 1;
                testStart(chatID, message, TestEnglishLevel.questionOne);


            } else if (callBackData.equals(TestEnglishLevel.NO_BUTTON)) {
                String text = "You have enough to prepare!";
                message.setChatId(chatID);
                message.setMessageId(messageID);
                message.setText(text);

                execute(message);

            } else {
                switch (questionNumber) {
                    case 1:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.questionTwo);
                        break;
                    case 2:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.questionThree);
                        break;
                    case 3:
                        personalResults.add(update.getCallbackQuery().getData());
                        //take a result of the test
                        testEnd(chatID, messageID);
                        break;
                }
            }

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


    private void sendDocument(long chatId, String fileId, String caption) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        InputFile inputFile = new InputFile(fileId);
        sendDocument.setDocument(inputFile);
        sendDocument.setCaption(caption);

        try {
            execute(sendDocument);
            log.info(chatId + " sent file");
        } catch (TelegramApiException e) {
            log.error("Error in method .sendDocument()" + e.getMessage());
            throw new RuntimeException();
        }
    }

    private void sendDocumentToAllSubscribers(long chatId, String fileId, String caption) {
        if (chatId == OWNER_ID || chatId == ADMIN_ID) {

            Iterable<User> users = userRepository.findAll();

            for (User user : users) {
                sendDocument(user.getChatID(), fileId, caption);
            }

            log.info(String.format("%d sent file to all", chatId));

        } else {
            sendMessage(chatId, "Sorry, only owner and admin can send messages" + "\uD83D\uDE0E");
        }
    }


    private void sendPhoto(long chatId, String photoId, String caption) {
        InputFile inputFile = new InputFile(photoId);
        SendPhoto sendPhoto = new SendPhoto();

        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setCaption(caption);

        try {
            execute(sendPhoto);
            log.info(chatId + " sent photo");
        } catch (TelegramApiException e) {
            log.error("Error in method .sendPhoto()" + e.getMessage());
            throw new RuntimeException();
        }
    }

    private void sendPhotoToAllSubscribers(long chatId, String photoId, String caption) {
        if (chatId == OWNER_ID || chatId == ADMIN_ID) {

            Iterable<User> users = userRepository.findAll();

            for (User user : users) {
                sendPhoto(user.getChatID(), photoId, caption);
            }

            log.info(String.format("%d sent photo to all subscribers", chatId));

        } else {
            sendMessage(chatId, "Sorry, only owner and admin can send messages" + "\uD83D\uDE0E");
        }
    }


    //test English level knowledge
    private void testEnglishLevelQuestion(long chatId) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Are you ready?");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();

        var buttonYes = new InlineKeyboardButton("Yes!");
        var buttonNo = new InlineKeyboardButton("No \uD83E\uDEE8");

        buttonYes.setCallbackData(TestEnglishLevel.YES_BUTTON);
        buttonNo.setCallbackData(TestEnglishLevel.NO_BUTTON);

        rowLine.add(buttonNo);
        rowLine.add(buttonYes);
        rowsList.add(rowLine);

        keyboardMarkup.setKeyboard(rowsList);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error in method .testEnglishLevelQuestion()" + e.getMessage());
            throw new RuntimeException();
        }
    }

    private void testStart(long chatID, EditMessageText message, List<String> question) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> rowOne = new ArrayList<>();
        List<InlineKeyboardButton> rowTwo = new ArrayList<>();
        List<InlineKeyboardButton> rowThree = new ArrayList<>();
        List<InlineKeyboardButton> rowFour = new ArrayList<>();

        var buttonOne = new InlineKeyboardButton(question.get(1));
        var buttonTwo = new InlineKeyboardButton(question.get(2));
        var buttonThree = new InlineKeyboardButton(question.get(3));
        var buttonFour = new InlineKeyboardButton(question.get(4));

        buttonOne.setCallbackData(question.get(1));
        buttonTwo.setCallbackData(question.get(2));
        buttonThree.setCallbackData(question.get(3));
        buttonFour.setCallbackData(question.get(4));

        rowOne.add(buttonOne);
        rowTwo.add(buttonTwo);
        rowThree.add(buttonThree);
        rowFour.add(buttonFour);

        rows.add(rowOne);
        rows.add(rowTwo);
        rows.add(rowThree);
        rows.add(rowFour);

        keyboard.setKeyboard(rows);

        message.setChatId(chatID);
        message.setText(question.get(0));
        message.setReplyMarkup(keyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error in method .testStart()" + e.getMessage());
            throw new RuntimeException();
        }
    }

    private void testEnd(long chatID, int messageID) {

        for (String str : personalResults) {
            if (TestEnglishLevel.correctAnswersList.contains(str)) testResult++;
        }

        EditMessageText message = new EditMessageText();
        message.setChatId(chatID);
        message.setMessageId(messageID);
        message.setText("Congratulation! Your result is " + testResult + "/" + TestEnglishLevel.correctAnswersList.size() + "questions!");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error in method .testEnd()" + e.getMessage());
            throw new RuntimeException();
        }
    }

}