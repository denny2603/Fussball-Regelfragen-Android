package de.simontenbeitel.regelfragen.database;

import android.net.Uri;

/**
 * Created by Simon on 08.09.2015.
 */
public class RegelfragenContract {

    public static final Uri BASE_URI = Uri.parse("content://" + RegelfragenContentProvider.AUTHORITY);

    public static final class Server implements RegelfragenDatabase.ServerColumns {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(RegelfragenContentProvider.PATH_SERVER).build();
    }

    public static final class Question implements RegelfragenDatabase.QuestionColumns {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(RegelfragenContentProvider.PATH_QUESTION).build();
    }

    public static final class Answer {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(RegelfragenContentProvider.PATH_ANSWER).build();
        public static final class Columns implements RegelfragenDatabase.AnswerColumns {}
    }

    public static final class AnswerQuestion {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(RegelfragenContentProvider.PATH_ANSWER_QUESTION).build();
        public static final class Columns implements RegelfragenDatabase.AnswerQuestionColumns {}
    }

    public static final class AnswerPossibilitiesGamesituation {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(RegelfragenContentProvider.PATH_ANSWERPOSSIBILITIES_GAMESITUATION).build();
        public static final class Columns implements RegelfragenDatabase.AnswerPossibilitiesGameSituationColumns {}
    }

    public static final class Exam {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(RegelfragenContentProvider.PATH_EXAM).build();
        public static final class Columns implements RegelfragenDatabase.ExamColumns {}
    }

    public static final class QuestionInExam {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(RegelfragenContentProvider.PATH_QUESTION_IN_EXAM).build();
        public static final class Columns implements RegelfragenDatabase.QuestionInExamColumns {}
    }

}
