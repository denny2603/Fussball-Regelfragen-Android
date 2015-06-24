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
public class QuestionDatabase extends SQLiteOpenHelper {

    // All Static variables

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "QuestionsDB";

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
        String ORDER = "order"; // Order within the spinner (the lower number is above the higher one)
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

    public interface DatenbaseVersionColumns {
        String SERVER = "server";
        String TIMESTAMP = "timestamp";
    }

    // values
    public interface BooleanValues {
        int TRUE = 1;
        int FALSE = 0;
    }

    public interface QuestionTypeValues {
        int GAMESITUATION = 1;
    }

    public interface DifficultyValues {
        int ASPIRANT = 1;
        int NORMAL = 2;
    }

    public interface ExamTypeValues {
        int NORMAL = 1;
        int SCHIEDSRICHTER_ZEITUNG = 2;
    }

    public QuestionDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.SERVER + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ServerColumns.URL + " TEXT UNIQUE NOT NULL, "
                        + ServerColumns.NAME + " TEXT, "
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
                        + AnswerQuestionColumns.CORRECT + " INTEGER DEFAULT " + BooleanValues.TRUE + ", "
                        + "FOREIGN KEY(" + AnswerQuestionColumns.QUESTION + ") REFERENCES " + Tables.QUESTION + "(" + BaseColumns._ID + "), "
                        + "FOREIGN KEY(" + AnswerQuestionColumns.ANSWER + ") REFERENCES " + Tables.ANSWER + "(" + BaseColumns._ID + "))"
        );
        db.execSQL("CREATE TABLE " + Tables.ANSWERPOSSIBILITIES_GAMESITUATION + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + AnswerPossibilitiesGameSituationColumns.SERVER + " INTEGER NOT NULL, "
                        + AnswerPossibilitiesGameSituationColumns.ANSWER + " INTEGER NOT NULL, "
                        + AnswerPossibilitiesGameSituationColumns.POSITION + " INTEGER NOT NULL, "
                        + AnswerPossibilitiesGameSituationColumns.ORDER + " INTEGER, "
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
        db.execSQL("CREATE TABLE " + Tables.DATABASE_VERSION + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + DatenbaseVersionColumns.SERVER + " INTEGER NOT NULL, "
                        + DatenbaseVersionColumns.TIMESTAMP + " INTEGER, "
                        + "FOREIGN KEY(" + DatenbaseVersionColumns.SERVER + ") REFERENCES " + Tables.SERVER + "(" + BaseColumns._ID + "))"
        );

        // Insert default values
        ContentValues defaultServer = new ContentValues();
        defaultServer.put(ServerColumns.URL, "http://www.simon-tenbeitel.de/regelfragenAPI");
        defaultServer.put(ServerColumns.NAME, RegelfragenApplication.getContext().getResources().getString(R.string.default_server));
        long serverID = db.insert(Tables.SERVER, null, defaultServer);

        // default answers (German)
        final String[] answersRestartMethod = new String[]{"weiterspielen", "Abstoß", "Anstoß", "Eckstoß", "Einwurf", "Freistoß (direkt)", "Freistoß (indirekt)", "Schiedsrichterball", "Strafstoß", "Wiederholung", "Halbzeit", "Spielende"};
        final String[] answersPositionOfRestart = new String[]{"weiterspielen", "Mittelpunkt", "Torraum", "Torraumlinie", "Strafraumlinie", "Eckstoßteilkreis", "Seitenlinie", "11m Punkt", "Höhe 11m Punkt", "Gleicher Ort", "(Tat-)Ort des Vergehens", "wo getroffen/sollte", "wo Ball bei Pfiff", "Spielende"};
        final String[] answersDisciplinarySanction = new String[]{"keine persönliche Strafe", "Gelb", "Gelb/Rot", "Gelb und Gelb/Rot", "Rot", "Nur Meldung möglich", "1x Gelb / 1x Rot", "2x Gelb", "2x Rot", "Spielabbruch"};
        ContentValues answerPossibilitiesGameSituaionQuestion = new ContentValues();
        answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.SERVER, serverID);
        answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.POSITION, GameSituationQuestion.SpinnerPositions.RESTART_METHOD);
        for (int indexRestartMethod = 0; indexRestartMethod < answersRestartMethod.length; indexRestartMethod++) {
            long antwortID = insertUniqueAnswer(db, serverID, answersRestartMethod[indexRestartMethod]);
            answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.ANSWER, antwortID);
            answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.ORDER, indexRestartMethod);
            db.insert(Tables.ANSWERPOSSIBILITIES_GAMESITUATION, null, answerPossibilitiesGameSituaionQuestion);
        }
        answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.POSITION, GameSituationQuestion.SpinnerPositions.POSITION_OF_RESTART);
        for (int indexOrtDerFortsetzung = 0; indexOrtDerFortsetzung < answersPositionOfRestart.length; indexOrtDerFortsetzung++) {
            long antwortID = insertUniqueAnswer(db, serverID, answersPositionOfRestart[indexOrtDerFortsetzung]);
            answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.ANSWER, antwortID);
            answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.ORDER, indexOrtDerFortsetzung);
            db.insert(Tables.ANSWERPOSSIBILITIES_GAMESITUATION, null, answerPossibilitiesGameSituaionQuestion);
        }
        answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.POSITION, GameSituationQuestion.SpinnerPositions.DISCIPLINARY_SANCTION);
        for (int indexPersoenlicheStrafe = 0; indexPersoenlicheStrafe < answersDisciplinarySanction.length; indexPersoenlicheStrafe++) {
            long antwortID = insertUniqueAnswer(db, serverID, answersDisciplinarySanction[indexPersoenlicheStrafe]);
            answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.ANSWER, antwortID);
            answerPossibilitiesGameSituaionQuestion.put(AnswerPossibilitiesGameSituationColumns.ORDER, indexPersoenlicheStrafe);
            db.insert(Tables.ANSWERPOSSIBILITIES_GAMESITUATION, null, answerPossibilitiesGameSituaionQuestion);
        }
        // Test values
        ContentValues frage1 = new ContentValues();
        frage1.put(QuestionColumns.SERVER, serverID);
        frage1.put(QuestionColumns.TEXT, "Ein Spieler befindet sich wegen einer Verletzung außerhalb des Spielfelds und beleidigt einen Gegner, der ihn kurz zuvor gefoult hatte. Deshalb verlässt dieser das Feld und stößt den verletzten Spieler heftig zu Boden. Wie ist zu entscheiden, wenn der in unmittelbarer Nähe befindliche SR beide Vorgänge wahrgenommen hat und deshalb das Spiel unterbricht?");
        frage1.put(QuestionColumns.TYPE, QuestionTypeValues.GAMESITUATION);
        db.insert(Tables.QUESTION, null, frage1);
        ContentValues antwort1 = new ContentValues();
        antwort1.put(AnswerQuestionColumns.QUESTION, 1);
        antwort1.put(AnswerQuestionColumns.ANSWER, 8);
        antwort1.put(AnswerQuestionColumns.POSITION, GameSituationQuestion.SpinnerPositions.RESTART_METHOD);
        db.insert(Tables.ANSWER_QUESTION, null, antwort1);
        antwort1.put(AnswerQuestionColumns.ANSWER, 13);
        antwort1.put(AnswerQuestionColumns.POSITION, GameSituationQuestion.SpinnerPositions.POSITION_OF_RESTART);
        db.insert(Tables.ANSWER_QUESTION, null, antwort1);
        antwort1.put(AnswerQuestionColumns.ANSWER, 9);
        antwort1.put(AnswerQuestionColumns.POSITION, GameSituationQuestion.SpinnerPositions.DISCIPLINARY_SANCTION);
        db.insert(Tables.ANSWER_QUESTION, null, antwort1);
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
