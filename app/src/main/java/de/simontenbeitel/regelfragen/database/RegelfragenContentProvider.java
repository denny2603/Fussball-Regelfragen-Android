package de.simontenbeitel.regelfragen.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Simon on 08.09.2015.
 */
public class RegelfragenContentProvider extends ContentProvider {

    public static final String AUTHORITY = "de.simontenbeitel.regelfragen.provider";

    public static final String PATH_SERVER = "server";
    public static final String PATH_QUESTION = "question";
    public static final String PATH_ANSWER = "answer";
    public static final String PATH_ANSWER_QUESTION = "answer_question";
    public static final String PATH_ANSWERPOSSIBILITIES_GAMESITUATION = "answerpossibilities_gamesituation";
    public static final String PATH_EXAM = "exam";
    public static final String PATH_QUESTION_IN_EXAM = "question_in_exam";

    private static final int SERVER = 10;
    private static final int QUESTION = 20;
    private static final int ANSWER = 30;
    private static final int ANSWER_QUESTION = 40;
    private static final int ANSWERPOSSIBILITIES_GAMESITUATION = 50;
    private static final int EXAM = 60;
    private static final int QUESTION_IN_EXAM = 70;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, PATH_SERVER, SERVER);
        sURIMatcher.addURI(AUTHORITY, PATH_QUESTION, QUESTION);
        sURIMatcher.addURI(AUTHORITY, PATH_ANSWER, ANSWER);
        sURIMatcher.addURI(AUTHORITY, PATH_ANSWER_QUESTION, ANSWER_QUESTION);
        sURIMatcher.addURI(AUTHORITY, PATH_ANSWERPOSSIBILITIES_GAMESITUATION, ANSWERPOSSIBILITIES_GAMESITUATION);
        sURIMatcher.addURI(AUTHORITY, PATH_EXAM, EXAM);
        sURIMatcher.addURI(AUTHORITY, PATH_QUESTION_IN_EXAM, QUESTION_IN_EXAM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final RegelfragenDatabase dbInstance = RegelfragenDatabase.getInstance();
        final SQLiteDatabase db = dbInstance.getReadableDatabase();
        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sURIMatcher.match(uri)) {
            case QUESTION:
                qb.setTables(RegelfragenDatabase.Tables.QUESTION);
                break;
            case SERVER:
                qb.setTables(RegelfragenDatabase.Tables.SERVER);
                break;
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Notify the context's ContentResolver
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final RegelfragenDatabase dbInstance = RegelfragenDatabase.getInstance();
        final SQLiteDatabase db = dbInstance.getWritableDatabase();

        switch (sURIMatcher.match(uri)) {
            case SERVER:
                // TODO: 28.09.2015 implement
                break;
            case QUESTION:
                long questionServerId = values.getAsLong(RegelfragenContract.Question.SERVER);
                String questionGuid = values.getAsString(RegelfragenContract.Question.GUID);
                String questionUpdateSelection = RegelfragenContract.Question.SERVER + "=? AND " + RegelfragenContract.Question.GUID + "=?";
                String[] questionUpdateSelectionArgs = new String[] {Long.toString(questionServerId), questionGuid};
                if (0 >= update(uri, values, questionUpdateSelection, questionUpdateSelectionArgs)) {
                    long row = db.insert(RegelfragenDatabase.Tables.QUESTION, null, values);
                    return RegelfragenContract.Question.URI.buildUpon().appendPath(Long.toString(row)).build();
                }
                break;
            case ANSWER:
                long answerServerId = values.getAsLong(RegelfragenContract.Answer.Columns.SERVER);
                String answerGuid = values.getAsString(RegelfragenContract.Answer.Columns.GUID);
                String answerUpdateSelection = RegelfragenContract.Answer.Columns.SERVER + "=? AND " + RegelfragenContract.Answer.Columns.GUID + "=?";
                String[] answerUpdateSelectionArgs = new String[] {Long.toString(answerServerId), answerGuid};
                if (0 >= update(uri, values, answerUpdateSelection, answerUpdateSelectionArgs)) {
                    long row = db.insert(RegelfragenDatabase.Tables.ANSWER, null, values);
                    return RegelfragenContract.Question.URI.buildUpon().appendPath(Long.toString(row)).build();
                }
                break;
            case ANSWER_QUESTION:
                String answerQuestionGuid = values.getAsString(RegelfragenContract.AnswerQuestion.Columns.GUID);
                String answerQuestion_question = values.getAsString(RegelfragenContract.AnswerQuestion.Columns.QUESTION);
                String answerQuestion_answer = values.getAsString(RegelfragenContract.AnswerQuestion.Columns.ANSWER);
                String answerQuestionUpdateSelection = RegelfragenContract.AnswerQuestion.Columns.GUID + "=?"
                        + " AND " + RegelfragenContract.AnswerQuestion.Columns.QUESTION + "=?"
                        + " AND " + RegelfragenContract.AnswerQuestion.Columns.ANSWER + "=?";
                String[] answerQuestionUpdateSelectionArgs = new String[] {answerQuestionGuid, answerQuestion_question, answerQuestion_answer};
                if (0 >= update(uri, values, answerQuestionUpdateSelection, answerQuestionUpdateSelectionArgs)) {
                    long row = db.insert(RegelfragenDatabase.Tables.ANSWER_QUESTION, null, values);
                    return RegelfragenContract.Question.URI.buildUpon().appendPath(Long.toString(row)).build();
                }
                break;
            case ANSWERPOSSIBILITIES_GAMESITUATION:
                long answerPossibilitiesGamesituationServerId = values.getAsLong(RegelfragenContract.Answer.Columns.SERVER);
                String answerPossibilitiesGamesituationGuid = values.getAsString(RegelfragenContract.AnswerPossibilitiesGamesituation.Columns.GUID);
                String answerPossibilitiesGamesituation_answer = values.getAsString(RegelfragenContract.AnswerPossibilitiesGamesituation.Columns.ANSWER);
                String answerPossibilitiesGamesituationUpdateSelection = RegelfragenContract.AnswerPossibilitiesGamesituation.Columns.SERVER + "=?"
                        + " AND " + RegelfragenContract.AnswerPossibilitiesGamesituation.Columns.GUID + "=?"
                        + " AND " + RegelfragenContract.AnswerPossibilitiesGamesituation.Columns.ANSWER + "=?";
                String[] answerPossibilitiesGamesituationUpdateSelectionArgs = new String[] {Long.toString(answerPossibilitiesGamesituationServerId), answerPossibilitiesGamesituationGuid, answerPossibilitiesGamesituation_answer};
                if (0 >= update(uri, values, answerPossibilitiesGamesituationUpdateSelection, answerPossibilitiesGamesituationUpdateSelectionArgs)) {
                    long row = db.insert(RegelfragenDatabase.Tables.ANSWERPOSSIBILITIES_GAMESITUATION, null, values);
                    return RegelfragenContract.Question.URI.buildUpon().appendPath(Long.toString(row)).build();
                }
                break;
            case EXAM:
                long examServerId = values.getAsLong(RegelfragenContract.Exam.Columns.SERVER);
                String examGuid = values.getAsString(RegelfragenContract.Exam.Columns.GUID);
                String examUpdateSelection = RegelfragenContract.Exam.Columns.SERVER + "=? AND " + RegelfragenContract.Exam.Columns.GUID + "=?";
                String[] examUpdateSelectionArgs = new String[] {Long.toString(examServerId), examGuid};
                if (0 >= update(uri, values, examUpdateSelection, examUpdateSelectionArgs)) {
                    long row = db.insert(RegelfragenDatabase.Tables.EXAM, null, values);
                    return RegelfragenContract.Question.URI.buildUpon().appendPath(Long.toString(row)).build();
                }
                break;
            case QUESTION_IN_EXAM:
                String questionInExamGuid = values.getAsString(RegelfragenContract.QuestionInExam.Columns.GUID);
                String questionInExam_question = values.getAsString(RegelfragenContract.QuestionInExam.Columns.QUESTION);
                String questionInExam_exam = values.getAsString(RegelfragenContract.QuestionInExam.Columns.EXAM);
                String questionInExamUpdateSelection = RegelfragenContract.QuestionInExam.Columns.GUID + "=?"
                        + " AND " + RegelfragenContract.QuestionInExam.Columns.QUESTION + "=?"
                        + " AND " + RegelfragenContract.QuestionInExam.Columns.EXAM + "=?";
                String[] questionInExamUpdateSelectionArgs = new String[] {questionInExamGuid, questionInExam_question, questionInExam_exam};
                if (0 >= update(uri, values, questionInExamUpdateSelection, questionInExamUpdateSelectionArgs)) {
                    long row = db.insert(RegelfragenDatabase.Tables.QUESTION_IN_EXAM, null, values);
                    return RegelfragenContract.Question.URI.buildUpon().appendPath(Long.toString(row)).build();
                }
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final RegelfragenDatabase dbInstance = RegelfragenDatabase.getInstance();
        final SQLiteDatabase db = dbInstance.getWritableDatabase();

        switch (sURIMatcher.match(uri)) {
            case SERVER:
                return db.update(RegelfragenDatabase.Tables.SERVER, values, selection, selectionArgs);
        }
        return 0;
    }

}