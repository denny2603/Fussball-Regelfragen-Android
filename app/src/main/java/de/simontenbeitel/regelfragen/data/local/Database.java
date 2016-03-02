package de.simontenbeitel.regelfragen.data.local;

import android.provider.BaseColumns;

public class Database {

    public abstract static class Question {
        public static final String TABLE_NAME = "question";

        public enum Type {
            MATCH_SITUATION(1),
            MULTIPLE_CHOICE(2);

            final int id;
            Type(int id) {
                this.id = id;
            }

            public int asInt() {
                return id;
            }
            public static Type fromInt(int id) {
                switch (id) {
                    case 0: return MATCH_SITUATION;
                    case 1: return MULTIPLE_CHOICE;
                    default: throw new IllegalArgumentException("No type for id " + id);
                }
            }
        }

        public interface Columns extends BaseColumns{
            String TEXT = "text";
            String TYPE = "type";
        }

        public static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
                + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Columns.TEXT + " TEXT, "
                + Columns.TYPE + " INTEGER NOT NULL)";
    }

    public abstract static class MultipleChoiceAnswer {
        public static final String TABLE_NAME = "multiplechoice_answer";

        public interface Columns extends BaseColumns{
            String TEXT = "text";
            String QUESTION = "question";
            String CORRECT = "correct";
        }

        public static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
                + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Columns.TEXT + " TEXT, "
                + Columns.QUESTION + " INTEGER NOT NULL, "
                + Columns.CORRECT + " INTEGER,"
                + "FOREIGN KEY(" + Columns.QUESTION + ") REFERENCES " + Question.TABLE_NAME + "(" + Question.Columns._ID + "))";
    }

    public abstract static class MatchSituationAnswerPossibility {
        public static final String TABLE_NAME = "matchsituation_answer_possibility";

        public enum Category {
            METHOD_OF_RESTART(0),
            POSITION_OF_RESTART(1),
            DISCIPLINARY_SANCTION(2);

            final int id;
            Category(int id) {
                this.id = id;
            }

            public int asInt() {
                return id;
            }
            public static Category fromInt(int id) {
                switch (id) {
                    case 0: return METHOD_OF_RESTART;
                    case 1: return POSITION_OF_RESTART;
                    case 2: return DISCIPLINARY_SANCTION;
                    default: throw new IllegalArgumentException("No category for id " + id);
                }
            }
        }

        public interface Columns extends BaseColumns {
            String TEXT = "text";
            String CATEGORY = "category";
            String ASCENDING_ORDER = "asc_order";
        }

        public static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
                + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Columns.TEXT + " TEXT, "
                + Columns.CATEGORY + " INTEGER NOT NULL, "
                + Columns.ASCENDING_ORDER + " INTEGER)";

    }

    public abstract static class MatchSituationAnswer {
        public static final String TABLE_NAME = "matchsituation_answer";

        public interface Columns extends BaseColumns {
            String QUESTION = "question";
            String ANSWER = "answer";
        }

        public static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
                + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Columns.QUESTION + " INTEGER NOT NULL, "
                + Columns.ANSWER + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + Columns.QUESTION + ") REFERENCES " + Question.TABLE_NAME + "(" + Question.Columns._ID + "), "
                + "FOREIGN KEY(" + Columns.ANSWER + ") REFERENCES " + MatchSituationAnswerPossibility.TABLE_NAME + "(" + MatchSituationAnswerPossibility.Columns._ID + "))";
    }

}
