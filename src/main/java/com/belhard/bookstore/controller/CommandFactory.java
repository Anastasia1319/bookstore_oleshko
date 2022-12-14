package com.belhard.bookstore.controller;

import com.belhard.bookstore.data.connection.DataSource;
import com.belhard.bookstore.data.dao.impl.BookDaoImpl;
import com.belhard.bookstore.service.BookService;
import com.belhard.bookstore.service.impl.BookServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, Command> commands;
    public static final CommandFactory INSTANCE = new CommandFactory();

    private CommandFactory() {
        BookService bookService = new BookServiceImpl(new BookDaoImpl(new DataSource()));
        commands = new HashMap<>();
    }
}