package com.edu.ulab.app.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

// E - entity
public interface IDao<E> {

    E create(E entity);

    E update(Long id, E entity);

    E delete(Long id);

    E getById(Long id);

    List<E> getAll();

    Stream<E> getAllAsStream();
}
