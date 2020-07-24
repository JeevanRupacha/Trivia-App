package com.example.triviaapp.Util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {
        this.sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighestScore(int score)
    {
        int currentScore = score;
        int lastScore = sharedPreferences.getInt("highestscore", 0);
        if(currentScore > lastScore)
        {
            sharedPreferences.edit().putInt("highestscore", currentScore).apply();
        }
    }

    public int getHighestScore()
    {
        return sharedPreferences.getInt("highestscore",0);
    }

    public void setState(int index)
    {
        sharedPreferences.edit().putInt("counter_index",index).apply();
    }

    public int getState()
    {
        return sharedPreferences.getInt("counter_index", 0);
    }

}
