package de.simontenbeitel.regelfragen.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

import de.simontenbeitel.regelfragen.injection.ApplicationContext;

public class OpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "regelfragen.db";
    public static final int DATABASE_VERSION = 1;

    @Inject
    public OpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Database.Question.CREATE);
        db.execSQL(Database.MultipleChoiceAnswer.CREATE);
        db.execSQL(Database.MatchSituationAnswerPossibility.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
