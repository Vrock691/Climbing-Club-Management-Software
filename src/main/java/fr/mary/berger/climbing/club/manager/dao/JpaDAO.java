package fr.mary.berger.climbing.club.manager.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaDAO<T, ID> implements DAO<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;
    private final Class<T> entityClass;

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }

    @Override
    public T save(T t) {
        entityManager.persist(t);
        return t;
    }

    @Override
    public void delete(T t) {
        entityManager.remove(t);
    }

    @Override
    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }
}
