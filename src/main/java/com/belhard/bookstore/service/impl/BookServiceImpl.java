package com.belhard.bookstore.service.impl;

import com.belhard.bookstore.data.dao.BookDao;
import com.belhard.bookstore.data.entity.Book;
import com.belhard.bookstore.exceptions.NotFoundException;
import com.belhard.bookstore.exceptions.NotUpdateException;
import com.belhard.bookstore.service.BookService;
import com.belhard.bookstore.service.dto.BookDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<BookDto> getAll() {
        return bookDao.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookDao.findById(id);
        if (book == null) {
            throw  new NotFoundException("Book with id: " + id + " not found!");
        }
        return toDto(book);
    }

    @Override
    public BookDto create(BookDto dto) {
        validate(dto);
        Book toCreate = toEntity(dto);
        Book created = bookDao.create(toCreate);
        return toDto(created);
    }

    private void validate(BookDto dto) {
        if (dto.getIsbn().length() > 13) {
            throw new NotUpdateException("ISBN number cannot be longer than 13 characters.");
        }
        LocalDate date = LocalDate.now();
        if (dto.getPublishinYear() < 0 || dto.getPublishinYear() > date.getYear()) {
            throw new NotUpdateException("Incorrect year of publication of the book entered.");
        }
        if (dto.getPrice().signum() <= 0) {
            throw new NotUpdateException("Incorrect price of the book entered.");
        }
    }

    @Override
    public BookDto update(BookDto dto) {
        validate(dto);
        Book toUpdate = toEntity(dto);
        Book updated = bookDao.update(toUpdate);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!bookDao.delete(id)) {
            throw new NotFoundException("Couldn't delete book with id: " + id + "!");
        }
    }

    private BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setPublishinYear(book.getPublishinYear());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        return bookDto;
    }

    private Book toEntity (BookDto dto) {
        Book book = new Book();
        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        book.setPublishinYear(dto.getPublishinYear());
        book.setIsbn(dto.getIsbn());
        book.setPrice(dto.getPrice());
        return book;
    }

    public BigDecimal sumPriceByAuthor (String author) {
        return bookDao.findByAuthor(author)
                .stream()
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}