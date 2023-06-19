package com.project.telegram_bot.util;

import java.util.ArrayList;
import java.util.List;

public class TestEnglishLevel {

    //Buttons for yes/no questions
    public static final String YES_BUTTON = "YES";
    public static final String NO_BUTTON = "NO";


    //Questions & answers
    public static List<String> correctAnswersList = new ArrayList<>();

    public static List<String> question1 = new ArrayList<>();
    public static List<String> question2 = new ArrayList<>();
    public static List<String> question3 = new ArrayList<>();
    public static List<String> question4 = new ArrayList<>();
    public static List<String> question5 = new ArrayList<>();
    public static List<String> question6 = new ArrayList<>();
    public static List<String> question7 = new ArrayList<>();
    public static List<String> question8 = new ArrayList<>();
    public static List<String> question9 = new ArrayList<>();
    public static List<String> question10 = new ArrayList<>();



    //questions and answers
    static {
        question1.add("Вопрос 1" +
                "\nНачни предложение:"+
                "\n____ in a jet plane?");
        question1.add("Did you ever fly");
        question1.add("Have you ever flown");
        question1.add("Have you ever flew");
        question1.add("\uD83E\uDD14");

        question2.add("Вопрос 2" +
                "\nЗакончи предложение:" +
                "\nIf you do not know the meaning of the word look ____.");
        question2.add("it up");
        question2.add("it down");
        question2.add("up it");
        question2.add("\uD83E\uDD14");

        question3.add("Вопрос 3" +
                "\nЗаполни пропуск в предложении:" +
                "\nTelevision ____ in the 20th century.");
        question3.add("invented");
        question3.add("was invented");
        question3.add("had invented");
        question3.add("\uD83E\uDD14");

        question4.add("Вопрос 4" +
                "\nЗаполни пропуск в предложении:" +
                "\nThe guide warned the tourists ____ the hotel.");
        question4.add("do not leave");
        question4.add("didn't leave");
        question4.add("not to leave");
        question4.add("\uD83E\uDD14");

        question5.add("Вопрос 5" +
                "\nЗаполни пропуск в предложении:" +
                "\nHelen told me she ____ the next morning.");
        question5.add("would call me");
        question5.add("called");
        question5.add("will call");
        question5.add("\uD83E\uDD14");

        question6.add("Вопрос 6" +
                "\nЗаполни пропуск в предложении:" +
                "\nWhen I ____ school, I will travel round the world.");
        question6.add("will leave");
        question6.add("left");
        question6.add("leave");
        question6.add("\uD83E\uDD14");

        question7.add("Вопрос 7" +
                "\nЗаполни пропуск в предложении:" +
                "\nI would study German if I ____ time.");
        question7.add("that");
        question7.add("would have");
        question7.add("had");
        question7.add("\uD83E\uDD14");

        question8.add("Вопрос 8" +
                "\nЗакончи предложение:" +
                "\nHow long is____?");
        question8.add("are your parents married");
        question8.add("have your parents been married");
        question8.add("have your parents married");
        question8.add("\uD83E\uDD14");

        question9.add("Вопрос 9" +
                "\nЗакончи предложение:" +
                "\nTom asked me Kate’s phone number ____.");
        question9.add("did I know");
        question9.add("if I knew");
        question9.add("if did I know");
        question9.add("\uD83E\uDD14");

        question10.add("Вопрос 10" +
                "\nЗакончи предложение:" +
                "\nI burnt my finger while ____.\n");
        question10.add("cooked");
        question10.add("had cooked");
        question10.add("I was cooking");
        question10.add("\uD83E\uDD14");


        //List of correct answers
        correctAnswersList.add("Have you ever flown");
        correctAnswersList.add("it up");
        correctAnswersList.add("was invented");
        correctAnswersList.add("not to leave");
        correctAnswersList.add("would call me");
        correctAnswersList.add("leave");
        correctAnswersList.add("had");
        correctAnswersList.add("have your parents been married");
        correctAnswersList.add("if I knew");
        correctAnswersList.add("I was cooking");
    }

}
