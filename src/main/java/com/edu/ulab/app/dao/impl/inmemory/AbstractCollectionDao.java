package com.edu.ulab.app.dao.impl.inmemory;

import com.edu.ulab.app.dao.IDao;
import com.edu.ulab.app.entity.AbstractEntity;
import com.edu.ulab.app.exception.DaoException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
public abstract class AbstractCollectionDao<E extends AbstractEntity> implements IDao<E> {
    protected final Map<Long, E> repo = new HashMap<>();

    @Override
    public E create(E entity) {
        if (entity == null) {
            throwError("Create entity == null");
        }

        if (entity.isNew()) {
            throwError("Create entity with id == null");
        }

        log.info("Create entity with id : {} {}", entity.getId(), entity);
        repo.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E update(Long id, E entity) {
        if (id == null) {
            throwError("Update entity with id == null");
        }

        if (entity == null) {
            throwError(String.format("Update entity with id : %d and another entity == null", entity.getId()));
        }

        log.info("Update entity with id : {} how {}", id, entity);
        entity.setId(id);
        repo.put(id, entity);
        return entity;
    }

    @Override
    public E delete(Long id) {
        if (id == null) {
            throwError("Delete entity with id == null");
        }

        log.info("Delete entity with id : {}", id);
        return repo.remove(id);
    }

    @Override
    public E getById(Long id) {
        if (id == null) {
            throwError("Get entity with id == null");
        }

        log.info("Get entity with id : {}", id);

        E entity = repo.get(id);
        if (entity == null) {
            throwError(String.format("Get entity with id : %d, element non-exist", id));
        }
        return entity;
    }

    @Override
    public List<E> getAll() {
        log.info("Get all contents of a repository as a list");
        return new ArrayList<>(repo.values());
    }

    @Override
    public Stream<E> getAllAsStream() {
        log.info("Get all contents of a repository as a stream");
        return getAll().stream();
    }

    protected final void replaceRepo(List<E> entities) {
        repo.clear();
        entities.stream()
                .filter(Objects::nonNull)
                .forEach(entity -> repo.put(entity.getId(), entity));
    }

    private void throwError(String msg) {
        log.info(msg);
        throw new DaoException(msg);
    }
}