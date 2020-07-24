package com.example.triviaapp.data;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.LongDef;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaapp.Controller.AppController;
import com.example.triviaapp.MainActivity;
import com.example.triviaapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    private ArrayList<Question> questionList;
    private Context ctx;

    public QuestionBank(Context ctx)
    {
        questionList = new ArrayList<>();
        this.ctx = ctx;
    }

    public List<Question> getQuestionList(final QuestionListAsyncResponse callback) throws InterruptedException {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        for (int i=0 ; i<response.length(); i++)
                        {
                            try {
                                Question question = new Question();
                                question.setQuestion(response.getJSONArray(i).get(0).toString());
                                question.setAnswerBool(response.getJSONArray(i).getBoolean(1));
                                questionList.add(question);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(callback != null) callback.finishedProcess(questionList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(AppController.TAG, "onErrorResponse: "+ error);
                    }
                });

            AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionList;
    }

}
