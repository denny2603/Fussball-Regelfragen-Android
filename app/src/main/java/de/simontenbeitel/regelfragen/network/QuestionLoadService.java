package de.simontenbeitel.regelfragen.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.simontenbeitel.regelfragen.database.RegelfragenDatabase;

/**
 * @author Simon Tenbeitel
 */
public class QuestionLoadService extends IntentService {

    public static final String ACTION = "de.simontenbeitel.regelfragen.questionload.completed";
    public static final String KEY_SUCCESSFUL = "successful";

    private SQLiteDatabase db;
    private long serverId;

    public QuestionLoadService() {
        super(QuestionLoadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getDataString();
        RegelfragenApi api = RegelfragenRestAdapter.getApi(url);
        long start = System.currentTimeMillis();
        Log.d(QuestionLoadService.class.getName(), "Start downloading questions");
        RegelfragenApiJsonObjects.QuestionResponse response = api.getQuestions();
        Log.d(QuestionLoadService.class.getName(), "Downloaded questions in " + (System.currentTimeMillis() - start) + " millis");

        db = RegelfragenDatabase.getInstance().getWritableDatabase();
        serverId = getServerId(url);
        if (0 > serverId) {
            Log.e(QuestionLoadService.class.getName(), "Server URL is not in local db: " + url);
            return;
        }
        start = System.currentTimeMillis();
        Log.d(QuestionLoadService.class.getName(), "Start db transaction");
        boolean successful = false;
        db.beginTransaction();
        try {
            insertLastUpdatedTimeStamp(response.timestamp);
            insertQuestions(response.questions);
            insertAnswers(response.answers);
            insertAnswerQuestions(response.answer_question);
            insertAnswerPossibilitiesGamesituation(response.answerpossibilities_gamesituation);
            insertExams(response.exams);
            insertQuestionInExam(response.question_in_exam);
            db.setTransactionSuccessful();
            successful = true;
            Log.d(QuestionLoadService.class.getName(), "Transaction successful");
        } finally {
            db.endTransaction();
            Log.d(QuestionLoadService.class.getName(), "End db transaction");
            Log.d(QuestionLoadService.class.getName(), "Completed db transaction in " + (System.currentTimeMillis() - start) + " millis");

            // Prepare intent to be sent via broadcast manager
            Intent sendCompletedIntent = new Intent();
            sendCompletedIntent.setAction(ACTION);
            sendCompletedIntent.putExtra(KEY_SUCCESSFUL, successful);

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            broadcastManager.sendBroadcast(sendCompletedIntent);
        }
    }

    private long getServerId(String url) {
        Cursor cursor = db.query(RegelfragenDatabase.Tables.SERVER, new String[]{BaseColumns._ID}, RegelfragenDatabase.ServerColumns.URL + "=?", new String[]{url}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        }
        return -1;
    }

    private void insertLastUpdatedTimeStamp(Timestamp timestamp) {
        ContentValues serverValues = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        serverValues.put(RegelfragenDatabase.ServerColumns.LAST_UPDATED, dateFormat.format(timestamp));
        db.update(RegelfragenDatabase.Tables.SERVER,
                serverValues,
                BaseColumns._ID + "=?",
                new String[]{Long.toString(serverId)});
    }


    private void insertQuestions(List<RegelfragenApiJsonObjects.Question> questions) {
        ContentValues questionValues = new ContentValues();
        questionValues.put(RegelfragenDatabase.QuestionColumns.SERVER, serverId);
        for (RegelfragenApiJsonObjects.Question question : questions) {
            questionValues.put(RegelfragenDatabase.QuestionColumns.GUID, question.guid);
            questionValues.put(RegelfragenDatabase.QuestionColumns.TEXT, question.text);
            questionValues.put(RegelfragenDatabase.QuestionColumns.TYPE, question.type);
            db.insert(RegelfragenDatabase.Tables.QUESTION, null, questionValues);
        }
    }


    private void insertAnswers(List<RegelfragenApiJsonObjects.Answer> answers) {
        ContentValues answerValues = new ContentValues();
        answerValues.put(RegelfragenDatabase.AnswerColumns.SERVER, serverId);
        for (RegelfragenApiJsonObjects.Answer answer : answers){
            answerValues.put(RegelfragenDatabase.AnswerColumns.GUID, answer.guid);
            answerValues.put(RegelfragenDatabase.AnswerColumns.TEXT, answer.text);
            db.insert(RegelfragenDatabase.Tables.ANSWER, null, answerValues);
        }
    }

    private void insertAnswerQuestions(List<RegelfragenApiJsonObjects.AnswerQuestion> answer_questions) {
        ContentValues answerQuestionValues = new ContentValues();
//        answerQuestionValues.put(RegelfragenDatabase.AnswerQuestionColumns.SERVER, serverId);
        for (RegelfragenApiJsonObjects.AnswerQuestion answer_question : answer_questions) {
            answerQuestionValues.put(RegelfragenDatabase.AnswerQuestionColumns.GUID, answer_question.guid);
            answerQuestionValues.put(RegelfragenDatabase.AnswerQuestionColumns.QUESTION, answer_question.question);
            answerQuestionValues.put(RegelfragenDatabase.AnswerQuestionColumns.ANSWER, answer_question.answer);
            answerQuestionValues.put(RegelfragenDatabase.AnswerQuestionColumns.POSITION, answer_question.position);
            answerQuestionValues.put(RegelfragenDatabase.AnswerQuestionColumns.CORRECT, answer_question.correct);
            db.insert(RegelfragenDatabase.Tables.ANSWER_QUESTION, null, answerQuestionValues);
        }
    }

    private void insertAnswerPossibilitiesGamesituation(List<RegelfragenApiJsonObjects.AnswerpossibilitiesGamesituation> answerpossibilities_gamesituation) {
        ContentValues anserpossibilityValues = new ContentValues();
        anserpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.SERVER, serverId);
        for (RegelfragenApiJsonObjects.AnswerpossibilitiesGamesituation answerpossibility : answerpossibilities_gamesituation) {
            anserpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.GUID, answerpossibility.guid);
            anserpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.ANSWER, answerpossibility.answer);
            anserpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.POSITION, answerpossibility.position);
            anserpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.ASCENDING_ORDER, answerpossibility.ascending_order);
            db.insert(RegelfragenDatabase.Tables.ANSWERPOSSIBILITIES_GAMESITUATION, null, anserpossibilityValues);
        }
    }

    private void insertExams(List<RegelfragenApiJsonObjects.Exam> exams) {
        ContentValues examValues = new ContentValues();
        examValues.put(RegelfragenDatabase.ExamColumns.SERVER, serverId);
        for (RegelfragenApiJsonObjects.Exam exam : exams) {
            examValues.put(RegelfragenDatabase.ExamColumns.GUID, exam.guid);
            examValues.put(RegelfragenDatabase.ExamColumns.NAME, exam.name);
            examValues.put(RegelfragenDatabase.ExamColumns.DIFFICULTY, exam.difficulty);
            examValues.put(RegelfragenDatabase.ExamColumns.TYPE, exam.type);
            db.insert(RegelfragenDatabase.Tables.EXAM, null, examValues);
        }
    }

    private void insertQuestionInExam(List<RegelfragenApiJsonObjects.QuestionInExam> question_in_exam) {
        ContentValues questionInExamValues = new ContentValues();
        for (RegelfragenApiJsonObjects.QuestionInExam qie : question_in_exam) {
            questionInExamValues.put(RegelfragenDatabase.QuestionInExamColumns.GUID, qie.guid);
            questionInExamValues.put(RegelfragenDatabase.QuestionInExamColumns.QUESTION, qie.question);
            questionInExamValues.put(RegelfragenDatabase.QuestionInExamColumns.EXAM, qie.exam);
            questionInExamValues.put(RegelfragenDatabase.QuestionInExamColumns.POSITION, qie.position);
            db.insert(RegelfragenDatabase.Tables.QUESTION_IN_EXAM, null, questionInExamValues);
        }
    }

}
