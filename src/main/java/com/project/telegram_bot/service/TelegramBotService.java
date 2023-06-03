package com.project.telegram_bot.service;

import com.project.telegram_bot.config.BotConfig;
import com.project.telegram_bot.constant.Constant;
import com.project.telegram_bot.model.dto.UserDTO;
import com.project.telegram_bot.util.TestEnglishLevel;
import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
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
public class TelegramBotService extends TelegramLongPollingBot {

    private final UserService userService;
    final BotConfig config;
    @Value("${owner_id}")
    private long OWNER_ID;
    @Value("${admin_id}")
    private long ADMIN_ID;
    int testResult = 0;
    int questionNumber = 0;
    List<String> personalResults = new ArrayList<>();

    @Autowired
    public TelegramBotService(BotConfig config, UserService userService) {
        this.config = config;
        this.userService = userService;
//        this.userRepository = userRepository;

        //Menu commands
        List<BotCommand> listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand("/start", "начать работу с ботом"));
        listOfCommands.add(new BotCommand("/subscribe", "подписаться"));
        listOfCommands.add(new BotCommand("/unsubscribe", "отменить подписку"));
        listOfCommands.add(new BotCommand("/test", "пройти тест"));
        listOfCommands.add(new BotCommand("/lesson", "получить бесплатный урок"));
        listOfCommands.add(new BotCommand("/info", "информация"));
        //Only owner and admin know about this functions
        //listOfCommands.add(new BotCommand("/send", "отправить сообщение всем подписчикам"));
        //listOfCommands.add(new BotCommand("/users", "получить список подписчиков бота"));


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
                case "/lesson":
                    giveFreeLessonPromoCode(update.getMessage());
                    break;
                case "/info":
                    sendMessage(chatId, Constant.INFO_MESSAGE);
                    break;
                case "/users":
                    getAllSubscribers(chatId);
                default:
                    if (update.getMessage().getChatId() == OWNER_ID || update.getMessage().getChatId() == ADMIN_ID) {
                        log.info("Owner or admin sent something");
                    } else {
                        sendMessage(chatId, "Sorry, this command wasn't recognized \uD83E\uDEE4");
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

                //ask the first question
                questionNumber = 1;
                testStart(chatID, message, TestEnglishLevel.question1);


            } else if (callBackData.equals(TestEnglishLevel.NO_BUTTON)) {
                String text = "У тебя достаточно времени чтобы подготовиться!";
                message.setChatId(chatID);
                message.setMessageId(messageID);
                message.setText(text);

                execute(message);

            } else {

                switch (questionNumber) {
                    case 1:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.question2);
                        break;
                    case 2:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.question3);
                        break;
                    case 3:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.question4);
                        break;
                    case 4:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.question5);
                        break;
                    case 5:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.question6);
                        break;
                    case 6:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.question7);
                        break;
                    case 7:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.question8);
                        break;
                    case 8:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.question9);
                        break;
                    case 9:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
                        testStart(chatID, message, TestEnglishLevel.question10);
                        break;
                    case 10:
                        personalResults.add(update.getCallbackQuery().getData());
                        questionNumber++;
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
        String answer = String.format(Constant.WELCOME_MSG, name);
        sendMessage(chatId, answer);
        log.info(String.format("Replied to user %d", chatId));
    }

    private void subscribeUser(Message message) {
        UserDTO user = userService.getUserById(message.getChatId());
        Long chatId = message.getChatId();
        Chat chat = message.getChat();

        if (user==null) {
            UserDTO userNew = UserDTO.builder()
                    .chatID(chatId)
                    .userName(chat.getUserName())
                    .firstName(chat.getFirstName())
                    .lastName(chat.getLastName())
                    .subscribedAt(new Timestamp(System.currentTimeMillis()))
                    .level(0)
                    .isActive("YES")
                    .build();
            userService.saveUser(userNew);
            sendMessage(chatId, "Подписка успешно оформлена!");
            log.info("User subscribed: " + userNew.toString());
        } else if (user.getIsActive().equals("NO")) {
            user.setIsActive("YES");
            userService.saveUser(user);
            sendMessage(chatId, "Вы снова подписались!");
        } else {
            sendMessage(message.getChatId(), "Вы уже подписаны на бота \uD83D\uDC4D");
        }
    }

    private void unSubscribeUser(Message message) {
        UserDTO user = userService.getUserById(message.getChatId());

        if (user!=null && user.getIsActive().equals("YES")) {
            user.setIsActive("NO");
            userService.saveUser(user);
            sendMessage(message.getChatId(), "Вы отписались от рассылки.");
        } else {
            sendMessage(message.getChatId(), "Вы не были подписаны на бота");
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
            List<UserDTO> users = userService.getAll();

            for (UserDTO user : users) {
                if (user.getIsActive().equals("YES")) {
                    sendMessage(user.getChatID(), textToSendForAll);
                }
            }
            log.info(String.format("%d sent message to all", chatId));
        } else {
            sendMessage(chatId, "Упс! Только админ может отправлять сообщения " + "\uD83D\uDE0E");
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
            List<UserDTO> users = userService.getAll();
            for (UserDTO user : users) {
                if (user.getIsActive().equals("YES")) {
                    sendDocument(user.getChatID(), fileId, caption);
                }
            }
            log.info(String.format("%d sent file to all", chatId));
        } else {
            sendMessage(chatId, Constant.ADMIN_ONLY);
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
            List<UserDTO> users = userService.getAll();
            for (UserDTO user : users) {
                if (user.getIsActive().equals("YES")) {
                    sendPhoto(user.getChatID(), photoId, caption);
                }
            }
            log.info(String.format("%d sent photo to all subscribers", chatId));
        } else {
            sendMessage(chatId, Constant.ADMIN_ONLY);
        }
    }


    /**
     * test English level knowledge method
     */

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

        String resultMessage = "Ты правильно ответил на " + testResult +
                " из " + TestEnglishLevel.correctAnswersList.size() + " вопросов!\n\n";

        if (testResult < 4) {
            resultMessage += "Нужно усерднее изучать английский язык.";
        } else if (testResult < 8) {
            resultMessage += "У тебя есть базовые знания английского языка, " +
                    "но требуется дополнительное обучение.";
        } else {
            resultMessage += "Oтлично! У тебя хорошие знания английского языка, " +
                    "но это не повод остонавливаться!";
        }

        EditMessageText message = new EditMessageText();
        message.setChatId(chatID);
        message.setMessageId(messageID);
        message.setText(resultMessage);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error in method .testEnd()" + e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Method share to admin/owner all active subscribers
     */
    private void getAllSubscribers(long chatId) {

        List<UserDTO> users = userService.getAll();
        StringBuilder subscribersList = new StringBuilder("Список подписчиков бота:\n");
        int i = 1;

        for (UserDTO user : users) {
            if (user.getIsActive().equals("YES")) {
                subscribersList
                        .append(i)
                        .append(" - ")
                        .append(user.getFirstName())
                        .append(" ")
                        .append(user.getLastName())
                        .append("\n");
                i++;
            }
        }

        if (chatId == OWNER_ID || chatId == ADMIN_ID) {
            sendMessage(chatId, subscribersList.toString().replace("null", ""));
        }
    }

    private void giveFreeLessonPromoCode(Message message) {
        Long chatId = message.getChatId();
        UserDTO user = userService.getUserById(chatId);

        if (user!=null && user.getIsActive().equals("YES")) {
            if (user.getFreeLesson() == 0) {
                String promoCode = "\nINF" + chatId.toString().substring(0, 4);
                sendMessage(chatId, "Лови промокод на один бесплатной урок со школой Infinitive:" +
                        promoCode + "\nУкажи его в чате @Infinitiveonline и выбирай удобное время для занятия!");
                user.setFreeLesson(1);
                userService.saveUser(user);
            } else {
                sendMessage(chatId, "Ты уже получал промокод на бонусный урок");
            }
        } else {
            sendMessage(chatId, "Бонусный урок доступен только подписчикам бота");
        }

    }
}