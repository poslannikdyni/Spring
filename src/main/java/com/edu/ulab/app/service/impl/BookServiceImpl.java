package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.impl.inmemory.BookDao;
import com.edu.ulab.app.dao.impl.inmemory.UserBookDao;
import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserBookEntity;
import com.edu.ulab.app.exception.ServiceException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.utility.ExceptionUtility;
import com.edu.ulab.app.utility.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final UserBookDao userBookDao;
    private final BookDao bookDao;
    private final BookMapper bookMapper;
    private final ExceptionUtility exceptionUtility;

    public BookServiceImpl(UserBookDao userBookDao,
                           BookDao bookDao,
                           BookMapper bookMapper,
                           ExceptionUtility exceptionUtility) {
        this.userBookDao = userBookDao;
        this.bookDao = bookDao;
        this.bookMapper = bookMapper;
        this.exceptionUtility = exceptionUtility;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        exceptionUtility.throwServiceExceptionIfNull(bookDto, "Create book failed : bookDto is null");

        bookDto.setId(IdGenerator.nextBookId());
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);
        bookEntity = bookDao.create(bookEntity);
        exceptionUtility.throwServiceExceptionIfNull(bookEntity, "Create book failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        log.info("Create book successfully {}", bookEntity);

        UserBookEntity userBookEntity = userBookDao.create(new UserBookEntity(IdGenerator.nextUserBookId(), bookEntity.getUserId(), bookEntity.getId()));
        exceptionUtility.throwServiceExceptionIfNull(userBookEntity, "Create user-book binding failed.");
        log.info("Create user-book binding successfully {}", userBookEntity);
        return bookMapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        exceptionUtility.throwServiceExceptionIfNull(bookDto, "Update book failed : bookDto is null");
        exceptionUtility.throwServiceExceptionIfNull(bookDto.getId(), "Update book failed : bookDto.getId() returns null");

        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);
        bookEntity = bookDao.update(bookDto.getId(), bookEntity);

        exceptionUtility.throwNotFoundExceptionIfNull(bookEntity, String.format("Update book failed : book with id = %s not exist", bookDto.getId()));
        log.info("Update book successfully {}", bookEntity);
        return bookMapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public BookDto getBookById(Long id) {
        exceptionUtility.throwServiceExceptionIfNull(id, "Get book failed : id is null");
        BookEntity bookEntity = bookDao.getById(id);
        exceptionUtility.throwNotFoundExceptionIfNull(bookEntity, String.format("Get book failed : book with id = %s not exist", id));
        log.info("Get book successfully {}", bookEntity);
        return bookMapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public void deleteBookById(Long id) {
        exceptionUtility.throwServiceExceptionIfNull(id, "Delete book failed : id is null");
        BookEntity bookEntity = bookDao.delete(id);
        exceptionUtility.throwNotFoundExceptionIfNull(bookEntity, String.format("Delete book failed : book with id = %s not exist", id));
        log.info("Delete book successfully {}", bookEntity);
    }

    @Override
    public List<BookDto> getBookByUserId(Long userId) {
        exceptionUtility.throwServiceExceptionIfNull(userId, "Get book by userId failed : userId is null");

        List<BookDto> bookDtoList = userBookDao.getBookWhereUserId(userId)
                    .stream()
                    .map(userBook -> bookDao.getById(userBook.getBookId()))
                    .map(bookMapper::bookEntityToBookDto)
                    .collect(Collectors.toList());

        log.info("Get book by userId successfully");
        return bookDtoList;
    }

    @Override
    public void deleteUserBookBinding(Long userId) {
        exceptionUtility.throwServiceExceptionIfNull(userId, "Delete user book by userId failed : userId is null");
        userBookDao.deleteUserBookBinding(userId);
        log.info("Delete user book by userId successfully. UserId = {}", userId);
    }
}
