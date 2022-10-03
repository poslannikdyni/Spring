package com.edu.ulab.app.dao.impl.inmemory;

import com.edu.ulab.app.dao.IDao;
import com.edu.ulab.app.entity.AbstractEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
public abstract class AbstractCollectionDao<E extends AbstractEntity> implements IDao<E> {
    protected final Map<Long, E> repo = new HashMap<>();

    @Override
    public E create(E entity) {
        log.info("Create entity with id : {} {}", entity.getId(), entity);
        repo.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E update(Long id, E entity) {
        log.info("Update entity with id : {} how {}", id, entity);
        entity.setId(id);
        return repo.replace(id, entity);
    }

    @Override
    public E delete(Long id) {
        log.info("Delete entity with id : {}", id);
        return repo.remove(id);
    }

    @Override
    public E getById(Long id) {
        log.info("Get entity with id : {}", id);
        return repo.get(id);
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
        log.info("Repository content replaced.");
        repo.clear();
        entities.stream()
                .filter(Objects::nonNull)
                .forEach(entity -> repo.put(entity.getId(), entity));
    }
}