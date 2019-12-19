package com.example.appproyecto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BugsDB extends SQLiteOpenHelper {

    public static int DATABASE_VERSION = 2;
    public BugsDB(@Nullable Context context,
                  @Nullable String name,
                  @Nullable SQLiteDatabase.CursorFactory factory,
                  int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table bugs(code INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title text NOT NULL, fecha text NOT NULL, descripcion text NOT NULL, adjunto text, estado INTEGER NOT NULL DEFAULT 0)");
        db.execSQL("create table fixes(code INTEGER PRIMARY KEY AUTOINCREMENT, title text NOT NULL, descripcion text NOT NULL)");
        db.execSQL("create table bugfixes(bugcode INTEGER NOT NULL, fixcode INTEGER NOT NULL," +
                " FOREIGN KEY(bugcode) REFERENCES bugs(code)," +
                " FOREIGN KEY(fixcode) REFERENCES fixes(code))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int
            oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS bugs");
        db.execSQL("DROP TABLE IF EXISTS fixes");
        db.execSQL("DROP TABLE IF EXISTS bugfixes");
        onCreate(db);
    }
}
