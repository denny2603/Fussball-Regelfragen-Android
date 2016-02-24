package de.simontenbeitel.regelfragen.domain.repository;

import de.simontenbeitel.regelfragen.domain.model.Model;

public interface Repository<T extends Model> {

    T get(long id);

    boolean insert(T entity);

    boolean update(T entity);

    boolean delete(T entity);

}
