package de.simontenbeitel.regelfragen.data.remote.dto;

public class ExamDTO {

    int id;
    int difficulty;
    String name;
    int type;

    public ExamDTO() {
    }

    public ExamDTO(int id, int difficulty, String name, int type) {
        this.id = id;
        this.difficulty = difficulty;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
