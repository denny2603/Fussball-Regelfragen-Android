package de.simontenbeitel.regelfragen.data.local;

import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.simontenbeitel.regelfragen.domain.model.question.MatchSituationQuestion;
import de.simontenbeitel.regelfragen.domain.model.question.MultipleChoiceQuestion;
import de.simontenbeitel.regelfragen.domain.model.question.Question;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(OpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    /**
     * Select a random question from local db for use as single question.
     *
     * @param exclude ids to exclude. Use it to avoid duplicate questions being shown during the session.
     * @return {@link Observable} for a single question or null if exclude parameter contains all ids in local db.
     */
    public Observable<Question> getRandomQuestion(long... exclude) {
        String questionProjection = Database.Question.Columns._ID + ", " + Database.Question.Columns.TEXT + ", " + Database.Question.Columns.TYPE;
        String whereCondition = "";
        String[] args = new String[0];
        if (null != exclude && 0 < exclude.length) {
            whereCondition = "NOT IN (" + makePlaceholders(exclude.length) + ")";
            args = new String[exclude.length];
            for (int index = 0; index < exclude.length; index++) {
                args[index] = Long.toString(exclude[index]);
            }
        }
        return mDb.createQuery(Database.Question.TABLE_NAME,
                "SELECT " + questionProjection
                        + " FROM " + Database.Question.TABLE_NAME
                        + " WHERE " + whereCondition
                        + " ORDER BY RANDOM() LIMIT 1",
                args)
                .mapToOneOrDefault(new Func1<Cursor, Question>() {
                    @Override
                    public Question call(Cursor cursor) {
                        long id = cursor.getLong(cursor.getColumnIndexOrThrow(Database.Question.Columns._ID));
                        String text = cursor.getString(cursor.getColumnIndexOrThrow(Database.Question.Columns.TEXT));
                        int type = cursor.getInt(cursor.getColumnIndexOrThrow(Database.Question.Columns.TYPE));
                        Question question = null;
                        switch (Database.Question.Type.fromInt(type)) {
                            case MATCH_SITUATION:
                                return getMatchSituationQuestion(id, text);
                            case MULTIPLE_CHOICE:
                                return getMultipleChoiceQuestion(id, text);
                        }
                        return question;
                    }
                }, null);
    }

    public MatchSituationQuestion getMatchSituationQuestion(long questionId, String questionText) {
        List<String> restartMethod = new ArrayList<>();
        List<String> positionOfRestart = new ArrayList<>();
        List<String> disciplinarySanction = new ArrayList<>();

        String projection = fullName(Database.MatchSituationAnswerPossibility.TABLE_NAME, Database.MatchSituationAnswerPossibility.Columns.TEXT)
                + ", " + fullName(Database.MatchSituationAnswerPossibility.TABLE_NAME, Database.MatchSituationAnswerPossibility.Columns.CATEGORY);
        String tables = Database.MatchSituationAnswer.TABLE_NAME + " JOIN " + Database.MatchSituationAnswerPossibility.TABLE_NAME
                + " ON " + fullName(Database.MatchSituationAnswer.TABLE_NAME, Database.MatchSituationAnswer.Columns.ANSWER)
                + "=" + fullName(Database.MatchSituationAnswerPossibility.TABLE_NAME, Database.MatchSituationAnswerPossibility.Columns._ID);
        String[] args = new String[]{Long.toString(questionId)};
        Cursor cursor = mDb.query("SELECT " + projection
                        + " FROM " + tables
                        + " WHERE " + fullName(Database.MatchSituationAnswer.TABLE_NAME, Database.MatchSituationAnswer.Columns.QUESTION) + "=?"
                        + " ORDER BY " + fullName(Database.MatchSituationAnswerPossibility.TABLE_NAME, Database.MatchSituationAnswerPossibility.Columns.ASCENDING_ORDER) + " ASC"
                , args);
        try {
            while (cursor.moveToNext()) {
                String answerText = cursor.getString(cursor.getColumnIndexOrThrow(Database.MatchSituationAnswerPossibility.Columns.TEXT));
                int category = cursor.getInt(cursor.getColumnIndexOrThrow(Database.MatchSituationAnswerPossibility.Columns.CATEGORY));
                switch (Database.MatchSituationAnswerPossibility.Category.fromInt(category)) {
                    case METHOD_OF_RESTART:
                        restartMethod.add(answerText);
                        break;
                    case POSITION_OF_RESTART:
                        positionOfRestart.add(answerText);
                        break;
                    case DISCIPLINARY_SANCTION:
                        disciplinarySanction.add(answerText);
                        break;
                    default:
                        throw new IllegalStateException("Cannot find category with id " + category);
                }
            }
        } finally {
            cursor.close();
        }
        return new MatchSituationQuestion(questionId, questionText, restartMethod, positionOfRestart, disciplinarySanction);
    }

    public MultipleChoiceQuestion getMultipleChoiceQuestion(long questionId, String questionText) {
        List<String> answers = new ArrayList<>();
        int solutionIndex = -1;

        String projection = Database.MultipleChoiceAnswer.Columns.TEXT + ", " + Database.MultipleChoiceAnswer.Columns.CORRECT;
        String[] args = new String[]{Long.toString(questionId)};
        Cursor cursor = mDb.query("SELECT " + projection
                        + " FROM " + Database.MultipleChoiceAnswer.TABLE_NAME
                        + " WHERE " + Database.MultipleChoiceAnswer.Columns.QUESTION + "=?"
                        + " ORDER BY RANDOM()",
                args);
        try {
            while (cursor.moveToNext()) {
                String answerText = cursor.getString(cursor.getColumnIndexOrThrow(Database.MultipleChoiceAnswer.Columns.TEXT));
                int correct = cursor.getInt(cursor.getColumnIndexOrThrow(Database.MultipleChoiceAnswer.Columns.CORRECT));
                answers.add(answerText);
                if (Database.BOOLEAN_TRUE == correct) {
                    solutionIndex = cursor.getPosition();
                }
            }
        } finally {
            cursor.close();
        }
        if (0 > solutionIndex) {
            throw new IllegalStateException("No solution found for question " + questionId);
        }
        return new MultipleChoiceQuestion(questionId, questionText, answers, solutionIndex);
    }

    private static String fullName(String table, String column) {
        return table + "." + column;
    }

    private static String makePlaceholders(int length) {
        if (length <= 0) return "";
        StringBuilder sb = new StringBuilder(length * 2 - 1);
        sb.append("?");
        for (int i = 1; i < length; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }

}
