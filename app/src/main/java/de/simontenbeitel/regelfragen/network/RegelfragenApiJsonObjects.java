package de.simontenbeitel.regelfragen.network;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Simon Tenbeitel
 */
public class RegelfragenApiJsonObjects {

    public static class Question {
        public String guid;
        public String text;
        public int type;
        public Timestamp created_at;
        public Timestamp updated_at;
    }

    public static class Answer {
        public String guid;
        public String text;
        public Timestamp created_at;
        public Timestamp updated_at;
    }

    public static class AnswerQuestion {
        public String guid;
        public long question;
        public long answer;
        public int position;
        public int correct;
        public Timestamp created_at;
        public Timestamp updated_at;
    }

    public static class AnswerpossibilitiesGamesituation {
        public String guid;
        public long answer;
        public int position;
        public long ascending_order;
        public Timestamp created_at;
        public Timestamp updated_at;
    }

    public static class Exam {
        public String guid;
        public int difficulty;
        public String name;
        public long type;
        public Timestamp created_at;
        public Timestamp updated_at;
    }

    public static class QuestionInExam {
        public String guid;
        public long question;
        public long exam;
        public int position;
        public Timestamp created_at;
        public Timestamp updated_at;
    }

    public static class QuestionResponse {
        public Timestamp timestamp;
        public List<Question> questions;
        public List<Answer> answers;
        public List<AnswerQuestion> answer_question;
        public List<AnswerpossibilitiesGamesituation> answerpossibilities_gamesituation;
        public List<Exam> exams;
        public List<QuestionInExam> question_in_exam;
    }

}
