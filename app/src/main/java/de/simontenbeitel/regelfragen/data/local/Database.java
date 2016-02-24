package de.simontenbeitel.regelfragen.data.local;

import android.provider.BaseColumns;

public class Database {

    public abstract static class Question {
        public static final String TABLE_NAME = "question";

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

    public abstract static class MatchSituationAnswer {
        public static final String TABLE_NAME = "matchsituation_answer";

        public enum Categories {
            METHOD_OF_RESTART(0),
            POSITION_OF_RESTART(1),
            DISCIPLINARY_SANCTION(2);

            final int id;
            Categories(int id) {
                this.id = id;
            }

            public int asInt() {
                return id;
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

}
