package com.project.telegram_bot.service;

import java.util.ArrayList;
import java.util.List;

public class TestEnglishLevel {

    //Buttons for yes/no questions
    public static final String YES_BUTTON = "YES";
    public static final String NO_BUTTON = "NO";


    //Questions & answers
    public static List<String> questionsList = new ArrayList<>();
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



    //try implement test with lists
    static {
        question1.add("Вопрос 1:\nКак правильно написать глагол \"to be\" во времени " +
                "Present Simple в 3-ем лице единственного числа?");
        question1.add("am");
        question1.add("is");
        question1.add("are");
        question1.add("\uD83E\uDD14");

        question2.add("Вопрос 2:\nКакое слово нужно использовать, чтобы указать на " +
                "местоположение предмета рядом с вами?");
        question2.add("this");
        question2.add("that");
        question2.add("those");
        question2.add("\uD83E\uDD14");

        question3.add("Вопрос 3:\nКак правильно написать отрицание глагола \"to have\" " +
                "во времени Present Simple в 1-ом лице единственного числа?");
        question3.add("have not");
        question3.add("has not");
        question3.add("had not");
        question3.add("\uD83E\uDD14");

        question4.add("Вопрос 4:\nКакой модальный глагол нужно использовать, чтобы " +
                "выразить возможность?");
        question4.add("can");
        question4.add("will");
        question4.add("would");
        question4.add("\uD83E\uDD14");

        question5.add("Вопрос 5:\nКакое слово нужно использовать, чтобы указать " +
                "на множественное число существительных?");
        question5.add("-s");
        question5.add("-es");
        question5.add("-ies");
        question5.add("\uD83E\uDD14");

        question6.add("Вопрос 6:\nКакое слово нужно использовать, чтобы указать " +
                "на прошедшее время глагола \"to be\"?");
        question6.add("was");
        question6.add("were");
        question6.add("been");
        question6.add("\uD83E\uDD14");

        question7.add("Вопрос 7:\nКакое слово нужно использовать, чтобы указать на " +
                "владение чем-то?");
        question7.add("have");
        question7.add("has");
        question7.add("had");
        question7.add("\uD83E\uDD14");

        question8.add("Вопрос 8:\nКак правильно написать вопрос в Present Simple?");
        question8.add("Do you like ice cream?");
        question8.add("Are you like ice cream?");
        question8.add("Like you ice cream?");
        question8.add("\uD83E\uDD14");

        //Обязательно нужна картинка
        question9.add("Вопрос 9:\nКакое слово нужно использовать, чтобы связать два " +
                "предложения в одно?");
        question9.add("and");
        question9.add("or");
        question9.add("but");
        question9.add("\uD83E\uDD14");

        question10.add("Вопрос 10:\nКак правильно написать вопросительное слово для " +
                "уточнения предмета?");
        question10.add("What");
        question10.add("Who");
        question10.add("Where");
        question10.add("\uD83E\uDD14");


        //List of correct answers
        correctAnswersList.add("is");
        correctAnswersList.add("this");
        correctAnswersList.add("have not");
        correctAnswersList.add("can");
        correctAnswersList.add("-s");
        correctAnswersList.add("was");
        correctAnswersList.add("has");
        correctAnswersList.add("Do you like ice cream?");
        correctAnswersList.add("but");
        correctAnswersList.add("What");
    }

}
