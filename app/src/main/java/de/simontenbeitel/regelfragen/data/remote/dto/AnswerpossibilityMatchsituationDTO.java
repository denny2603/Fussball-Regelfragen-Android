package de.simontenbeitel.regelfragen.data.remote.dto;

public class AnswerpossibilityMatchsituationDTO {

    int id;
    String text;
    int category;
    int ascending_order;

    public AnswerpossibilityMatchsituationDTO() {
    }

    public AnswerpossibilityMatchsituationDTO(int id, String text, int category, int ascending_order) {
        this.id = id;
        this.text = text;
        this.category = category;
        this.ascending_order = ascending_order;
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getAscending_order() {
        return ascending_order;
    }

    public void setAscending_order(int ascending_order) {
        this.ascending_order = ascending_order;
    }

}
