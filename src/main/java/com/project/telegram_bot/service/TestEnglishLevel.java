package com.project.telegram_bot.service;

import java.util.ArrayList;
import java.util.List;

public class TestEnglishLevel {

    //Buttons for yes/no questions
    public static final String YES_BUTTON = "YES";
    public static final String NO_BUTTON = "NO";


    //Questions & answers
    public static final String QUESTION_ONE = "Question 1:\nHow many letters in the english alphabet?";
    public static final String ANSWER_ONE_1 = "10";
    public static final String ANSWER_ONE_2 = "20";
    public static final String ANSWER_ONE_3 = "26"; //correct answer
    public static final String ANSWER_ONE_4 = "30";

    public static final String QUESTION_TWO = "Question 2:\nThird form of verb 'be'";
    public static final String ANSWER_TWO_1 = "be";
    public static final String ANSWER_TWO_2 = "was";
    public static final String ANSWER_TWO_3 = "were";
    public static final String ANSWER_TWO_4 = "been"; //correct answer

    public static final String QUESTION_THREE = "Question 3:\nHow many colors are in the rainbow?";
    public static final String ANSWER_THREE_1 = "4";
    public static final String ANSWER_THREE_2 = "5";
    public static final String ANSWER_THREE_3 = "6";
    public static final String ANSWER_THREE_4 = "7"; //correct answer

    public static List<String> questionsList = new ArrayList<>();
    public static List<String> correctAnswersList = new ArrayList<>();

    public static List<String> questionOne = new ArrayList<>();
    public static List<String> questionTwo = new ArrayList<>();
    public static List<String> questionThree = new ArrayList<>();

    //try implement test with lists
    static {
        questionOne.add(QUESTION_ONE);
        questionOne.add(ANSWER_ONE_1);
        questionOne.add(ANSWER_ONE_2);
        questionOne.add(ANSWER_ONE_3);
        questionOne.add(ANSWER_ONE_4);

        questionTwo.add(QUESTION_TWO);
        questionTwo.add(ANSWER_TWO_1);
        questionTwo.add(ANSWER_TWO_2);
        questionTwo.add(ANSWER_TWO_3);
        questionTwo.add(ANSWER_TWO_4);

        questionThree.add(QUESTION_THREE);
        questionThree.add(ANSWER_THREE_1);
        questionThree.add(ANSWER_THREE_2);
        questionThree.add(ANSWER_THREE_3);
        questionThree.add(ANSWER_THREE_4);

        questionsList.add(QUESTION_ONE);
        questionsList.add(QUESTION_TWO);
        questionsList.add(QUESTION_THREE);

        correctAnswersList.add(ANSWER_ONE_3);
        correctAnswersList.add(ANSWER_TWO_4);
        correctAnswersList.add(ANSWER_THREE_4);
    }

}
