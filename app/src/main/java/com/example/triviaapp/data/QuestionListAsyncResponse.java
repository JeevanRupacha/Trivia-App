package com.example.triviaapp.data;

import com.example.triviaapp.model.Question;

import java.util.ArrayList;
import java.util.List;

public interface QuestionListAsyncResponse
{
    public void finishedProcess(List<Question> questionList);
}
