package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.impl.inmemory.BookDao;
import com.edu.ulab.app.dao.impl.inmemory.UserBookDao;
import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserBookEntity;
import com.edu.ulab.app.exception.DaoException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.utility.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final UserBookDao userBookDao;
    private final BookDao bookDao;
    private final BookMapper bookMapper;

    public BookServiceImpl(UserBookDao userBookDao,
                           BookDao bookDao,
                           BookMapper bookMapper) {
        this.userBookDao = userBookDao;
        this.bookDao = bookDao;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        bookDto.setId(IdGenerator.nextBookId());
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);
        try {
            bookEntity = bookDao.create(bookEntity);
            userBookDao.create(new UserBookEntity(IdGenerator.nextUserBookId(), bookEntity.getUserId(), bookEntity.getId()));
        } catch (DaoException e) {
            throw e;
        }
        return bookMapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);
        try {
            bookEntity = bookDao.update(bookDto.getId(), bookEntity);
        } catch (DaoException e) {
            throw e;
        }
        return bookMapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public BookDto getBookById(Long id) {
        try {
            BookEntity bookEntity = bookDao.getById(id);
            return bookMapper.bookEntityToBookDto(bookEntity);
        } catch (DaoException e) {
            throw e;
        }
    }

    @Override
    public void deleteBookById(Long id) {
        try {
            bookDao.delete(id);
        } catch (DaoException e) {
            throw e;
        }
    }

    @Override
    public List<BookDto> getBookByUserId(Long userId) {
        try {
            return userBookDao.getBookWhereUserId(userId)
                    .stream()
                    .map(userBook -> bookDao.getById(userBook.getBookId()))
                    .map(bookMapper::bookEntityToBookDto)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw e;
        }
    }

    @Override
    public void deleteUserBookBinding(Long userId) {
        try {
            userBookDao.deleteUserBookBinding(userId);
        } catch (DaoException e) {
            throw e;
        }
    }

}
