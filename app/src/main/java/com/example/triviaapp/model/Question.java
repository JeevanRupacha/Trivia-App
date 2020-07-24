package com.example.triviaapp.model;

import androidx.annotation.NonNull;

public class Question {
    private String question ;
    private boolean answerBool;

    public Question()
    {

    }

    public Question(String question, boolean answerBool)
    {
        this.question = question;
        this.answerBool = answerBool;
    }

    public String getQuestion() {
        return question;
    }

    public boolean getAnswerBool() {
        return answerBool;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswerBool(boolean answerBool) {
        this.answerBool = answerBool;
    }


    @NonNull
    @Override
    public String toString() {
       return question + " " + answerBool;
    }
}
