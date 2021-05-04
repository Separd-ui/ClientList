 package com.example.clientlist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Client.class},version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public static final Object LOCK= new Object();
    public static final String DATABASE_NAME="client_list_db";
    private static AppDataBase instanceDB;

    public static AppDataBase getInstance(Context context)
    {
        if(instanceDB==null)
        {
            synchronized (LOCK)
            {
                instanceDB= Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,AppDataBase.DATABASE_NAME).build();
            }
        }
        return instanceDB;
    }
    public abstract ClientDAO clientDAO();
}
