package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaapp.Controller.AppController;
import com.example.triviaapp.Score.HighestScore;
import com.example.triviaapp.Util.Prefs;
import com.example.triviaapp.data.QuestionBank;
import com.example.triviaapp.data.QuestionListAsyncResponse;
import com.example.triviaapp.model.Question;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question_textView;
    private TextView counter_textView;
    private Button true_btn;
    private Button false_btn;
    private ImageButton pre_image_btn;
    private ImageButton next_image_btn;
    private CardView question_cardView;
    private TextView highestScore;
    private TextView currentScore;
    private int counter_index;
    private int score = 0;

    //classes object
    private Prefs prefs;
    private HighestScore hScore;


    private List<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        question_textView = (TextView) findViewById(R.id.question_textView);
        counter_textView = (TextView) findViewById(R.id.counter_question);
        pre_image_btn = (ImageButton) findViewById(R.id.prev_button);
        next_image_btn = (ImageButton) findViewById(R.id.next_button);
        true_btn = (Button) findViewById(R.id.true_button);
        false_btn = (Button) findViewById(R.id.false_button);
        highestScore = (TextView) findViewById(R.id.highest_score_textView);
        currentScore = (TextView) findViewById(R.id.current_score_textView);
        question_cardView = (CardView) findViewById(R.id.question_card);

        pre_image_btn.setOnClickListener(this);
        next_image_btn.setOnClickListener(this);
        true_btn.setOnClickListener(this);
        false_btn.setOnClickListener(this);

        //classes object initialize
        prefs = new Prefs(this);
        hScore = new HighestScore();
        counter_index = prefs.getState();


        updateScore(score,prefs.getHighestScore());


        QuestionBank questionBank = new QuestionBank(this);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgress(0);
        progressDialog.show();

        try {
            questionList = questionBank.getQuestionList(new QuestionListAsyncResponse() {
                @Override
                public void finishedProcess(List<Question> questionList) {

                    //stop Progress Dialog

                    progressDialog.cancel();

                    //setting the card question
                    question_textView.setText(questionList.get(prefs.getState()).getQuestion());

                    //setting the counter values
                    counter_textView.setText((prefs.getState()+1) +"/"+ questionList.size());

                    Log.d("main class", "finishedProcess: " + questionList.toString());
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void updateScore(int score, int hScore) {
    currentScore.setText(MessageFormat.format("Score : {0}", String.valueOf(score)));
    highestScore.setText(MessageFormat.format("Highest : {0}", String.valueOf(hScore)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.prev_button:
                if(counter_index > 0)
                {
                    enableButton(true);
                  counter_index = (counter_index - 1) % questionList.size();
                    updateQuestion(counter_index);
                }
                break;
            case R.id.next_button:
                try{
                    enableButton(true);
                     counter_index = (counter_index + 1) % questionList.size();
                    updateQuestion(counter_index);
                }catch (ArithmeticException e)
                {
                    Log.d("Exception", "onClick: "+ e);
                }
                updateQuestion(counter_index);
                break;
            case R.id.true_button:
                checkAnswer(true);
                break;
            case R.id.false_button:
                checkAnswer(false);
                break;
        }
    }

    private void checkAnswer(boolean userAnswer) {

        //disable the clickable button
        enableButton(false);

        Boolean correctAnswer = questionList.get(counter_index).getAnswerBool();
        if(correctAnswer == userAnswer)
        {
            score += 1;
            fadeAnimation();
            updateQuestion(counter_index);
        }
        else
        {
//            question_cardView.setCardBackgroundColor(getResources().getColor(R.color.blueColor));
            if(score > 0) {
                score -= 1;
            }else{
                score =0;
            }
            updateQuestion(counter_index);
            shakeAnimation();
        }
    }

    private void enableButton(Boolean value) {
        true_btn.setEnabled(value);
        false_btn.setEnabled(value);
    }

    private void fadeAnimation() {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(150);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(1);
        question_cardView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                question_cardView.setCardBackgroundColor(getResources().getColor(R.color.blueColor));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                question_cardView.setCardBackgroundColor(getResources().getColor(R.color.whiteColor));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation animate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        question_cardView.startAnimation(animate);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                question_cardView.setCardBackgroundColor(getResources().getColor(R.color.redColor));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                question_cardView.setCardBackgroundColor(getResources().getColor(R.color.whiteColor));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void updateQuestion(int counter_index) {
        hScore.setHighestScore(score);
        prefs.saveHighestScore(score);
        String question = questionList.get(counter_index).getQuestion();
        question_textView.setText(question);
        counter_textView.setText((counter_index + 1) +"/"+ questionList.size());
        updateScore(score,prefs.getHighestScore());
    }


    @Override
    protected void onPause() {
        super.onPause();

        prefs.setState(counter_index);
    }
}
