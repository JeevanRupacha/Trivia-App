package com.example.triviaapp.Controller;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application{
    private static AppController instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    public static final String TAG = AppController.class.getSimpleName();


//
//    private AppController(Context context) {
//        ctx = context;
//        requestQueue = getRequestQueue();
//    }

    public static synchronized AppController getInstance() {
//        if (instance == null) {
//            instance = new AppController(context);
//        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
    if(requestQueue != null)
        {
        requestQueue.cancelAll(tag);
        }
    }
}
