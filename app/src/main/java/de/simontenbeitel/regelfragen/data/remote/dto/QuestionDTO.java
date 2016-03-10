package de.simontenbeitel.regelfragen.data.remote.dto;

public class QuestionDTO {

    int id;
    String text;
    int type;

    public QuestionDTO() {
    }

    public QuestionDTO(int id, String text, int type) {
        this.id = id;
        this.text = text;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
