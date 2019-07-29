package br.com.ronanlima.twolevelmenu.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import br.com.ronanlima.twolevelmenu.database.converters.DateConverter;
import br.com.ronanlima.twolevelmenu.model.Menu;

@Database(version = 1, entities = {Menu.class}, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final String TAG = AppDatabase.class.getCanonicalName().toUpperCase();
    public static AppDatabase sInstance;
    public static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "TwoLevelMenu";

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MoreUsedMenuDAO menuDAO();
}
