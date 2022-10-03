package com.edu.ulab.app.dao.impl.inmemory;

import com.edu.ulab.app.entity.UserBookEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class UserBookDao extends AbstractCollectionDao<UserBookEntity> {

    private static Predicate<UserBookEntity> getFilterByUserId(Long userId, boolean isEqualsId) {
        return entity -> Objects.nonNull(entity) &&
                entity.getUserId() != null && entity.getBookId() != null &&
                (entity.getUserId().longValue() == userId.longValue()) == isEqualsId;
    }

    public List<UserBookEntity> getBookWhereUserId(Long userId) {
        return getAllAsStream()
                .filter(getFilterByUserId(userId, true))
                .collect(Collectors.toList());
    }

    public void deleteUserBookBinding(Long userId) {
        replaceRepo(getAllAsStream()
                .filter(getFilterByUserId(userId, false))
                .collect(Collectors.toList()));
    }

    private static Predicate<UserBookEntity> getFilterForBind(Long userId, Long bookId) {
        return entity -> Objects.nonNull(entity) &&
                entity.getUserId() != null && entity.getBookId() != null &&
                entity.getUserId().longValue() == userId.longValue() &&
                entity.getBookId().longValue() == bookId.longValue();
    }

    public void createBind(Long userId, Long bookId) {
        boolean hasBind = getAllAsStream()
                .anyMatch(getFilterForBind(userId, bookId));

        if (!hasBind) {
            create(new UserBookEntity(userId, bookId));
        }
    }
}
