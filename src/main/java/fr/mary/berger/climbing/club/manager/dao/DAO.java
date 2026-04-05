package fr.mary.berger.climbing.club.manager.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {

    Optional<T> findById(ID id);

    List<T> findAll();

    T save(T t);

    void delete(T t);

    void deleteById(ID id);

}
