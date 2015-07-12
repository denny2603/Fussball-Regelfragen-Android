package de.simontenbeitel.regelfragen.network;

import java.sql.Timestamp;

/**
 * @author Simon Tenbeitel
 */
public class RegelfragenApiJsonObjects {

    public static class Question {
        public long _id;
        public String text;
        public int type;
        public Timestamp created_at;
        public Timestamp updated_at;
    }

}
