package de.simontenbeitel.regelfragen.domain.model;

public abstract class Model {

    protected long id; //id in local db

    public Model(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
