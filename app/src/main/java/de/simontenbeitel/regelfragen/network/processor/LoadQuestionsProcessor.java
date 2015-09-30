package de.simontenbeitel.regelfragen.network.processor;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.simontenbeitel.regelfragen.database.RegelfragenContentProvider;
import de.simontenbeitel.regelfragen.database.RegelfragenContract;
import de.simontenbeitel.regelfragen.database.RegelfragenDatabase;
import de.simontenbeitel.regelfragen.network.RegelfragenApi;
import de.simontenbeitel.regelfragen.network.RegelfragenApiJsonObjects;
import de.simontenbeitel.regelfragen.network.RegelfragenRestAdapter;

/**
 * @author Simon Tenbeitel
 */
public class LoadQuestionsProcessor extends Processor {

    private final ContentResolver mContentResolver;
    private final ArrayList<ContentProviderOperation> mContentProviderOperations = new ArrayList<>();
    private long mServerId;

    public LoadQuestionsProcessor(Context context) {
        super(context);
        mContentResolver = mContext.getContentResolver();
    }

    @Override
    public Bundle execute(Bundle extras) {
        Bundle result = new Bundle();

        // extract extras from Bundle
        String url = extras.getString(RegelfragenContract.Server.URL);
        Log.d(LoadQuestionsProcessor.class.getName(), "Started with URL: " + url);

        // check for server id
        mServerId = RegelfragenDatabase.getServerId(url);
        if (0 > mServerId) {
            result.putBoolean(Extras.RESULT_SUCCESSFUL, false);
            return result;
        }

        RegelfragenApi mApi = RegelfragenRestAdapter.getApi(url);
        RegelfragenApiJsonObjects.QuestionResponse response = mApi.getQuestions();

        insertLastUpdatedTimestamp(response.timestamp);
        insertQuestions(response.questions);
        insertAnswers(response.answers);
        insertAnswerQuestions(response.answer_question);
        insertAnswerPossibilitiesGamesituation(response.answerpossibilities_gamesituation);
        insertExams(response.exams);
        insertQuestionInExam(response.question_in_exam);

        try {
            mContentResolver.applyBatch(RegelfragenContentProvider.AUTHORITY, mContentProviderOperations);
            result.putBoolean(Extras.RESULT_SUCCESSFUL, true);
        } catch (Exception e) {
            e.printStackTrace();
            result.putBoolean(Extras.RESULT_SUCCESSFUL, false);
        }
        return result;
    }

    private void insertLastUpdatedTimestamp(Timestamp timestamp) {
        ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(RegelfragenContract.Server.URI);

        operationBuilder.withSelection(BaseColumns._ID + "=?", new String[]{Long.toString(mServerId)});

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String lastUpdated = dateFormat.format(timestamp);
        operationBuilder.withValue(RegelfragenContract.Server.LAST_UPDATED, lastUpdated);

        ContentProviderOperation operation = operationBuilder.build();
        mContentProviderOperations.add(operation);
    }

    private void insertQuestions(List<RegelfragenApiJsonObjects.Question> questions) {
        ContentValues questionValues = new ContentValues();
        questionValues.put(RegelfragenDatabase.QuestionColumns.SERVER, mServerId);
        for (RegelfragenApiJsonObjects.Question question : questions) {
            questionValues.put(RegelfragenDatabase.QuestionColumns.GUID, question.guid);
            questionValues.put(RegelfragenDatabase.QuestionColumns.TEXT, question.text);
            questionValues.put(RegelfragenDatabase.QuestionColumns.TYPE, question.type);

            ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(RegelfragenContract.Question.URI);
            operationBuilder.withValues(questionValues);
            ContentProviderOperation operation = operationBuilder.build();
            mContentProviderOperations.add(operation);
        }
    }

    private void insertAnswers(List<RegelfragenApiJsonObjects.Answer> answers) {
        ContentValues answerValues = new ContentValues();
        answerValues.put(RegelfragenDatabase.AnswerColumns.SERVER, mServerId);
        for (RegelfragenApiJsonObjects.Answer answer : answers){
            answerValues.put(RegelfragenDatabase.AnswerColumns.GUID, answer.guid);
            answerValues.put(RegelfragenDatabase.AnswerColumns.TEXT, answer.text);

            ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(RegelfragenContract.Answer.URI);
            operationBuilder.withValues(answerValues);
            ContentProviderOperation operation = operationBuilder.build();
            mContentProviderOperations.add(operation);
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

            ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(RegelfragenContract.AnswerQuestion.URI);
            operationBuilder.withValues(answerQuestionValues);
            ContentProviderOperation operation = operationBuilder.build();
            mContentProviderOperations.add(operation);
        }
    }

    private void insertAnswerPossibilitiesGamesituation(List<RegelfragenApiJsonObjects.AnswerpossibilitiesGamesituation> answerpossibilities_gamesituation) {
        ContentValues answerpossibilityValues = new ContentValues();
        answerpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.SERVER, mServerId);
        for (RegelfragenApiJsonObjects.AnswerpossibilitiesGamesituation answerpossibility : answerpossibilities_gamesituation) {
            answerpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.GUID, answerpossibility.guid);
            answerpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.ANSWER, answerpossibility.answer);
            answerpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.POSITION, answerpossibility.position);
            answerpossibilityValues.put(RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns.ASCENDING_ORDER, answerpossibility.ascending_order);

            ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(RegelfragenContract.AnswerPossibilitiesGamesituation.URI);
            operationBuilder.withValues(answerpossibilityValues);
            ContentProviderOperation operation = operationBuilder.build();
            mContentProviderOperations.add(operation);
        }
    }

    private void insertExams(List<RegelfragenApiJsonObjects.Exam> exams) {
        ContentValues examValues = new ContentValues();
        examValues.put(RegelfragenDatabase.ExamColumns.SERVER, mServerId);
        for (RegelfragenApiJsonObjects.Exam exam : exams) {
            examValues.put(RegelfragenDatabase.ExamColumns.GUID, exam.guid);
            examValues.put(RegelfragenDatabase.ExamColumns.NAME, exam.name);
            examValues.put(RegelfragenDatabase.ExamColumns.DIFFICULTY, exam.difficulty);
            examValues.put(RegelfragenDatabase.ExamColumns.TYPE, exam.type);

            ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(RegelfragenContract.Exam.URI);
            operationBuilder.withValues(examValues);
            ContentProviderOperation operation = operationBuilder.build();
            mContentProviderOperations.add(operation);
        }
    }

    private void insertQuestionInExam(List<RegelfragenApiJsonObjects.QuestionInExam> question_in_exam) {
        ContentValues questionInExamValues = new ContentValues();
        for (RegelfragenApiJsonObjects.QuestionInExam qie : question_in_exam) {
            questionInExamValues.put(RegelfragenDatabase.QuestionInExamColumns.GUID, qie.guid);
            questionInExamValues.put(RegelfragenDatabase.QuestionInExamColumns.QUESTION, qie.question);
            questionInExamValues.put(RegelfragenDatabase.QuestionInExamColumns.EXAM, qie.exam);
            questionInExamValues.put(RegelfragenDatabase.QuestionInExamColumns.POSITION, qie.position);

            ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(RegelfragenContract.QuestionInExam.URI);
            operationBuilder.withValues(questionInExamValues);
            ContentProviderOperation operation = operationBuilder.build();
            mContentProviderOperations.add(operation);
        }
    }

}