package com.example.clientlist.database;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppThread {
    public static final Object LOCK=new Object();
    private static AppThread instanceThread;
    private final Executor discIO;
    private final Executor mainIO;
    private final Executor networkIO;

    public AppThread(Executor discIO, Executor mainIO, Executor networkIO) {
        this.discIO = discIO;
        this.mainIO = mainIO;
        this.networkIO = networkIO;
    }
    private static class MainThreadHandler implements Executor
    {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute( @NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
    public static AppThread getInstance()
    {
        if(instanceThread==null)
        {
            synchronized (LOCK)
            {
                instanceThread=new AppThread(Executors.newSingleThreadExecutor(),new MainThreadHandler(),Executors.newFixedThreadPool(3));
            }
        }
        return instanceThread;
    }

    public Executor getDiscIO() {
        return discIO;
    }

    public Executor getMainIO() {
        return mainIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }
}
