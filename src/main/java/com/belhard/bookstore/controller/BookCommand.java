package com.belhard.bookstore.controller;

import com.belhard.bookstore.service.BookService;
import com.belhard.bookstore.service.dto.BookDto;
import jakarta.servlet.http.HttpServletRequest;

public class BookCommand implements Command{
    private final BookService bookService;

    public BookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        long id = processReq(req);
        BookDto bookDto = bookService.getById(id);
        req.setAttribute("book", bookDto);
        return "jsp/book.jsp";
    }

    private static long processReq(HttpServletRequest req) {
        return Long.parseLong(req.getParameter("id"));
    }
}
