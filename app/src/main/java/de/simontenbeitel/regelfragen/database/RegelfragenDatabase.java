package de.simontenbeitel.regelfragen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.RegelfragenApplication;
import de.simontenbeitel.regelfragen.objects.GameSituationQuestion;

/**
 * Helper class for the database which contains the questions
 *
 * @author Simon Tenbeitel
 */
public class RegelfragenDatabase extends SQLiteOpenHelper {

    // All Static variables

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "RegelfragenDB";

    // Table names
    public interface Tables {
        String SERVER = "server";
        String QUESTION = "question";
        String ANSWER = "answer";
        String ANSWER_QUESTION = "answer_question";
        String ANSWERPOSSIBILITIES_GAMESITUATION = "apgs";
        String EXAM = "exam";
        String QUESTION_IN_EXAM = "question_in_exam";
        String DATABASE_VERSION = "dbversion";
    }

    // Table columns
    public interface ServerColumns {
        String URL = "url";
        String NAME = "name";
        String LAST_UPDATED = "last_updated";
        String DELETION_FLAG = "flag";
    }

    public interface QuestionColumns {
        String SERVER = "server";
        String TEXT = "text";
        String TYPE = "type";
    }

    public interface AnswerColumns {
        String SERVER = "server";
        String TEXT = "text";
    }

    public interface AnswerQuestionColumns {
        String SERVER = "server";
        String QUESTION = "question";
        String ANSWER = "answer";
        String POSITION = "position";
        String CORRECT = "correct";
    }

    public interface AnswerPossibilitiesGameSituationColumns {
        String SERVER = "server";
        String ANSWER = "answer";
        String POSITION = "position"; // For which position is this answer? (RestartMethod, PositionOfRestart, DisciplinarySanction)
        String ASCENDING_ORDER = "asc_order"; // Order within the spinner (the lower number is above the higher one)
    }

    public interface ExamColumns {
        String SERVER = "server";
        String NAME = "name";
        String DIFFICULTY = "difficulty";
        String TYPE = "type";
    }

    public interface QuestionInExamColumns {
        String QUESTION = "question";
        String EXAM = "exam";
        String POSITION = "position";
    }

    // values
    public interface BooleanValues {
        int TRUE = 1;
        int FALSE = 0;
    }

    public interface QuestionTypeValues {
        int GAMESITUATION = 1;
        int MULTIPLECHOICE = 2;
    }

    public interface DifficultyValues {
        int ASPIRANT = 1;
        int NORMAL = 2;
    }

    public interface ExamTypeValues {
        int NORMAL = 1;
        int SCHIEDSRICHTER_ZEITUNG = 2;
    }

    private static RegelfragenDatabase sInstance;

    private RegelfragenDatabase() {
        super(RegelfragenApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized RegelfragenDatabase getInstance() {
        if (null == sInstance)
            sInstance = new RegelfragenDatabase();
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.SERVER + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ServerColumns.URL + " TEXT UNIQUE NOT NULL, "
                        + ServerColumns.NAME + " TEXT, "
                        + ServerColumns.LAST_UPDATED + " DATETIME DEFAULT 0, "
                        + ServerColumns.DELETION_FLAG + " INTEGER DEFAULT " + BooleanValues.FALSE + ")"
        );
        db.execSQL("CREATE TABLE " + Tables.QUESTION + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + QuestionColumns.SERVER + " INTEGER NOT NULL, "
                        + QuestionColumns.TEXT + " TEXT, "
                        + QuestionColumns.TYPE + " INTEGER NOT NULL, "
                        + "FOREIGN KEY(" + QuestionColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.ANSWER + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + AnswerColumns.SERVER + " INTEGER NOT NULL, "
                        + AnswerColumns.TEXT + " TEXT NOT NULL, "
                        + "FOREIGN KEY(" + AnswerColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.ANSWER_QUESTION + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + AnswerQuestionColumns.SERVER + " INTEGER NOT NULL, FOREIGN KEY(" + BeantwortetColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "), "
                        + AnswerQuestionColumns.QUESTION + " INTEGER NOT NULL, "
                        + AnswerQuestionColumns.ANSWER + " INTEGER NOT NULL, "
                        + AnswerQuestionColumns.POSITION + " INTEGER, "
                        + AnswerQuestionColumns.CORRECT + " INTEGER,"
                        + "FOREIGN KEY(" + AnswerQuestionColumns.QUESTION + ") REFERENCES " + Tables.QUESTION + "(" + BaseColumns._ID + "), "
                        + "FOREIGN KEY(" + AnswerQuestionColumns.ANSWER + ") REFERENCES " + Tables.ANSWER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.ANSWERPOSSIBILITIES_GAMESITUATION + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + AnswerPossibilitiesGameSituationColumns.SERVER + " INTEGER NOT NULL, "
                        + AnswerPossibilitiesGameSituationColumns.ANSWER + " INTEGER NOT NULL, "
                        + AnswerPossibilitiesGameSituationColumns.POSITION + " INTEGER NOT NULL, "
                        + AnswerPossibilitiesGameSituationColumns.ASCENDING_ORDER + " INTEGER, "
                        + "FOREIGN KEY(" + AnswerPossibilitiesGameSituationColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "), "
                        + "FOREIGN KEY(" + AnswerPossibilitiesGameSituationColumns.ANSWER + ") REFERENCES " + Tables.ANSWER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.EXAM + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ExamColumns.SERVER + " INTEGER NOT NULL, "
                        + ExamColumns.NAME + " TEXT NOT NULL, "
                        + ExamColumns.DIFFICULTY + " INTEGER NOT NULL, "
                        + ExamColumns.TYPE + " INTEGER NOT NULL DEFAULT " + ExamTypeValues.NORMAL + ", "
                        + "FOREIGN KEY(" + ExamColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.QUESTION_IN_EXAM + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + QuestionInExamColumns.QUESTION + " INTEGER NOT NULL, "
                        + QuestionInExamColumns.EXAM + " INTEGER NOT NULL, "
                        + QuestionInExamColumns.POSITION + " INTEGER NOT NULL, "
                        + "FOREIGN KEY(" + QuestionInExamColumns.QUESTION + ") REFERENCES " + Tables.QUESTION + "(" + BaseColumns._ID + "), "
                        + "FOREIGN KEY(" + QuestionInExamColumns.EXAM + ") REFERENCES " + Tables.EXAM + "(" + BaseColumns._ID + "))"
        );

        // Insert default values
        ContentValues defaultServer = new ContentValues();
        defaultServer.put(ServerColumns.URL, "http://regelfragen.simon-tenbeitel.de/api");
        defaultServer.put(ServerColumns.NAME, RegelfragenApplication.getContext().getResources().getString(R.string.default_server));
        long serverID = db.insert(Tables.SERVER, null, defaultServer);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private long insertUniqueAnswer(SQLiteDatabase db, long server, String antworttext) {
        Cursor cursor = db.query(Tables.ANSWER, new String[]{BaseColumns._ID}, AnswerColumns.TEXT + "=? AND " + AnswerColumns.SERVER + "=?", new String[]{antworttext, Long.toString(server)}, null, null, null);
        if (cursor.moveToFirst()) return cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        ContentValues antwortValues = new ContentValues();
        antwortValues.put(AnswerColumns.SERVER, server);
        antwortValues.put(AnswerColumns.TEXT, antworttext);
        long rowID = db.insert(Tables.ANSWER, null, antwortValues);
        return rowID;
    }

}
