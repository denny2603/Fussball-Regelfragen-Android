package de.simontenbeitel.regelfragen.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;

import java.util.HashSet;
import java.util.Set;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.RegelfragenApplication;

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
        String ANSWERED_EXAM = "answered_exam";
        String ANSWERED_QUESTION = "answered_question";
    }
    // Table columns
    public interface ServerColumns {

        String URL = "url";
        String NAME = "name";
        String LAST_UPDATED = "last_updated";
        String ACTIVE = "active";
        String DELETION_FLAG = "flag";
    }
    public interface QuestionColumns {

        String SERVER = "server";
        String GUID = "guid";
        String TEXT = "text";
        String TYPE = "type";
    }
    public interface AnswerColumns {

        String SERVER = "server";
        String GUID = "guid";
        String TEXT = "text";
    }
    public interface AnswerQuestionColumns {

        //        String SERVER = "server";
        String GUID = "guid";
        String QUESTION = "question";
        String ANSWER = "answer";
        String POSITION = "position";
        String CORRECT = "correct";
    }
    public interface AnswerPossibilitiesGameSituationColumns {

        String SERVER = "server";
        String GUID = "guid";
        String ANSWER = "answer";
        String POSITION = "position"; // For which position is this answer? (RestartMethod, PositionOfRestart, DisciplinarySanction)
        String ASCENDING_ORDER = "asc_order"; // Order within the spinner (the lower number is above the higher one)
    }
    public interface ExamColumns {

        String SERVER = "server";
        String GUID = "guid";
        String NAME = "name";
        String DIFFICULTY = "difficulty";
        String TYPE = "type";
    }
    public interface QuestionInExamColumns {

        String QUESTION = "question";
        String GUID = "guid";
        String EXAM = "exam";
        String POSITION = "position";
    }
    public interface AnsweredExamColumns {
        String EXAM = "exam";
        String FAULTS = "faults";
        String TIMESTAMP = "answered_timestamp";
    }
    public interface AnsweredQuestionColumns {
        String QUESTION = "question";
        String POSITION = "position";
        String FAULTS = "faults";
        String ANSWERED_EXAM = "answered_exam";
        String TIMESTAMP = "answered_timestamp";
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
                        + ServerColumns.ACTIVE + " INTEGER DEFAULT " + BooleanValues.TRUE + ","
                        + ServerColumns.DELETION_FLAG + " INTEGER DEFAULT " + BooleanValues.FALSE + ")"
        );
        db.execSQL("CREATE TABLE " + Tables.QUESTION + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + QuestionColumns.GUID + " TEXT UNIQUE NOT NULL, "
                        + QuestionColumns.SERVER + " INTEGER NOT NULL, "
                        + QuestionColumns.TEXT + " TEXT, "
                        + QuestionColumns.TYPE + " INTEGER NOT NULL, "
                        + "FOREIGN KEY(" + QuestionColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.ANSWER + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + AnswerColumns.GUID + " TEXT UNIQUE NOT NULL, "
                        + AnswerColumns.SERVER + " INTEGER NOT NULL, "
                        + AnswerColumns.TEXT + " TEXT NOT NULL, "
                        + "FOREIGN KEY(" + AnswerColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.ANSWER_QUESTION + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + AnswerQuestionColumns.SERVER + " INTEGER NOT NULL, FOREIGN KEY(" + BeantwortetColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "), "
                        + AnswerQuestionColumns.GUID + " TEXT UNIQUE NOT NULL, "
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
                        + AnswerPossibilitiesGameSituationColumns.GUID + " TEXT UNIQUE NOT NULL, "
                        + AnswerPossibilitiesGameSituationColumns.ANSWER + " INTEGER NOT NULL, "
                        + AnswerPossibilitiesGameSituationColumns.POSITION + " INTEGER NOT NULL, "
                        + AnswerPossibilitiesGameSituationColumns.ASCENDING_ORDER + " INTEGER, "
                        + "FOREIGN KEY(" + AnswerPossibilitiesGameSituationColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "), "
                        + "FOREIGN KEY(" + AnswerPossibilitiesGameSituationColumns.ANSWER + ") REFERENCES " + Tables.ANSWER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.EXAM + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ExamColumns.SERVER + " INTEGER NOT NULL, "
                        + ExamColumns.GUID + " TEXT UNIQUE NOT NULL, "
                        + ExamColumns.NAME + " TEXT NOT NULL, "
                        + ExamColumns.DIFFICULTY + " INTEGER NOT NULL, "
                        + ExamColumns.TYPE + " INTEGER NOT NULL DEFAULT " + ExamTypeValues.NORMAL + ", "
                        + "FOREIGN KEY(" + ExamColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.QUESTION_IN_EXAM + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + QuestionInExamColumns.GUID + " TEXT UNIQUE NOT NULL, "
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

    /**
     * Returns the BaseColumns._ID for the given server URL in the local db
     *
     * @param url The server url
     * @return _id value, or -1L if no entry exists for given url
     */
    public static long getServerId(String url) {
        final RegelfragenDatabase dbInstance = getInstance();
        final SQLiteDatabase db = dbInstance.getWritableDatabase();
        Cursor serverCursor = db.query(Tables.SERVER,
                new String[]{BaseColumns._ID},
                RegelfragenContract.Server.URL + "=?",
                new String[]{url},
                null, null, null);
        if (serverCursor.moveToFirst()) {
            long serverId = serverCursor.getLong(serverCursor.getColumnIndex(BaseColumns._ID));
            serverCursor.close();
            return serverId;
        }
        return -1;
    }

    /**
     *
     * @param projection desired columns
     * @return
     */
    public static Cursor getPossibleExams(String[] projection) {
        final RegelfragenDatabase dbInstance = getInstance();
        final SQLiteDatabase db = dbInstance.getReadableDatabase();
        Context context = RegelfragenApplication.getContext();

        String selection = null;
        String[] selectionArgs = null;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean limitDifficulties = sharedPref.getBoolean(context.getString(R.string.pref_key_limit_difficulties), false);
        if (limitDifficulties) {
            Set<String> activeDifficulties = new HashSet<>();

            boolean normalDifficultyActive = sharedPref.getBoolean(context.getString(R.string.pref_key_normal_active), true);
            if (normalDifficultyActive) {
                activeDifficulties.add(Long.toString(DifficultyValues.NORMAL));
            }

            boolean aspirantDifficultyActive = sharedPref.getBoolean(context.getString(R.string.pref_key_aspirant_active), true);
            if (aspirantDifficultyActive) {
                activeDifficulties.add(Long.toString(DifficultyValues.ASPIRANT));
            }

            selectionArgs = activeDifficulties.toArray(new String[activeDifficulties.size()]);
            selection = ExamColumns.DIFFICULTY + " IN (" + makePlaceholders(selectionArgs.length) + ")";
        }

        return db.query(Tables.EXAM, projection, selection, selectionArgs, null, null, null);
    }

    public static String makePlaceholders(int length) {
        if(length <= 0) return "";
        StringBuilder sb = new StringBuilder(length * 2 - 1);
        sb.append("?");
        for (int i = 1; i < length; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }

}
